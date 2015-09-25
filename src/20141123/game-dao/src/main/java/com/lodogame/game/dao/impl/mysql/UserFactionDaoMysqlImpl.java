package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserFactionDao;
import com.lodogame.model.UserFaction;

public class UserFactionDaoMysqlImpl implements UserFactionDao {

	@Autowired
	private Jdbc jdbc;

	private String table = "user_faction";

	@Override
	public List<UserFaction> getFactionMemberByFid(int fid) {
		String sql = "SELECT * FROM " + table + " WHERE faction_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(fid);
		return this.jdbc.getList(sql, UserFaction.class, parameter);
	}

	@Override
	public boolean addUserFaction(UserFaction userFaction) {
		return this.jdbc.insert(userFaction) > 0;
	}

	@Override
	public boolean delUserFaction(String userId, int factionId) {
		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND faction_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(factionId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean delAllUserFactionByFactionId(int fid) {
		String sql = "DELETE FROM " + table + " WHERE faction_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(fid);
		return this.jdbc.update(sql, parameter) > 0;
	}



}
