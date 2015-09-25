package com.lodogame.game.dao.impl.redis;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemEquip;

public class SystemEquipDaoRedisImpl implements SystemEquipDao {

	@Override
	public List<SystemEquip> getSystemEquipList() {
		throw new NotImplementedException();
	}

	@Override
	public SystemEquip get(int equipId) {

		String key = RedisKey.getSystemEquipKey();
		String json = JedisUtils.getFieldFromObject(key, String.valueOf(equipId));
		return Json.toObject(json, SystemEquip.class);
	}

	@Override
	public boolean add(SystemEquip systemEquip) {

		String key = RedisKey.getSystemEquipKey();
		String json = Json.toJson(systemEquip);
		JedisUtils.setFieldToObject(key, String.valueOf(systemEquip.getEquipId()), json);
		return true;
	}

}
