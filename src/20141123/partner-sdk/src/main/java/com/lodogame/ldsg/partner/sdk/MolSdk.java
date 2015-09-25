package com.lodogame.ldsg.partner.sdk;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.game.utils.MD5;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.mol.MolPaymentObj;

public class MolSdk extends BaseSdk {
	
	private static final Logger logger = Logger.getLogger(BaiduSdk.class);
	
	private static MolSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String applicationCode;
	private String appSecret;

	public static MolSdk instance() {
		synchronized (MolSdk.class) {
			if (ins == null) {
				ins = new MolSdk();
			}
		}

		return ins;
	}

	private MolSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("MolSdk.partnerId");
			applicationCode = prop.getProperty("MolSdk.applicationCode");
			appSecret = prop.getProperty("MolSdk.appSecret");
//			appSecret = "S1J9aoKeWZO8fAmllA9030BwwgtG1a01";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	

	@SuppressWarnings("unchecked")
	public boolean checkPayCallbackSign(MolPaymentObj cb) {
		String json = Json.toJson(cb);
		Map<String, Object> map = Json.toObject(json, Map.class);
		try {
			String sign = makeSign(map);
			return sign.equalsIgnoreCase(cb.getSignature());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public String makeSign(Map<String, Object> paraMap) throws Exception{
		Set<String> keys = paraMap.keySet();
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(keys);
		String signSrcStr = "";
		for(String key : treeSet){
			if(key.equals("signature")){
				continue;
			}
			Object value = paraMap.get(key);
			signSrcStr += (value == null ? "" : value);
		}
		return MD5.MD5Encode(signSrcStr+appSecret);
	}
	public String getApplicationCode() {
		return applicationCode;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public static void main(String[] args){
		try {
			System.out.println(URLDecoder.decode("2012-12-31T14%3A59%3A59Z","utf-8"));
		} catch (Exception e) {
			// TODO: handle exception
		}
//{"amount":"100","applicationCode":"Acm9vRnzEHr8IaPOOa9b3beZZMWgcvbc","channelId":"1","currencyCode":"USD","customerId":"0150012014041600000001","paymentId":"MPO119740","paymentStatusCode":"paymentStatusCode","paymentStatusDate":"2014-08-28T09:18:59Z","referenceId":"0150012014041600000001","signature":"c695bd6bf86a743f6345670e34f513d8","version":"v1"}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("applicationCode","Acm9vRnzEHr8IaPOOa9b3beZZMWgcvbc");
		paraMap.put("referenceId","0150012014041600000001");
		paraMap.put("version","v1");
		paraMap.put("amount","100");
		paraMap.put("currencyCode","USD");
		paraMap.put("customerId","0150012014041600000001");
		paraMap.put("channelId","1");
		paraMap.put("paymentId","MPO119740");
		paraMap.put("paymentStatusCode","00");
		paraMap.put("paymentStatusDate","2014-08-28T09:18:59Z");
		try {
			System.out.println(MolSdk.instance().makeSign(paraMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
