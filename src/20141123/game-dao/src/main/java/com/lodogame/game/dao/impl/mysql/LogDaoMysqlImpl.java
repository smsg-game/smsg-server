package com.lodogame.game.dao.impl.mysql;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.LogDao;
import com.lodogame.game.utils.TableUtils;

public class LogDaoMysqlImpl implements LogDao {

	private final static String logFormat = "flg[{0}], userId[{1}], useType[2], amount[{3}], success[{4}]";

	private final static String toolLogFormat = "flag[{0}], userId[{1}], useType[{2}], toolType[{3}], toolId[{4}], num[{5}, success[{6}]]";

	private static final Logger logger = Logger.getLogger(LogDaoMysqlImpl.class);

	private static Logger copperLog = Logger.getLogger("copper");

	private static Logger goldLog = Logger.getLogger("gold");

	private static Logger expLog = Logger.getLogger("exp");

	private static Logger toolLog = Logger.getLogger("tool");

	@Autowired
	private Jdbc jdbc;

	public void copperLog(String userId, int useType, int amount, int flag, boolean success) {
		String message = MessageFormat.format(logFormat, flag, userId, useType, amount, success);
		copperLog.info(message);

		String table = TableUtils.getCopperUseLogTable(userId);
		if (success) {// 成功才记数据库
			this.log(table, userId, useType, amount, flag);
		}
	}

	public void goldLog(String userId, int useType, int amount, int flag, boolean success, long beforeAmount, long afterAmount) {
		String message = MessageFormat.format(logFormat, flag, userId, useType, amount, success);
		goldLog.info(message);

		if (success) {// 成功才记数据库
			this.log("user_gold_use_log", userId, useType, amount, flag, beforeAmount, afterAmount);
		}
	}

	public void expLog(String userId, int useType, int amount, boolean success) {
		String message = MessageFormat.format(logFormat, 1, userId, useType, amount, success);
		expLog.info(message);
	}

	private void log(String table, String userId, int useType, int amount, int flag, long beforeAmount, long afterAmount) {

		String sql = "INSERT INTO " + table + "(user_id, use_type, amount, flag, created_time, before_amount, after_amount) " + "VALUES(?, ?, ?, ?, now(), ?, ?)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(useType);
		parameter.setInt(amount);
		parameter.setInt(flag);
		parameter.setLong(beforeAmount);
		parameter.setLong(afterAmount);

		this.jdbc.update(sql, parameter);
	}

	private void log(String table, String userId, int useType, int amount, int flag) {

		String sql = "INSERT INTO " + table + "(user_id, use_type, amount, flag, created_time) " + "VALUES(?, ?, ?, ?, now())";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(useType);
		parameter.setInt(amount);
		parameter.setInt(flag);

		this.jdbc.update(sql, parameter);
	}

	public void toolLog(String userId, int toolType, int toolId, int num, int useType, int flag, String extinfo, boolean success) {

		try {
			String message = MessageFormat.format(toolLogFormat, flag, userId, useType, toolType, toolId, num, success);
			toolLog.info(message);

			if (!success) {
				return;
			}

			String table = TableUtils.getToolUseLogTable(userId);

			String sql = "INSERT INTO " + table + "(user_id, tool_id, tool_type, tool_num, flag, use_type, created_time, extinfo) ";
			sql += "VALUES(?, ?, ?, ?, ?, ?, now(), ?)";

			SqlParameter parameter = new SqlParameter();
			parameter.setString(userId);
			parameter.setInt(toolId);
			parameter.setInt(toolType);
			parameter.setInt(num);
			parameter.setInt(flag);
			parameter.setInt(useType);
			parameter.setString(extinfo);

			this.jdbc.update(sql, parameter);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void heroLog(String userId, String userHeroId, int systemHeroId, int useType, int flag, int heroExp, int heroLevel) {

		String table = TableUtils.getUseHeroLogTable(userId);

		String sql = "INSERT INTO " + table + "(user_id, system_hero_id, use_type, user_hero_id, flag, hero_exp, hero_level, created_time) ";
		sql += "VALUES(?, ?, ?, ?, ?, ?, ?, now())";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(systemHeroId);
		parameter.setInt(useType);
		parameter.setString(userHeroId);
		parameter.setInt(flag);
		parameter.setInt(heroExp);
		parameter.setInt(heroLevel);
		this.jdbc.update(sql, parameter);

	}

	@Override
	public void levelUpLog(String userId, int level, int exp) {

		String sql = "INSERT INTO user_level_up_log(user_id, exp, level, created_time) ";
		sql += "VALUES(?, ?, ?, now())";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(exp);
		parameter.setInt(level);

		this.jdbc.update(sql, parameter);
	}

}
