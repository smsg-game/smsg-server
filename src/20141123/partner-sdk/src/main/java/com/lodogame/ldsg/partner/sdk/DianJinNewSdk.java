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
import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.UrlRequestUtils.Mode;
import com.lodogame.game.utils.dianjin.MD5;
import com.lodogame.ldsg.partner.model.dianjin.DianJinPaymentObj;
import com.lodogame.ldsg.partner.model.dianjin.DianJinPaymentObj_new;

public class DianJinNewSdk extends BaseSdk {
	
	private static final Logger logger = Logger.getLogger(DianJinNewSdk.class);
	
	private static DianJinNewSdk ins;

	private static Properties prop;

	private final static String PROTOCOL_HEAD = "http://";
	private String partnerId;
	private String appKey;
	private String appId;
	private String verifyUrl;

	public static DianJinNewSdk instance() {
		synchronized (DianJinNewSdk.class) {
			if (ins == null) {
				ins = new DianJinNewSdk();
			}
		}

		return ins;
	}

	private DianJinNewSdk() {
		loadSdkProperties();
	}

	public void reload(){
		loadSdkProperties();
	}
	
	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sdk.properties"));
			appKey = prop.getProperty("DianJinNewSdk.appKey");
			appId = prop.getProperty("DianJinNewSdk.appId");
			partnerId = prop.getProperty("DianJinNewSdk.partnerId");
			verifyUrl = prop.getProperty("DianJinNewSdk.verifyUrl");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public boolean verifySession(String token,String uid){
		String url = PROTOCOL_HEAD +getVerifyUrl();
		Map<String, String> paraMap = new HashMap<String, String>();
		String act = "4";
		StringBuilder strSign = new StringBuilder();
		strSign.append(appId);
		strSign.append(act);
		strSign.append(uid);
		strSign.append(token);
		strSign.append(appKey);
		String sign = MD5.md5(strSign.toString());
		paraMap.put("Appid",appId);
		paraMap.put("Act", act);
		paraMap.put("Uin",uid);
		paraMap.put("SessionId", token);
		paraMap.put("Sign", sign);
		logger.info(paraMap);
		//请求方式为get请求
		String jsonStr = UrlRequestUtils.execute(url, paraMap, Mode.GET);
		if(StringUtils.isNotBlank(jsonStr)){
			JSONObject jsonObject = JSON.parseObject(jsonStr);
			logger.info("点金登录验证返回值："+jsonObject.getString("ErrorCode"));
			if(jsonObject.containsKey("ErrorCode")
					&& jsonObject.getIntValue("ErrorCode") == 1){
				return true;
			}
		}
		return false;
	}
	

	public boolean checkPayCallbackSign(DianJinPaymentObj_new data) {
		StringBuilder strSign = new StringBuilder();
		String act="1";
		strSign.append(getAppId());
		strSign.append(act);
		strSign.append(data.getProductName());
		strSign.append(data.getConsumeSreamId());
		strSign.append(data.getCooOrderSerial());
		strSign.append(data.getUin());
		strSign.append(data.getGoodsId());
		strSign.append(data.getGoodsInfo());
		strSign.append(data.getGoodsCount());
		strSign.append(data.getOriginalMoney());
		strSign.append(data.getOrderMoney());
		strSign.append(data.getNote());
		strSign.append(data.getPayStatus());
		strSign.append(data.getCreateTime());
		strSign.append(getAppKey());
		String sign = MD5.md5(strSign.toString());
		if(!sign.toLowerCase().equals(data.getSign().toLowerCase())){
			return false; //sign无效
		}else {
			return true;
		}
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
