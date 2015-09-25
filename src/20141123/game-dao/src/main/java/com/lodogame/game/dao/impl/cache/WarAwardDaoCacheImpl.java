package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.WarAwardDao;
import com.lodogame.game.dao.impl.mysql.WarAwardDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.WarAward;

public class WarAwardDaoCacheImpl implements WarAwardDao, ReloadAble {

	private Map<Integer, WarAward> cacheMap = new ConcurrentHashMap<Integer, WarAward>();
	
	private WarAwardDaoMysqlImpl warAwardDaoMysqlImpl;
	
	public void setWarAwardDaoMysqlImpl(WarAwardDaoMysqlImpl warAwardDaoMysqlImpl) {
		this.warAwardDaoMysqlImpl = warAwardDaoMysqlImpl;
	}

	@Override
	public WarAward get(int awardId) {
		if(cacheMap.containsKey(awardId)){
			return cacheMap.get(awardId);
		}
		WarAward warAward = this.warAwardDaoMysqlImpl.get(awardId);
		cacheMap.put(awardId, warAward);
		return warAward;
	}

	@Override
	public List<WarAward> getAll() {
		return this.warAwardDaoMysqlImpl.getAll();
	}

	@Override
	public void reload() {
		this.cacheMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
		
	}

}
