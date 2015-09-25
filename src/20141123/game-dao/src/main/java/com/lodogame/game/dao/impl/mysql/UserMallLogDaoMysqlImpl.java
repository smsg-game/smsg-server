package com.lodogame.game.dao.impl.mysql;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserMallLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.UserMallLog;

public class UserMallLogDaoMysqlImpl implements UserMallLogDao {

	private final static String table = "user_mall_log";

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean add(UserMallLog userMallLog) {
		return this.jdbc.insert(userMallLog) > 0;
	}

	/**
	 * 获取该用户今日的商城日志信息 初始化缓存用的
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserMallLog> getUserTodayMallLogList(String userId) {

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND created_time > ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(DateUtils.getDateAtMidnight());
		return this.jdbc.getList(sql, UserMallLog.class, parameter);
	}

	@Override
	public Map<Integer, Integer> getUserTodayPurchaseNum(String userId) {
		throw new NotImplementedException();
	}

}
