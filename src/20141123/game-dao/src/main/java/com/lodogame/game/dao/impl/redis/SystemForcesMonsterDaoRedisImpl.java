package com.lodogame.game.dao.impl.redis;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.lodogame.game.dao.SystemForcesMonsterDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemForcesMonster;
import com.mysql.jdbc.StringUtils;

public class SystemForcesMonsterDaoRedisImpl implements SystemForcesMonsterDao {

	private static final Logger logger = Logger.getLogger(SystemForcesMonsterDaoRedisImpl.class);

	@Override
	@SuppressWarnings("unchecked")
	public List<SystemForcesMonster> getForcesMonsterList(int forcesId) {
		String key = RedisKey.getForcesMonsterCacheKey(forcesId);
		String json = JedisUtils.get(key);
		if (!StringUtils.isNullOrEmpty(json)) {
			List<SystemForcesMonster> list = new ArrayList<SystemForcesMonster>();
			try {
				List<Map<String, Object>> mlist = Json.toObject(json, List.class);
				for (Map<String, Object> m : mlist) {
					SystemForcesMonster systemForcesMonster = new SystemForcesMonster();
					BeanUtils.populate(systemForcesMonster, m);
					list.add(systemForcesMonster);
				}
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException ie) {
				logger.error(ie.getMessage(), ie);
			}
			return list;
		}
		return null;
	}

}
