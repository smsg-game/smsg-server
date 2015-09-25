package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserApplyFactionDao;
import com.lodogame.model.UserApplyFaction;

public class UserApplyFactionDaoMysqlImpl implements UserApplyFactionDao {

	@Autowired
	private Jdbc jdbc;

	private String table = "user_apply_faction";

	@Override
	public List<UserApplyFaction> getApplyFactionByFid(int fid) {
		String sql = "SELECT * FROM " + table + " WHERE faction_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(fid);
		return this.jdbc.getList(sql, UserApplyFaction.class, parameter);
	}

	@Override
	public boolean addUserApplyFaction(UserApplyFaction userApplyFaction) {
		return this.jdbc.insert(userApplyFaction) > 0;
	}

	@Override
	public boolean delUserApplyFaction(String userId, int factionId) {
		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND faction_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(factionId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean delUserApplyFaction(String userId) {
		String sql = "DELETE FROM " + table + " WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<UserApplyFaction> getUserApplyFactionByUserId(String userId) {
		String sql = "SELECT faction_id FROM " + table + " WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.getList(sql, UserApplyFaction.class, parameter);
	}

}
