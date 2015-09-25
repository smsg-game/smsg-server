package com.lodogame.game.dao.impl.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserTavernDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserTavern;

public class UserTavernDaoRedisImpl extends RedisMapBase<UserTavern> implements
		UserTavernDao {

	public void initUserTavernList(String userId,List<UserTavern> list){
		Map<String,UserTavern> map = new HashMap<String,UserTavern>();
		if(list!=null){
		for(UserTavern userTavern:list){
			map.put(userTavern.getType()+"", userTavern);
		}
		}
		this.initEntry(userId, map);
	}
	@Override
	public boolean add(UserTavern userTavern) {
		if(this.existUserId(userTavern.getUserId())){
			this.updateEntryEntry(userTavern.getUserId(), userTavern.getType()+"", userTavern);
		}
		return true;
	}

	@Override
	public UserTavern get(String userId, int type) {
		return this.getEntryEntry(userId, type+"");
	}

	@Override
	public List<UserTavern> getList(String userId) {
		return this.getAllEntryValue(userId);
	}

	@Override
	public boolean updateTavernInfo(String userId, int type, Date updatedTime,
			int totalTimes, int amendTimes, int hadUsedMoney) {
		UserTavern result =  this.getEntryEntry(userId, type+"");
		if(result!=null){
			result.setTotalTimes(totalTimes);
			result.setAmendTimes(amendTimes);
			result.setHadUsedMoney(hadUsedMoney);
			if(updatedTime!=null){
				result.setUpdatedTime(updatedTime);
			}
			this.updateEntryEntry(userId, type+"", result);
		}
		return true;
	}

	@Override
	public String getPreKey() {
		return RedisKey.getUserTavernKeyPre();
	}

}
