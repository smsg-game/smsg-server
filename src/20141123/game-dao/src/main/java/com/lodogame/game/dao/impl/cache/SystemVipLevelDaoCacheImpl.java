package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.impl.mysql.SystemVipLevelDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemVipLevel;

public class SystemVipLevelDaoCacheImpl implements SystemVipLevelDao,ReloadAble {

	private SystemVipLevelDaoMysqlImpl systemVipLevelDaoMysqlImpl;
	private Map<Integer,SystemVipLevel> getMap = new ConcurrentHashMap<Integer,SystemVipLevel>();
	@Override
	public SystemVipLevel get(int vipLevel) {
		 return getMap.get(vipLevel);
	}

	@Override
	public SystemVipLevel getBuyMoney(int money) {
		SystemVipLevel result = null;
		
		for(SystemVipLevel systemVipLevel:getMap.values()){
			if(result==null){
				if(systemVipLevel.getNeedMoney()<=money){
					result = systemVipLevel;
				}
			}else{
				if(systemVipLevel.getNeedMoney()<=money&&systemVipLevel.getVipLevel()>result.getVipLevel()){
					result = systemVipLevel;
				}
			}
		}
//		SystemVipLevel systemVipLevel = systemVipLevelDaoMysqlImpl.getBuyMoney(money);
//		if(systemVipLevel.getVipLevel()==result.getVipLevel()){
//			System.out.println("与数据库查询出来的一致");
//		}else{
//			throw new NullPointerException("与数据库查询出来的不一致");
//		}
		return result;
	}

	@Override
	public void reload() {
		initCach();
	}
	public void setSystemVipLevelDaoMysqlImpl(SystemVipLevelDaoMysqlImpl systemVipLevelDaoMysqlImpl) {
		this.systemVipLevelDaoMysqlImpl = systemVipLevelDaoMysqlImpl;
	}
    private void initCach(){
    	getMap.clear();
    	List<SystemVipLevel> list = systemVipLevelDaoMysqlImpl.getAll();
    	for(SystemVipLevel systemVipLevel:list){
    		getMap.put(systemVipLevel.getVipLevel(), systemVipLevel);
    	}
    }
	@Override
	public void init() {
		initCach();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
