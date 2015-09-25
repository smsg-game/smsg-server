package com.lodogame.ldsg.handler.event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CallbackRewardDao;
import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.PkGroupAwardLogDao;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.ForcesRankBO;
import com.lodogame.ldsg.bo.PkInfoBO;
import com.lodogame.ldsg.bo.PowerRankBO;
import com.lodogame.ldsg.bo.WealthRankBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.LoginEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.RankService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.CallbackReward;
import com.lodogame.model.PkGroupAwardLog;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.User;
import com.lodogame.model.UserOnlineLog;

public class LoginEventHandle implements EventHandle {

	private static final Logger logger = Logger.getLogger(LoginEventHandle.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PkService pkService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RankService rankService;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private CallbackRewardDao callbackRewardDao;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	private PkGroupAwardLogDao pkGroupAwardLogDao;

	private User getUser(String userId) {
		try {
			return userService.get(userId);
		} catch (ServiceException e) {
			logger.warn(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public boolean handle(Event event) {

		if (!(event instanceof LoginEvent)) {
			return true;
		}

		String userId = event.getUserId();
		User user = this.getUser(userId);
		if (user == null) {
			return false;
		}
		PkGroupAwardLog pkGroupAwardLog = pkGroupAwardLogDao.get(userId);

		// 老玩家召回奖励
		sendOldPlayerBackReward(userId);

		// 保存登录日志
		String userIp = event.getString("userIp");
		saveOnlineLog(userId, userIp);

		// 争霸称号玩家上线
		if (pkGroupAwardLog != null) {
			if (!StringUtils.isBlank(pkGroupAwardLog.getTitle()) && !pkGroupAwardLog.getTitle().equals("0")) {
				messageService.sendPkUserLogin(pkGroupAwardLog.getTitle(), user.getUsername());
			}
		}

		// 世界广播之顶级玩家登录(争霸赛)
		PkInfoBO upk = pkService.getUserPkInfo(userId);
		if (upk != null && upk.getRank() > 0 && upk.getRank() < PkService.TITLE_ZH_CN.length) {

			if (Config.ins().getIsHK() == 1) {
				messageService.sendRankUserLoginMsg(userId, PkService.TITLE_ZH_HK[upk.getRank()], user.getUsername());
			} else {
				messageService.sendRankUserLoginMsg(userId, PkService.TITLE_ZH_CN[upk.getRank()], user.getUsername());
			}
			return true;
		}

		// 战斗力第一名
		List<PowerRankBO> powerRankBOList = rankService.getPowerRanks();
		if (powerRankBOList != null && powerRankBOList.size() > 0) {
			PowerRankBO powerRankBO = powerRankBOList.get(0);
			if (powerRankBO.getUsername().equals(user.getUsername())) {

				if (Config.ins().getIsHK() == 1) {
					messageService.sendRankUserLoginMsg(userId, "戰神--戰鬥力排行榜第一位 ", user.getUsername());
				} else {
					messageService.sendRankUserLoginMsg(userId, "战神--战斗力排行榜第一位 ", user.getUsername());
				}

				return true;
			}
		}

		// 财富排行第一名
		List<WealthRankBO> wealthRankBOList = rankService.getWealthRanks();
		if (wealthRankBOList != null && wealthRankBOList.size() > 0) {
			WealthRankBO wealthRankBO = wealthRankBOList.get(0);
			if (wealthRankBO.getUsername().equals(user.getUsername())) {

				if (Config.ins().getIsHK() == 1) {
					messageService.sendRankUserLoginMsg(userId, "富可敵國--財富排行榜第一位 ", user.getUsername());

				} else {
					messageService.sendRankUserLoginMsg(userId, "富可敌国--财富排行榜第一位 ", user.getUsername());
				}

				return true;
			}
		}

		// 关卡排行榜第一名
		List<ForcesRankBO> forcesRankBOList = rankService.getForcesRanks();
		if (forcesRankBOList != null && forcesRankBOList.size() > 0) {
			ForcesRankBO forcesRankBO = forcesRankBOList.get(0);
			if (forcesRankBO.getUsername().equals(user.getUsername())) {
				if (Config.ins().getIsHK() == 1) {
					messageService.sendRankUserLoginMsg(userId, "破千軍--關卡排行榜第一位", user.getUsername());

				} else {
					messageService.sendRankUserLoginMsg(userId, "破千军--关卡排行榜第一位", user.getUsername());
				}
				return true;
			}
		}

		return true;
	}

	private void saveOnlineLog(String userId, String userIp) {

		Date now = new Date();

		// 添加新的登录日志
		UserOnlineLog userOnlineLog = new UserOnlineLog();
		userOnlineLog.setUserId(userId);
		userOnlineLog.setLoginTime(now);
		userOnlineLog.setLogoutTime(DateUtils.add(now, Calendar.SECOND, 1));
		userOnlineLog.setUserIp(userIp);
		this.userOnlineLogDao.add(userOnlineLog);

	}

	/**
	 * 发送老玩家召回奖励
	 * 
	 * @param userId
	 */
	private void sendOldPlayerBackReward(String userId) {

		Date now = new Date();
		UserOnlineLog userOnlineLog = userOnlineLogDao.getLastOnlineLog(userId);

		User user = this.getUser(userId);

		int dayDiff = 0;
		if (userOnlineLog != null) {
			dayDiff = DateUtils.getDayDiff(userOnlineLog.getLogoutTime(), now);
		} else {
			dayDiff = DateUtils.getDayDiff(user.getPowerAddTime(), now);
		}

		if (dayDiff < 7) {
			return;
		}

		// 没有活动
		if (!isOldPlayerBackRewardActivityOpen()) {
			return;
		}

		CallbackReward callbackReward = callbackRewardDao.getReward(dayDiff, user.getLevel());
		if (callbackReward == null) {
			logger.error("老玩家召回活动奖励获取不到,day[" + dayDiff + "], level[" + user.getLevel() + "]");
			return;
		}

		String title = configDataDao.getString(ConfigKey.CALLBACK_MAIL_TITLE, "邮件标题");
		String content = configDataDao.getString(ConfigKey.CALLBACK_MAIL_CONTENT, "邮件内容");

		mailService.send(title, content, callbackReward.getToolIds(), MailTarget.USERS, user.getLodoId() + "", IDGenerator.getID(), new Date(), null);

	}

	private boolean isOldPlayerBackRewardActivityOpen() {

		SystemActivity systemActivity = systemActivityDao.get(ActivityId.OLD_PLAYER_CALL_BACK_ID);
		// 检测充值返利活动
		if (activityService.checkActivityIsOpenAdd(systemActivity) == 1) {
			return true;
		}
		return false;
	}

	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
