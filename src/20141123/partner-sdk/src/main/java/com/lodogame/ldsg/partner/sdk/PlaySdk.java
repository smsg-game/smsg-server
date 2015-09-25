package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.game.utils.MD5;
import com.lodogame.ldsg.partner.model.play.PlayPaymentObj;

public class PlaySdk extends BaseSdk {
	
	private static final Logger logger = Logger.getLogger(PlaySdk.class);
	
	private static PlaySdk ins;
	private final static String PROTOCOL_HEAD = "http://";
	private static Properties prop;
	private String appId;
	private String appKey;
	private String partnerId;
	private String appSecret;
	private String host;
	private String easouAppId;
	private String easouPartnerId;

	public static PlaySdk instance() {
		synchronized (PlaySdk.class) {
			if (ins == null) {
				ins = new PlaySdk();
			}
		}
		return ins;
	}

	private PlaySdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("PlaySdk.partnerId");
			easouAppId = prop.getProperty("PlaySdk.easouAppId");
			easouPartnerId = prop.getProperty("PlaySdk.easouPartnerId");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public boolean verifySession(String session, String uid){
		return false;
	}
	

	public boolean checkPayCallbackSign(PlayPaymentObj data) {
		try {
			String sign = null;
			if("isagSmsPay".equals(data.getPayType())){
				sign = MD5.MD5Encode(data.getResultCode()+data.getCpparam(),"utf-8");
				if(data.getValidatecode().equals(sign)){
					return true;
				}
			}else{
				sign = MD5.MD5Encode(data.getSerialno()+data.getGameUserId(),"utf-8");
				if(data.getValidatecode().equals(sign)){
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	public String getPartnerId() {
		return partnerId;
	}

	public String getHost() {
		return host;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getEasouAppId() {
		return easouAppId;
	}

	public String getEasouPartnerId() {
		return easouPartnerId;
	}
}
