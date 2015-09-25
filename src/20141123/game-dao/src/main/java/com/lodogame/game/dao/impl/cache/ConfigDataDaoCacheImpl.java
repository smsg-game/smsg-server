package com.lodogame.game.dao.impl.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.ConfigData;

public class ConfigDataDaoCacheImpl implements ConfigDataDao, ReloadAble {

	private ConfigDataDao configDataDaoMysqlImpl;

	private Map<String, ConfigData> cacheMap = new ConcurrentHashMap<String, ConfigData>();

	public void setConfigDataDaoMysqlImpl(ConfigDataDao configDataDaoMysqlImpl) {
		this.configDataDaoMysqlImpl = configDataDaoMysqlImpl;
	}

	@Override
	public ConfigData get(String configKey) {
		if (cacheMap.containsKey(configKey)) {
			return cacheMap.get(configKey);
		}
		ConfigData result = this.configDataDaoMysqlImpl.get(configKey);
		if (result != null) {
			cacheMap.put(configKey, result);
		}
		return result;
	}

	@Override
	public int getInt(String configKey, int defaultValue) {
		ConfigData configData = this.get(configKey);
		if (configData != null) {
			return Double.valueOf(configData.getConfigValue()).intValue();
		}
		return defaultValue;
	}

	@Override
	public String getString(String configKey, String defaultValue) {
		ConfigData configData = this.get(configKey);
		if (configData != null) {
			return configData.getConfigValue();
		}
		return defaultValue;
	}

	@Override
	public void reload() {
		cacheMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
