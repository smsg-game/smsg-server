package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.impl.mysql.SystemUserLevelDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemUserLevel;

public class SystemUserLevelDaoCacheImpl implements SystemUserLevelDao,ReloadAble {

	@Autowired
	private SystemUserLevelDaoMysqlImpl systemUserLevelDaoMysqlImpl;
	private Map<Integer,SystemUserLevel> getMap = new ConcurrentHashMap<Integer,SystemUserLevel>();

	public SystemUserLevel getUserLevel(long exp) {
		SystemUserLevel result = null;
		for(SystemUserLevel systemUserLevel:getMap.values()){
			if(result==null){
				if(systemUserLevel.getExp()<=exp){
					result = systemUserLevel;
				}
			}else{
				if(systemUserLevel.getExp()<=exp&&systemUserLevel.getUserLevel()>result.getUserLevel()){
					result = systemUserLevel;
				}
			}
		}
//		SystemUserLevel result2 = this.systemUserLevelDaoMysqlImpl.getUserLevel(exp);
//        if(result.getUserLevel()==result2.getUserLevel()){
//        	System.out.println("与数据库查询出来的一致~~~~~~~~~~~~~");
//        }else{
//        	throw new NullPointerException("与数据库中查询出来的不一致~~");
//        }
		return result;
	}

	public SystemUserLevel get(int level) {
		return getMap.get(level);
	}

	public void add(SystemUserLevel systemUserLevel) {
		throw new NotImplementedException();
	}
	
    private void initCache(){
    	getMap.clear();
    	List<SystemUserLevel> list = systemUserLevelDaoMysqlImpl.getAll();
    	for(SystemUserLevel systemUserLevel:list){
    		getMap.put(systemUserLevel.getUserLevel(), systemUserLevel);
    	}
    }
    
	@Override
	public void reload() {
		initCache();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
		initCache();
	}
}
