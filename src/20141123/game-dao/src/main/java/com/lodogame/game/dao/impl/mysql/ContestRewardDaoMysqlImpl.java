package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestRewardDao;
import com.lodogame.model.ContestReward;
import com.lodogame.model.UserRecContestRewardLog;

public class ContestRewardDaoMysqlImpl implements ContestRewardDao{
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<ContestReward> getContestRewardListByDay(int dayOfContest) {
		String sql = "SELECT * FROM system_contest_reward WHERE day_of_contest = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(dayOfContest);
		return this.jdbc.getList(sql, ContestReward.class, parameter);

	}

	@Override
	public UserRecContestRewardLog getUserRecRewardLog(String userId, int session, int rewardId) {
		String sql = "SELECT * FROM user_rec_contest_reward_log WHERE session = ? AND reward_id = ? AND user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(session);
		parameter.setInt(rewardId);
		parameter.setString(userId);
		return this.jdbc.get(sql, UserRecContestRewardLog.class, parameter);
	}

	@Override
	public ContestReward getContestReward(int rewardId) {
		String sql = "SELECT * FROM system_contest_reward WHERE reward_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(rewardId);
		return this.jdbc.get(sql, ContestReward.class, parameter);
	}

	@Override
	public void saveUserRecRewardLog(UserRecContestRewardLog recLog) {
		this.jdbc.insert(recLog);
		
	}
}
