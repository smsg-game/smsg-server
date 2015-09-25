package com.ldsg.battle;

import java.util.HashMap;
import java.util.Map;

import com.ldsg.battle.util.Convert;

public class ASObject {

	private Map<String, Object> data;

	public ASObject() {
		this.data = new HashMap<String, Object>();
	}

	public ASObject(Map<String, Object> map) {
		this.data = map;
	}

	/**
	 * 获取整形
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {

		if (this.data.containsKey(key)) {
			return Convert.toInt32(this.data.get(key).toString());
		}

		return 0;
	}

	public Object get(String key) {
		if (this.data.containsKey(key)) {
			return this.data.get(key);
		}
		return null;
	}

	/**
	 * 获取字符串
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {

		if (this.data.containsKey(key)) {
			return Convert.toString(this.data.get(key));
		}

		return null;
	}

	public boolean containsKey(String key) {
		return this.data.containsKey(key);
	}

	public void put(String key, Object value) {
		this.data.put(key, value);
	}
}
