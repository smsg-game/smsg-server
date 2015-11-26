package com.lodogame.ldsg.partner.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.uc.PayCallbackResponse;
import com.lodogame.ldsg.partner.model.uc.SidInfoResponse;
import com.lodogame.ldsg.partner.model.uc.UcGame;
import com.lodogame.ldsg.partner.model.uc.Util;

import cn.uc.g.sdk.cp.model.SDKException;
import cn.uc.g.sdk.cp.model.UserGameData;
import cn.uc.g.sdk.cp.service.SDKServerService;

public class UcSdk extends BaseSdk {
	private static final Logger logger = Logger.getLogger(UcSdk.class);

//	private static ObjectMapper objectMapper = new ObjectMapper();

	private static UcSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String host;
	private UcGame ucGame;
	private String apiKey;

	public static UcSdk instance() {
		synchronized (UcSdk.class) {
			if (ins == null) {
				ins = new UcSdk();
			}
		}

		return ins;
	}

	private UcSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			host = prop.getProperty("UcSdk.host");
			ucGame = new UcGame();
			ucGame.setCpId(Integer.valueOf(prop.getProperty("UcSdk.cpId", "0")));
			ucGame.setGameId(Integer.valueOf(prop.getProperty("UcSdk.gameId", "0")));
			apiKey = prop.getProperty("UcSdk.apiKey");
			partnerId = prop.getProperty("UcSdk.partnerId");
//			ucGame.setChannelId(prop.getProperty("UcSdk.channelId", "")); add by lz
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 验证SID信息
	 * 
	 * @param sid
	 * @param serverId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public SidInfoResponse sidInfo(String sid, String channelId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.toString(System.currentTimeMillis()));// 当前系统时间
		params.put("service", "account.verifySession");

		Map<String, String> data = new HashMap<String, String>();
		data.put("sid", sid);// 在uc sdk登录成功时，游戏客户端通过uc ; sdk的api获取到sid，再游戏客户端由传到游戏服务器
		params.put("data", data);
		
		Map<String, Integer> game = new HashMap<String, Integer>();
		game.put("gameId", ucGame.getGameId());
		params.put("game", game);
		
		params.put("encrypt", "md5");

		String signSource = Util.getSignData(data) + apiKey;
//		String signSource = "sid=" + sid + apiKey;

		String sign = Util.getMD5Str(signSource);
		params.put("sign", sign);
//		String sign = EncryptUtil.getMD5(signSource);
//		params.put("sign", sign.toLowerCase());
		
		String httpReqStr = PROTOCOL_HEAD + host + "/cp/account.verifySession";
		
		logger.info("UcSdk.sidInfo request httpReqStr : "+ httpReqStr +" & jsonStr : "+Json.toJson(params));

		String jsonStr = doPost(httpReqStr, Json.toJson(params));

		logger.info("UcSdk.sidInfo result jsonStr : " + jsonStr);

		return Json.toObject(jsonStr, SidInfoResponse.class);
	}
	
	public SidInfoResponse sidInfo2(String sid, String channelId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.toString(System.currentTimeMillis()));// 当前系统时间
		params.put("service", "ucid.user.sidInfo");
//		ucGame.setServerId(1333);  add by lz
		// ucGame.setChannelId(channelId);
		params.put("game", ucGame);

		Map<String, String> data = new HashMap<String, String>();
		data.put("sid", sid);// 在uc sdk登录成功时，游戏客户端通过uc
								// sdk的api获取到sid，再游戏客户端由传到游戏服务器
		params.put("data", data);

		params.put("encrypt", "md5");

		String signSource = ucGame.getCpId() + "sid=" + sid + apiKey;

		String sign = EncryptUtil.getMD5(signSource);
		params.put("sign", sign.toLowerCase());

		String jsonStr = doPost(PROTOCOL_HEAD + host + "/ss/", Json.toJson(params));

		logger.info("UcSdk.sidInfo result:" + jsonStr);

		return Json.toObject(jsonStr, SidInfoResponse.class);
	}

	public String doPost(String url, String body) {
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		HttpResponse response = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);// 将Expect: 100-Continue设置为关闭

			httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			}
		}
		return stringBuffer.toString();
	}

	public boolean checkPayCallbackSign(PayCallbackResponse rsp) {
//		String signSource = ucGame.getCpId() + "amount=" + rsp.getData().getAmount() + "callbackInfo=" + rsp.getData().getCallbackInfo() + "failedDesc="
//				+ rsp.getData().getFailedDesc() + "gameId=" + rsp.getData().getGameId() + "orderId=" + rsp.getData().getOrderId() + "orderStatus=" + rsp.getData().getOrderStatus()
//				+ "payWay=" + rsp.getData().getPayWay() + "serverId=" + rsp.getData().getServerId() + "ucid=" + rsp.getData().getUcid() + apiKey;
		String segment_one = "accountId="+rsp.getData().getAccountId()+"amount="+rsp.getData().getAmount()
				+"callbackInfo="+rsp.getData().getCallbackInfo();
		
		String segment_two = "creator="+rsp.getData().getCreator()+"failedDesc="+rsp.getData().getFailedDesc()
				+"gameId="+rsp.getData().getGameId()+"orderId="+rsp.getData().getOrderId()
				+"orderStatus="+rsp.getData().getOrderStatus()+"payWay="+rsp.getData().getPayWay();
		
		String signSource = "";
		if(rsp.getOwnFlag()){
			signSource=segment_one+"cpOrderId="+rsp.getData().getCpOrderId()+segment_two+apiKey;
		}else {
			signSource=segment_one+segment_two+apiKey;
		}
		
		logger.info("checkPayCallbackSign signSource : " + signSource);
		
		String sign = Util.getMD5Str(signSource);
		if (sign.toLowerCase().equals(rsp.getSign().toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 发送游戏数据到UC服务器
	 * @param sid   sessionId
	 * @param level  等级
	 * @param roleName 角色名称
	 * @param zoneName 服务器名
	 * @param roleId   角色id
	 * @param zoneId   服务器id
	 */
	public void sendGameData(String sid,String level,String roleName,String zoneName,String roleId,String zoneId){
	        
	        //玩家的游戏数据
	        Map<String, String> content = new HashMap<String, String>();
	        content.put("roleLevel", level);
	        content.put("roleName", roleName);
	        content.put("zoneName", zoneName);
	        content.put("roleId", roleId);
	        content.put("zoneId", zoneId);
	        
	        //构造玩家的游戏数据对象
	        UserGameData gameData = new UserGameData();
	        gameData.setCategory("loginGameRole");
	        gameData.setContent(content);
	        try {
	            boolean result = SDKServerService.gameData(sid, gameData);
	            System.out.println("同步数据的结果:"+result);
	        } catch (SDKException e) {
	            System.err.println(e.getErrorCode() + "--" + e.getMessage());
	        }
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public static void main(String[] args) throws Exception {
		String json = "{\"id\":1376815057712,\"state\":{\"code\":10,\"msg\":\"无效的请求数据,校验签名失败\"},\"data\":\"\"}";
		SidInfoResponse s = Json.toObject(json, SidInfoResponse.class);
		System.out.println(s);
	}

	public String getPartnerId() {
		return partnerId;
	}
}
