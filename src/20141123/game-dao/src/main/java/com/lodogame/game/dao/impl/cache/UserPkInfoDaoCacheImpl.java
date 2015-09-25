package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.dao.impl.mysql.UserPkInfoDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserPkInfoDaoRedisImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.UserPkInfo;

public class UserPkInfoDaoCacheImpl implements UserPkInfoDao, ReloadAble {

	private UserPkInfoDaoMysqlImpl userPkInfoDaoMysqlImpl;
	private UserPkInfoDaoRedisImpl userPkInfoDaoRedisImpl;

	public UserPkInfoDaoRedisImpl getUserPkInfoDaoRedisImpl() {
		return userPkInfoDaoRedisImpl;
	}

	public void setUserPkInfoDaoRedisImpl(UserPkInfoDaoRedisImpl userPkInfoDaoRedisImpl) {
		this.userPkInfoDaoRedisImpl = userPkInfoDaoRedisImpl;
	}

	// 用户id对应的排行信息
	private Map<String, UserPkInfo> userPkInfoCache = new ConcurrentHashMap<String, UserPkInfo>();
	// 名次对应用户id
	private Map<Integer, String> rankToUserIdCache = new ConcurrentHashMap<Integer, String>();
	// 用户记录的行锁
	// private Map<String, ReentrantLock> lockForRecord = new
	// ConcurrentHashMap<String, ReentrantLock>();
	private Object updateLock = new Object();

	@Override
	public boolean add(UserPkInfo userPkInfo) {
		// 添加的时候size会出现同步的问题 所以加上该锁
		if (userPkInfoDaoMysqlImpl.add(userPkInfo)) {
			synchronized (userPkInfoCache) {
				userPkInfo.setRank(userPkInfoCache.size() + 1);
				if (userPkInfo.getUpdatePkTime() == null) {
					userPkInfo.setUpdatePkTime(new Date());
				}
				initOneRecord(userPkInfo);
			}
		}
		return true;
	}

	@Override
	public UserPkInfo getByRank(int rank) {
		String userId = rankToUserIdCache.get(rank);
		if (userId != null) {
			UserPkInfo userInfo = userPkInfoCache.get(userId);
			return colneUserPkInfo(userInfo);

		}
		return null;
	}

	private UserPkInfo colneUserPkInfo(UserPkInfo userInfo) {
		if (userInfo == null) {
			return null;
		}
		UserPkInfo result = new UserPkInfo();
		result.setPkTimes(new Integer(userInfo.getPkTimes()));
		result.setRank(new Integer(userInfo.getRank()));
		result.setScore(new Integer(userInfo.getScore()));
		result.setUpdatePkTime(new Date(userInfo.getUpdatePkTime().getTime()));
		result.setUserId(new String(userInfo.getUserId()));
		return result;
	}

	@Override
	public UserPkInfo getByUserId(String userId) {
		return colneUserPkInfo(userPkInfoCache.get(userId));
	}

	@Override
	public boolean update(UserPkInfo userPkInfo) {
		if (userPkInfoDaoMysqlImpl.update(userPkInfo)) {
			synchronized (updateLock) {
				userPkInfoCache.put(userPkInfo.getUserId(), userPkInfo);
				rankToUserIdCache.put(userPkInfo.getRank(), userPkInfo.getUserId());
			}
			return true;
		}
		return false;
	}

	@Override
	public List<UserPkInfo> getByRankRange(int start, int end) {
		List<UserPkInfo> result = new ArrayList<UserPkInfo>();
		for (int i = start; i <= end; i++) {
			String userId = rankToUserIdCache.get(i);
			if (userId != null) {
				result.add(colneUserPkInfo(userPkInfoCache.get(userId)));
			}
		}
		return result;
	}

	@Override
	public List<UserPkInfo> getRanks(List<Integer> rankList) {
		List<UserPkInfo> result = new ArrayList<UserPkInfo>();

		for (Integer rank : rankList) {

			String userId1 = rankToUserIdCache.get(rank);
			if (userId1 != null) {
				result.add(getByUserId(userId1));
			}
		}

		return result;
	}

