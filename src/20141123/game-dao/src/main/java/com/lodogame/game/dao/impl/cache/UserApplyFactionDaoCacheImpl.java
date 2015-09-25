package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.FactionDao;
import com.lodogame.game.dao.UserApplyFactionDao;
import com.lodogame.game.dao.UserFactionDao;
import com.lodogame.model.Faction;
import com.lodogame.model.UserApplyFaction;
import com.lodogame.model.UserFaction;



public class UserApplyFactionDaoCacheImpl implements UserApplyFactionDao {

	private UserApplyFactionDao userApplyFactionDaoMysqlImpl;

	public void setUserApplyFactionDaoMysqlImpl(
			UserApplyFactionDao userApplyFactionDaoMysqlImpl) {
		this.userApplyFactionDaoMysqlImpl = userApplyFactionDaoMysqlImpl;
	}

	private Map<Integer, List<UserApplyFaction>> userApplyFactionMap = new HashMap<Integer, List<UserApplyFaction>>();

	@Override
	public List<UserApplyFaction> getApplyFactionByFid(int fid) {
		if (!userApplyFactionMap.containsKey(fid)) {
			List<UserApplyFaction> userIdList = this.userApplyFactionDaoMysqlImpl.getApplyFactionByFid(fid);
			if (userIdList != null) {
				userApplyFactionMap.put(fid, userIdList);
			}
		}
		return userApplyFactionMap.get(fid);
	}

	@Override
	public boolean addUserApplyFaction(UserApplyFaction userApplyFaction) {
		boolean succ = this.userApplyFactionDaoMysqlImpl.addUserApplyFaction(userApplyFaction);
		if(succ) {
			if (userApplyFactionMap.containsKey(userApplyFaction.getFactionId())) {
				userApplyFactionMap.get(userApplyFaction.getFactionId()).add(userApplyFaction);
			}
		}
		return false;
	}

	@Override
	public boolean delUserApplyFaction(String userId, int factionId) {
		boolean succ = this.userApplyFactionDaoMysqlImpl.delUserApplyFaction(userId, factionId);
		if (succ) {
			if (userApplyFactionMap.containsKey(factionId)) {
				userApplyFactionMap.remove(factionId);
			}
		}
		return false;
	}

	@Override
	public boolean delUserApplyFaction(String userId) {
		List<UserApplyFaction> userApplyFactionList = this.getUserApplyFactionByUserId(userId);
		boolean succ =  this.userApplyFactionDaoMysqlImpl.delUserApplyFaction(userId);
		if (succ) {
			for (UserApplyFaction userApplyFaction : userApplyFactionList) {
				if (userApplyFactionMap.containsKey(userApplyFaction.getFactionId())) {
					userApplyFactionMap.remove(userApplyFaction.getFactionId());
				}
			}
		}
		return succ;
	}

	@Override
	public List<UserApplyFaction> getUserApplyFactionByUserId(String userId) {
		return this.userApplyFactionDaoMysqlImpl.getUserApplyFactionByUserId(userId);
	}

}
