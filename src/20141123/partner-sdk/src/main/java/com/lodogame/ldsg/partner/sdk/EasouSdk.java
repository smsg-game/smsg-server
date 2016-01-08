package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class EasouSdk extends BaseSdk {
	private static final Logger logger = Logger.getLogger(EasouSdk.class);
	private static EasouSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String host;
	private String payBackUrl;
	private String payBackUrl1025;
	private String appId;
	private String easouPartnerId;

	public static EasouSdk instance() {
		synchronized (EasouSdk.class) {
			if (ins == null) {
				ins = new EasouSdk();
			}
		}

		return ins;
	}
	
	private EasouSdk() {
		 loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
//			host = prop.getProperty("EasouSdk.host");
			payBackUrl = prop.getProperty("EasouSdk.payBackUrl");
			payBackUrl1025 = prop.getProperty("EasouSdk.payBackUrl1025");
			appId = prop.getProperty("EasouSdk.appId", "1685");
			easouPartnerId = prop.getProperty("EasouSdk.easouPartnerId", "1000100010001010");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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

	public String getPayBackUrl1025() {
		return payBackUrl1025;
	}

	public void setPayBackUrl1025(String payBackUrl1025) {
		this.payBackUrl1025 = payBackUrl1025;
	}
	
}
