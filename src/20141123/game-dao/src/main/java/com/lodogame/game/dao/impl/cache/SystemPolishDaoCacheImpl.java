package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.SystemDeifyNodeDao;
import com.lodogame.game.dao.SystemPolishDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemDeifyNode;
import com.lodogame.model.SystemPolish;

public class SystemPolishDaoCacheImpl implements SystemPolishDao, ReloadAble {


	private Map<Integer, SystemPolish> systemPolishMap = new HashMap<Integer, SystemPolish>();
	
	private  SystemPolishDao systemPolishDaoMysqlImpl;

	public void setSystemPolishDaoMysqlImpl(SystemPolishDao systemPolishDaoMysqlImpl) {
		this.systemPolishDaoMysqlImpl = systemPolishDaoMysqlImpl;
	}

	@Override
	public SystemPolish getSystemPolish(int systemEuqipId) {
		
		SystemPolish systemPolish = null;
		if (!systemPolishMap.containsKey(systemEuqipId)) {
			systemPolish = this.systemPolishDaoMysqlImpl.getSystemPolish(systemEuqipId);
			if (systemPolish != null) {
				systemPolishMap.put(systemEuqipId,systemPolish );
			}
			
		}
		return systemPolishMap.get(systemEuqipId);
	}
	
	@Override
	public void reload() {
		systemPolishMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}


}
