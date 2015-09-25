package com.ldsg.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ldsg.battle.bo.BattleInfo;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.config.SkillConfig;
import com.ldsg.battle.util.Convert;
import com.ldsg.battle.util.Json;
import com.ldsg.battle.util.UrlRequestUtils;

public class WorkerImpl extends Thread implements Worker {

	private static Logger logger = Logger.getLogger(WorkerImpl.class);

	private final static String REQUEST_POOL_KEY = "battle_request_pool_key";

	private ThreadPoolTaskExecutor executer;

	private String url;

	public void setUrl(String url) {
		this.url = url;
	}

	private JedisPool pool;

	private int port;

	public void setExecuter(ThreadPoolTaskExecutor executer) {
		this.executer = executer;
	}

	public void setPool(JedisPool pool, int port) {
		this.pool = pool;
		this.port = port;
	}

	private BattleHandle handle = new BattleHandle();

	private String blockPopMsg(String key) {
		Jedis jedis = pool.getResource();
		try {
			List<String> ret = jedis.blpop(2, key);
			if (ret != null) {
				return ret.get(1);
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	private void pushMsg(String key, String val) {

		Jedis jedis = pool.getResource();
		try {
			jedis.lpush(key, val);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.returnResource(jedis);
		}

	}

	private void monitor() {

		try {

			Map<String, String> params = new HashMap<String, String>();
			params.put("method", "battle_status_report");
			params.put("port", String.valueOf(port));
			String jsonStr = UrlRequestUtils.execute(url, params, UrlRequestUtils.Mode.GET);
			logger.debug("ret:" + jsonStr);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException ie) {
			logger.error(ie.getMessage());
		}

	}

	private void startMonitor() {

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					monitor();
				}
			}

		}).start();

	}

	public void run() {

		SkillConfig.init();
		EffectConfig.init();

		// 开始监听器
		startMonitor();

		while (true) {

			try {
				String json = this.blockPopMsg(REQUEST_POOL_KEY);
				if (json != null) {
					if (StringUtils.equalsIgnoreCase(json, "restart")) {
						logger.debug("relaod data");
						SkillConfig.reload();
						EffectConfig.reload();
					} else {
						this.handleRequest(json);
					}
				} else {
					logger.info("[" + this.getName() + "]当前没有战斗请求");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				try {
					Thread.sleep(100);
				} catch (InterruptedException ie) {
					logger.error(ie.getMessage());
				}
			}

		}

	}

	/**
	 * 处理请求
	 * 
	 * @param json
	 */
	private void handleRequest(final String json) {

		Runnable task = new Runnable() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run() {

				Map<String, Object> data = Json.toObject(json, Map.class);

				String callback = Convert.toString(data.get("callback"));
				String id = Convert.toString(data.get("id"));

				logger.debug("request id[" + id + "], data[" + json + "]");

				// 进攻方战斗信息
				BattleInfo attackBattleInfo = new BattleInfo();

				List<Map> at = (List<Map>) data.get("at");

				List<ASObject> attackHeroList = new ArrayList<ASObject>();
				for (Map m : at) {
					attackHeroList.add(new ASObject(m));
				}

				attackBattleInfo.setHeroList(attackHeroList);
				attackBattleInfo.setLevel(Convert.toInt32(data.get("al")));
				attackBattleInfo.setAddRatio(Convert.toDouble(data.get("aar")));
				attackBattleInfo.setWarcraftList(new ArrayList<ASObject>());

				// 防守方战斗信息
				BattleInfo defenseBattleInfo = new BattleInfo();

				List<Map> df = (List<Map>) data.get("df");

				List<ASObject> defenseHeroList = new ArrayList<ASObject>();
				for (Map m : df) {
					defenseHeroList.add(new ASObject(m));
				}

				defenseBattleInfo.setHeroList(defenseHeroList);
				defenseBattleInfo.setLevel(Convert.toInt32(data.get("dl")));
				defenseBattleInfo.setAddRatio(Convert.toDouble(data.get("dar")));
				defenseBattleInfo.setWarcraftList(new ArrayList<ASObject>());

				Map<String, Object> result = handle.handle(attackBattleInfo, defenseBattleInfo, 0);
				result.put("id", id);

				// 回传战斗结果
				pushMsg(callback, Json.toJson(result));
			}
		};

		executer.execute(task);
	}

}
