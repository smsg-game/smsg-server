package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.dao.impl.mysql.SystemToolDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemTool;

public class SystemToolDaoCacheImpl implements SystemToolDao,ReloadAble {

	private SystemToolDaoMysqlImpl systemToolDaoMysqlImpl;
	
	private Map<Integer,SystemTool> systemToolCache = new ConcurrentHashMap<Integer,SystemTool>();
	
	private Map<Integer, List<SystemTool>> cacheMaps = new ConcurrentHashMap<Integer, List<SystemTool>>();
	@Override
	public SystemTool get(int toolId) {
		return systemToolCache.get(toolId);
	}

	@Override
	public List<SystemTool> getSystemToolList() {
		List<SystemTool> list = new ArrayList<SystemTool>();
		list.addAll(systemToolCache.values());
		return list;
	}

	@Override
	public void reload() {
		initCache();
	}

	private void initCache(){ 
		List<SystemTool> list = systemToolDaoMysqlImpl.getSystemToolList();
		for(SystemTool systemTool:list){
			systemToolCache.put(systemTool.getToolId(), systemTool);
		}
	}
	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	public void setSystemToolDaoMysqlImpl(SystemToolDaoMysqlImpl systemToolDaoMysqlImpl) {
		this.systemToolDaoMysqlImpl = systemToolDaoMysqlImpl;
	}

	@Override
	public List<SystemTool> getSystemToolListByType(int type) {
		if(cacheMaps.containsKey(type)){
			return this.cacheMaps.get(type);
		}
		List<SystemTool> list = this.systemToolDaoMysqlImpl.getSystemToolListByType(type);
		if(list != null && list.size() > 0){
			cacheMaps.put(type, list);
			return list;
		}
		return null;
	}
	
	

}
