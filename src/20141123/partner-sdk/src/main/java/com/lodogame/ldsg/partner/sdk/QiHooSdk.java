package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.UrlRequestUtils.Mode;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.qiHoo.PayCallbackObject;

/**
 *  奇虎360SDK
 * @author chenjian
 *
 */
public class QiHooSdk extends BaseSdk {
	
	private static final Logger LOG = Logger.getLogger(QiHooSdk.class);
	
	private static QiHooSdk ins;
	
	private static Properties prop;
	
	private final static String PROTOCOL_HEAD = "https://";
	private String host;
	private String appId;
	private String appKey;
	private String appSecret;
	private String payCallback;
	private String partnerId;
	
	public static QiHooSdk instance(){
		synchronized (QiHooSdk.class) {
			if(ins == null){
				ins = new QiHooSdk();
			}
		}
		
		return ins;
	}
	
	private QiHooSdk(){
		loadSdkProperties();
	}
	
	public void reload(){
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			host = prop.getProperty("QiHooSdk.host");
			appId = prop.getProperty("QiHooSdk.appId");
			appKey = prop.getProperty("QiHooSdk.appKey");
			appSecret = prop.getProperty("QiHooSdk.appSecret");
			payCallback = prop.getProperty("QiHooSdk.payCallback");
			partnerId = prop.getProperty("QiHooSdk.partnerId");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> assessToken(String code){
		String url = PROTOCOL_HEAD + host + "/oauth2/access_token";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("grant_type", "authorization_code");
		paraMap.put("code", code);
		paraMap.put("client_id", appKey);
		paraMap.put("client_secret", appSecret);
		paraMap.put("redirect_uri", "oob");
		paraMap.put("sign", makeSign(paraMap));
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.GET);
		Map<String, String> ret = Json.toObject(jsonStr, Map.class);
//		if(ret != null && ret.containsKey("access_token")){
//			return ret.get("access_token");
//		}
		return ret;
	}
	
