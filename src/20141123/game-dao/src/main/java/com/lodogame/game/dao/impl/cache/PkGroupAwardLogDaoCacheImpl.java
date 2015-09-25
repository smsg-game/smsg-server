package com.lodogame.game.dao.impl.cache;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.PkGroupAwardLogDao;
import com.lodogame.game.dao.impl.mysql.PkGroupAwardLogDaoMysqlImpl;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.PkGroupAwardLog;

public class PkGroupAwardLogDaoCacheImpl implements PkGroupAwardLogDao {

	private PkGroupAwardLogDaoMysqlImpl pkGroupAwardLogDaoMysqlImpl;
	
	private PkAwardDaoCacheImpl pkAwardDaoCacheImpl;

	private Map<String, List<PkGroupAwardLog>> pkGroupMap = new ConcurrentHashMap<String, List<PkGroupAwardLog>>();

	public void setPkGroupAwardLogDaoMysqlImpl(PkGroupAwardLogDaoMysqlImpl pkGroupAwardLogDaoMysqlImpl) {
		this.pkGroupAwardLogDaoMysqlImpl = pkGroupAwardLogDaoMysqlImpl;
	}
	
	public void setPkAwardDaoCacheImpl(PkAwardDaoCacheImpl pkAwardDaoCacheImpl) {
		this.pkAwardDaoCacheImpl = pkAwardDaoCacheImpl;
	}

	@Override
	public void add(PkGroupAwardLog pkGroupAwardLog) {
		this.pkGroupAwardLogDaoMysqlImpl.add(pkGroupAwardLog);
	}

	@Override
	public List<PkGroupAwardLog> getList(int groupId) {
		String key = groupId + "~" + DateUtils.getDate();
		if (DateUtils.getHour() >= 21) {
			String date = DateUtils.getDate();
			if (pkAwardDaoCacheImpl.isAwardSended(date)) {
				pkGroupMap.clear();
				key = groupId + "~" + DateUtils.getDate(DateUtils.getAfterTime(new Date(), 60*60*24));
				List<PkGroupAwardLog> list = this.pkGroupAwardLogDaoMysqlImpl.getList(groupId);
				pkGroupMap.put(key, list);
				return list;
			} 
		}else{ 
			String date = DateUtils.getDate();
			Collection keys = pkGroupMap.keySet();
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				Object k = iterator.next();
				if(k != null){
					String kstr = k.toString();
					if(!kstr.split("~")[1].equals(date)){
						pkGroupMap.clear();
					}
				}
			}
		}
		if (pkGroupMap.containsKey(key)) {
			return pkGroupMap.get(key);
		}
		List<PkGroupAwardLog> list = this.pkGroupAwardLogDaoMysqlImpl.getList(groupId);
		pkGroupMap.put(key, list);
		return list;

	}


	@Override
	public PkGroupAwardLog get(String userId) {
		return this.pkGroupAwardLogDaoMysqlImpl.get(userId);
	}

	@Override
	public void deleteRecord() {
		this.pkGroupAwardLogDaoMysqlImpl.deleteRecord();
	}

	@Override
	public void updateHostory() {
		this.pkGroupAwardLogDaoMysqlImpl.updateHostory();
	}

	@Override
	public boolean updateGet(String userId, Date date) {
		return this.pkGroupAwardLogDaoMysqlImpl.updateGet(userId, date);
	}

}
