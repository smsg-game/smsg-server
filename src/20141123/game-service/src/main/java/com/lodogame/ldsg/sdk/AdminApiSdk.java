package com.lodogame.ldsg.sdk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.config.Config;
import com.lodogame.model.GameServer;
import com.lodogame.model.Partner;

public class AdminApiSdk {
	private final static Logger LOG = Logger.getLogger(GameApiSdk.class);

	private final static String HTTP_URL_HEAD = "http://";
	private final static String HOST = "admin.sg.xworldgame.com";
//	private final static String HOST = "admin.ldsg.lodogame.com:8088";
	//private final static String HOST = "localhost:8088";
	private static AdminApiSdk sdk;
	
	private AdminApiSdk() {
	}

	public static AdminApiSdk getInstance() {
		synchronized (AdminApiSdk.class) {
			if (sdk == null) {
				sdk = new AdminApiSdk();
			}
		}
		return sdk;
	}
	
	@SuppressWarnings("unchecked")
	public List<GameServer> loadServers(String partnerId) {
		try {
			//String url = HTTP_URL_HEAD + HOST + "/adminApi/serverList.do";
			Map<String, String> params = new HashMap<String, String>();
			params.put("partnerId", partnerId);
			String jsonStr = UrlRequestUtils.execute(Config.ins().getGetServerListUrl(), params, UrlRequestUtils.Mode.GET);
			Map<String, String> map = Json.toObject(jsonStr, Map.class);
			String serverJson = map.get("servers");
			List<GameServer> ret = Json.toList(serverJson, GameServer.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Partner> loadPartners() {
		try {
			String url = HTTP_URL_HEAD + HOST + "/adminApi/partners.do";
			Map<String, String> params = new HashMap<String, String>();
			String jsonStr = UrlRequestUtils.execute(url, params, UrlRequestUtils.Mode.GET);
			Map<String, String> map = Json.toObject(jsonStr, Map.class);
			String serverJson = map.get("partners");
			List<Partner> ret = Json.toList(serverJson, Partner.class);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
}
