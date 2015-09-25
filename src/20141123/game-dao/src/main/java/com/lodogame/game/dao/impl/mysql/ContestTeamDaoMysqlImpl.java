package com.lodogame.game.dao.impl.mysql;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestTeamDao;
import com.lodogame.model.ContestReward;
import com.lodogame.model.ContestTeam;
import com.lodogame.model.UserContestBetLog;
import com.lodogame.model.UserContestLog;
import com.lodogame.model.UserContestRank;
import com.lodogame.model.UserRecContestRewardLog;



public class ContestTeamDaoMysqlImpl implements ContestTeamDao{

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<ContestTeam> getTeamList() {
		
		return null;
	}

	@Override
	public ContestTeam getTeamByTeamId(int session, int teamId) {
		String sql = "SELECT * FROM contest_team WHERE team_id=? AND session = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(teamId);
		parameter.setInt(session);

		return this.jdbc.get(sql, ContestTeam.class, parameter);
	}

	@Override
	public void saveTeam(ContestTeam team) {
		this.jdbc.insert(team);
		
	}

	@Override
	public ContestTeam getTeamForNextFight() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean updateMembCount(ContestTeam team) {
		String sql = "UPDATE contest_team SET player_num = ? WHERE contest_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(team.getPlayerNum());
		parameter.setString(team.getContestId());
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<ContestTeam> getTeamListBySession(int session) {
		String sql = "SELECT * FROM contest_team WHERE session = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(session);
		return this.jdbc.getList(sql, ContestTeam.class, parameter);
	}

	@Override
	public void clearTeamMapCache() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeamFighted(ContestTeam team) {
		String sql = "UPDATE contest_team SET is_fighted = ? WHERE contest_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(team.getIsFighted());
		parameter.setString(team.getContestId());
		this.jdbc.update(sql, parameter);
	}

	@Override
	public ContestTeam getLatestCreatedTeam(int session) {
		String sql = "SELECT * FROM contest_team WHERE session = ? ORDER BY created_time DESC LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(session);
		return this.jdbc.get(sql, ContestTeam.class, parameter);
	}

	@Override
	public ContestTeam getLatestTeam() {
		String sql = "SELECt * FROM contest_team ORDER BY created_time DESC LIMIT 1";
		List<ContestTeam> list = this.jdbc.getList(sql, ContestTeam.class);
		if (list != null && list.size() != 0) {
			return list.get(0);
		}
		return null;
	}
}