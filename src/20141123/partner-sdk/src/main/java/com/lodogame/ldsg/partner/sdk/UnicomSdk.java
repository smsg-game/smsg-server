package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.game.utils.MD5;
import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.UrlRequestUtils.Mode;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.baidu.BaiduPaymentObj;
import com.lodogame.ldsg.partner.model.unicom.UnicomPaymentObj;

import flex.messaging.log.Log;

public class UnicomSdk extends BaseSdk {
	private static final Logger logger = Logger.getLogger(UnicomSdk.class);
	private static UnicomSdk ins;
	private static final String keys="bc4217a7b95d331b2108631e";
 
	private static final String serverKey="d9dfa118b78c255ac2fb09791942e7";
 

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String host;
	private String gameId;
	private String certKey;
	private String partnerId;
	private String easouAppId;

	private String payBackUrl;
	private String appKay;

	public static UnicomSdk instance() {
		synchronized (UnicomSdk.class) {
			if (ins == null) {
				ins = new UnicomSdk();
			}
		}

		return ins;
	}

	private UnicomSdk() {
		loadSdkProperties();
	}
	
	public void reload(){
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			
			         
	        prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
	        appKay=prop.getProperty("UnicomSdk.appKey");			        		
			easouAppId = prop.getProperty("UnicomSdk.easouAppId");			 
			partnerId = prop.getProperty("UnicomSdk.partnerId");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean verifySession(String session, String uid){
		return false;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPayBackUrl() {
		return payBackUrl;
	}

	public void setPayBackUrl(String payBackUrl) {
		this.payBackUrl = payBackUrl;
	}

	public String getAppId() {
		return gameId;
	}

	public void setAppId(String appId) {
		this.gameId = appId;
	}

	public String getAppKey() {
		return certKey;
	}

	public void setAppKey(String appKey) {
		this.certKey = appKey;
	}

	public boolean checkPayCallbackSign(UnicomPaymentObj cb) {
		try {
			String sign = null;
			//MD5数据校验
			String str=cb.getGameCode()+keys+cb.getMoney()+cb.getResult()+cb.getPaymentId()+cb.getCustomer()+serverKey;			 
				sign = MD5.MD5Encode(str,"utf-8");
				logger.info(str);
				if(cb.getPkey().equals(sign)){
					logger.info("成功签名");
					return true;
				}
			 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public String getPartnerId() {
		return partnerId;
	}
}
