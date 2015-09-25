package com.lodogame.ldsg.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.LogOperatorDao;
import com.lodogame.game.dao.LogPoolDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.ldsg.bo.ActivityDrawToolBO;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.model.Command;
import com.lodogame.model.HeroLog;
import com.lodogame.model.PaymentLog;
import com.lodogame.model.UserLevelUpLog;

public class UnSynLogServiceImpl implements UnSynLogService {

	/**
	 * 插入内部日志库用到的dao
	 */
	@Autowired
	private CommandDao commandDao;

	@Autowired
	private LogPoolDao logPoolDao;

	/**
	 * 插入外部日志库用到的dao
	 */
	@Autowired
	private LogOperatorDao logOperatorDaoRedisImpl;

	public void copperLog(String userId, int useType, int amount, int flag, boolean success) {
		String table = TableUtils.getCopperUseLogTable(userId);
		if (success) {// 成功才记数据库
			this.unSynSaveLog(table, userId, useType, amount, flag);
			this.userCopperLog(userId, useType + "", flag * amount, new Date());
		}
	}

	public void goldLog(String userId, int userLevel, int useType, int amount, int flag, boolean success) {
		if (success) {// 成功才记数据库
			// this.unSynSaveGoldLog("user_gold_use_log", userId, useType,
			// amount, flag);
			this.userGoldLog(userId, userLevel, flag > 0 ? 1 : 2, useType, amount, new Date());
		}
	}

	/**
	 * 异步存日志
	 * 
	 * @param userId
	 * @param useType
	 * @param amount
	 * @param flag
	 */
	private void unSynSaveLog(String table, String userId, int useType, int amount, int flag) {

		String sql = "INSERT INTO " + table + "(user_id, use_type, amount, flag, created_time) " + "VALUES('" + userId + "', " + useType + ", " + amount + ", " + flag + ", now())";
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_SAVE_LOG);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sql", sql);

