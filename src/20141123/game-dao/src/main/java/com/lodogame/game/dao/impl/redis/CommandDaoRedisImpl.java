package com.lodogame.game.dao.impl.redis;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.Command;

public class CommandDaoRedisImpl implements CommandDao {

	private static final Logger LOG = Logger.getLogger(CommandDaoRedisImpl.class);

	private static final int TIME_OUT = 2;

	private String getKey(int priority) {
		return RedisKey.getCommandKey(priority);
	}

	@Override
	public boolean add(Command command) {
		String key = getKey(command.getPriority());
		LOG.debug("key[" + key + "], value[" + Json.toJson(command) + "]");
		JedisUtils.pushMsg(key, Json.toJson(command));
		return true;
	}

	@Override
	public Command get(Integer... proritys) {

		String[] keys = new String[proritys.length];
		for (int i = 0; i < proritys.length; i++) {
			String key = this.getKey(proritys[i]);
			keys[i] = key;
		}

		String json = JedisUtils.blockPopMsg(TIME_OUT, keys);
		if (json != null) {
			return Json.toObject(json, Command.class);
		}
		return null;
	}

	public boolean cacheReload(String className) {

		Command command = new Command();
		command.setCommand(20002);
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("className", className);
		command.setParams(maps);
		command.setPriority(10);
		add(command);
		return true;
	}

}
