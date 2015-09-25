package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserMailDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserMailBO;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.MailStatus;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.model.Command;
import com.lodogame.model.SystemMail;
import com.lodogame.model.User;
import com.lodogame.model.UserMail;
import com.lodogame.model.UserMapper;

public class MailServiceImpl implements MailService {

	private static final Logger LOG = Logger.getLogger(MailServiceImpl.class);

	private int sourceId = 0;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private SystemMailDao systemMailDao;

	@Autowired
	private UserMailDao userMailDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Override
	public CommonDropBO receive(String userId, int userMailId) {

		UserMail userMail = this.userMailDao.get(userId, userMailId);
		if (userMail == null) {
			String message = "领取邮件附件错误，邮件不存在.userId[" + userId + "], userMailId[" + userMailId + "]";
			LOG.info(message);
			throw new ServiceException(RECEIVE_MAIL_ERROR_MAIL_NOT_EXISTS, message);
		}

		SystemMail systemMail = this.systemMailDao.get(userMail.getSystemMailId());
		if (systemMail == null) {
			String message = "领取邮件附件错误，邮件对应的系统邮件不存在.userId[" + userId + "], userMailId[" + userMailId + "], systemMailId[" + userMail.getSystemMailId() + "]";
			LOG.info(message);
			throw new ServiceException(RECEIVE_MAIL_ERROR_MAIL_NOT_EXISTS, message);
		}

		if (userMail.getReceiveStatus() != MailStatus.STATUS_NOT_RECEIVE) {
			String message = "领取邮件附件错误，邮件已经领取.userId[" + userId + "], userMailId[" + userMailId + "]";
			LOG.info(message);
			throw new ServiceException(RECEIVE_MAIL_ERROR_MAIL_HAD_RECEIVE, message);
		}

		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemMail.getToolIds());
		if (dropToolBOList.isEmpty()) {
			String message = "领取邮件附件错误，邮件没有任何附件.userId[" + userId + "], userMailId[" + userMailId + "]";
			LOG.info(message);
			throw new ServiceException(RECEIVE_MAIL_ERROR_MAIL_NOT_REWARD, message);
		}

		if (!this.userMailDao.updateReceiveStatus(userId, userMailId, MailStatus.STATUS_RECEIVE)) {
			String message = "领取邮件附件错误，邮件已经领取.userId[" + userId + "], userMailId[" + userMailId + "]";
			LOG.info(message);
			throw new ServiceException(RECEIVE_MAIL_ERROR_MAIL_HAD_RECEIVE, message);
		}

		CommonDropBO commonDropBO = new CommonDropBO();

