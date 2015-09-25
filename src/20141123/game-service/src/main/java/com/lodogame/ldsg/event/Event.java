package com.lodogame.ldsg.event;

/**
 * 事件接口
 * 
 * @author jacky
 * 
 */
public interface Event {

	public String getUserId();

	public Object getObject(String key);

	public String getString(String key);

	public Integer getInt(String key);

	public void setObject(String key, Object value);
}
