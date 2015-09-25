package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ContestTeamDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.ContestPlayerPair;
import com.lodogame.model.ContestReward;
import com.lodogame.model.ContestTeam;
import com.lodogame.model.UserContestBetLog;
import com.lodogame.model.UserContestLog;
import com.lodogame.model.UserContestRank;
import com.lodogame.model.UserRecContestRewardLog;

public class ContestTeamDaoCacheImpl implements ContestTeamDao {

	private ContestTeamDao contestTeamDaoMysqlImpl;
	public void setContestTeamDaoMysqlImpl(ContestTeamDao contestTeamDaoMysqlImpl) {
		this.contestTeamDaoMysqlImpl = contestTeamDaoMysqlImpl;
	}
	
	/**
	 * teamMap 用于存放一届比赛中的 ContestTeam。
	 * 
	 * <p>teamMap 的 key 是 ContestTeam 对象的 teamId。128强的四个队伍的 teamId 分别是12801、12802、12803和12804，
	 */
	private Map<Integer, ContestTeam> teamMap = new HashMap<Integer, ContestTeam>();

	private ContestTeam teamForNextFight = new ContestTeam();
	
	

	@Override
	public List<ContestTeam> getTeamList() {
		List<ContestTeam> teamList = new ArrayList<ContestTeam>();
		
		Iterator<Entry<Integer, ContestTeam>> iterator = teamMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Entry<Integer, ContestTeam> entry = iterator.next();
			teamList.add(entry.getValue());
		}
		return teamList;
	}
	

	@Override
	public ContestTeam getTeamByTeamId(int session, int teamId) {
		ContestTeam team = teamMap.get(teamId);
		if (team == null) {
			team = contestTeamDaoMysqlImpl.getTeamByTeamId(session, teamId);
			teamMap.put(teamId, team);
		}
		
		return team;
	
	}

	@Override
	public void saveTeam(ContestTeam team) {
		teamMap.put(team.getTeamId(), team);
		contestTeamDaoMysqlImpl.saveTeam(team);
	}

	@Override
	public ContestTeam getTeamForNextFight() {
		return teamForNextFight;
	}
	

	@Override
	public boolean updateMembCount(ContestTeam team) {
		return contestTeamDaoMysqlImpl.updateMembCount(team);
	}


	@Override
	public List<ContestTeam> getTeamListBySession(int session) {
		if (teamMap.size() == 0) {
			List<ContestTeam> teamList = contestTeamDaoMysqlImpl.getTeamListBySession(session);
			
			for (ContestTeam team : teamList) {
				teamMap.put(team.getTeamId(), team);
			}
		} 
		
		return getTeamList();
	}
	

	@Override
	public void clearTeamMapCache() {
		teamMap.clear();
	}


	@Override
	public void setTeamFighted(ContestTeam team) {
		contestTeamDaoMysqlImpl.setTeamFighted(team);
		
	}


	@Override
	public ContestTeam getLatestCreatedTeam(int session) {
		return contestTeamDaoMysqlImpl.getLatestCreatedTeam(session);
	}


	@Override
	public ContestTeam getLatestTeam() {
		return contestTeamDaoMysqlImpl.getLatestTeam();
	}

}
