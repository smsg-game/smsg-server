package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.GiftCodeDao;
import com.lodogame.model.GiftCode;

public class GiftCodeDaoMysqlImpl implements GiftCodeDao {

	@Autowired
	private Jdbc jdbcCommon;

	@Override
	public GiftCode get(String code) {

		String sql = "SELECT * FROM gift_code WHERE code = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(code);

		return this.jdbcCommon.get(sql, GiftCode.class, parameter);

	}

	@Override
	public boolean update(String code, String userId) {

		String sql = "UPDATE gift_code SET flag = 1, user_id = ?, updated_time = now() WHERE code = ? AND flag = 0";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(code);

		return this.jdbcCommon.update(sql, parameter) > 0;

	}

}
