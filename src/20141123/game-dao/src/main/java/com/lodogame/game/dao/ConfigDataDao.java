package com.lodogame.game.dao;

import com.lodogame.model.ConfigData;

public interface ConfigDataDao {

	public ConfigData get(String configKey);

	public int getInt(String configKey, int defaultValue);

	public String getString(String configKey, String defaultValue);

}