		for (DropToolBO dropToolBO : dropToolBOList) {

			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> drs = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_MAIL_REWARD);
			for (DropToolBO bo : drs) {
				this.toolService.appendToDropBO(userId, commonDropBO, bo);
			}
		}

		return commonDropBO;

	}

	@Override
	public void read(String userId, int userMailId) {
		this.userMailDao.updateStatus(userId, userMailId, MailStatus.STATUS_READ);
	}

	@Override
	public void delete(String userId, int userMailId) {
		this.userMailDao.updateStatus(userId, userMailId, MailStatus.STATUS_DEL);
	}

	@Override
	public List<UserMailBO> getMailList(String userId, int lastId) {

		Date lastReceiveTime = this.userMailDao.getLastReceiveTime(userId);
		User user = this.userDao.get(userId);
		if (lastReceiveTime == null) {
			// lastReceiveTime = DateUtils.str2Date("1971-01-01 00:00:00");
			lastReceiveTime = user.getRegTime();
		}

		Date now = new Date();

		this.userMailDao.setLastReceiveTime(userId, now);

		List<SystemMail> systemMailList = this.systemMailDao.getSystemMailByTime(lastReceiveTime);

		if (!systemMailList.isEmpty()) {

			List<UserMail> list = new ArrayList<UserMail>();

			for (SystemMail systemMail : systemMailList) {

				if (!needAdd(user, systemMail)) {
					continue;
				}

				if (this.userMailDao.getBySystemMailId(userId, systemMail.getSystemMailId()) != null) {
					continue;
				}

				UserMail userMail = new UserMail();
				userMail.setStatus(0);
				userMail.setSystemMailId(systemMail.getSystemMailId());
				userMail.setUpdatedTime(now);
				userMail.setUserId(userId);
				userMail.setReceiveStatus(0);
				userMail.setCreatedTime(now);

				list.add(userMail);
			}

			this.userMailDao.add(list);
		}

		List<UserMail> userMailList = this.userMailDao.getList(userId);

		List<UserMailBO> userMailBOList = new ArrayList<UserMailBO>();
		for (UserMail userMail : userMailList) {

			if (userMail.getUserMailId() <= lastId) {
				continue;
			}

			UserMailBO userMailBO = new UserMailBO();

			String systemMailId = userMail.getSystemMailId();
			SystemMail systemMail = this.systemMailDao.get(systemMailId);
			if (systemMail == null) {
				continue;
			}

			Date expiredTime = DateUtils.addDays(systemMail.getCreatedTime(), 15);
			if (expiredTime.before(now)) {// 已经过期
				continue;
			}

			userMailBO.setUserMailId(userMail.getUserMailId());
			userMailBO.setStatus(userMail.getStatus());
			userMailBO.setReceiveStatus(userMail.getReceiveStatus());
			userMailBO.setCreatedTime(systemMail.getCreatedTime());
			userMailBO.setType(2);
			userMailBO.setExpiredTime(expiredTime);

			userMailBO.setTitle(systemMail.getTitle());
			userMailBO.setContent(systemMail.getContent());
			userMailBO.setDropToolBOList(DropToolHelper.parseDropTool(systemMail.getToolIds()));

			userMailBOList.add(userMailBO);
		}

		return userMailBOList;

	}

	/**
	 * 是否满意收邮件的条件
	 * 
	 * @param userId
	 * @param systemMail
	 * @return
	 */
	private boolean needAdd(User user, SystemMail systemMail) {

		int target = systemMail.getTarget();

		if (target == MailTarget.USERS) {
			return false;
		} else if (target == MailTarget.ALL) {
			if (user.getRegTime().before(systemMail.getMailTime())) {
				return true;
			}
		} else if (target == MailTarget.LOGIN) {
			if (this.userOnlineLogDao.isLogin(user.getUserId(), systemMail.getMailTime())) {
				return true;
			}
		} else if (target == MailTarget.PAYMENT) {
			Date startTime = DateUtils.getDateAtMidnight(systemMail.getMailTime());
			Date endTime = DateUtils.getDateAtMidnight(DateUtils.addDays(systemMail.getMailTime(), 1));

			if (this.paymentLogDao.getPaymentTotalByTime(user.getUserId(), startTime, endTime) > 0) {
				return true;
			}
		} else if (target == MailTarget.PARTNER) {
			UserMapper userMapper = userMapperDao.get(user.getUserId());
			if (userMapper.getPartnerId().equals(systemMail.getPartner())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 新邮件通知
	 */
	private void notifyNewMail(int type, Object object) {

		Map<String, String> params = new HashMap<String, String>();

		if (type == CommandType.PUSH_PARTNER) {
			params.put("partner", (String) object);
		} else {
			params.put("date", DateUtils.getTimeStr((Date) object));
		}

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_NEW_MAIL);
		command.setType(type);
		command.setPriority(Priority.HIGH);
		command.setParams(params);

		commandDao.add(command);

	}
	
	/**
	 * 新邮件通知
	 */
	public void notifyNewMail(List<String> userIdList) {

		for (String userId : userIdList) {

			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", userId);

			Command command = new Command();
			command.setCommand(CommandType.COMMAND_PUSH_NEW_MAIL);
			command.setType(CommandType.PUSH_USER);
			command.setPriority(Priority.HIGH);
			command.setParams(params);

			commandDao.add(command);
		}
	}

	private synchronized int getSourceId() {

		if (sourceId != 0) {
			sourceId += 1;
			return sourceId;
		}

		sourceId = systemMailDao.getMaxSourceId();
		if (sourceId < 1000000) {
			sourceId = 1000000;
		} else {
			sourceId += 1;
		}

		return sourceId;
	}

	@Override
	public boolean hasNewMail(String userId) {

		List<UserMailBO> userMailBOList = this.getMailList(userId, 0);

		for (UserMailBO userMailBO : userMailBOList) {
			if (userMailBO.getStatus() == MailStatus.STATUS_NEW) {
				return true;
			}
		}

		return false;

	}

	@Override
	public List<Integer> delete(String userId) {

		List<UserMail> userMailList = this.userMailDao.getList(userId);
		List<Integer> mailIdList = new ArrayList<Integer>();

		for (UserMail userMail : userMailList) {
			if (userMail.getStatus() == MailStatus.STATUS_READ) {
				if (userMail.getReceiveStatus() == MailStatus.STATUS_RECEIVE) {

					mailIdList.add(userMail.getUserMailId());

				} else {
					SystemMail systemMail = this.systemMailDao.get(userMail.getSystemMailId());
					if (systemMail == null || StringUtils.isEmpty(systemMail.getToolIds())) {
						mailIdList.add(userMail.getUserMailId());
					}

				}
			}
		}

		if (!mailIdList.isEmpty()) {
			this.userMailDao.updateStatus(userId, mailIdList, MailStatus.STATUS_DEL);
		}

		return mailIdList;
	}

	@Override
	public void send(String title, String content, String toolIds, int target, String userLodoIds, String sourceId, Date date, String partner) {

		Date now = new Date();

		SystemMail systemMail = new SystemMail();

		String systemMailId = IDGenerator.getID();
		systemMail.setSystemMailId(systemMailId);
		systemMail.setContent(content);
		systemMail.setTarget(target);
		systemMail.setTitle(title);
		if (!StringUtils.isEmpty(sourceId)) {
			systemMail.setSourceId(sourceId);
		} else {
			systemMail.setSourceId(IDGenerator.getID());
		}
		systemMail.setToolIds(toolIds);
		systemMail.setLodoIds(userLodoIds);
		systemMail.setMailTime(date);

		systemMail.setCreatedTime(now);
		systemMail.setUpdatedTime(now);
		systemMail.setPartner(partner);

		this.systemMailDao.add(systemMail);

		if (target == MailTarget.ALL) {

			notifyNewMail(CommandType.PUSH_ALL, systemMail.getMailTime());

		}
		if (target == MailTarget.LOGIN) {

			notifyNewMail(CommandType.PUSH_LOGIN, systemMail.getMailTime());

		}
		if (target == MailTarget.PAYMENT) {

			notifyNewMail(CommandType.PUSH_PAY, systemMail.getMailTime());

		}
		if (target == MailTarget.PARTNER) {

			notifyNewMail(CommandType.PUSH_PARTNER, systemMail.getPartner());
		} else {

			List<String> userIdList = new ArrayList<String>();

			String[] lodoIds = StringUtils.split(userLodoIds, ",");
			for (String s : lodoIds) {
				User user = this.userDao.getByPlayerId(s);
				if (user != null) {
					UserMail userMail = new UserMail();
					userMail.setUserId(user.getUserId());
					userMail.setSystemMailId(systemMailId);
					userMail.setStatus(MailStatus.STATUS_NEW);
					userMail.setReceiveStatus(MailStatus.STATUS_NOT_RECEIVE);
					userMail.setCreatedTime(now);
					userMail.setUpdatedTime(now);

					List<UserMail> userMailList = new ArrayList<UserMail>();
					userMailList.add(userMail);
					userIdList.add(user.getUserId());

					this.userMailDao.add(userMailList);
				}
			}

			notifyNewMail(userIdList);
		}

	}
	
	

}
