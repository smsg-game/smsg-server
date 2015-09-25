package com.lodogame.ldsg.partner.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lodogame.game.utils.MD5;
import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.UrlRequestUtils.Mode;
import com.lodogame.ldsg.partner.model.dianjin.DianJinPaymentObj;

public class DianJinSdk extends BaseSdk {
	
	private static final Logger logger = Logger.getLogger(DianJinSdk.class);
	
	private static DianJinSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String appKey;
	private String appId;
	private String verifyUrl;

	public static DianJinSdk instance() {
		synchronized (DianJinSdk.class) {
			if (ins == null) {
				ins = new DianJinSdk();
			}
		}

		return ins;
	}

	private DianJinSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appKey = prop.getProperty("DianJinSdk.appKey");
			appId = prop.getProperty("DianJinSdk.appId");
			partnerId = prop.getProperty("DianJinSdk.partnerId");
			verifyUrl = prop.getProperty("DianJinSdk.verifyUrl");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public boolean verifySession(String token,String uid){
		String sign = MD5.MD5Encode("Act=3&AppId="+getAppId()+"&SessionId="+token+"&Uin="+uid+"&Version=1.07"+getAppKey());
		String url = PROTOCOL_HEAD +getVerifyUrl();
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("Act","3");
		paraMap.put("AppId",getAppId());
		paraMap.put("Uin",uid);
		paraMap.put("SessionId",token);
		paraMap.put("Version","1.07");
		paraMap.put("Sign",sign);
		String jsonStr = UrlRequestUtils.execute(url, paraMap, Mode.POST);
		if(StringUtils.isNotBlank(jsonStr)){
			JSONObject jsonObject = JSON.parseObject(jsonStr);
			if(jsonObject.containsKey("Error_Code")
					&& jsonObject.getIntValue("Error_Code") == 0){
				return true;
			}
		}
		return false;
	}
	

	public boolean checkPayCallbackSign(DianJinPaymentObj data) {
		try {
			String sign = MD5.MD5Encode("App_Id="+getAppId()+"&Create_Time="+data.getCreateTime()+
					"&Extra="+data.getExtra()+"&Pay_Status="+data.getPayStatus()+
					"&Recharge_Gold_Count="+data.getRechargeGoldCount()+
					"&Recharge_Money="+data.getRechargeMoney()+
					"&Uin="+data.getUin()+"&Urecharge_Id="+data.getUrechargeId()+getAppKey());
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
}
