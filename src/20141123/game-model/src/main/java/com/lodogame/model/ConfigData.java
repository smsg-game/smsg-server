package com.lodogame.model;

import java.io.Serializable;

public class ConfigData implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 配置key
	 */
	private String configKey;

	/**
	 * 配置值
	 */
	private String configValue;

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

}
