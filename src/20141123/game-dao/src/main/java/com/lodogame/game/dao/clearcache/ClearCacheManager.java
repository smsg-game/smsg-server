package com.lodogame.game.dao.clearcache;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * 缓存清理管理器
 * @author foxwang
 *
 */
public class ClearCacheManager{
   private List<ClearCacheOnLoginOut> cacheList = new ArrayList<ClearCacheOnLoginOut>();
   private static final Logger LOG = Logger.getLogger(ClearCacheManager.class);
   private static ClearCacheManager cacheManager;
   private ClearCacheManager(){}
   
   public static ClearCacheManager getInstance(){
	   if(cacheManager==null){
		   cacheManager = new ClearCacheManager();
	   }
	   return cacheManager;
   }
   
	/**
	 * 添加缓存
	 * @param cache
	 */
   public void addCache(ClearCacheOnLoginOut cache){
	   cacheList.add(cache);
   }
   /**
    * 清理缓存
    */
   public void executeUserLogOut(String userId){
	   for(ClearCacheOnLoginOut cacheOnLoginOut:cacheList){
		   try {
			   cacheOnLoginOut.clearOnLoginOut(userId);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error(e);
		}
	   }
   }
}
