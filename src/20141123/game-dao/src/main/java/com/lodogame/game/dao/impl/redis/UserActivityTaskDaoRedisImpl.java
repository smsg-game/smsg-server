package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserActivityTaskDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserActivityRewardLog;
import com.lodogame.model.UserActivityTask;

public class UserActivityTaskDaoRedisImpl extends RedisMapBase<UserActivityTask> implements UserActivityTaskDao {
    /**
     * 初始化用户活跃度记录
     * @param userId
     * @param activityTasks
     */
	public void initUserActivityTask(String userId,List<UserActivityTask> activityTasks){
		Map<String,UserActivityTask> map = new HashMap<String,UserActivityTask>();
		if(activityTasks!=null){
		for(UserActivityTask activityTask:activityTasks){
			map.put(activityTask.getActivityTaskId()+"",activityTask);
		 }
		}
		initEntry(userId, map);
	}
	/**
	 * 初始化用户领取活跃度记录的日志
	 * @param userId
	 * @param activityRewardLogs
	 */
	public void initUserActivityRewardLog(String userId,List<UserActivityRewardLog> activityRewardLogs){
		String key = RedisKey.getUserActivityRewardLogKey(userId);
		Map<String,String> map = new HashMap<String,String>();
		for(UserActivityRewardLog activityRewardLog:activityRewardLogs){
			map.put(activityRewardLog.getActivityTaskRewardId()+"", Json.toJson(activityRewardLog));
		}
		JedisUtils.setObject(key, map);
	}
	
	@Override
	public List<UserActivityTask> getUserActivityTaskList(String userId) {
		List<UserActivityTask> list = this.getAllEntryValue(userId);
		List<UserActivityTask> result =null;
		if(list!=null&&list.size()>0){
			result = new ArrayList<UserActivityTask>();
			for(UserActivityTask activityTask:list){
				//过滤掉过期的数据
				if(activityTask.getCreatedTime().getTime()>=getTodyZeroDate()){
					result.add(activityTask);
				}
			}
		}
		return result;
	}

	private long getTodyZeroDate(){
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
	}
	@Override
	public UserActivityTask getUserActivityTask(String userId,
			int activityTaskId) {
		return this.getEntryEntry(userId, activityTaskId+"");
	}

	@Override
	public boolean updateUserActivityTask(String userId, int activityTaskId,
			int times, int status) {
		UserActivityTask userActivityTask = this.getEntryEntry(userId, activityTaskId+"");
		if(userActivityTask!=null){
			if(userActivityTask.getCreatedTime().getTime()>=getTodyZeroDate()){
				userActivityTask.setFinishTimes(times);
				userActivityTask.setStatus(status);
				this.updateEntryEntry(userId, userActivityTask.getActivityTaskId()+"", userActivityTask);
			}
		}
		return true;
	}

	@Override
	public List<UserActivityRewardLog> getUserActivityRewardLogList(
			String userId) {
		String key = RedisKey.getUserActivityRewardLogKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		List<UserActivityRewardLog> temp = null;
		List<UserActivityRewardLog> result = null;
		if(jsonList!=null&&jsonList.size()>0){
			temp = Json.toList(jsonList, UserActivityRewardLog.class);
			if(temp!=null&&temp.size()>0){
				result = new ArrayList<UserActivityRewardLog>();
				for(UserActivityRewardLog activityRewardLog:temp){
					if(activityRewardLog.getCreatedTime().getTime()>=getTodyZeroDate()){
						result.add(activityRewardLog);
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean addUserActivityRewardLog(
			UserActivityRewardLog userActivityRewardLog) {
		String key = RedisKey.getUserActivityRewardLogKey(userActivityRewardLog.getUserId());
		if(JedisUtils.exists(key)){
			JedisUtils.setFieldToObject(key, userActivityRewardLog.getActivityTaskRewardId()+"", Json.toJson(userActivityRewardLog));
		}
		return true;
	}

	@Override
	public boolean addUserActivityTask(
			List<UserActivityTask> userActivityTaskList) {
		//在外部实现
		throw new NotImplementedException();
	}
	@Override
	public String getPreKey() {
		return RedisKey.getUserActivityTaskKeyPre();
	}

}
