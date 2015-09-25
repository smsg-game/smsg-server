package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.game.dao.SystemPolishConsumeDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemPolishConsume;

public class SystemPolishConsumeDaoCacheImpl implements SystemPolishConsumeDao, ReloadAble {


	private Map<Integer, SystemPolishConsume> systemPolishConsumeMap = new HashMap<Integer, SystemPolishConsume>();
	
	private  SystemPolishConsumeDao systemPolishConsumeDaoMysqlImpl;

	public void setSystemPolishConsumeDaoMysqlImpl(SystemPolishConsumeDao systemPolishConsumeDaoMysqlImpl) {
		this.systemPolishConsumeDaoMysqlImpl = systemPolishConsumeDaoMysqlImpl;
	}

	@Override
	public SystemPolishConsume getSystemPolishConsume(int polishType) {
		
		SystemPolishConsume systemPolishConsume = null;
		if (!systemPolishConsumeMap.containsKey(polishType)) {
			systemPolishConsume = this.systemPolishConsumeDaoMysqlImpl.getSystemPolishConsume(polishType);
			if (systemPolishConsume != null) {
				systemPolishConsumeMap.put(polishType, systemPolishConsume );
			}
			
		}
		return systemPolishConsumeMap.get(polishType);
	}
	
	@Override
	public void reload() {
		systemPolishConsumeMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}


}
