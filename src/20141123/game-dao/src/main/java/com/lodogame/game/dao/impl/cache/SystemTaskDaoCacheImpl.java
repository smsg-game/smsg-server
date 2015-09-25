package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemTaskDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemTask;

public class SystemTaskDaoCacheImpl implements SystemTaskDao, ReloadAble {

	private SystemTaskDao systemTaskDaoMysqlImpl;

	private Map<Integer, List<SystemTask>> posTaskMap = new ConcurrentHashMap<Integer, List<SystemTask>>();

	private Map<Integer, SystemTask> taskMap = new ConcurrentHashMap<Integer, SystemTask>();

	public void setSystemTaskDaoMysqlImpl(SystemTaskDao systemTaskDaoMysqlImpl) {
		this.systemTaskDaoMysqlImpl = systemTaskDaoMysqlImpl;
	}

	@Override
	public SystemTask get(int systemTaskId) {

		if (taskMap.containsKey(systemTaskId)) {
			return taskMap.get(systemTaskId);
		}

		SystemTask systemTask = null;

		synchronized (SystemTaskDaoCacheImpl.class) {
			systemTask = this.systemTaskDaoMysqlImpl.get(systemTaskId);
			if (systemTask != null) {
				taskMap.put(systemTaskId, systemTask);
			}
		}

		return systemTask;

	}

	@Override
	public List<SystemTask> getPosTaskList(int systemTaskId) {

		if (posTaskMap.containsKey(systemTaskId)) {
			return posTaskMap.get(systemTaskId);
		}

		List<SystemTask> systemTaskList = null;

		synchronized (SystemTaskDaoCacheImpl.class) {
			systemTaskList = this.systemTaskDaoMysqlImpl.getPosTaskList(systemTaskId);
			if (systemTaskList != null) {
				posTaskMap.put(systemTaskId, systemTaskList);
			}
		}

		return systemTaskList;

	}
	
	@Override
	public List<SystemTask> getByTaskTargetType(int targetType) {

		List<SystemTask> list = new ArrayList<SystemTask>();
		
		Iterator<Entry<Integer, SystemTask>> iterator = taskMap.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<Integer, SystemTask> entry = iterator.next();
			SystemTask systemTask = entry.getValue();
			if (systemTask.getTaskTarget() == targetType) {
				list.add(systemTask);
			}
		}
		
		if (list.size() != 0) {
			return list;
		} else {
			return systemTaskDaoMysqlImpl.getByTaskTargetType(targetType);
		}
	}

	@Override
	public void reload() {
		posTaskMap.clear();
		taskMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	

}
