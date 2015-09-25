package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.PkAwardDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.PkAward;

public class PkAwardDaoCacheImpl implements PkAwardDao, ReloadAble {

	private Map<String, PkAward> cache = new ConcurrentHashMap<String, PkAward>();

	private PkAwardDao mysqlDaoImpl;

	private PkAwardDao redisDaoImpl;

	@Override
	public List<PkAward> getAll() {
		List<PkAward> list = new ArrayList<PkAward>();
		if (cache == null || cache.size() == 0) {
			list = initCache();
		} else {
			Collection<PkAward> cl = cache.values();
			for (PkAward pkAward : cl) {
				list.add(pkAward);
			}
		}
		return list;
	}

	private List<PkAward> initCache() {
		List<PkAward> list = null;
		list = mysqlDaoImpl.getAll();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				PkAward pkAward = list.get(i);
				cache.put(Integer.toString(pkAward.getAwardId()), pkAward);
			}
		}
		return list;
	}

	public PkAwardDao getMysqlDaoImpl() {
		return mysqlDaoImpl;
	}

	public void setMysqlDaoImpl(PkAwardDao mysqlDaoImpl) {
		this.mysqlDaoImpl = mysqlDaoImpl;
	}

	public PkAwardDao getRedisDaoImpl() {
		return redisDaoImpl;
	}

	public void setRedisDaoImpl(PkAwardDao redisDaoImpl) {
		this.redisDaoImpl = redisDaoImpl;
	}

	@Override
	public PkAward getById(int awardId) {
		PkAward pkAward = cache.get(Integer.toString(awardId));
		if (pkAward == null) {
			initCache();
			return cache.get(Integer.toString(awardId));
		}
		return pkAward;
	}

	@Override
	public boolean isAwardSended(String date) {
		return this.mysqlDaoImpl.isAwardSended(date);
	}

	@Override
	public boolean addAwardSendLog(String date) {
		return this.mysqlDaoImpl.addAwardSendLog(date);
	}

	@Override
	public void reload() {
		cache.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
