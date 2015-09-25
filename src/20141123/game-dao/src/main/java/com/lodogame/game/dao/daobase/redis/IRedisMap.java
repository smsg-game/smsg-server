package com.lodogame.game.dao.daobase.redis;

import java.util.List;

/**
 * t为map中的值
 * @author foxwang
 *
 * @param <E>
 * @param <T>
 */
public interface IRedisMap<M,T> extends IRedisBean<M>{
	
    /**
     * 删除一个map对象中的一个键值对
     * @param rootKey
     * @param entryKey
     */
    public void delEntryEntry(String rootKey,String entryKey);
    /**
     * 获取一个map中一个键的对象
     * @param rootKkey
     * @param entryKey
     */
    public T getEntryEntry(String rootKey,String entryKey);
    /**
     * 更新map对象中德一个值
     * @param rootKey
     * @param entryKey
     * @return
     */
    public boolean updateEntryEntry(String rootKey,String entryKey,T t);
    /**
     * 获取Map中所有的对象
     * @param rootKey
     * @return
     */
    public List<T> getAllEntryValue(String rootKey);
}
