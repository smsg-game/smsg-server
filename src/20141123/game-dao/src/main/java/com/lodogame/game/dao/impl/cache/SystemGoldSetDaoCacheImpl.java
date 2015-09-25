package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.game.dao.impl.mysql.SystemGoldSetDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemGoldSet;

public class SystemGoldSetDaoCacheImpl extends ReloadAbleBase implements SystemGoldSetDao {

	private Map<Integer, List<SystemGoldSet>> goldSetMap = new ConcurrentHashMap<Integer, List<SystemGoldSet>>();

	private SystemGoldSetDaoMysqlImpl systemGoldSetDaoMysqlImpl;

	@Override
	public List<SystemGoldSet> getList(int type) {
		if (goldSetMap.containsKey(type)) {
			return goldSetMap.get(type);
		}
		List<SystemGoldSet> list = systemGoldSetDaoMysqlImpl.getList(type);
		if (list != null) {
			goldSetMap.put(type, list);
		}
		return list;
	}

	@Override
	public SystemGoldSet getByPayAmount(int type, double amount) {
		SystemGoldSet result = null;
		List<SystemGoldSet> list = getList(type);
		if (list != null) {
			for (SystemGoldSet systemGoldSet : list) {
				if (result == null) {
					if (systemGoldSet.getMoney().doubleValue() <= amount) {
						result = systemGoldSet;
					}
				} else {
					if (systemGoldSet.getMoney().doubleValue() <= amount && systemGoldSet.getGold() > result.getGold()) {
						result = systemGoldSet;
					}
				}
			}
		}
		// SystemGoldSet result2 =
		// systemGoldSetDaoMysqlImpl.getByPayAmount(type, amount);
		// if(result2.getGold()==result.getGold()){
		// System.out.println("和数据库查询出来的是一致的");
		// }else{
		// throw new NullPointerException("和数据库查询出来的不是一致的~~~~~~");
		// }
		return result;
	}

	public void setSystemGoldSetDaoMysqlImpl(SystemGoldSetDaoMysqlImpl systemGoldSetDaoMysqlImpl) {
		this.systemGoldSetDaoMysqlImpl = systemGoldSetDaoMysqlImpl;
	}

	public void init() {

	}

	@Override
	public void reload() {
		goldSetMap.clear();
		// getByPayAmountCache.clear();
	}

}
