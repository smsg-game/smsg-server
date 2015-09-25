package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserPkInfo;

public class UserPkInfoDaoRedisImpl implements UserPkInfoDao {

	@Autowired
	private UserPkInfoDao userPkInfoDaoMysqlImpl;

	@Override
	public boolean add(UserPkInfo userPkInfo) {
		JedisUtils.setFieldToObject(RedisKey.getPkInfoKey(), userPkInfo.getUserId(), Json.toJson(userPkInfo));
		JedisUtils.zadd(RedisKey.getRankScoreInfoKey(), userPkInfo.getRank(), userPkInfo.getUserId());
		return true;
	}

	@Override
	public UserPkInfo getByRank(int rank) {
		Set<String> userIdSet = JedisUtils.zmember(RedisKey.getRankScoreInfoKey(), rank, rank);
		if (userIdSet != null) {
			Iterator<String> it = userIdSet.iterator();
			if (it.hasNext()) {
				String pkInfoStr = JedisUtils.getFieldFromObject(RedisKey.getPkInfoKey(), it.next());
				if (!StringUtils.isBlank(pkInfoStr)) {
					return Json.toObject(pkInfoStr, UserPkInfo.class);
				} else {
					UserPkInfo info = userPkInfoDaoMysqlImpl.getByRank(rank);
					add(info);
					return info;
				}
			}
		}
		return null;
	}

	@Override
	public UserPkInfo getByUserId(String userId) {
		String pkInfoStr = JedisUtils.getFieldFromObject(RedisKey.getPkInfoKey(), userId);
		return Json.toObject(pkInfoStr, UserPkInfo.class);
	}

	@Override
	public boolean update(UserPkInfo userPkInfo) {
		JedisUtils.setFieldToObject(RedisKey.getPkInfoKey(), userPkInfo.getUserId(), Json.toJson(userPkInfo));
		JedisUtils.zadd(RedisKey.getRankScoreInfoKey(), userPkInfo.getRank(), userPkInfo.getUserId());
		return true;
	}

	@Override
	public List<UserPkInfo> getByRankRange(int start, int end) {
		List<UserPkInfo> list = new ArrayList<UserPkInfo>();
		Set<String> userIdSet = JedisUtils.zmember(RedisKey.getRankScoreInfoKey(), start, end);
		if (userIdSet != null) {
			Iterator<String> it = userIdSet.iterator();
			while (it.hasNext()) {
				String userId = it.next();
				String pkInfoStr = JedisUtils.getFieldFromObject(RedisKey.getPkInfoKey(), userId);
				if (StringUtils.isBlank(pkInfoStr)) {
					list.add(Json.toObject(pkInfoStr, UserPkInfo.class));
				} else {
					UserPkInfo info = userPkInfoDaoMysqlImpl.getByUserId(userId);
					list.add(info);
					add(info);
				}
			}
		}
		return list;
	}

	@Override
	public List<UserPkInfo> getRanks(List<Integer> rankList) {
		List<UserPkInfo> list = new ArrayList<UserPkInfo>(10);
		for (Integer rank : rankList) {
			list.add(getByRank(rank));
		}
		return list;
	}

	@Override
	public UserPkInfo getLastUserPkInfo() {
		return null;
	}

	@Override
	public void backUserPkInfo() {
		throw new NotImplementedException();
	}

	@Override
	public boolean changeRank(String userId, String targetUserId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addFirst(UserPkInfo userPkInfo) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addScore(String userId, int score) {
		throw new NotImplementedException();
	}

	@Override
	public boolean buyPkTimes(String userId, int buyPkTimes) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addPkTimes(String userId, int pkTimes) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserPkInfo> getGroupRanks(int beginLevel, int endLevel) {
		throw new NotImplementedException();
	}

	@Override
	public void updateTimes(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void clearTimes(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserPkInfo> getGrankTen(int beginLevel, int endLevel) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserPkInfo> getTotalTen() {
		throw new NotImplementedException();
	}

	@Override
	public void updateUserLevel(String userId, int level) {
		throw new NotImplementedException();
	}

	@Override
	public UserPkInfo getGroupFirstRanks(int beginLevel, int endLevel) {
		throw new NotImplementedException();
	}

}
