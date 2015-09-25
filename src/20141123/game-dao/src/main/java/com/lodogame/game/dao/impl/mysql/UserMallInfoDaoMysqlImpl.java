package com.lodogame.game.dao.impl.mysql;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserMallInfoDao;
import com.lodogame.model.UserMallInfo;

public class UserMallInfoDaoMysqlImpl implements UserMallInfoDao {

	@Autowired
	private Jdbc jdbc;

	private String table = "user_mall_info";

	@Override
	public boolean add(String userId, int mallId, int totalBuyNum, int dayBuyNum) {

		String sql = "INSERT INTO " + table + "(user_id, mall_id, total_buy_num, day_buy_num, created_time, updated_time) VALUES(?, ?, ?, ?, now(), now()) ";
		sql += "ON DUPLICATE KEY update total_buy_num = VALUES(total_buy_num), day_buy_num = VALUES(day_buy_num), updated_time = now()";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(mallId);
		parameter.setInt(totalBuyNum);
		parameter.setInt(dayBuyNum);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean add(UserMallInfo userMallInfo) {
		throw new NotImplementedException();
	}

	@Override
	public UserMallInfo get(String userId, int systemMallId) {

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND mall_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(systemMallId);

		return this.jdbc.get(sql, UserMallInfo.class, parameter);

	}

}
