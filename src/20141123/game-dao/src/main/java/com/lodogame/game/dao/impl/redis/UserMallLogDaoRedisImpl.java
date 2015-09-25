package com.lodogame.game.dao.impl.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserMallLogDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserMallLog;

public class UserMallLogDaoRedisImpl extends RedisMapBase<UserMallLog> implements UserMallLogDao {
	/**
	 * 初始化缓存
	 * 
	 * @param userId
	 * @param list
	 */
	public void initUserMallLog(String userId, List<UserMallLog> list) {
		Map<String, UserMallLog> map = new HashMap<String, UserMallLog>();
		if (list != null) {
			for (UserMallLog userMallLog : list) {
				map.put(userMallLog.hashCode() + "" + System.nanoTime(), userMallLog);
			}
		}
		this.initEntry(userId, map);
	}

	@Override
	public boolean add(UserMallLog userMallLog) {

		if (this.existUserId(userMallLog.getUserId())) {
			this.updateEntryEntry(userMallLog.getUserId(), userMallLog.hashCode() + "" + System.nanoTime(), userMallLog);
		}
		return true;
	}

	@Override
	public Map<Integer, Integer> getUserTodayPurchaseNum(String userId) {

		Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
		List<UserMallLog> list = this.getAllEntryValue(userId);
		if (list != null && list.size() > 0) {
			for (UserMallLog userMallLog : list) {
				if (userMallLog.getCreatedTime().getTime() > DateUtils.getDateAtMidnight().getTime()) {
					if (resultMap.containsKey(userMallLog.getMallId())) {
						int nowNum = resultMap.get(userMallLog.getMallId());
						resultMap.put(userMallLog.getMallId(), nowNum + userMallLog.getNum());
					} else {
						resultMap.put(userMallLog.getMallId(), userMallLog.getNum());
					}
				}
			}
		}
		return resultMap;
	}

	@Override
	public String getPreKey() {
		return RedisKey.getUserMallLogKeyPre();
	}

}
