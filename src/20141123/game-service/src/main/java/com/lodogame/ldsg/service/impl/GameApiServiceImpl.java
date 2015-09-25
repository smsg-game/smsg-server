package com.lodogame.ldsg.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.dao.SystemMallDiscountDao;
import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.dao.UserInvitedDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserMonthlyCardTaskDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.PaymentUtil;
import com.lodogame.ldsg.bo.AdminSendAttachBO;
import com.lodogame.ldsg.bo.GrantToolBo;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ActivityType;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.PushType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.ldsg.service.GoldSetService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.PaymentLog;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemGoldSet;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemMallDiscountActivity;
import com.lodogame.model.SystemMallDiscountItems;
import com.lodogame.model.SystemTool;
import com.lodogame.model.User;
import com.lodogame.model.UserInvited;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserMonthlyCardTask;

public class GameApiServiceImpl implements GameApiService {
	public static Logger LOG = Logger.getLogger(GameApiServiceImpl.class);

	public final static int USER_NOT_EXIST = 2001;

	private static Logger paymentLog = Logger.getLogger("paymentLog");
	
	@Autowired
	private UserMonthlyCardTaskDao userMonthlyCardTaskDao;

	@Autowired
	private SystemMallDiscountDao systemMallDiscountDao;

	@Autowired
	private UserService userService;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private TaskService taskServcie;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private GoldSetService goldSetService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemToolDao systemToolDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SystemGoldSetDao systemGoldSetDao;
	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private SystemMailDao systemMailDao;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private UserInvitedDao userInvitedDao;

	public UserMapperDao getUserMapperDao() {
		return userMapperDao;
	}

	public void setUserMapperDao(UserMapperDao userMapperDao) {
		this.userMapperDao = userMapperDao;
	}

	@Override
	public UserMapper loadUserMapper(String partnerUserId, String serverId, String partnerId, String qn, String imei, String mac, String idfa) {
		if (StringUtils.isBlank(partnerUserId) || StringUtils.isBlank(serverId) || StringUtils.isBlank(partnerId)) {
			return null;
		}

		UserMapper userMapper = userMapperDao.getByPartnerUserId(partnerUserId, partnerId, serverId);
		
		if (userMapper == null) {
			paymentLog.info("userMapper不存在----partnerUserId:"+partnerUserId+",partnerId:"+partnerId+",serverId:"+serverId);
			userMapper = new UserMapper();
			userMapper.setPartnerId(partnerId);
			userMapper.setPartnerUserId(partnerUserId);
			userMapper.setServerId(serverId);
			userMapper.setUserId(IDGenerator.getID());
			userMapper.setQn(qn);
			userMapper.setImei(imei);
			userMapper.setIdfa(idfa);
			userMapper.setMac(mac);
			userMapper.setCreatedTime(new Date());
			userMapper.setUpdatedTime(new Date());
			userMapperDao.save(userMapper);
			// 用户注册日志
			unSynLogService.userRegLog(userMapper.getUserId(), partnerUserId, partnerId, qn, new Date());
		} else {
			paymentLog.info("userMapper存在----partnerUserId:"+partnerUserId+",partnerId:"+partnerId+",serverId:"+serverId);
			userMapperDao.updatePhoneInfo(userMapper.getUserId(), userMapper.getImei(), userMapper.getMac(), userMapper.getIdfa());
		}

		return userMapper;

	}

