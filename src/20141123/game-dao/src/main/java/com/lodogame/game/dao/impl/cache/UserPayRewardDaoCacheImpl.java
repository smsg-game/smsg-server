package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserPayRewardDao;
import com.lodogame.model.UserPayReward;

public class UserPayRewardDaoCacheImpl implements UserPayRewardDao {

	@Autowired
	private UserPayRewardDao userPayRewardDaoMysqlImpl;

	private Map<String, List<UserPayReward>> cache = new HashMap<String, List<UserPayReward>>();

	@Override
	public boolean add(UserPayReward userPayReward) {
		if (userPayRewardDaoMysqlImpl.add(userPayReward)) {
			List<UserPayReward> list = cache.get(userPayReward.getUserId());
			if (list == null) {
				list = new ArrayList<UserPayReward>();
				list.add(userPayReward);
			} else {
				list.add(userPayReward);
			}
			cache.put(userPayReward.getUserId(), list);
			return true;
		}

		return false;
	}

	@Override
	public List<UserPayReward> getReceivedOrderIdList(String userId, int aid, int rid, Date startTime, Date endTime) {
		List<UserPayReward> ret = null;
		List<UserPayReward> list = cache.get(userId);
		if (list == null || list.isEmpty()) {
			list = getList(userId);
			ret = getReceivedOrderList(userId, aid, rid, list, startTime, endTime);
		} else {
			ret = getReceivedOrderList(userId, aid, rid, list, startTime, endTime);
		}
		return ret;
	}

	private List<UserPayReward> getReceivedOrderList(String userId, int aid, int rid, List<UserPayReward> userPayRewards, Date startTime, Date endTime) {
		List<UserPayReward> ret = new ArrayList<UserPayReward>();
		if (userPayRewards != null && !userPayRewards.isEmpty()) {
			for (UserPayReward reward : userPayRewards) {
				if (reward.getUserId().equals(userId) && reward.getActivityId() == aid && reward.getRewardId() == rid && 
						reward.getCreatedTime().compareTo(startTime) >= 0 && reward.getCreatedTime().compareTo(endTime) < 0) {
					ret.add(reward);
				}
			}
		}
		return ret;
	}

	public UserPayRewardDao getUserPayRewardDaoMysqlImpl() {
		return userPayRewardDaoMysqlImpl;
	}

	public void setUserPayRewardDaoMysqlImpl(UserPayRewardDao userPayRewardDaoMysqlImpl) {
		this.userPayRewardDaoMysqlImpl = userPayRewardDaoMysqlImpl;
	}

	@Override
	public List<UserPayReward> getList(String userId) {
		List<UserPayReward> list = cache.get(userId);
		if (list == null || list.isEmpty()) {
			list = userPayRewardDaoMysqlImpl.getList(userId);
			cache.put(userId, list);
		}
		return list;
	}

	@Override
	public UserPayReward getUserPayReward(String userId, int aid, int rid, Date startTime, Date endTime) {
		return userPayRewardDaoMysqlImpl.getUserPayReward(userId, aid, rid, startTime, endTime);
	}
}
