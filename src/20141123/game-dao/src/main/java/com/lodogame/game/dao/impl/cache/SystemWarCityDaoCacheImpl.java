package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.lodogame.game.dao.SystemWarCityDao;
import com.lodogame.game.dao.impl.mysql.SystemWarCityDaoMysqlImpl;
import com.lodogame.model.SystemWarCity;

public class SystemWarCityDaoCacheImpl implements SystemWarCityDao {
	
	private Map<Integer, SystemWarCity> cacheMap = new ConcurrentHashMap<Integer, SystemWarCity>();
	
	private SystemWarCityDaoMysqlImpl systemWarCityDaoMysqlImpl;
	
	public void setSystemWarCityDaoMysqlImpl(SystemWarCityDaoMysqlImpl systemWarCityDaoMysqlImpl) {
		this.systemWarCityDaoMysqlImpl = systemWarCityDaoMysqlImpl;
	}

	public SystemWarCity get(Integer point){
		if(cacheMap.containsKey(point)){
			return cacheMap.get(point);
		}
		SystemWarCity systemWarCity = this.systemWarCityDaoMysqlImpl.get(point);
		cacheMap.put(point, systemWarCity);
		return systemWarCity;
	}
}
