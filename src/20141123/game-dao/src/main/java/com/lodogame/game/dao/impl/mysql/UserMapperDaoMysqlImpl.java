package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.model.UserMapper;

public class UserMapperDaoMysqlImpl implements UserMapperDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "user_mapper";

	public final static String columns = "*";

	@Override
	public boolean save(UserMapper userMapper) {
		jdbc.insert(userMapper);
		return true;
	}

	
	
	@Override
	public UserMapper getByPartnerUserId(String partnerUserId, String partnerId, String serverId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE partner_user_id = ? AND partner_id = ? AND server_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(partnerUserId);
		parameter.setString(partnerId);
		parameter.setString(serverId);

		return this.jdbc.get(sql, UserMapper.class, parameter);
	}

	@Override
	public UserMapper get(String userId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, UserMapper.class, parameter);
	}

	@Override
	public boolean delete(String userId) {

		String sql = "DELETE FROM " + table + " WHERE user_id = ? LIMIT 1;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updatePhoneInfo(String userId, String imei, String mac, String idfa) {
		String sql = "UPDATE " + table + " SET imei = ?, mac = ?, idfa = ? where user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(imei);
		parameter.setString(mac);
		parameter.setString(idfa);
		parameter.setString(userId);
		return this.jdbc.update(sql, parameter) > 0;
	}

}
