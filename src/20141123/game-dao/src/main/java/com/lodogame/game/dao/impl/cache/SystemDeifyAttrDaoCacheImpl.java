package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemDeifyAttrDao;
import com.lodogame.game.dao.impl.mysql.SystemDeifyAttrDaoMysqlImpl;
import com.lodogame.model.SystemDeifyAttr;

public class SystemDeifyAttrDaoCacheImpl implements SystemDeifyAttrDao {

	private SystemDeifyAttrDaoMysqlImpl systemDeifyAttrDaoMysqlImpl;
	
	private Map<String, SystemDeifyAttr> cacheMap = new ConcurrentHashMap<String, SystemDeifyAttr>();
	
	
	public void setSystemDeifyAttrDaoMysqlImpl(SystemDeifyAttrDaoMysqlImpl systemDeifyAttrDaoMysqlImpl) {
		this.systemDeifyAttrDaoMysqlImpl = systemDeifyAttrDaoMysqlImpl;
	}


	@Override
	public SystemDeifyAttr get(int deifyId, int deifyLevel) {
		String key = deifyId + "-" + deifyLevel;
		if(cacheMap.containsKey(key)){
			return cacheMap.get(key);
		}
		SystemDeifyAttr systemDeifyAttr = systemDeifyAttrDaoMysqlImpl.get(deifyId, deifyLevel);
		if(systemDeifyAttr != null){
			cacheMap.put(key, systemDeifyAttr);
			return systemDeifyAttr;
		}
		return null;
	}


	@Override
	public int maxLevel(int deifyId) {
		
		return this.systemDeifyAttrDaoMysqlImpl.maxLevel(deifyId);
	}

}
