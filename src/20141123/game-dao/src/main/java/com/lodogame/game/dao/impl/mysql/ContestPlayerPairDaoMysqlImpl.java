package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestPlayerPairDao;
import com.lodogame.model.ContestPlayerPair;

public class ContestPlayerPairDaoMysqlImpl implements ContestPlayerPairDao{

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<ContestPlayerPair> getPlayerPairsByContestId(String contestId) {
		String sql = "SELECT * FROM contest_player_pair WHERE contest_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(contestId);
		return this.jdbc.getList(sql, ContestPlayerPair.class, parameter);
	}

	@Override
	public ContestPlayerPair getLastPlayerPair(String contestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void savePlayerPair(ContestPlayerPair playerPair) {
		this.jdbc.insert(playerPair);
		
	}

	@Override
	public boolean updatePlayerPairFightResult(ContestPlayerPair playerPair) {
		String sql = "UPDATE contest_player_pair SET result = ? WHERE player_pair_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(playerPair.getResult());
		parameter.setString(playerPair.getPlayerPairId());
		return this.jdbc.update(sql, parameter) > 0;
		
	}

	@Override
	public void saveDefUser(ContestPlayerPair playerPair) {
		String sql = "UPDATE contest_player_pair SET def_user_id=?, def_user_name = ? WHERE contest_id = ? AND att_user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(playerPair.getDefUserId());
		parameter.setString(playerPair.getDefUserName());
		parameter.setString(playerPair.getContestId());
		parameter.setString(playerPair.getAttUserId());
		this.jdbc.update(sql, parameter);
	}

	@Override
	public void incrAttUserBetNum(ContestPlayerPair playerPair) {
		String sql = "UPDATE contest_player_pair SET att_bet_num = ? WHERE contest_id = ? AND att_user_id = ? AND def_user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(playerPair.getAttBetNum());
		parameter.setString(playerPair.getContestId());
		parameter.setString(playerPair.getAttUserId());
		parameter.setString(playerPair.getDefUserId());
		this.jdbc.update(sql, parameter);
	}

	@Override
	public void incrDefUserBetNum(ContestPlayerPair playerPair) {
		String sql = "UPDATE contest_player_pair SET def_bet_num = ? WHERE contest_id = ? AND att_user_id = ? AND def_user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(playerPair.getAttBetNum());
		parameter.setString(playerPair.getContestId());
		parameter.setString(playerPair.getAttUserId());
		parameter.setString(playerPair.getDefUserId());
		this.jdbc.update(sql, parameter);
		
	}

}
