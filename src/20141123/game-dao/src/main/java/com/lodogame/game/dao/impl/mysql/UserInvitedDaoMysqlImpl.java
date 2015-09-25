package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserInvitedDao;
import com.lodogame.model.UserInvited;

public class UserInvitedDaoMysqlImpl implements UserInvitedDao {

	@Autowired
	private Jdbc jdbc;

	private final static String table = "user_invited";

	@Override
	public boolean add(UserInvited userInvited) {
		return this.jdbc.insert(userInvited) > 0;
	}

	@Override
	public UserInvited getByInvitedUserId(String invitedUserId) {
		String sql = "SELECT * FROM " + table + " WHERE invited_user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(invitedUserId);
		return this.jdbc.get(sql, UserInvited.class, parameter);
	}

	@Override
	public boolean update(String invitedUserId, String finishTaskIds) {

		String sql = "UPDATE " + table + " SET finish_task_ids = ? WHERE invited_user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(finishTaskIds);
		parameter.setString(invitedUserId);
		return this.jdbc.update(sql, parameter) > 0;
	}

}