	public boolean doPayment(String partnerId, String serverId, String partnerUserId, String channel, String orderId, BigDecimal amount, final int gold, String userIp, String remark) {

		paymentLog.info("do payment.partnerId[" + partnerId + "], serverId[" + serverId + "], partnerUserId[" + partnerUserId + "], channel[" + channel + "], orderId[" + orderId + "],  amount[" + amount + "], gold["
				+ gold + "], userIp[" + userIp + "], remark[" + remark + "]");

		UserMapper userMapper = this.userMapperDao.getByPartnerUserId(partnerUserId, partnerId, serverId);
		// UserMapper userMapper = this.userMapperDao.get
		if (userMapper == null) {
			String message = "充值失败,用户不存在.partnerId[" + partnerId + "], serverId[" + serverId + "], partnerUserId[" + partnerUserId + "]";
			LOG.error(message);
			throw new ServiceException(PAYMENT_ERROR_USER_NOT_EXISTS, message);
		}

		User user = this.userService.get(userMapper.getUserId());
		if (user == null) {

			String message = "充值失败,用户不存在.partnerId[" + partnerId + "], serverId[" + serverId + "], userId[" + userMapper.getUserId() + "]";
			LOG.error(message);
			throw new ServiceException(PAYMENT_ERROR_USER_NOT_EXISTS, message);
		}

		final String userId = user.getUserId();
		boolean success = this.userService.addGold(userId, gold, ToolUseType.ADD_PAYMENT, user.getLevel());
		if (!success) {
			String message = "充值失败，更新用户金币失败.userId[" + userId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		SystemGoldSet systemGoldSet = this.goldSetService.getByPayAmount(amount.doubleValue());
		if (systemGoldSet != null) {

			int returnGold = systemGoldSet.getGold() - gold;
			if (returnGold > 0) {// 有返还金币
				LOG.info("充值返还金币.amount[" + amount + "], gold[" + gold + "], returnGold[" + returnGold + "]");
				boolean retSuccess = this.userService.addGold(userId, returnGold, ToolUseType.ADD_PAYMENT_RETURN, user.getLevel());
				if (!retSuccess) {
					String message = "充值返还金币失败，更新用户金币失败.userId[" + userId + "]";
					LOG.error(message);
				}
			}

		}

		int payAmount = this.paymentLogDao.getPaymentTotalGold(userId);// 首冲双倍
		if (payAmount == 0) {
			boolean retSuccess = this.userService.addGold(userId, gold, ToolUseType.ADD_PAYMENT_FIRST_PAY_DOUBLE, user.getLevel());
			if (!retSuccess) {
				String message = "首充双倍赠送失败，更新用户金币失败.userId[" + userId + "]";
				LOG.error(message);
			}
		}

		// User user = this.userService.get(userId);

		PaymentLog paymentLog = new PaymentLog();
		paymentLog.setChannel(channel);
		paymentLog.setCreatedTime(new Date());

		if (amount.compareTo(new BigDecimal(0.1)) == 0) {
			// 开发测试充1毛钱时日志加5块钱的日志
			paymentLog.setGold(50);
			LOG.debug("测试充值");
		} else {
			paymentLog.setGold(gold);
			LOG.debug("普通数据");
		}
		paymentLog.setAmount(amount);
		paymentLog.setOrderId(orderId);
		paymentLog.setPartnerId(partnerId);
		paymentLog.setPartnerUserId(partnerUserId);
		paymentLog.setRemark(remark);
		paymentLog.setServerId(serverId);
		paymentLog.setUserId(userId);
		paymentLog.setUserIp(userIp);
		paymentLog.setUserName(user.getUsername());

		this.paymentLogDao.add(paymentLog);

		// 插入运营日志
		unSynLogService.userPayLog(paymentLog, user.getLevel());

		// 更新VIP等级
		this.userService.updateVipLevel(userId);

		// 更新积天充值
		this.activityService.addUserTotalDayPayRewardLog(userId);

		SystemActivity systemActivity = systemActivityDao.get(ActivityId.PAY_RATES_REWARD_ID);

		try {

			// 检测充值返利活动
			if (systemActivity != null && systemActivity.getActivityType() == ActivityType.ACTIVITY_TYPE_PAY_RETURN) {// 防止数值坑
				if (activityService.checkActivityIsOpenAdd(systemActivity) == 1) {
					double rate = PaymentUtil.parserRate(systemActivity.getParam());

					if (rate > 0 || rate < 2) {
						int returnGold = (int) (rate * gold);
						String toolIds = ToolType.GOLD + "," + ToolId.TOOL_GLOD_ID + "," + returnGold;
						mailService.send("充值返利活动奖励", "尊敬的合伙人：\n感谢您对大三国的厚爱，您参与的充值返利活动奖励已经发放。发放内容： 元宝*" + returnGold + "。请您注意查收，祝您游戏愉快！", toolIds, MailTarget.USERS, user.getLodoId() + "", null, new Date(), partnerId);
					}
				}
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
		}

		try {

			// 邀请返利
			UserInvited userInvited = userInvitedDao.getByInvitedUserId(userId);
			if (userInvited != null) {
				int rabetaGold = (int) (gold * 0.2);
				String toolIds = ToolType.GOLD + "," + ToolId.TOOL_GLOD_ID + "," + rabetaGold;
				User invitedUser = this.userService.get(userInvited.getUserId());
				mailService.send("邀请返利奖励", "尊敬的合伙人：\n谢谢您对我们游戏的支持！您所邀请的玩家" + user.getUsername() + "成功充值元宝*" + gold + "，因此您可获得返利元宝*" + rabetaGold + "，请您注意查收，祝您游戏愉快！", toolIds, MailTarget.USERS, invitedUser.getLodoId()
						+ "",null, new Date(), partnerId);
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
		}

		addCommand(userId, CommandType.COMMAND_PUSH_USER_INFO, CommandType.PUSH_USER);

		// 发送充值完成标识，用于处理一些充值后需要触发的事情，比如

		addCommand(userId, CommandType.COMMAND_PAY_COMPLETED, CommandType.PUSH_USER);
		// 处理任务
		taskServcie.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				int needGold = NumberUtils.toInt(params.get("gold"));

				if (taskTarget == TaskTargetType.TASK_TYPE_SINGLE_PAYMENT) {
					return gold >= needGold;
				} else if (taskTarget == TaskTargetType.TASK_TYPE_TOTAL_PAYMENT) {
					int paymentTotal = paymentLogDao.getPaymentTotalGold(userId);
					return paymentTotal >= needGold;
				}

				return false;
			}

		});
		
		// 添加30天月卡任务
		if (isUserMonthlyCardTask(userId, amount) == true) {
			taskServcie.refreshUserMonthlyCardTask(userId);

			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", userId);
			map.put("systemTaskId", String.valueOf(7001));
			map.put("pushType", String.valueOf(PushType.PUSH_TYPE_ADD));

			Command command = new Command();
			command.setCommand(CommandType.COMMAND_PUSH_USER_TASK);
			command.setType(CommandType.PUSH_USER);
			command.setParams(map);

			commandDao.add(command);

			addCommand(userId, CommandType.COMMAND_MONTHLY_CARD, CommandType.PUSH_USER);
		}

		return success;
	}
	
