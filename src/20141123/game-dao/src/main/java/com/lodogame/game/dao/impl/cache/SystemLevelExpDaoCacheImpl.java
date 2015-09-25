package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemLevelExpDao;
import com.lodogame.game.dao.impl.mysql.SystemLevelExpDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemLevelExp;

public class SystemLevelExpDaoCacheImpl implements SystemLevelExpDao,ReloadAble {

	private Map<Integer,SystemLevelExp> getHeroExpCache = new ConcurrentHashMap<Integer,SystemLevelExp>();
	
	private SystemLevelExpDaoMysqlImpl  systemLevelExpDaoMysqlImpl;
	@Override
	public SystemLevelExp getHeroLevel(int exp) {
		SystemLevelExp result = null;
		for(SystemLevelExp keyExp:getHeroExpCache.values()){
				if(result==null){
					if(keyExp.getExp()<=exp){
						result = keyExp;
					}
				}else{
					if(keyExp.getExp()<=exp&&keyExp.getLevel()>result.getLevel()){
						result = keyExp;
					}
				}
		}
//		SystemLevelExp result2 =  systemLevelExpDaoMysqlImpl.getHeroLevel(exp);
//		if(result2.getExp()==result.getExp()&&result2.getLevel()==result.getLevel()){
//			System.out.println("和数据库查询出来的是一致的"+result2.getExp()+"_"+result.getLevel());
//		}else{
//			throw new NullPointerException("和数据库查询出来的不是一致的~~~~~~");
//		}
		return result;
	}

	@Override
	public SystemLevelExp getHeroExp(int level) {
		return getHeroExpCache.get(level);
	}

	private void initCache(){
		getHeroExpCache.clear();
		List<SystemLevelExp> list = systemLevelExpDaoMysqlImpl.getSystemLevelExpList();
		for(SystemLevelExp systemLevleExp:list){
			getHeroExpCache.put(systemLevleExp.getLevel(), systemLevleExp);
		}
	}
	@Override
	public void reload() {
		initCache();
	}

	public void setSystemLevelExpDaoMysqlImpl(SystemLevelExpDaoMysqlImpl systemLevelExpDaoMysqlImpl) {
		this.systemLevelExpDaoMysqlImpl = systemLevelExpDaoMysqlImpl;
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
