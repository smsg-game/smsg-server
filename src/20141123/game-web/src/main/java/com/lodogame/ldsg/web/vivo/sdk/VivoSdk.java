package com.lodogame.ldsg.web.vivo.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.easou.sso.sdk.util.TradeInfo;
import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.UrlRequestUtils.Mode;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;


/**
 * VivoSdk
 * 
 * @author yezp
 */
public class VivoSdk {
	
	private static final Logger logger = Logger.getLogger(VivoSdk.class);
	
	private static VivoSdk ins;
	private final static String PROTOCOL_HEAD = "https://";
	private static Properties prop;
	private String host;
	private String key;
	private String appId;
	private String cpId;

	private static final String QSTRING_EQUAL = "=";
	private static final String QSTRING_SPLIT = "&";
	// 签名方法key
	private final static String SIGN_METHOD = "signMethod";
	public final static String SIGNATURE = "signature";
	
	private static String LINK = "https://pay.vivo.com.cn/vcoin/trade";
	
	public static VivoSdk instance() {
		synchronized (VivoSdk.class) {
			if (ins == null) {
				ins = new VivoSdk();
			}
		}

		return ins;
	}

	private VivoSdk() {
		loadSdkProperties();
	}

	public void reload() {
		loadSdkProperties();
	}

	private void loadSdkProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource(
					"sdk.properties"));
			host = prop.getProperty("VivoSdk.host");
			key = prop.getProperty("VivoSdk.key");
			appId = prop.getProperty("VivoSdk.appId");
			cpId = prop.getProperty("VivoSdk.cpId");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取用户的uid
	 * 
	 * @param access_token
	 * @return
	 */
	public String getUid(String access_token) {
		String url = PROTOCOL_HEAD + host + "/auth/user/info";
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("access_token", access_token);
		String jsonStr = UrlRequestUtils.executeHttps(url, paraMap, Mode.POST);
		
		logger.info("Vivo sdk getUid:" + jsonStr);
		VivoUserInfo user = Json.toObject(jsonStr, VivoUserInfo.class);
		return user.getUid();
	}
	

	public boolean checkPayCallbackSign(VivoPaymentObj paymentObj) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("respCode", paymentObj.getRespCode());
		para.put("respMsg", paymentObj.getRespMsg());
		para.put("signMethod", paymentObj.getSignMethod());
		para.put("signature", paymentObj.getSignature());
		para.put("tradeType", paymentObj.getTradeType());
		para.put("tradeStatus", paymentObj.getTradeStatus());
		para.put("cpId", paymentObj.getCpId());
		para.put("appId", paymentObj.getAppId());
		para.put("uid",paymentObj.getUid());
		para.put("cpOrderNumber",paymentObj.getCpOrderNumber());
		para.put("orderNumber",paymentObj.getOrderNumber());
		para.put("orderAmount",paymentObj.getOrderAmount());
		para.put("extInfo",paymentObj.getExtInfo());
		para.put("payTime",paymentObj.getPayTime());
		
		String sign = VivoSignUtils.getVivoSign(para, key);	
		logger.info("vivo payment callback sign:" + sign);
		return VivoSignUtils.verifySignature(para, key);
	}
	
	public static void main(String[] args) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("version", "1.0.0");
		para.put("signMethod", "MD5");
		para.put("cpId", "20140814165209600000");
		para.put("appId", "1d98c21beef0e97adbca2adb6176b841");
		para.put("cpOrderNumber", "0110282014082700000014");
		para.put("notifyUrl", "http://203.195.190.121:8089/webApi/vivoPayment.do");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");   
		String orderTime = sdf.format(System.currentTimeMillis());
		para.put("orderTime", orderTime);
		para.put("orderAmount", 500+"");
		para.put("orderTitle", "钻石");
		para.put("orderDesc", "ft");
		
		Map<String, String> filteredReq = VivoSdk.instance().paraFilter(para);

		String signature = VivoSdk.instance().getVivoSign(filteredReq, "c8bb4c8e29e6b628891252a1c5688a5e");
		filteredReq.put(SIGNATURE, signature);
		filteredReq.put(SIGN_METHOD, "MD5");
		
		System.out.println(signature);
		System.out.println(VivoSdk.instance().verifySignature(filteredReq, "c8bb4c8e29e6b628891252a1c5688a5e"));
		
	}
	
	public void createServerOrder(TradeInfo info) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("version", "1.0.0");
		para.put("signMethod", "MD5");
		para.put("cpId", cpId);
		para.put("appId", appId);
		para.put("cpOrderNumber", info.getTradeId());
		para.put("notifyUrl", info.getNotifyUrl());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");   
		String orderTime = sdf.format(System.currentTimeMillis());
		para.put("orderTime", orderTime);
