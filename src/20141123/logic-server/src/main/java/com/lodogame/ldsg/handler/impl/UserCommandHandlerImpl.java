package com.lodogame.ldsg.handler.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.ldsg.bo.Message;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.factory.MessageFactory;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.model.Command;
import com.lodogame.model.UserMapper;

public class UserCommandHandlerImpl implements CommandHandler {

	private static final Logger logger = Logger.getLogger(UserCommandHandlerImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private CommandDao commandDao;

	@Override
	public void handle(final Command command) {

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {
		case CommandType.COMMAND_PUSH_USER_INFO:
			handlePushUser(type, params);
			break;
		case CommandType.COMMAND_PUSH_USER_TASK:
			handlePushTask(type, params);
			break;
		case CommandType.COMMAND_PUSH_MESSAGE:
			handlePushMsg(type, params, command.getPriority());
			break;
		case CommandType.COMMAND_PAY_COMPLETED:
			handlePayCompleted(type, params);
			break;
		case CommandType.COMMAND_PUSH_MIDNIGHT:
			handlePushMidnight(type, params);
			break;
		case CommandType.COMMAND_PUSH_NEW_MAIL:
			handlePushNewMail(type, params);
			break;
		case CommandType.COMMAND_BATTLE_START:
			handlerPushBattleStart(type,params);
			break;
		default:
			break;
		}

	}


	protected void handlePayCompleted(int type, Map<String, String> params) {
		// 在此加入充值完成后要处理的逻辑
		// 检测是否有新的单笔充值奖励可领
		String userId = params.get("userId");
		pushHandler.pushOncePayRewardActivated(userId);
		// 检测是否有新的累计充值奖励可领
		pushHandler.pushTotalPayRewardActivated(userId);
		// 检查积天充值奖励是否可领
		pushHandler.pushTotalDayPayReward(userId);
		
	}

	private void handlePushUser(int type, Map<String, String> params) {

		if (type == CommandType.PUSH_USER) {
			String userId = params.get("userId");
			logger.debug("开始处理推送用户信息命令.userId[" + userId + "]");
			pushHandler.pushUser(userId);
		} else {

		}

	}

	private void handlePushTask(int type, Map<String, String> params) {

		if (type == CommandType.PUSH_USER) {
			String userId = params.get("userId");
			int systemTaskId = NumberUtils.toInt(params.get("systemTaskId"));
			int pushType = NumberUtils.toInt(params.get("pushType"));
			logger.debug("开始处理推送用户任务命令.userId[" + userId + "], systemTaskId[" + systemTaskId + "]");
			pushHandler.pushTask(userId, systemTaskId, pushType);
		} else {

		}
	}

	/**
	 * 是否需要发送
	 * 
	 * @param userId
	 * @param fromUserId
	 * @param poolSize
	 * @return
	 */
	private boolean needPush(String userId, String fromUserId, long poolSize, int priority, String partnerIds) {

		if (partnerIds != null && !partnerIds.equalsIgnoreCase("all")) {// 指定合作商
			UserMapper userMapper = this.userMapperDao.get(userId);
			if (partnerIds.indexOf(userMapper.getPartnerId()) == -1) {
				return false;
			}
		}

		if (priority != Priority.MESSAGE) {// 不是普通消息则发送
			return true;
		}

		if (StringUtils.equalsIgnoreCase(userId, fromUserId)) {// 自己的消息必须看到
			return true;
		}

		int rand = 100;

		if (poolSize > 100000) {// 阻塞10W消息,则只随机2%
			rand = 2;
		} else if (poolSize > 50000) {// 阻塞5W消息,则只随机5%
			rand = 5;
		} else if (poolSize > 30000) {// 阻塞3W消息,则只随机10%
			rand = 10;
		} else if (poolSize > 10000) {// 阻塞1W消息,则只随机15%
			rand = 15;
		}

		return RandomUtils.nextInt(100) <= rand;
	}

	/**
	 * 推送消息
	 * 
	 * @param type
	 * @param params
	 */
	private void handlePushMsg(int type, Map<String, String> params, int priority) {

		if (type == CommandType.PUSH_ALL) {

			logger.debug("开始处理推送走马灯命令");

			String key = RedisKey.getCommandKey(priority);
			long poolSize = JedisUtils.llen(key);
			logger.debug("当前消息队列长度[" + poolSize + "]");

			String fromUserId = params.get("userId");
			String partnerIds = params.get("partnerIds");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {

				if (!this.needPush(userId, fromUserId, poolSize, priority, partnerIds)) {
					continue;
				}

				params.put("userId", userId);

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_PUSH_MESSAGE);
				command.setType(CommandType.PUSH_USER);
				command.setParams(params);
				command.setPriority(priority);

				commandDao.add(command);
			}

		} else {

			String userId = params.get("userId");
			int msgType = NumberUtils.toInt(params.get("msgType"));// 消息类型
			List<Message> msgList = MessageFactory.ins().getMessage(msgType, params);
			this.pushHandler.pushMessage(userId, msgList);
		}

	}

	/**
	 * 午夜推送
	 * 
	 * @param type
	 * @param params
	 */
	private void handlePushMidnight(int type, Map<String, String> params) {

		if (type == CommandType.PUSH_ALL) {

			logger.debug("开始晚上12点推送");

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {
				this.pushHandler.pushMidnightData(userId);
			}

		}

	}

	private void handlerPushBattleStart(int type, Map<String, String> params){
		this.pushHandler.pushBattleStart();
	}
	
	private void handlePushNewMail(int type, Map<String, String> params) {

		if (type == CommandType.PUSH_ALL || type == CommandType.PUSH_PAY || type == CommandType.PUSH_LOGIN || type == CommandType.PUSH_PARTNER) {

			logger.debug("开始新邮件推送");
			
			Date mailTime = null;
			
			String date = params.get("date");
			if (date != null) {
				mailTime = DateUtils.str2Date(date);
			}

			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {

				if (type == CommandType.PUSH_PAY) {

					Date start = DateUtils.getDateAtMidnight(mailTime);
					Date end = DateUtils.getDateAtMidnight(DateUtils.addDays(mailTime, 1));

					if (this.paymentLogDao.getPaymentTotalByTime(userId, start, end) == 0) {
						continue;
					}

				} else if (type == CommandType.PUSH_LOGIN) {
					if (!userOnlineLogDao.isLogin(userId, mailTime)) {
						continue;
					}
				} else if (type == CommandType.PUSH_PARTNER) {
					String partner = params.get("partner");

					UserMapper userMapper = userMapperDao.get(userId);
					if (!userMapper.getPartnerId().equals(partner)) {
						continue;
					}
				}

				this.pushHandler.pushNewMail(userId);
			}

		} else {
			String userId = params.get("userId");
			this.pushHandler.pushNewMail(userId);
		}

	}
	

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(1, this);
	}

}
