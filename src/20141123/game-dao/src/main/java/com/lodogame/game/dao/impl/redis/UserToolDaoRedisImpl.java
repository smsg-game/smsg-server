package com.lodogame.game.dao.impl.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserToolDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserTool;
import com.mysql.jdbc.NotImplemented;

public class UserToolDaoRedisImpl extends RedisMapBase<UserTool> implements
		UserToolDao {

	public void initUserToolList(String userId,List<UserTool> list){
		Map<String,UserTool> map = new HashMap<String,UserTool>();
		if(list!=null){
		for(UserTool userTool:list){
			map.put(userTool.getToolId()+"", userTool);
		}
		}
		this.initEntry(userId, map);
	}
	@Override
	public UserTool get(String userId, int toolId) {
		return this.getEntryEntry(userId, toolId+"");
	}

	@Override
	public int getUserToolNum(String userId, int toolId) {
	    int result = 0;
	    UserTool userTool = this.getEntryEntry(userId, toolId+"");
	    if(userTool!=null){
	    	result = userTool.getToolNum();
	    }
		return result;
	}

	@Override
	public boolean reduceUserTool(String userId, int toolId, int num) {
		 UserTool userTool = this.getEntryEntry(userId, toolId+"");
		 if(userTool!=null&&userTool.getToolNum()>=num){
			 userTool.setToolNum(userTool.getToolNum()-num);
			 this.updateEntryEntry(userId, toolId+"", userTool);
			 return true;
		 }
		return false;
	}

	@Override
	public boolean addUserTool(String userId, int toolId, int num) {
		UserTool userTool = this.getEntryEntry(userId, toolId+"");
		 if(userTool!=null){
			 userTool.setToolNum(userTool.getToolNum()+num);
			 this.updateEntryEntry(userId, toolId+"", userTool);
			 return true;
		 }
		return false;
	}

	@Override
	public boolean add(UserTool userTool) {
		if(this.existUserId(userTool.getUserId())){
			this.updateEntryEntry(userTool.getUserId(), userTool.getToolId()+"", userTool);
			return true;
		}
		return false;
	}

	@Override
	public List<UserTool> getList(String userId) {
		return this.getAllEntryValue(userId);
	}

	@Override
	public boolean deleteZeroNumTools(String userId) {
        this.delEntry(userId);
		return true;
	}

	@Override
	public String getPreKey() {
		// TODO Auto-generated method stub
		return RedisKey.getUserToolKeyPre();
	}
	@Override
	public List<UserTool> getUserToolListByToolType(String userId, int toolType) {
		throw new NotImplementedException();
	}

}