//		para.put("orderAmount", new java.text.DecimalFormat("#.00").format(Integer.valueOf(info.getReqFee())));
		para.put("orderAmount", Integer.valueOf(info.getReqFee())*100+"");//单位分
		para.put("orderTitle", "金币");
		para.put("orderDesc", "smsg");
		
		Map<String, String> filteredReq = paraFilter(para);
		
		String signature = getVivoSign(filteredReq, key);
		logger.info("vivosdk signature: " + signature);
		// 签名结果与签名方式加入请求提交参数组中
		filteredReq.put(SIGNATURE, signature);
		filteredReq.put(SIGN_METHOD, "MD5");

		String linkPara = createLinkString(filteredReq, false, true);	//请求字符串，key不需要排序，value需要URL编码
		logger.info("vivo linkPara = " + linkPara);
		
		String response = null;
		try {
			response = HttpUtils.post(LINK, linkPara);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(ServiceReturnCode.FAILD, "订单创建失败");
		}
		logger.info("vivosdk response: " + response);
		Map<String, Object> jsonMap = Json.toObject(response, Map.class);
		logger.info("VivoSdk jsonMap = " + jsonMap);
		
		String SUCCESS = "200";
		String EXCEPTION = "400";
		if (SUCCESS.equals((String) jsonMap.get("respCode"))) {

			String vivoOrder = (String) jsonMap.get("orderNumber");
			String accessKey = (String) jsonMap.get("accessKey");
			logger.info("VivoSdk orderNumber = " + vivoOrder + " accessKey = " + accessKey);
			info.setExectInfo(vivoOrder + ":" + accessKey);
			return;
		} else if (EXCEPTION.equals((String) jsonMap.get("respCode"))) {
			// 抛出异常
			logger.error("VivoSdk createServerOrder 创建订单失败");
			throw new ServiceException(ServiceReturnCode.FAILD, "订单创建失败");
		} else {
			logger.error("VivoSdk createServerOrder 创建订单失败 " + (String) jsonMap.get("respMsg")); 
			throw new ServiceException(ServiceReturnCode.FAILD, "订单创建失败");
		}
	}
//	
//	private String createLinkPara(Map<String, String> map) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("version=" + "1.0.0" + "&");
//		sb.append("signMethod=" + "MD5" + "&");
//		sb.append("storeId=" + map.get("storeId") + "&");
//		sb.append("appId=" + map.get("appId") + "&");
//		sb.append("storeOrder=" + map.get("storeOrder") + "&");
//		sb.append("notifyUrl=" + map.get("notifyUrl") + "&");
//		sb.append("orderTime=" + map.get("orderTime") + "&");
//		sb.append("orderAmount=" + map.get("orderAmount") + "&");
//		sb.append("orderTitle=" + map.get("orderTitle") + "&");
//		sb.append("orderDesc=" + map.get("orderDesc") + "&");
//		sb.append("signature=" + map.get("signature"));
//		return sb.toString();
//	}
	
	/**
	 * 获取vivo签名
	 * @param para	参与签名的要素<key,value>
	 * @param key	vivo分配给商户的密钥
	 * @return 签名结果
	 */
	private String getVivoSign(Map<String, String> para, String key) {
		// 除去数组中的空值和签名参数
        Map<String, String> filteredReq = paraFilter(para);
		
        String prestr = createLinkString(filteredReq, true, false);	// 得到待签名字符串 需要对map进行sort，不需要对value进行URL编码
		prestr = prestr + QSTRING_SPLIT + md5Summary(key);
		
		return md5Summary(prestr);
	}
	
	/**
     * 把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param para 请求要素
     * @param sort 是否需要根据key值作升序排列
     * @param encode 是否需要URL编码
     * @return 拼接成的字符串
     */
    public String createLinkString(Map<String, String> para, boolean sort, boolean encode) {

        List<String> keys = new ArrayList<String>(para.keySet());
        
        if (sort) {
        	Collections.sort(keys);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = para.get(key);
            
            if (encode) {
				try {
					value = URLEncoder.encode(value, "utf-8");
				} catch (UnsupportedEncodingException e) {
					logger.error(e.toString(), e);
				}
            }
            
            if (i == keys.size() - 1) { // 拼接时，不包括最后一个&字符
                sb.append(key).append(QSTRING_EQUAL).append(value);
            } else {
                sb.append(key).append(QSTRING_EQUAL).append(value).append(QSTRING_SPLIT);
            }
        }
        return sb.toString();
    }
    
    /**
     * 对传入的参数进行MD5摘要
     * @param str	需进行MD5摘要的数据
     * @return		MD5摘要值
     */
    public String md5Summary(String str) {

		if (str == null) {
			return null;
		}

		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("utf-8"));
		} catch (NoSuchAlgorithmException e) {
			return str;
		} catch (UnsupportedEncodingException e) {
			return str;
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}
    
    /** 
     * 除去请求要素中的空值和签名参数
     * @param para 请求要素
     * @return 去掉空值与签名参数后的请求要素
     */
    public Map<String, String> paraFilter(Map<String, String> para) {

        Map<String, String> result = new HashMap<String, String>();

        if (para == null || para.size() <= 0) {
            return result;
        }

        for (String key : para.keySet()) {
            String value = para.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase(SIGNATURE)
                || key.equalsIgnoreCase(SIGN_METHOD)) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }
    
    /**
     * 异步通知消息验签
     * @param para	异步通知消息
     * @param key	vivo分配给商户的密钥
     * @return 验签结果
     */
    public boolean verifySignature(Map<String, String> para, String key) {
        // 除去数组中的空值和签名参数
        Map<String, String> filteredReq = paraFilter(para);
        // 根据参数获取vivo签名
        String signature = getVivoSign(filteredReq, key);
        // 获取参数中的签名值
        String respSignature = para.get(SIGNATURE);
        System.out.println("服务器签名：" + signature + " | 请求消息中的签名：" + respSignature);
        // 对比签名值
        if (null != respSignature && respSignature.equals(signature)) {
			return true;
		}else {
            return false;
        }
    }
}
