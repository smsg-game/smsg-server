package com.lodogame.game.dao.daobase.redis;

public interface IRedisList<L,T> extends IRedisBean<T>{
    /**
     * 获取list
     * @return
     */
	public L getAllEntryList();
	
	
}
