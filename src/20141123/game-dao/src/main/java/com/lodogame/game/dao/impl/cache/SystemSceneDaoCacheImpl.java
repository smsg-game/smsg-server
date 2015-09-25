package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.dao.impl.mysql.SystemSceneDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemScene;

public class SystemSceneDaoCacheImpl implements SystemSceneDao, ReloadAble {

	private SystemSceneDaoMysqlImpl systemSceneDaoMysqlImpl;
	private Map<Integer,SystemScene> sceneCache = new ConcurrentHashMap<Integer,SystemScene>();
	@Override
	public void reload() {
		initCache();
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public List<SystemScene> getSystemSceneList() {
		List<SystemScene> result = new ArrayList<SystemScene>();
		result.addAll(sceneCache.values());
		return result;
	}

	@Override
	public SystemScene get(int sceneId) {
		return sceneCache.get(sceneId);
	}

	private void initCache(){
		sceneCache.clear();
		List<SystemScene> list = systemSceneDaoMysqlImpl.getSystemSceneList();
		for(SystemScene systemScene:list){
			sceneCache.put(systemScene.getSceneId(), systemScene);
		}
	}
	
	public void setSystemSceneDaoMysqlImpl(SystemSceneDaoMysqlImpl systemSceneDaoMysqlImpl) {
		this.systemSceneDaoMysqlImpl = systemSceneDaoMysqlImpl;
	}

}
