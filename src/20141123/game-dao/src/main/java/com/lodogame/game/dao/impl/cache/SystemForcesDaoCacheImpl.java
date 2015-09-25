package com.lodogame.game.dao.impl.cache;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemForces;

public class SystemForcesDaoCacheImpl implements SystemForcesDao, ReloadAble {

	private static final List<SystemForces> EMPTY_SYSTEM_FORCES = Collections.emptyList();
	
	private SystemForcesDao systemForcesDaoMysqlImpl;
	
	private Map<Integer, SystemForces> systemForcesMap = new ConcurrentHashMap<Integer, SystemForces>();
	private Map<Integer, List<SystemForces>> preSystemForcesMap = new ConcurrentHashMap<Integer, List<SystemForces>>();
	private Map<Integer, List<SystemForces>> typeSystemForcesMap = new ConcurrentHashMap<Integer, List<SystemForces>>(); 

	public void setSystemForcesDaoMysqlImpl(SystemForcesDao systemForcesDaoMysqlImpl) {
		this.systemForcesDaoMysqlImpl = systemForcesDaoMysqlImpl;
	}

	public List<SystemForces> getSceneForcesList(int sceneId) {

		return systemForcesDaoMysqlImpl.getSceneForcesList(sceneId);

	}

	@Override
	public SystemForces get(int forcesId) {

		if (systemForcesMap.containsKey(forcesId)) {
			return systemForcesMap.get(forcesId);
		}

		SystemForces systemForces = null;

		synchronized (SystemForcesDaoCacheImpl.class) {

			systemForces = this.systemForcesDaoMysqlImpl.get(forcesId);
			if (systemForces != null) {
				systemForcesMap.put(forcesId, systemForces);
			}

		}

		return systemForces;
	}

	@Override
	public List<SystemForces> getSystemForcesByPreForcesId(int preForcesId) {

		if (preSystemForcesMap.containsKey(preForcesId)) {
			return preSystemForcesMap.get(preForcesId);
		}

		List<SystemForces> systemForcesList = null;

		synchronized (SystemForcesDaoCacheImpl.class) {

			systemForcesList = this.systemForcesDaoMysqlImpl.getSystemForcesByPreForcesId(preForcesId);
			if (systemForcesList != null) {
				preSystemForcesMap.put(preForcesId, systemForcesList);
			}

		}

		return systemForcesList;

	}
	
	@Override
	public List<SystemForces> getSystemForcesByType(int type) {
		if (typeSystemForcesMap.containsKey(type) && typeSystemForcesMap.get(type).size() > 0) {
			return typeSystemForcesMap.get(type);
		}

		synchronized (SystemForcesDaoCacheImpl.class) {

			List<SystemForces> systemForcesList = this.systemForcesDaoMysqlImpl.getSystemForcesByType(type);
			if (systemForcesList != null) {
				typeSystemForcesMap.put(type, Collections.unmodifiableList(systemForcesList));
			} else {
				typeSystemForcesMap.put(type, EMPTY_SYSTEM_FORCES);
			}

		}

		return typeSystemForcesMap.get(type);
	}

	public void clearTypeAndForceId(int forcesId,int type){
		if(typeSystemForcesMap.containsKey(type) && typeSystemForcesMap.get(type).size() > 0){
			List<SystemForces> list = typeSystemForcesMap.get(type);
			for(SystemForces s : list){
				if(s.getForcesId() == forcesId){
					list.remove(s);
				}
			}
		}
	}
	@Override
	public void reload() {
		systemForcesMap.clear();
		preSystemForcesMap.clear();
		typeSystemForcesMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public int updateTimes(int forcesId, int times) {
		return systemForcesDaoMysqlImpl.updateTimes(forcesId,times);
	}

}
