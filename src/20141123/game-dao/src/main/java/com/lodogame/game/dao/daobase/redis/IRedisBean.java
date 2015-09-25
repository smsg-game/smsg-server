package com.lodogame.game.dao.daobase.redis;

public interface IRedisBean <T>{
    /**
     * 初始化缓存
     * @param t
     */
	public void initEntry(String userId,T t);
	
	/**
	 * 更新缓存
	 * @param t
	 */
    public void updateEntry(String userId,T t);
    
    /**
     * 删除记录
     * @param t
     */
    public void delEntry(String userId);
    /**
     * 查询一个对象
     * @param key
     * @return
     */
    public T getEntry(String userId);
    
    /**
     * 是否存在某个key
     * @param userId
     * @return
     */
    public boolean existUserId(String userId);
    /**
     * 获取key前缀
     * @param key
     * @return
     */
    public String getPreKey();
    
}
