package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserTaskDao;
import com.lodogame.game.dao.daobase.redis.RedisMapBase;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.UserTask;

public class UserTaskDaoRedisImpl extends RedisMapBase<UserTask> implements UserTaskDao {

	public void initUserTaskCache(String userId, List<UserTask> list) {
		Map<String, UserTask> map = new HashMap<String, UserTask>();
		for (UserTask task : list) {
			map.put(task.getSystemTaskId() + "", task);
		}
		this.initEntry(userId, map);
	}

	@Override
	public List<UserTask> getList(String userId, int status) {
		// TODO Auto-generated method stub
		List<UserTask> list = this.getAllEntryValue(userId);
		List<UserTask> result = null;
		if (list != null && list.size() > 0) {
			result = new ArrayList<UserTask>();
			for (UserTask task : list) {
				if (task.getStatus() != 4) {
					if (status == 100) {
						result.add(task);
					} else {
						if (status == task.getStatus()) {
							result.add(task);
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean update(String userId, int systemTaskId, int finishTimes, int status) {
		// TODO Auto-generated method stub
		UserTask task = this.getEntryEntry(userId, systemTaskId + "");
		if (task != null) {
			task.setFinishTimes(finishTimes);
			task.setStatus(status);
			task.setUpdatedTime(new Date());
			this.updateEntryEntry(userId, systemTaskId + "", task);
		}
		return true;
	}

	@Override
	public boolean update(String userId, int systemTaskId, int status) {
		// TODO Auto-generated method stub
		UserTask task = this.getEntryEntry(userId, systemTaskId + "");
		if (task != null) {
			task.setStatus(status);
			task.setUpdatedTime(new Date());
			this.updateEntryEntry(userId, systemTaskId + "", task);
		}
		return true;
	}

	@Override
	public void add(List<UserTask> userTaskList) {
		// TODO Auto-generated method stub
		if (userTaskList != null && userTaskList.size() > 0) {
			String userId = userTaskList.get(0).getUserId();
			// 存在key的时候才往里插入 不然会不同步
			if (this.existUserId(userId)) {
				for (UserTask task : userTaskList) {
					this.updateEntryEntry(userId, task.getSystemTaskId() + "", task);
				}
			}
		}
	}

	@Override
	public UserTask get(String userId, int systemTaskId) {
		// TODO Auto-generated method stub
		return this.getEntryEntry(userId, systemTaskId + "");
	}

	@Override
	public boolean delete(String userId, int systemTaskId) {
		// TODO Auto-generated method stub
		this.delEntryEntry(userId, systemTaskId + "");
		return true;
	}

	@Override
	public String getPreKey() {
		// TODO Auto-generated method stub
		return RedisKey.getUserTaskKeyPre();
	}

}
