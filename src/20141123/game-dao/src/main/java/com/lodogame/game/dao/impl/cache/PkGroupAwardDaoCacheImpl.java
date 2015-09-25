package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.impl.PkGroupAwardDao;
import com.lodogame.game.dao.impl.mysql.PkGroupAwardDaoMysqlImpl;
import com.lodogame.model.PkGroupAward;

public class PkGroupAwardDaoCacheImpl implements PkGroupAwardDao {

	private PkGroupAwardDaoMysqlImpl pkGroupAwardDaoMysqlImpl;

	private Map<String, PkGroupAward> pkAwardMap = new ConcurrentHashMap<String, PkGroupAward>();
	
	public void setPkGroupAwardDaoMysqlImpl(PkGroupAwardDaoMysqlImpl pkGroupAwardDaoMysqlImpl) {
		this.pkGroupAwardDaoMysqlImpl = pkGroupAwardDaoMysqlImpl;
	}

	@Override
	public PkGroupAward getGroupAward(int gid, int rank) {
		String key = gid + "-" + rank;
		if(pkAwardMap.containsKey(key)){
			return pkAwardMap.get(key);
		}
		
		PkGroupAward pkGroupAward = this.pkGroupAwardDaoMysqlImpl.getGroupAward(gid, rank);
		pkAwardMap.put(key, pkGroupAward);
		
		return pkGroupAward;
	}
	
}
