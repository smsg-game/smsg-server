package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.game.utils.MD5;
import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.ldsg.partner.model.ppassistant.PPAssistantPaymentObj;

public class PPAssistantSdk extends BaseSdk {
	
	private static final Logger logger = Logger.getLogger(PPAssistantSdk.class);
	
	private static PPAssistantSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String appKey;
	private String appId;
	private String verifyUrl;

	public static PPAssistantSdk instance() {
		synchronized (PPAssistantSdk.class) {
			if (ins == null) {
				ins = new PPAssistantSdk();
			}
		}

		return ins;
	}

	private PPAssistantSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appKey = prop.getProperty("PPAssistantSdk.appKey");
			appId = prop.getProperty("PPAssistantSdk.appId");
			partnerId = prop.getProperty("PPAssistantSdk.partnerId");
			verifyUrl = prop.getProperty("PPAssistantSdk.verifyUrl");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public String verifySession(String token){
		return UrlRequestUtils.executeHttpByString(PROTOCOL_HEAD+verifyUrl,token);
	}
	

	public boolean checkPayCallbackSign(PPAssistantPaymentObj data) {
		try {
			String sign = data.getOrder_id()+"|"+data.getBillno()+
					"|"+data.getAccount()+"|"+data.getAmount()+"|"+data.getStatus()+"|"+data.getApp_id()+
					"|"+data.getUuid()+"|"+data.getZone()+"|"+data.getRoleid()+"|"+getAppKey();
			sign = MD5.MD5Encode(sign,"utf-8");
			if(data.getSign().equals(sign)){
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

	public String getAppKey() {
		return appKey;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public String getAppId() {
		return appId;
	}
	
	public static void main(String[] args){
		PPAssistantSdk.instance().verifySession("");
	}
}
