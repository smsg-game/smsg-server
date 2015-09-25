package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class EasouIOSSdk extends BaseSdk {
	private static final Logger logger = Logger.getLogger(EasouIOSSdk.class);
	private static EasouIOSSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String host;
	private String payBackUrl;
	private String appId;
	private String easouPartnerId;

	public static EasouIOSSdk instance() {
		synchronized (EasouIOSSdk.class) {
			if (ins == null) {
				ins = new EasouIOSSdk();
			}
		}

		return ins;
	}
	
	private EasouIOSSdk() {
		 loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
//			host = prop.getProperty("EasouSdk.host");
			payBackUrl = prop.getProperty("EasouIOSSdk.payBackUrl");
			appId = prop.getProperty("EasouIOSSdk.appId", "1688");
			easouPartnerId = prop.getProperty("EasouIOSSdk.easouPartnerId", "1000100010001028");
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
}
