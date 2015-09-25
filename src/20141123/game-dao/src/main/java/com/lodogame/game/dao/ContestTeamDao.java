package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ContestPlayerPair;
import com.lodogame.model.ContestReward;
import com.lodogame.model.ContestTeam;
import com.lodogame.model.UserContestBetLog;
import com.lodogame.model.UserContestLog;
import com.lodogame.model.UserContestRank;
import com.lodogame.model.UserRecContestRewardLog;

public interface ContestTeamDao {

	public List<ContestTeam> getTeamList();

	public ContestTeam getTeamByTeamId(int session, int teamId);

	public void saveTeam(ContestTeam team);
	
	public ContestTeam getTeamForNextFight();

	public boolean updateMembCount(ContestTeam team);

	public List<ContestTeam> getTeamListBySession(int session);

	public void clearTeamMapCache();

	/**
	 * 将小组的状态修改为已经打完了比赛
	 * @param team
	 */
	public void setTeamFighted(ContestTeam team);

	public ContestTeam getLatestCreatedTeam(int session);

	public ContestTeam getLatestTeam();

}
