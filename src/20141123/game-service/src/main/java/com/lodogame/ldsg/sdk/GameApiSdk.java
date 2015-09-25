package com.lodogame.ldsg.sdk;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.model.GameServer;

public class GameApiSdk {

	private final static Logger LOG = Logger.getLogger(GameApiSdk.class);

	private final static String HTTP_URL_HEAD = "http://";
	private static String HOST = "wapi.andr.sg.xworldgame.com:8080";
//	private static String HOST = "sg.fantingame.com:8088";

	private static GameApiSdk sdk;

	private Map<String, Integer> payMap;
	private Map<String, Integer> payAllMap;

	private GameApiSdk() {
	}
	
	private String getHost(String partnerId,String serverId){
		List<GameServer> gameServers =  GameServerCache.getInstance().getGameServers(partnerId);
		for(GameServer gameServer:gameServers){
			if(gameServer.getServerId().equals(serverId)){
				return gameServer.getServerHost();
			}
		}
		return HOST;
	}

	public static GameApiSdk getInstance() {
		synchronized (GameApiSdk.class) {
			if (sdk == null) {
				sdk = new GameApiSdk();
			}
		}
		return sdk;
	}

	@SuppressWarnings("unchecked")
	public UserToken loadUserToken(String partnerUserId, String partnerId, String serverId, String qn, Map<String, String> params) {
		try {
			String host = getHost(partnerId,serverId);
			String url = HTTP_URL_HEAD + host + "/gameApi/loadUserToken.do";
			LOG.debug("url[" + url + "]");
			String imei = params.get("imei");
			String mac = params.get("mac");
			String idfa = params.get("idfa");
			
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("partnerUserId", partnerUserId);
			paraMap.put("partnerId", partnerId);
			paraMap.put("serverId", serverId);
			paraMap.put("qn", qn);
			paraMap.put("imei", imei);
			paraMap.put("mac", mac);
			paraMap.put("idfa", idfa);
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			LOG.info(jsonStr);
			Map<String, Object> ret = Json.toObject(jsonStr, Map.class);
			Map<String, String> map = (Map<String, String>) ret.get("userToken");
			UserToken tk = new UserToken();
			tk.setPartnerId(map.get("partnerId"));
			tk.setPartnerUserId(map.get("partnerUserId"));
			tk.setServerId(map.get("serverId"));
			tk.setToken(map.get("token"));
			tk.setUserId(map.get("userId"));
			return tk;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean doPayment(String partnerId, String serverId, String partnerUserId, String channel, String orderId, BigDecimal amount, int gold, String userIp, String remark) {
		try {
			String host = getHost(partnerId,serverId);
			String url = HTTP_URL_HEAD  + host + "/gameApi/payment.do";
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("partnerId", partnerId);
			paraMap.put("serverId", serverId);
			paraMap.put("partnerUserId", partnerUserId);
			paraMap.put("channel", channel);
			paraMap.put("orderId", orderId);
			paraMap.put("amount", String.valueOf(amount));
			paraMap.put("gold", String.valueOf(gold));
			paraMap.put("userIp", userIp);
			paraMap.put("remark", remark);
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			LOG.info(jsonStr);
			Map<String, Object> retVal = Json.toObject(jsonStr, Map.class);
			int rc = Integer.parseInt(retVal.get("rc").toString());
			return rc == ServiceReturnCode.SUCCESS;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}

	}

	/**
	 * 获取套餐
	 * 
	 * @param serverId
	 * @param playerIds
	 * @param gold
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getSystemGoldSetId(String partnerId,String serverId, String amount) {
		if (payMap != null && !payMap.isEmpty()) {
			return payMap.get(amount);
		}
		payMap = new HashMap<String, Integer>();
		try {
			String host = getHost(partnerId,serverId);
			String url = HTTP_URL_HEAD + host + "/gameApi/getPaySettings.do";
			Map<String, String> paraMap = new HashMap<String, String>();
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			Map<String, List<Map<String, Object>>> retVal = Json.toObject(jsonStr, Map.class);
			List<Map<String, Object>> list = retVal.get("list");
			for (Map<String, Object> item : list) {
				BigDecimal money = new BigDecimal((Double) item.get("money"));
				payMap.put(Double.toString(money.doubleValue()), (Integer) item.get("systemGoldSetId"));
			}

			return payMap.get(amount);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
	}
	
	/**
	 * 获取套餐
	 * 
	 * @param serverId
	 * @param playerIds
	 * @param gold
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getSystemGold(String partnerId,String serverId, String amount) {
		if (payMap != null && !payMap.isEmpty()) {
			return payMap.get(amount);
		}
		payMap = new HashMap<String, Integer>();
		try {
			String host = getHost(partnerId,serverId);
			String url = HTTP_URL_HEAD + host + "/gameApi/getPaySettings.do";
			Map<String, String> paraMap = new HashMap<String, String>();
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			Map<String, List<Map<String, Object>>> retVal = Json.toObject(jsonStr, Map.class);
			List<Map<String, Object>> list = retVal.get("list");
			for (Map<String, Object> item : list) {
				BigDecimal money = new BigDecimal((Double) item.get("money"));
				String desc = (String)item.get("description");
				desc = StringUtils.remove(desc,"额外赠送");
				desc = StringUtils.remove(desc,"元宝").trim();
				payMap.put(Double.toString(money.doubleValue()),(Integer)item.get("gold")-Integer.parseInt(desc));
			}
			return payMap.get(amount);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
	}
	
	/**
	 * 获取套餐
	 * 
	 * @param serverId
	 * @param playerIds
	 * @param gold
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getAllSystemGold(String partnerId,String serverId, String amount) {
		if (payAllMap != null && !payAllMap.isEmpty()) {
			return payAllMap.get(amount);
		}
		payAllMap = new HashMap<String, Integer>();
		try {
			String host = getHost(partnerId,serverId);
			String url = HTTP_URL_HEAD + host + "/gameApi/getPaySettings.do";
			Map<String, String> paraMap = new HashMap<String, String>();
			String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.GET);
			Map<String, List<Map<String, Object>>> retVal = Json.toObject(jsonStr, Map.class);
			List<Map<String, Object>> list = retVal.get("list");
			for (Map<String, Object> item : list) {
				BigDecimal money = new BigDecimal((Double) item.get("money"));
				payAllMap.put(Double.toString(money.doubleValue()),(Integer)item.get("gold"));
			}
			return payAllMap.get(amount);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return 0;
		}
	}

	public static void main(String[] args) {

//		GameApiSdk.getInstance().payMap = new HashMap<String, Integer>();
//		BigDecimal money =  new BigDecimal((Double) 5100.00);
//		GameApiSdk.getInstance().payMap.put(Double.toString(money.doubleValue()),20);
//		
//		GameApiSdk.getInstance().getSystemGoldSetId("0000", Double.toString(new BigDecimal("5100").intValue()));
	}

}
