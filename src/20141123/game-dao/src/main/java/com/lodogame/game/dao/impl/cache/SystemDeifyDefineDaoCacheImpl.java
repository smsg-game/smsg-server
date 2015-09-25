package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemDeifyDefineDao;
import com.lodogame.game.dao.impl.mysql.SystemDeifyDefineDaoMysqlImpl;
import com.lodogame.model.SystemDeifyDefine;

public class SystemDeifyDefineDaoCacheImpl implements SystemDeifyDefineDao{
	
	private SystemDeifyDefineDaoMysqlImpl systemDeifyDefineDaoMysqlImpl;
	
	private Map<Integer, SystemDeifyDefine> deifyMap = new ConcurrentHashMap<Integer, SystemDeifyDefine>();
	
	private Map<Integer, List<SystemDeifyDefine>> heroMap = new ConcurrentHashMap<Integer, List<SystemDeifyDefine>>();
	
	private Map<String, SystemDeifyDefine> typeMap = new ConcurrentHashMap<String, SystemDeifyDefine>();
	
	public void setSystemDeifyDefineDaoMysqlImpl(SystemDeifyDefineDaoMysqlImpl systemDeifyDefineDaoMysqlImpl) {
		this.systemDeifyDefineDaoMysqlImpl = systemDeifyDefineDaoMysqlImpl;
	}

	@Override
	public SystemDeifyDefine get(int deifyId) {
		if(deifyMap.containsKey(deifyId)){
			return deifyMap.get(deifyId);
		}
		SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDaoMysqlImpl.get(deifyId);
		if(systemDeifyDefine != null){
			deifyMap.put(deifyId, systemDeifyDefine);
			return systemDeifyDefine;
		}
		return null;
	}

	@Override
	public List<SystemDeifyDefine> getByHeroId(int heroId) {
		if(heroMap.containsKey(heroId)){
			return heroMap.get(heroId);
		}
		List<SystemDeifyDefine> systemDeifyDefines = this.systemDeifyDefineDaoMysqlImpl.getByHeroId(heroId);
		if(systemDeifyDefines != null && systemDeifyDefines.size() > 0 ){
			heroMap.put(heroId, systemDeifyDefines);
			return systemDeifyDefines;
		}
		return new ArrayList<SystemDeifyDefine>();
	}

	@Override
	public SystemDeifyDefine getByHeroIdAndType(int heroId, int type) {
		String key = heroId + "-" + type;
		if(typeMap.containsKey(key)){
			return typeMap.get(key);
		}
		SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDaoMysqlImpl.getByHeroIdAndType(heroId, type);
		if(systemDeifyDefine != null){
			typeMap.put(key, systemDeifyDefine);
			return systemDeifyDefine;
		}
		return null;
	}

}
