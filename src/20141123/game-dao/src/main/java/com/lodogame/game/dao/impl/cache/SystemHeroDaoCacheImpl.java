package com.lodogame.game.dao.impl.cache;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemHero;

public class SystemHeroDaoCacheImpl implements SystemHeroDao, ReloadAble {

	private Map<Integer, SystemHero> heroMap = new ConcurrentHashMap<Integer, SystemHero>();

	private SystemHeroDao systemHeroDaoMysqlImpl;

	public void setSystemHeroDaoMysqlImpl(SystemHeroDao systemHeroDaoMysqlImpl) {
		this.systemHeroDaoMysqlImpl = systemHeroDaoMysqlImpl;
	}

	public List<SystemHero> getSysHeroList() {

		return this.systemHeroDaoMysqlImpl.getSysHeroList();
	}

	public SystemHero get(Integer systemHeroId) {

		if (heroMap.containsKey(systemHeroId)) {
			return heroMap.get(systemHeroId);
		}

		SystemHero systemHero = null;

		synchronized (SystemHeroDaoCacheImpl.class) {

			systemHero = this.systemHeroDaoMysqlImpl.get(systemHeroId);
			if (systemHero != null) {
				heroMap.put(systemHeroId, systemHero);
			}
		}

		return systemHero;
	}

	public void add(SystemHero systemHero) {
		throw new NotImplementedException();
	}

	@Override
	public void reload() {
		heroMap.clear();
	}

	public void init() {
		ReloadManager.getInstance().register(this.getClass().getSimpleName(), this);
	}

	@Override
	public int getSystemHeroId(Integer heroId, Integer heroColor) {
		
		Iterator<Entry<Integer, SystemHero>> iter = heroMap.entrySet().iterator(); 
		while (iter.hasNext()) {
			Entry<Integer, SystemHero> next = iter.next();
			SystemHero systemHero = next.getValue();
			if (systemHero.getHeroId() == heroId && systemHero.getHeroColor() == heroColor) {
				return systemHero.getSystemHeroId();
			}
			
		}
		
		int systemHeroId = systemHeroDaoMysqlImpl.getSystemHeroId(heroId, heroColor);
		if (systemHeroId != 0) {
			heroMap.put(systemHeroId, get(systemHeroId));
		}
		return systemHeroId;
	}

}
