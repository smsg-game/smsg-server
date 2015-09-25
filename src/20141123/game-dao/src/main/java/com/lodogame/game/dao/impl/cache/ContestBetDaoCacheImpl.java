package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.ContestBetDao;
import com.lodogame.game.dao.impl.mysql.ContestBetDaoMysqlImpl;
import com.lodogame.model.UserContestBetLog;

public class ContestBetDaoCacheImpl implements ContestBetDao{

	private ContestBetDaoMysqlImpl contestBetDaoMysqlImpl;
	
	public void setContestBetDaoMysqlImpl(
			ContestBetDaoMysqlImpl contestBetDaoMysqlImpl) {
		this.contestBetDaoMysqlImpl = contestBetDaoMysqlImpl;
	}

	/**
	 * 缓存用户下注记录，key 是下注用户的 userId，
	 */
	private Map<String, UserContestBetLog> betCache = new HashMap<String, UserContestBetLog>();

	@Override
	public void saveContestBetLog(UserContestBetLog betLog) {
		betCache.put(betLog.getUserId(), betLog);
		contestBetDaoMysqlImpl.saveContestBetLog(betLog);
	}


	@Override
	public List<String> getGoodBetUserIdList(int session, String champUserId) {
		return contestBetDaoMysqlImpl.getGoodBetUserIdList(session, champUserId);
	}

	@Override
	public UserContestBetLog getUserBetLog(int session, String userId) {
		 UserContestBetLog betLog = betCache.get(userId);
		 if (betLog == null) {
			 betLog = contestBetDaoMysqlImpl.getUserBetLog(session, userId);
			 if (betLog != null) {
				 betCache.put(betLog.getUserId(), betLog);
			 }
		 }
		
		return betLog;
		
	}

	@Override
	public void clearBetCache() {
		betCache.clear();
	}
}
