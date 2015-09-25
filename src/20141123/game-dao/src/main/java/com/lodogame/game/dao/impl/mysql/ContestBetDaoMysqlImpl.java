package com.lodogame.game.dao.impl.mysql;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestBetDao;
import com.lodogame.model.UserContestBetLog;

public class ContestBetDaoMysqlImpl implements ContestBetDao{
	
	@Autowired
	private Jdbc jdbc;
	
	
	@Override
	public void saveContestBetLog(UserContestBetLog betLog) {
		this.jdbc.insert(betLog);
	}

	@Override
	public List<String> getGoodBetUserIdList(int session, String champUserId) {
		String sql = "SELECT * FROM user_contest_bet_log WHERE session = ? AND bet_on_user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(session);
		parameter.setString(champUserId);
		
		List<UserContestBetLog> betLogList = this.jdbc.getList(sql, UserContestBetLog.class, parameter);
		List<String> userIdList = new ArrayList<String>();
				
		for (UserContestBetLog betLog : betLogList) {
			userIdList.add(betLog.getUserId());
		}
		
		return userIdList;
	}

	@Override
	public UserContestBetLog getUserBetLog(int session, String userId) {
		String sql = "SELECT * FROM user_contest_bet_log WHERE session = ? AND user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(session);
		parameter.setString(userId);

		return this.jdbc.get(sql, UserContestBetLog.class, parameter);
	}

	@Override
	public void clearBetCache() {
		
	}

}