		command.setParams(params);
		commandDao.add(command);
	}

	public void toolLog(String userId, int toolType, int toolId, int num, int useType, int flag, String extinfo, boolean success) {
		if (!success) {
			return;
		}
		// 批量处理 获得英雄和装备的操作日志在外面已经插了 这里不需要插了
		if ((toolType == ToolType.EQUIP || toolType == ToolType.HERO) && toolId == 0 && flag > 0) {
			// 符合该条件的日志 外面插了 这里不用插了
		} else {
			this.userTreasureLog(userId, toolId, toolType, flag > 0 ? 1 : 2, useType + "", num, extinfo, new Date());
		}

		String table = TableUtils.getToolUseLogTable(userId);
		String sql = "INSERT INTO " + table + "(user_id, tool_id, tool_type, tool_num, flag, use_type, created_time, extinfo) ";
		sql += "VALUES('" + userId + "', " + toolId + "," + toolType + ", " + num + ", " + flag + ", " + useType + ", now(), '" + extinfo + "')";

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_SAVE_LOG);
		Map<String, String> params = new HashMap<String, String>();
		params.put("sql", sql);

		command.setParams(params);
		commandDao.add(command);

	}

	@Override
	public void heroLog(String userId, String userHeroId, int systemHeroId, int useType, int flag, int heroExp, int heroLevel) {

		HeroLog log = new HeroLog();
		log.setUserId(userId);
		log.setUserHeroId(userHeroId);
		log.setSystemHeroId(systemHeroId);
		log.setUseType(useType);
		log.setHeroExp(heroExp);
		log.setHeroLevel(heroLevel);
		log.setFlag(flag);
		logPoolDao.add(log);
	}

	@Override
	public void levelUpLog(String userId, int exp, int level) {

		UserLevelUpLog log = new UserLevelUpLog();
		log.setUserId(userId);
		log.setLevel(level);
		log.setExp(exp);
		logPoolDao.add(log);
	}

	@Override
	public void userRegLog(String userId, String userName, String parteneId, String qn, Date regTime) {
		String time = DateUtils.getTimeStr(regTime);
		String sql = "INSERT INTO user_reg_log(USER_ID,USER_NAME,CHANNEL,SMALL_CHANNEL,REG_TIME) VALUES('" + userId + "','" + userName + "','" + parteneId + "','" + qn + "','" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userCreatRoleLog(String userId, String userName, String name, int sex, Date regTime, long loadId) {
		String time = DateUtils.getTimeStr(regTime);
		String sql = "INSERT INTO user(USER_ID,USER_NAME,NAME,SEX,LODO_ID,REG_TIME) VALUES('" + userId + "', '" + userName + "', '" + name + "', " + sex + "," + loadId + ", '" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userLoginLog(String userId, int level, String ip, Date loginTime) {
		String time = DateUtils.getTimeStr(loginTime);
		String sql = "INSERT INTO user_login_log(USER_ID,LEVEL,IP,LOGIN_TIME) VALUES('" + userId + "'," + level + ",'" + ip + "','" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userLogOutLog(String userId, String ip, Date loginOutTime, int liveMinutes) {
		String time = DateUtils.getTimeStr(loginOutTime);
		String sql = "INSERT INTO user_logout_log(USER_ID,IP,LOGOUT_TIME,LIVE_MINUTES) VALUES('" + userId + "','" + ip + "','" + time + "'," + liveMinutes + ")";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userLevelUpLog(Date levelTime, String userId, int level) {
		String time = DateUtils.getTimeStr(levelTime);
		String sql = "INSERT INTO user_levelup_log(TIME,USER_ID,LEVEL) VALUES('" + time + "','" + userId + "'," + level + ")";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userPayLog(PaymentLog paymentLog, int userLevel) {
		String time = DateUtils.getTimeStr(paymentLog.getCreatedTime());

		String sql = "INSERT INTO payment_log(PARTNER_ID,SERVER_ID,PARTNER_USER_ID,USER_ID,USER_NAME,USER_LEVEL,CHANNEL,ORDER_ID,AMOUNT,GOLD,USER_IP,REMARK,CREATED_TIME) " + "VALUES('" + paymentLog.getPartnerId()
				+ "','" + paymentLog.getServerId() + "','" + paymentLog.getPartnerUserId() + "','" + paymentLog.getUserId() + "','" + paymentLog.getUserName() + "'," + userLevel + ",'" + paymentLog.getChannel() + "','"
				+ paymentLog.getOrderId() + "'," + paymentLog.getAmount() + "," + paymentLog.getGold() + ",'" + paymentLog.getUserIp() + "','" + paymentLog.getRemark() + "','" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userEquipMentOperatorLog(String userId, String userEquipMentId, int equipMentId, String userHeroId, int equipMentLevel, int type, Date operatorTime) {
		String time = DateUtils.getTimeStr(operatorTime);
		String now = DateUtils.getDateForLog();
		String sql = "INSERT INTO user_equipment_log_" + now + "(USER_ID,USER_EQUIPMENT_ID,EQUIPMENT_ID,USER_HERO_ID,EQUIPMENT_LEVEL,TYPE,OPERATION_TIME) VALUES" + "('" + userId + "','" + userEquipMentId + "',"
				+ equipMentId + ",'" + userHeroId + "'," + equipMentLevel + "," + type + ",'" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userHeroOperatorLog(String userId, String userHeroId, int heroId, int type, long exp, int level, int pos, int expNum, Date opratorTime, String heroName) {
		String time = DateUtils.getTimeStr(opratorTime);
		String now = DateUtils.getDateForLog();
		String sql = "INSERT INTO user_hero_log_" + now + "(USER_ID,USER_HERO_ID,HERO_ID,TYPE,EXP,LEVEL,POS,EXP_NUM,OPERATION_TIME,HERO_NAME) VALUES" + "('" + userId + "','" + userHeroId + "'," + heroId + "," + type
				+ "," + exp + "," + level + "," + pos + "," + expNum + ",'" + time + "','" + heroName + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	public void userGoldLog(String userId, int userLevel, int getOrUse, int type, int changeNum, Date opratorTime) {
		String time = DateUtils.getTimeStr(opratorTime);
		String sql = "INSERT INTO user_gold_log(USER_ID,USER_LEVEL,CATEGORY,TYPE,CHANGE_NUM,TIME) VALUES('" + userId + "'," + userLevel + "," + getOrUse + "," + type + "," + changeNum + ",'" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	public void userCopperLog(String userId, String type, int money, Date opratorTime) {
		String time = DateUtils.getTimeStr(opratorTime);
		String now = DateUtils.getDateForLog();
		String sql = "INSERT INTO user_resource_log_" + now + "(USER_ID,OPERATION,MONEY,CREATE_TIME) VALUES('" + userId + "','" + type + "'," + money + ",'" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	public void userTreasureLog(String userId, int treasureId, int treasureType, int getOrUse, String oprationType, int changeNum, String extInfo, Date oprationTime) {
		String time = DateUtils.getTimeStr(oprationTime);
		String now = DateUtils.getDateForLog();
		String sql = "INSERT INTO user_treasure_log_" + now + "(USER_ID,TREASURE_ID,TREASURE_TYPE,CATEGORY,OPERATION,CHANGE_NUM,EXTINFO,CREATE_TIME) " + "VALUES('" + userId + "'," + treasureId + "," + treasureType + ","
				+ getOrUse + ",'" + oprationType + "'," + changeNum + ",'" + extInfo + "','" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	public void userOnlineLog(Date oprationTime, int userAmount, String userIdStr) {
		String time = DateUtils.getTimeStr(oprationTime);
		String sql = "INSERT INTO user_online_log(TIME,ONLINE_AMOUNT,USER_STR) " + "VALUES('" + time + "'," + userAmount + ",'" + userIdStr + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	public void userBattleLog(String userId, int userLevel, String targetId, int type, int flag, Date creatTime) {
		String time = DateUtils.getTimeStr(creatTime);
		String now = DateUtils.getDateForLog();
		String sql = "INSERT INTO user_battle_report_" + now + "(USER_ID,USER_LEVEL,TARGET_ID,TYPE,FLAG,CREAT_TIME) " + "VALUES('" + userId + "'," + userLevel + ",'" + targetId + "'," + type + "," + flag + ",'" + time
				+ "')";

		logOperatorDaoRedisImpl.add(sql);
	}

	public void userVipLevelLog(String userId, int vipLevel, Date creatTime) {
		String time = DateUtils.getTimeStr(creatTime);
		String sql = "INSERT INTO user_vip_log(USER_ID,VIP_LEVEL,TIME)  VALUES('" + userId + "'," + vipLevel + ",'" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	public void userMallLog(String userId, int userLevel, int toolId, int buyNum, int cost, int costCopperNum, Date creatTime) {
		String time = DateUtils.getTimeStr(creatTime);
		String sql = "INSERT INTO user_mall_log(USER_ID,USER_LEVEL,TREASURE_ID,BUY_NUM,COST,COSTCOPPER,TIME) " + "VALUES('" + userId + "'," + userLevel + "," + toolId + "," + buyNum + "," + cost + "," + costCopperNum
				+ ",'" + time + "')";
		logOperatorDaoRedisImpl.add(sql);
	}

	@Override
	public void userActionLog(String userId, int userLevel, Date creatTime, String ip, int actionId) {
		String time = DateUtils.getTimeStr(creatTime);
		String sql = "INSERT INTO user_action_log(USER_ID,USER_LEVEL,IP,SOURCE,ACTION_ID,TIME) " + "VALUES('" + userId + "'," + userLevel + ",'" + ip + "'," + 0 + "," + actionId + ",'" + time + "')";

		logOperatorDaoRedisImpl.add(sql);

	}

	public void creatTable() {

	}

	@Override
	public void userDrawLog(List<ActivityDrawToolBO> drawToolBOList, String userId, int times) {
		for (ActivityDrawToolBO entity : drawToolBOList) {
			String sql = "insert into activity_draw_log(user_id,tool_id,tool_num,create_time,tool_type,out_id,times) ";
			sql += "VALUES('" + userId + "', " + entity.getToolId() + "," + entity.getToolNum() + ", '" + DateUtils.getTime() + "', " + entity.getToolType() + ", " + entity.getOutId() + ", " + times + ")";

			Command command = new Command();
			command.setCommand(CommandType.COMMAND_SAVE_LOG);
			Map<String, String> params = new HashMap<String, String>();
			params.put("sql", sql);
			command.setParams(params);
			commandDao.add(command);
		}
	}
}
