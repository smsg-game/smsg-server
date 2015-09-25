package com.lodogame.game.dao.reload;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ReloadManager {

	private static final Logger LOG = Logger.getLogger(ReloadManager.class);

	private Map<String, ReloadAble> reloadAbleMap = new HashMap<String, ReloadAble>();

	private static ReloadManager manager = null;

	private ReloadManager() {

	}

	public static ReloadManager getInstance() {

		if (manager == null) {
			manager = new ReloadManager();
		}
		return manager;
	}

	public void register(String className, ReloadAble reloadAble) {
		LOG.info("reloadAble register.class[" + className + "]");
		reloadAbleMap.put(className, reloadAble);
	}

	public void reload(String targetClassName) {

		for (Entry<String, ReloadAble> entry : reloadAbleMap.entrySet()) {
			String className = entry.getKey();

			if (!StringUtils.equalsIgnoreCase("ALL", targetClassName) && !StringUtils.equalsIgnoreCase(targetClassName, className)) {
				LOG.info("reload skip.class[" + className + "]");
				continue;
			}

			ReloadAble reloadAble = entry.getValue();
			try {
				LOG.info("reload data.class[" + className + "]");
				reloadAble.reload();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

	}
}
