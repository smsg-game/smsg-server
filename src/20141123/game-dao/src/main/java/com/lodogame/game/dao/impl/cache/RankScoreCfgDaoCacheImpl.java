package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.RankScoreCfgDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.RankScoreCfg;

public class RankScoreCfgDaoCacheImpl implements RankScoreCfgDao, ReloadAble {

	private Map<String, RankScoreCfg> cache = new ConcurrentHashMap<String, RankScoreCfg>();

	private RankScoreCfgDao mysqlDaoImpl;

	private RankScoreCfgDao redisDaoImpl;

	public RankScoreCfgDao getRedisDaoImpl() {
		return redisDaoImpl;
	}

	public void setRedisDaoImpl(RankScoreCfgDao redisDaoImpl) {
		this.redisDaoImpl = redisDaoImpl;
	}

	@Override
	public RankScoreCfg getByRank(int rank) {
		RankScoreCfg config = cache.get(Integer.toString(rank));
		if (config == null) {
			config = mysqlDaoImpl.getByRank(rank);
			if (config != null) {
				cache.put(Integer.toString(rank), config);
			}
		}
		return config;
	}

	public RankScoreCfgDao getMysqlDaoImpl() {
		return mysqlDaoImpl;
	}

	public void setMysqlDaoImpl(RankScoreCfgDao mysqlDaoImpl) {
		this.mysqlDaoImpl = mysqlDaoImpl;
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
