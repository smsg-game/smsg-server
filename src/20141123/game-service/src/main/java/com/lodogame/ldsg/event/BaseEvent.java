package com.lodogame.ldsg.event;

import java.util.HashMap;
import java.util.Map;

public class BaseEvent implements Event {

	protected Map<String, Object> data = new HashMap<String, Object>();

	protected String userId;

	public String getUserId() {
		return this.userId;
	}

	public void setObject(String key, Object value) {
		this.data.put(key, value);
	}

	public Object getObject(String key) {
		return this.data.get(key);
	}

	public String getString(String key) {
		Object obj = this.data.get(key);
		if (obj != null) {
			return obj.toString();
		}
		return null;
	}

	public Integer getInt(String key) {
		return Integer.parseInt(getString(key));
	}

}
