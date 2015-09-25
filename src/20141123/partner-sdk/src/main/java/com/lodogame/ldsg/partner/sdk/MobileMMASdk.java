package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.game.utils.MD5;
import com.lodogame.ldsg.partner.model.mobilemm.MobileMMPaymentObj;

public class MobileMMASdk extends BaseSdk {
	private static final Logger logger = Logger.getLogger(MobileMMASdk.class);
	private static MobileMMASdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String host;
	private String partnerId;
	private String payBackUrl;
	private String easouAppId;
	private String easouPartnerId;
	private String appId;
	private String appKey;

	public static MobileMMASdk instance() {
		synchronized (MobileMMASdk.class) {
			if (ins == null) {
				ins = new MobileMMASdk();
			}
		}
		return ins;
	}
	
	private MobileMMASdk() {
		 loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			partnerId = prop.getProperty("MobileMMSdkA.partnerId","3001");
			easouAppId = prop.getProperty("MobileMMSdkA.easouAppId", "1685");
			easouPartnerId = prop.getProperty("MobileMMSdkA.easouPartnerId", "1000100010001010");
			appId = prop.getProperty("MobileMMSdkA.appId","300008159386");
			appKey = prop.getProperty("MobileMMSdkA.appKey","867B894F3A31BAC9");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean  checkPayCallbackSign(MobileMMPaymentObj obj){
		String sign = obj.getOrderID()+"#"+obj.getChannelID()+"#"+
				obj.getPayCode()+"#"+getAppKey();
		sign = MD5.MD5Encode(sign,"utf-8").toUpperCase();
		if(obj.getMd5Sign().equals(sign)){
			return true;
		}
		return false;
	}

	public String getEasouAppId() {
		return easouAppId;
	}

	public String getAppKey() {
		return appKey;
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
		return appId;
	}

	public String getEasouPartnerId() {
		return easouPartnerId;
	}

	public String getPartnerId() {
		return partnerId;
	}
	
}
