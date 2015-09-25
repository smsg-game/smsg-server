package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemMallDao;
import com.lodogame.game.dao.impl.mysql.SystemMallDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemMall;

public class SystemMallDaoCacheImpl implements SystemMallDao,ReloadAble {

	private SystemMallDaoMysqlImpl  systemMallDaoMysqlImpl;
	
	private List<SystemMall> cache;
	@Override
	public List<SystemMall> getList() {
		return cache;
	}
	@Override
	public SystemMall get(int mallId) {
		for(SystemMall systemMall:cache){
			if(systemMall.getMallId()==mallId){
				return systemMall;
			} 
		}
		return null;
	}

	private void initCache(){
		cache = systemMallDaoMysqlImpl.getList();
	}
	@Override
	public void reload() {
		initCache();
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	public void setSystemMallDaoMysqlImpl(SystemMallDaoMysqlImpl systemMallDaoMysqlImpl) {
		this.systemMallDaoMysqlImpl = systemMallDaoMysqlImpl;
	}

}
