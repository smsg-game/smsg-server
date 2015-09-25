package com.lodogame.game.dao.impl.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserGiftbagDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserGiftbag;

public class UserGiftbagDaoRedisImpl extends RedisMapBase<UserGiftbag> implements
		UserGiftbagDao {
	/**
	 * 初始化用户背包礼物数据 这里是所有数据
	 * @param userId
	 * @param list
	 */
	public void initUserGiftbag(String userId,List<UserGiftbag> list){
		Map<String,UserGiftbag> map = new HashMap<String,UserGiftbag>();
		if(list!=null){
		for(UserGiftbag giftbag:list){
			map.put(giftbag.getGiftbagId()+"", giftbag);
		}
		}
		this.initEntry(userId, map);
	}
	@Override
	public UserGiftbag getLast(String userId, int type) {
		// TODO Auto-generated method stub
		List<UserGiftbag> list = this.getAllEntryValue(userId);
		UserGiftbag result = null;
		if(list!=null&&list.size()>0){
			for(UserGiftbag userGiftbag:list){
				if(result==null){
					if(userGiftbag.getType()==type){
						result = userGiftbag;
					}
				}else{
					if(userGiftbag.getType()==type&&userGiftbag.getUpdatedTime().getTime()>result.getUpdatedTime().getTime()){
						result = userGiftbag;
					}
				}
				
			}
		}
		return result;
	}

	@Override
	public boolean addOrUpdateUserGiftbag(String userId, int type, int giftBagId) {
		// TODO Auto-generated method stub
		if(this.existUserId(userId)){
			UserGiftbag giftbag = this.getEntryEntry(userId, giftBagId+"");
			Date now = new Date();
			if(giftbag!=null){
				giftbag.setTotalNum(giftbag.getTotalNum()+1);
				giftbag.setUpdatedTime(now);
				this.updateEntryEntry(userId, giftBagId+"", giftbag);
			}else{
				giftbag = new UserGiftbag();
				giftbag.setUserId(userId);
				giftbag.setCreatedTime(now);
				giftbag.setGiftbagId(giftBagId);
				giftbag.setTotalNum(1);
				giftbag.setType(type);
				giftbag.setUpdatedTime(now);
				this.updateEntryEntry(userId, giftBagId+"", giftbag);
			}
		} 
		return true;
	}
	
	@Override
	public int getCount(String userId, int type, int giftBagId) {
		UserGiftbag giftbag = this.getEntryEntry(userId, giftBagId + "");
		return giftbag == null ? -1 : giftbag.getTotalNum();
	}

	@Override
	public String getPreKey() {
		// TODO Auto-generated method stub
		return RedisKey.getUserGiftBagKeyPre();
	}

}
