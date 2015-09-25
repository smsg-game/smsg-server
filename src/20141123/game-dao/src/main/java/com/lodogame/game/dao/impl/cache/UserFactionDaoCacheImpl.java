package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserFactionDao;
import com.lodogame.model.UserFaction;



public class UserFactionDaoCacheImpl implements UserFactionDao {

	private UserFactionDao userFactionDaoMysqlImpl;
	
	public void setUserFactionDaoMysqlImpl(UserFactionDao userFactionDaoMysqlImpl) {
		this.userFactionDaoMysqlImpl = userFactionDaoMysqlImpl;
	}

	private Map<Integer, List<UserFaction>> userFactionMap = new HashMap<Integer, List<UserFaction>>();

	@Override
	public List<UserFaction> getFactionMemberByFid(int fid) {
		if (!userFactionMap.containsKey(fid)) {
			List<UserFaction> userIdList = this.userFactionDaoMysqlImpl.getFactionMemberByFid(fid);
			userFactionMap.put(fid, userIdList);
		}
		
		return userFactionMap.get(fid);
	}

	@Override
	public boolean addUserFaction(UserFaction userFaction) {
		boolean succ = this.userFactionDaoMysqlImpl.addUserFaction(userFaction);
		if (succ) {
			List<UserFaction> userIdList = userFactionMap.get(userFaction.getFactionId());
			if (userIdList != null) {
				userIdList.add(userFaction);
			}
		}
		return succ;
	}

	@Override
	public boolean delUserFaction(String userId, int fid) {
		boolean succ = this.userFactionDaoMysqlImpl.delUserFaction(userId, fid);
		if (succ) {
			if (userFactionMap.containsKey(fid)) {
				userFactionMap.remove(fid);
			}
		}
		return succ;
	}

	@Override
	public boolean delAllUserFactionByFactionId(int fid) {
		boolean succ = this.userFactionDaoMysqlImpl.delAllUserFactionByFactionId(fid);
		if(succ){
			userFactionMap.remove(fid);
		}
		return succ;
	}

}
