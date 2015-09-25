package com.lodogame.game.dao.impl.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserDailyGainLog;

public class UserDailyGainLogDaoRedisImpl extends RedisMapBase<Integer> implements
		UserDailyGainLogDao {
    /**
     * 初始化今日用户奖励领取记录
     * @param userId
     * @param type
     * @param date
     * @return
     */
	public void initUserDailyGainList(String userId, List<UserDailyGainLog> list){
		Map<String,Integer> map = new HashMap<String,Integer>();
		//让redis中有值 当用户没有数据的时候 不让其一直查询数据库
		if(list==null||list.size()==0){
			map.put(0+"", 0);
		}
		for(UserDailyGainLog userDailyGainLog:list){
			map.put(getSecondKey(userDailyGainLog.getType(),DateUtils.getDate(userDailyGainLog.getDate())), userDailyGainLog.getAmount());
		}
		this.initEntry(userId, map);
	}
	
	private String getSecondKey(int type,String date){
		return type+"_"+date;
	}
	@Override
	public int getUserDailyGain(String userId, int type) {
		String date = DateUtils.getDate();
		Integer bean = this.getEntryEntry(userId, getSecondKey(type,date));
		int result = (bean==null?0:bean);
		return result;
	}

	@Override
	public boolean addUserDailyGain(String userId, int type, int amount) {
		if(this.existUserId(userId)){
			String date = DateUtils.getDate();
			Integer result = this.getEntryEntry(userId, getSecondKey(type,date));
			if(result!=null){
				result = result+amount;
			}else{
				result = amount;
			}
			this.updateEntryEntry(userId, getSecondKey(type,date), result);
		}
		return true;
	}

	@Override
	public String getPreKey() {
		// TODO Auto-generated method stub
		return RedisKey.getUserDailyGainKeyPre();
	}

}