	@Override
	public UserPkInfo getLastUserPkInfo() {
		String userId = rankToUserIdCache.get(rankToUserIdCache.size());
		return getByUserId(userId);
	}

	@Override
	public void backUserPkInfo() {
		this.userPkInfoDaoMysqlImpl.backUserPkInfo();
	}

	@Override
	public boolean changeRank(String userId, String targetUserId) {
		if (userPkInfoDaoMysqlImpl.changeRank(userId, targetUserId)) {
			synchronized (updateLock) {
				UserPkInfo userPkInfo1 = userPkInfoCache.get(userId);
				UserPkInfo userPkInfo2 = userPkInfoCache.get(targetUserId);
				int rank1 = userPkInfo1.getRank();
				int rank2 = userPkInfo2.getRank();
				userPkInfo1.setRank(rank2);
				userPkInfo2.setRank(rank1);
				rankToUserIdCache.put(rank1, userPkInfo2.getUserId());
				rankToUserIdCache.put(rank2, userPkInfo1.getUserId());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean addFirst(UserPkInfo userPkInfo) {
		if (userPkInfoDaoMysqlImpl.addFirst(userPkInfo)) {
			synchronized (userPkInfoCache) {
				if (userPkInfo.getUpdatePkTime() == null) {
					userPkInfo.setUpdatePkTime(new Date());
				}
				initOneRecord(userPkInfo);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean buyPkTimes(String userId, int buyPkTimes) {
		return true;
	}

	@Override
	public void reload() {

	}

	private void initOneRecord(UserPkInfo userPkInfo) {
		userPkInfoCache.put(userPkInfo.getUserId(), userPkInfo);
		rankToUserIdCache.put(userPkInfo.getRank(), userPkInfo.getUserId());
	}

	@Override
	public void init() {
		List<UserPkInfo> allList = userPkInfoDaoMysqlImpl.getAllUserPkInfo();
		if (allList != null && allList.size() > 0) {
			for (UserPkInfo userPkInfo : allList) {
				initOneRecord(userPkInfo);
			}
		}
	}

	public void setuserPkInfoDaoMysqlImpl(UserPkInfoDaoMysqlImpl userPkInfoDaoMysqlImpl) {
		this.userPkInfoDaoMysqlImpl = userPkInfoDaoMysqlImpl;
	}

	@Override
	public boolean addScore(String userId, int score) {
		return this.userPkInfoDaoMysqlImpl.addScore(userId, score);
	}

	@Override
	public boolean addPkTimes(String userId, int pkTimes) {
		return this.userPkInfoDaoMysqlImpl.addPkTimes(userId, pkTimes);
	}

	@Override
	public List<UserPkInfo> getGroupRanks(int beginLevel, int endLevel) {
		return this.userPkInfoDaoMysqlImpl.getGroupRanks(beginLevel, endLevel);
	}

	@Override
	public void updateTimes(String userId) {
		this.userPkInfoDaoMysqlImpl.updateTimes(userId);
		
	}

	@Override
	public void clearTimes(String userId) {
		this.userPkInfoDaoMysqlImpl.clearTimes(userId);
		
	}

	@Override
	public List<UserPkInfo> getGrankTen(int beginLevel, int endLevel) {
		return this.userPkInfoDaoMysqlImpl.getGrankTen(beginLevel, endLevel);
	}

	@Override
	public List<UserPkInfo> getTotalTen() {
		return this.userPkInfoDaoMysqlImpl.getTotalTen();
	}

	@Override
	public void updateUserLevel(String userId, int level) {
		this.userPkInfoDaoMysqlImpl.updateUserLevel(userId, level);
	}

	@Override
	public UserPkInfo getGroupFirstRanks(int beginLevel, int endLevel) {
		return this.userPkInfoDaoMysqlImpl.getGroupFirstRanks(beginLevel, endLevel);
	}

}
