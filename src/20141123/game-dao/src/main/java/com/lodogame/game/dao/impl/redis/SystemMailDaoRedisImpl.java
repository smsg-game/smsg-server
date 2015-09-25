package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemMail;
import com.mysql.jdbc.StringUtils;

public class SystemMailDaoRedisImpl implements SystemMailDao {

	@Override
	public List<SystemMail> getSystemMailByTime(Date date) {
		throw new NotImplementedException();
	}

	@Override
	public List<SystemMail> getSystemList() {
		String key = RedisKey.getSystemMailKey();
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			List<SystemMail> systemMailList = new ArrayList<SystemMail>();
			for (String json : jsonList) {
				SystemMail systemMail = Json.toObject(json, SystemMail.class);
				systemMailList.add(systemMail);
			}
			return systemMailList;
		}
		return new ArrayList<SystemMail>();
	}

	@Override
	public SystemMail get(String systemMailId) {
		String key = RedisKey.getSystemMailKey();
		String json = JedisUtils.getFieldFromObject(key, systemMailId);
		if (!StringUtils.isNullOrEmpty(json)) {
			return Json.toObject(json, SystemMail.class);
		}
		return null;
	}

	@Override
	public boolean add(SystemMail systemMail) {

		String json = Json.toJson(systemMail);
		String key = RedisKey.getSystemMailKey();
		JedisUtils.setFieldToObject(key, systemMail.getSystemMailId(), json);

		return true;
	}

	@Override
	public int getMaxSourceId() {
		throw new NotImplementedException();
	}

}