	private boolean isUserMonthlyCardTask(String userId, BigDecimal amount) {
		if (amount.intValue() != 30) {
			return false;
		}

		UserMonthlyCardTask task = userMonthlyCardTaskDao.getByUserId(userId);

		if (task == null) {
			String startDate = DateUtils.getDate();

			Date addDays = DateUtils.addDays(new Date(), 30);
			String endDate = DateUtils.getDate(addDays);
			task = new UserMonthlyCardTask(userId, startDate, endDate);
			userMonthlyCardTaskDao.add(task);

			return true;
		}

		if (task.isTaskExpired() == true) {
			String startDate = DateUtils.getDate();

			Date addDays = DateUtils.addDays(new Date(), 30);
			String endDate = DateUtils.getDate(addDays);
			task.setStartDate(startDate);
			task.setEndDate(endDate);
			userMonthlyCardTaskDao.update(task);

			return true;
		}

		return false;
	}

	/**
	 * 添加命令
	 * 
	 * @param userId
	 */
	private void addCommand(final String userId, int cmd, int type) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(cmd);
		command.setType(type);
		command.setParams(params);

		commandDao.add(command);
	}

	@Override
	public String addGold(int gold, String[] playerIds) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer();
		sb.append(grantToolBo.getFailPlayId());
		for (User u : users) {
			try {
				if (!userService.addGold(u.getUserId(), gold, ToolUseType.ADD_ADMIN_ADD, u.getLevel())) {
					sb.append(u.getLodoId()).append(",");
				}
			} catch (Exception e) {
				sb.append(u.getLodoId()).append(",");
			}

		}
		return sb.toString();
	}

	@Override
	public String addCopper(int copper, String[] playerIds) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer();
		sb.append(grantToolBo.getFailPlayId());
		for (User u : users) {
			try {
				if (!userService.addCopper(u.getUserId(), copper, ToolUseType.ADD_ADMIN_ADD)) {
					sb.append(u.getLodoId()).append(",");
				}
			} catch (Exception e) {
				sb.append(u.getLodoId()).append(",");
			}
		}
		return sb.toString();
	}

	@Override
	public String reduceCopper(int copper, String[] playerIds) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer();
		sb.append(grantToolBo.getFailPlayId());
		for (User u : users) {
			try {
				if (!userService.reduceCopper(u.getUserId(), Math.abs(copper), ToolUseType.REDUCE_ADMIN_REDUCE)) {
					sb.append(u.getLodoId()).append(",");
				}
			} catch (Exception e) {
				sb.append(u.getLodoId()).append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 根据playerId列表获取用户对象
	 * 
	 * @param playerIds
	 * @return
	 */
	private GrantToolBo getUsersByPlayerIds(String[] playerIds) {
		GrantToolBo grantToolBo = new GrantToolBo();
		List<User> users = new ArrayList<User>();
		StringBuffer failPlayId = new StringBuffer();
		for (int i = 0; i < playerIds.length; i++) {
			try {
				User user = userService.getByPlayerId(playerIds[i]);
				users.add(user);
			} catch (Exception e) {
				failPlayId.append(playerIds[i]).append(",");
			}
		}
		grantToolBo.setFailPlayId(failPlayId.toString());
		grantToolBo.setList(users);
		return grantToolBo;
	}

	/**
	 * 根据userId列表获取用户对象
	 * 
	 * @param playerIds
	 * @return
	 */
	private GrantToolBo getUsersByUserIds(String[] userIds) {
		GrantToolBo grantToolBo = new GrantToolBo();
		List<User> users = new ArrayList<User>();
		StringBuffer failUserIdStr = new StringBuffer();
		for (int i = 0; i < userIds.length; i++) {
			try {
				User user = userService.get(userIds[i]);
				users.add(user);
			} catch (Exception e) {
				failUserIdStr.append(userIds[i]).append(",");
			}
		}
		grantToolBo.setFailPlayId(failUserIdStr.toString());
		grantToolBo.setList(users);
		return grantToolBo;
	}

	@Override
	public String addHeros(String[] heroIds, String[] playerIds) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer("&");
		Map<String, Integer> userHeroMap = new HashMap<String, Integer>();

		for (User u : users) {
			for (String systemHeroId : heroIds) {
				String userHeroId = IDGenerator.getID();
				userHeroMap.put(userHeroId, Integer.parseInt(systemHeroId));
			}
			try {
				heroService.addUserHero(u.getUserId(), userHeroMap, ToolUseType.ADD_ADMIN_ADD);
			} catch (Exception e) {
				sb.append(u.getLodoId()).append(",");
			}
		}
		return grantToolBo.getFailPlayId() + sb.toString();
	}

	@Override
	public String addEquips(String[] equipIds, String[] playerIds) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer("&");

		for (User u : users) {
			for (String systemEquipId : equipIds) {
				String userEquipId = IDGenerator.getID();
				try {
					equipService.addUserEquip(u.getUserId(), userEquipId, Integer.parseInt(systemEquipId), ToolUseType.ADD_ADMIN_ADD);
				} catch (Exception e) {
					sb.append(u.getLodoId()).append("-E").append(systemEquipId).append(",");
				}
			}
		}
		return grantToolBo.getFailPlayId() + sb.toString();
	}

	@Override
	public void sendSysMsg(String content, String partnerIds) {
		messageService.sendSystemMsg(content, partnerIds);
	}

	@Override
	public List<SystemHero> getAllSystemHero() {
		return heroService.getAllSystemHero();
	}

	@Override
	public List<SystemEquip> getAllSystemEquip() {
		return equipService.getAllSystemEquip();
	}

	@Override
	public String getPartnerUserId(int playerId, String serverId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		BigDecimal b = new BigDecimal(Double.parseDouble("0.1"));
		BigDecimal a = new BigDecimal(0.1);
		System.out.println(b.compareTo(a));
	}

	@Override
	public List<SystemTool> getAllOtherTool() {
		return systemToolDao.getSystemToolList();
	}

	@Override
	public String addTools(String[] toolIds, String[] playerIds) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer("&");

		for (User u : users) {
			for (String toolId : toolIds) {
				try {
					SystemTool tool = systemToolDao.get(Integer.parseInt(toolId));
					toolService.addTool(u.getUserId(), tool.getType(), tool.getToolId(), 1, ToolUseType.ADD_ADMIN_ADD);
				} catch (Exception e) {
					sb.append(u.getLodoId()).append("-T").append(toolId).append(",");
				}
			}
		}
		return grantToolBo.getFailPlayId() + sb.toString();
	}

	@Override
	public String addTools(String[] toolIds, String[] playerIds, int nums) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer("&");
		for (User u : users) {
			for (String toolId : toolIds) {
				try {
					SystemTool tool = systemToolDao.get(Integer.parseInt(toolId));
					toolService.addTool(u.getUserId(), tool.getType(), tool.getToolId(), nums, ToolUseType.ADD_ADMIN_ADD);
				} catch (Exception e) {
					sb.append(u.getLodoId()).append("-T").append(toolId).append(",");
				}
			}
		}
		return grantToolBo.getFailPlayId() + sb.toString();
	}

	@Override
	public List<SystemGoldSet> getPaySettings() {
		return systemGoldSetDao.getList(1);
	}

	@Override
	public User getUserInfo(String userId) {
		return userService.get(userId);
	}

	@Override
	public String addUserHeroBag(String[] playerIds, int nums) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		String failPlayerIds = grantToolBo.getFailPlayId();
		for (User u : users) {
			try {
				userService.addUserHeroBag(u.getUserId(), nums * 5);
			} catch (Exception e) {
				failPlayerIds += u.getLodoId() + ",";
			}
		}
		return failPlayerIds;
	}

	@Override
	public String addUserEquipBag(String[] playerIds, int nums) {
		GrantToolBo grantToolBo = getUsersByPlayerIds(playerIds);
		List<User> users = grantToolBo.getList();
		String failPlayerIds = grantToolBo.getFailPlayId();
		for (User u : users) {
			try {
				userService.addUserEquipBag(u.getUserId(), nums * 5);
			} catch (Exception e) {
				failPlayerIds += u.getLodoId() + ",";
			}
		}
		return failPlayerIds;
	}

	@Override
	public List<UserHeroBO> getUserHeroList(String userId) {
		return heroService.getUserHeroList(userId, 0);
	}

	@Override
	public List<UserEquipBO> getUserEquipList(String userId) {

		return equipService.getUserEquipList(userId);
	}

	@Override
	public List<UserToolBO> getUserToolList(String userId) {
		return toolService.getUserToolList(userId);
	}

	@Deprecated
	@Override
	public Map<String, List<AdminSendAttachBO>> addToolsOrEquipMent(List<AdminSendAttachBO> attachList, String[] userIdsAdd, boolean isAllUser) {
		Map<String, List<AdminSendAttachBO>> failMap = new HashMap<String, List<AdminSendAttachBO>>();
		List<String> userIds = null;
		if (isAllUser) {// 全服发放
			userIds = userService.getAllUserIds();
			sendToolsOrEquipMentToUsers(userIds, attachList, failMap);
		} else {
			GrantToolBo grantToolBo = getUsersByUserIds(userIdsAdd);
			List<User> users = grantToolBo.getList();
			// 记录查询用户失败记录
			String failUserId = grantToolBo.getFailPlayId();
			if (failUserId != null && !failUserId.equals("")) {
				String[] failArray = failUserId.split(",");
				for (int m = 0; m < failArray.length; m++) {
					if (failArray[m] != null && !failArray[m].equals("")) {
						failMap.put(failArray[m], attachList);
					}
				}
			}
			userIds = new ArrayList<String>();
			for (User user : users) {
				userIds.add(user.getUserId());
			}
			sendToolsOrEquipMentToUsers(userIds, attachList, failMap);
		}

		return failMap;
	}

	/**
	 * 发放具体动作
	 * 
	 * @param users
	 *            用户列表
	 * @param attachList
	 *            发放的道具或装备列表
	 * @param failMap
	 *            失败map
	 */
	private void sendToolsOrEquipMentToUsers(List<String> usersIds, List<AdminSendAttachBO> attachList, Map<String, List<AdminSendAttachBO>> failMap) {
		List<AdminSendAttachBO> failTempList = null;
		boolean success = false;
		for (String u : usersIds) {
			for (AdminSendAttachBO sendAttachBO : attachList) {
				if (sendAttachBO.getAttachType() == 1) {// 发放道具
					try {
						SystemTool tool = systemToolDao.get(sendAttachBO.getAttachId());
						success = toolService.addTool(u, tool.getType(), tool.getToolId(), sendAttachBO.getAttachNum(), ToolUseType.ADD_ADMIN_ADD);
					} catch (Exception e) {
						success = false;
					}
				} else if (sendAttachBO.getAttachType() == 2) {// 发放装备
					for (int i = 0; i < sendAttachBO.getAttachNum(); i++) {
						String userEquipId = IDGenerator.getID();
						try {
							success = equipService.addUserEquip(u, userEquipId, sendAttachBO.getAttachId(), ToolUseType.ADD_ADMIN_ADD);
						} catch (Exception e) {
							success = false;
							// 记录未发放成功的数量
							sendAttachBO.setAttachNum(sendAttachBO.getAttachNum() - i);
							break;
						}
					}
				} else {
					// 其他类型直接返回失败
					success = false;
				}
				// 记录失败
				if (!success) {
					if (failMap.containsKey(u)) {
						failMap.get(u).add(sendAttachBO);
					} else {
						failTempList = new ArrayList<AdminSendAttachBO>();
						failTempList.add(sendAttachBO);
						failMap.put(u, failTempList);
					}
				}
			}
		}
	}

	@Override
	public String addGoldByUserIds(int gold, String[] userIds) {
		GrantToolBo grantToolBo = getUsersByUserIds(userIds);
		List<User> users = grantToolBo.getList();
		StringBuffer sb = new StringBuffer();
		sb.append(grantToolBo.getFailPlayId());
		for (User u : users) {
			try {
				if (!userService.addGold(u.getUserId(), gold, ToolUseType.ADD_ADMIN_ADD, u.getLevel())) {
					sb.append(u.getUserId()).append(",");
				}
			} catch (Exception e) {
				sb.append(u.getUserId()).append(",");
			}

		}
		return sb.toString();
	}

	@Override
	public SystemMallDiscountActivity addMallDiscount(String activityId, String startTime, String endTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		SystemMallDiscountActivity activity = new SystemMallDiscountActivity();
		activity.setActivityId(activityId);
		try {
			activity.setStartTime(df.parse(startTime));
			activity.setEndTime(df.parse(endTime));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		systemMallDiscountDao.addDiscountActivity(activity);
		return activity;
	}

	@Override
	public List<SystemMallDiscountItems> addMallDiscountItems(String activityId, String mallIds, String discounts) {
		List<SystemMallDiscountItems> itemList = new ArrayList<SystemMallDiscountItems>();

		String[] mallIdArr = mallIds.split(",");
		String[] discountArr = discounts.split(",");

		if (mallIdArr.length != discountArr.length) {

		}

		int len = mallIdArr.length;

		for (int i = 0; i < len; i++) {
			SystemMallDiscountItems item = new SystemMallDiscountItems();
			item.setActivityId(activityId);
			item.setMallId(Integer.valueOf(mallIdArr[i]));
			item.setDiscount(Integer.valueOf(discountArr[i]));

			systemMallDiscountDao.addDiscountItem(item);
			itemList.add(item);
		}

		return itemList;

	}

	@Override
	public void delActivity(String activityId) {
		systemMallDiscountDao.delActivity(activityId);

	}

	@Override
	public void delItems(String activityId) {
		systemMallDiscountDao.delItems(activityId);
	}

	@Override
	public void sendMail(String title, String content, String toolIds, int target, String userLodoIds, String sourceId, Date date, String partner) {
		this.mailService.send(title, content, toolIds, target, userLodoIds, sourceId, date, partner);
	}

	@Override
	public boolean banUser(String userId, String dueTime) {
		boolean success = userService.banUser(userId, dueTime);
		return success;
	}

	@Override
	public boolean assignVipLevel(String userId, int vipLevel) {
		return userService.assignVipLevel(userId, vipLevel, true);
	}

	@Override
	public boolean createUser(String username, String userId) {

		boolean success = userService.create(userId, 1, username, new EventHandle() {

			public boolean handle(Event event) {
				return true;
			}

		});

		return success;
	}

	@Override
	public boolean addActivity(SystemActivity systemActivity) {
		boolean success = systemActivityDao.addActivity(systemActivity);
		commandDao.cacheReload("SystemActivityDaoCacheImpl");
		return success;
	}

	@Override
	public boolean modifyActivity(SystemActivity systemActivity) {
		boolean success = systemActivityDao.modifyActivity(systemActivity);
		commandDao.cacheReload("SystemActivityDaoCacheImpl");
		return success;

	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {
		return this.userService.updateUserLevel(userId, level, exp);
	}

	@Override
	public int modifyForcesTimes(int forcesId, int times) {
		int succ = systemForcesDao.updateTimes(forcesId, times);
		commandDao.cacheReload("SystemForcesDaoCacheImpl");
		return succ;
	}

	@Override
	public boolean dataSync(String table, String sqls) {
		return this.activityService.dataSync(table, sqls);
	}

}