	public Map<String, String> refreshToken(String accessToken, String refreshToken){
		String url = PROTOCOL_HEAD + host + "/oauth2/access_token";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("grant_type", "refresh_token");
		paraMap.put("refresh_token", refreshToken);
		paraMap.put("client_id", appKey);
		paraMap.put("client_secret", appSecret);
		paraMap.put("scope", "basic");
//		paraMap.put("sign", makeSign(paraMap));
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.GET);
		Map<String, String> ret = Json.toObject(jsonStr, Map.class);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public boolean verifyPayment(PayCallbackObject cb){
		String url = PROTOCOL_HEAD + host + "/pay/verify_mobile_notification.json";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("product_id", cb.getProduct_id());
		paraMap.put("amount", cb.getAmount());
		paraMap.put("app_uid", cb.getApp_uid());
		paraMap.put("order_id", cb.getOrder_id());
		paraMap.put("app_order_id", cb.getApp_order_id());
		paraMap.put("sign_type", cb.getSign_type());
		paraMap.put("sign_return", cb.getSign_return());
		paraMap.put("app_key", cb.getApp_key());
		paraMap.put("client_id", appId);
		paraMap.put("client_secret", appSecret);
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.GET);
		Map<String, String> ret = Json.toObject(jsonStr, Map.class);
		if(ret != null && ret.containsKey("ret") && ret.get("ret").equals("verified")){
			return true;
		}
		return false;
	}
	
	public Map<String, String> getUserInfo(String accessToken){
		String url = PROTOCOL_HEAD + host + "/user/me.json";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("access_token", accessToken);
//		paraMap.put("sign", makeSign(paraMap));
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.GET);
		Map<String, String> ret = Json.toObject(jsonStr, Map.class);
		return ret;
	}
	
	private String makeSign(Map<String, String> paraMap){
		Set<String> keys = paraMap.keySet();
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(keys);
		String signSrcStr = "";
		for(String key : treeSet){
			String value = paraMap.get(key);
			if(!StringUtils.isBlank(value)){
				signSrcStr += value + "#" ;
			}
		}
		signSrcStr += appSecret;
		
		LOG.info("makeSign signSrcStr="+signSrcStr);
		
		return EncryptUtil.getMD5(signSrcStr);
	}
	
	
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean checkPayCallbackSign(PayCallbackObject cb) {
		Map<String, String> map = Json.toObject(Json.toJson(cb), Map.class);
		map.remove("sign");
		map.remove("sign_return");
//		String sign = makeSign(map);
		
		Set<String> keys = map.keySet();
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(keys);
		String signSrcStr = "";
		for(String key : treeSet){
			String value = map.get(key);
			if(!StringUtils.isBlank(value)){
				signSrcStr += value + "#" ;
			}
		}
//		signSrcStr += "7de7773d9da1736ccb17910459f4014a";
		signSrcStr += appSecret;
		String sign = EncryptUtil.getMD5(signSrcStr);
		
		System.out.println("checkPayCallbackSign signSrcStr="+signSrcStr+"&generateSign="+sign+"&sign()=" + cb.getSign());
		
//		LOG.info("checkPayCallbackSign signSrcStr="+signSrcStr+"&generateSign="+sign+"&sign()=" + cb.getSign());
		
		return sign.equalsIgnoreCase(cb.getSign());
	}

	public String getPayCallback() {
		return payCallback;
	}

	public void setPayCallback(String payCallback) {
		this.payCallback = payCallback;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		String json = "{\"app_key\":\"a09843ce791e1cd95cf59d8266cb6ef8\",\"product_id\":\"0110032015030300000005\",\"amount\":\"10\"" +
//				",\"app_uid\":\"1420282247\",\"app_ext1\":\"充值0.1元,得1元宝\",\"app_ext2\":\"备注信息：\",\"user_id\":\"1420282247\",\"order_id\":\"1503037417736336041\"" +
//				",\"gateway_flag\":\"success\",\"sign_type\":\"md5\",\"app_order_id\":\"0110032015030300000005\",\"sign_return\"" +
//				":\"4a7faa2bc7f0028b2872dd6b1ecaa234\",\"sign\":\"9dda5b5ac1f27df68d8f322c7e353257\"}";
//
//		Map<String, String> paraMap = Json.toObject(json, Map.class);
//		paraMap.remove("sign");
//		paraMap.remove("sign_return");
//		Set<String> keys = paraMap.keySet();
//		TreeSet<String> treeSet = new TreeSet<String>();
//		treeSet.addAll(keys);
//		String signSrcStr = "";
//		for(String key : treeSet){
//			String value = paraMap.get(key);
//			if(!StringUtils.isBlank(value)){
//				signSrcStr +=  value + "#";
//			}
//		}
//		signSrcStr +=  "7de7773d9da1736ccb17910459f4014a";
//		System.out.println(signSrcStr);
		
//		String signSrcStr = "10#充值0.1元,得1元宝#备注信息：#a09843ce791e1cd95cf59d8266cb6ef8#0110032015030300000005#1420282247#success#1503037417736336041#0110032015030300000005#md5#1420282247#7de7773d9da1736ccb17910459f4014a";
//		System.out.println(EncryptUtil.getMD5(signSrcStr));
//		
//		String sign = "9dda5b5ac1f27df68d8f322c7e353257";
//		
		PayCallbackObject pc = new PayCallbackObject();
		pc.setAmount("10");
		pc.setApp_ext1("充值0.1元,得1元宝");
		pc.setApp_ext2("备注信息：");
		pc.setApp_key("a09843ce791e1cd95cf59d8266cb6ef8");
		pc.setApp_order_id("0110032015030300000005");
		pc.setApp_uid("1420282247");
		pc.setGateway_flag("success");
		pc.setOrder_id("1503037417736336041");
		pc.setProduct_id("0110032015030300000005");
		pc.setSign_type("md5");
		pc.setSign("9dda5b5ac1f27df68d8f322c7e353257");
		pc.setUser_id("1420282247");
		pc.setSign_return("4a7faa2bc7f0028b2872dd6b1ecaa234");
		
		QiHooSdk qHooSdk = new QiHooSdk();
		qHooSdk.checkPayCallbackSign(pc);
		
	}

	public String getPartnerId() {
		return partnerId;
	}
}
