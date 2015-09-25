package com.lodogame.ldsg.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.helper.CompressHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;

/**
 * 战斗服务类
 * 
 * @author jacky
 * 
 */
public class BattleServiceImpl implements BattleService {

	private static final Logger LOG = Logger.getLogger(BattleServiceImpl.class);

	private static String CACHE_KEY = null;

	private static boolean stated = false;

	@Autowired
	private HeroService heroService;
	
	@Autowired
	private UserService userService;

	private ThreadPoolTaskExecutor executer;

	public void setExecuter(ThreadPoolTaskExecutor executer) {
		this.executer = executer;
	}

	/**
	 * 请求的key
	 */
	private final static String BATTLE_REQUEST_POOL_KEY = "battle_request_pool_key";

	/**
	 * 响应的key
	 */
	private final static String BATTLE_RESPONSE_POOL_KEY = "battle_response_pool_key_";

	private Map<String, EventHandle> handles = new ConcurrentHashMap<String, EventHandle>();

	/**
	 * 实现对战功能
	 * 
	 * <h1>说明</h1> 
	 * 参数attack和defense分别封装了对战的双方，type 用来指名战斗的类型，即表示这次战斗是 Boss
	 * 战、百人斩或者擂台赛等等战斗类型中的哪一种。handle 用来处理战斗结束后的逻辑。
	 * 
	 * <h1>使用方法</h1>
	 * 双方对战时，怎么判定胜负的逻辑已经封装在这个方法内部，每次要用到对战功能时，只需要调用这个方法，新建一个EventHandle
	 * 类并实现里面的handle方法。这个handle 方法描述了你希望怎么处理这个战斗结果。
	 * 
	 */
	public void fight(BattleBO attack, BattleBO defense, int type, EventHandle handle) {

		String id = IDGenerator.getID();
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("at", CompressHelper.compress(attack.getBattleHeroBOList()));
		request.put("df", CompressHelper.compress(defense.getBattleHeroBOList()));
		request.put("al", attack.getUserLevel());
		request.put("dl", defense.getUserLevel());
		request.put("aar", attack.getAddRatio());
		request.put("dar", defense.getAddRatio());
		request.put("type", type);
		request.put("id", id);
		request.put("callback", this.getBattleCallbackKey());

		handles.put(id, handle);

		JedisUtils.pushMsg(BATTLE_REQUEST_POOL_KEY, Json.toJson(request));

	}

	/**
	 * 处理战斗返回的请求
	 * 
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	private void handleResponse(final String response) throws Exception {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				LOG.debug("战斗响应处理.response[" + response + "]");

				Map<String, Object> res = Json.toObject(response, HashMap.class);

				String report = String.valueOf(res.get("bp"));
				final String id = String.valueOf(res.get("id"));

				// 残血信息
				Map<String, Integer> lifeMap = new HashMap<String, Integer>();
				for (Entry<String, Object> entry : res.entrySet()) {
					String key = entry.getKey();
					if (key.startsWith("L_")) {
						lifeMap.put(key, new Double(entry.getValue().toString()).intValue());
					}
				}

				int flag = -100;
				if (res.containsKey("rf")) {
					flag = Integer.parseInt(String.valueOf(res.get("rf")));
				} else {
					LOG.error("战斗出错:[" + res.get("msg") + "]");
					if (handles.containsKey(id)) {
						handles.remove(id);
					}
					return;
				}

				final BattleResponseEvent battleResponseEvent = new BattleResponseEvent(report, flag, lifeMap);

				if (handles.containsKey(id)) {

					EventHandle handle = handles.get(id);
					LOG.info("开始处理战斗请求响应.id[" + id + "]");
					try {
						handle.handle(battleResponseEvent);
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
					// 清除handle
					handles.remove(id);

				} else {
					LOG.warn("战斗请求对的处理器不存在.id[" + id + "]");
				}
			}
		};

		executer.execute(task);
	}

	/**
	 * 监听战斗请求的返回
	 */
	private void checkBattleResponse() {

		try {

			String response = JedisUtils.blockPopMsg(2, getBattleCallbackKey());
			if (response == null) {
				// LOG.info("当前没有战斗响应返回");
				return;
			}

			this.handleResponse(response);

		} catch (Throwable t) {
			LOG.error(t.getMessage());
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {
				LOG.error(ie.getMessage());
			}
		}
	}

	@Override
	public void saveReport(String userId, String targetId, int type, int flag, String report, boolean async) {

	}

	private String getBattleCallbackKey() {

		if (CACHE_KEY == null) {
			CACHE_KEY = BATTLE_RESPONSE_POOL_KEY + IDGenerator.getID();
		}

		return CACHE_KEY;
	}

	public void init() {

		if (stated) {
			return;
		}

		if (!Config.ins().isGameServer()) {
			return;
		}

		stated = true;

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					checkBattleResponse();
				}
			}

		}).start();

	}

	@Override
	public BattleBO createBattleBO(String userId) {
		User user = userService.get(userId);
		
		BattleBO battleBO = new BattleBO();
		List<BattleHeroBO> userBattleHeroBOList = heroService.getUserBattleHeroBOList(userId);
		battleBO.setUserLevel(user.getLevel());
		battleBO.setBattleHeroBOList(userBattleHeroBOList);
		return battleBO;
	}
}
