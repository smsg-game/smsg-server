package com.lodogame.game.cache;

/**
 * 缓存接口
 * @author CJ
 *
 */
public interface Cache {
	/**
	 * set值到缓存，指定过期时间，非memcached不支持
	 * @param key
	 * @param value
	 * @param second
	 */
	public void set(String key, String value, int second);
	
	/**
	 * set值到缓存 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);
	
	/**
	 * 从缓存中获取值
	 * @param key
	 * @return
	 */
	public String get(String key);
}
