package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.UserMapperDaoMysqlImpl;
import com.lodogame.model.UserMapper;

public class UserMapperDaoCacheImpl implements UserMapperDao,ClearCacheOnLoginOut {
	
	private UserMapperDaoMysqlImpl userMapperDaoMysqlImpl; 
    //用户id与UserMapper的映射map
	private Map<String,UserMapper> userIdMapCache = new ConcurrentHashMap<String,UserMapper>();
	//PartnerUserId serverId partnerid的映射
	private Map<String,UserMapper> partnerUserIdserverIdpartnerUserIdMapCache = new ConcurrentHashMap<String,UserMapper>();
	@Override
	public boolean save(UserMapper userMapper) {
		if(userMapperDaoMysqlImpl.save(userMapper)){
			userIdMapCache.put(userMapper.getUserId(), userMapper);
			partnerUserIdserverIdpartnerUserIdMapCache.put(generatorKey(userMapper.getPartnerId(),userMapper.getPartnerUserId(),userMapper.getServerId()), userMapper);
		    return true;
		}
		return false;
	}

	private String generatorKey(String partnerId,String partnerUserId,
			 String serverId){
		return partnerId+"-"+partnerUserId+"-"+serverId;
	}
	/**
	 * 初始化缓存
	 * @param userMapper
	 */
	private void initCache(UserMapper userMapper){
		String key = generatorKey(userMapper.getPartnerId(),userMapper.getPartnerUserId(),userMapper.getServerId());
		partnerUserIdserverIdpartnerUserIdMapCache.put(key, userMapper);
		userIdMapCache.put(userMapper.getUserId(), userMapper);
	}
	/**
	 * 清理缓存
	 * @param userId
	 */
	private void removeCache(String userId){
		if(userIdMapCache.containsKey(userId)){
			UserMapper userMapper = userIdMapCache.get(userId);
			partnerUserIdserverIdpartnerUserIdMapCache.remove(generatorKey(userMapper.getPartnerId(),userMapper.getPartnerUserId(),userMapper.getServerId()));
			userIdMapCache.remove(userId);
		}
	}
	
	@Override
	public UserMapper getByPartnerUserId(String partnerUserId,
			String partnerId, String serverId) {
		String key = generatorKey(partnerId,partnerUserId,serverId);
		if(partnerUserIdserverIdpartnerUserIdMapCache.containsKey(key)){
			return partnerUserIdserverIdpartnerUserIdMapCache.get(key);
		}
		UserMapper mapper = userMapperDaoMysqlImpl.getByPartnerUserId(partnerUserId, partnerId, serverId);
		if(mapper!=null){
			initCache(mapper);
		}
		return mapper;
	}

	@Override
	public UserMapper get(String userId) {
		if(userIdMapCache.containsKey(userId)){
			return userIdMapCache.get(userId);
		}
		UserMapper mapper = userMapperDaoMysqlImpl.get(userId);
		if(mapper!=null){
			initCache(mapper);
		}
		return mapper;
	}

	@Override
	public boolean delete(String userId) {
		if(userMapperDaoMysqlImpl.delete(userId)){
			removeCache(userId);
			return true;
		}
		return false;
	}

	public void setUserMapperDaoMysqlImpl(UserMapperDaoMysqlImpl userMapperDaoMysqlImpl) {
		this.userMapperDaoMysqlImpl = userMapperDaoMysqlImpl;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		removeCache(userId);
	}

	@Override
	public boolean updatePhoneInfo(String userId, String imei, String mac, String idfa) {
		return userMapperDaoMysqlImpl.updatePhoneInfo(userId, imei, mac, idfa);
	}

}
