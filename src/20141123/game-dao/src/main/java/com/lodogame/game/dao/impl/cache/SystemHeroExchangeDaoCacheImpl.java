package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemHeroExchangeDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemHeroExchange;

public class SystemHeroExchangeDaoCacheImpl implements SystemHeroExchangeDao, ReloadAble {

	private Map<Integer, List<SystemHeroExchange>> systemHeroExchangeMap = new ConcurrentHashMap<Integer, List<SystemHeroExchange>>();

	private Map<Integer,SystemHeroExchange> getCache = new ConcurrentHashMap<Integer,SystemHeroExchange>();
	
	private SystemHeroExchangeDao systemHeroExchangeDaoMysqlImpl;

	public void setSystemHeroExchangeDaoMysqlImpl(SystemHeroExchangeDao systemHeroExchangeDaoMysqlImpl) {
		this.systemHeroExchangeDaoMysqlImpl = systemHeroExchangeDaoMysqlImpl;
	}

	@Override
	public List<SystemHeroExchange> getList(int week) {

		if (systemHeroExchangeMap.containsKey(week)) {
			return systemHeroExchangeMap.get(week);
		}else{
			List<SystemHeroExchange> systemHeroExchangeList = this.systemHeroExchangeDaoMysqlImpl.getList(week);
			systemHeroExchangeMap.put(week, systemHeroExchangeList);
			return systemHeroExchangeList;
		}
	}

	@Override
	public SystemHeroExchange get(int exchangeHeroId) {
		if(getCache.containsKey(exchangeHeroId)){
			return getCache.get(exchangeHeroId);
		} 
		SystemHeroExchange result = this.systemHeroExchangeDaoMysqlImpl.get(exchangeHeroId);
		if(result!=null){
			getCache.put(exchangeHeroId, result);
		}
		return result;
	}

	@Override
	public void reload() {
		systemHeroExchangeMap.clear();
		getCache.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
