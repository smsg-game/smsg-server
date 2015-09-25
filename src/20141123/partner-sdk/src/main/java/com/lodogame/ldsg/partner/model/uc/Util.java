package com.lodogame.ldsg.partner.model.uc;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 工具类。
 */
public class Util {

	private static  Log Logger = LogFactory.getLog(Util.class);
	
	/** 
     * MD5 加密 
     */  
    public static String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
            messageDigest.reset();  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            Logger.error("NoSuchAlgorithmException caught!");
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            Logger.error(e.toString());
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }
    
    
    /**
	 * 将Map组装成待签名数据。 待签名的数据必须按照一定的顺序排列
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getSignData(Map params) {
		StringBuffer content = new StringBuffer();

		// 按照key做排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key) == null ? "" : params.get(key).toString();
			if (value != null) {
				content.append( key + "=" + value);
			} else {
				content.append(key + "=");
			}
		}

		return content.toString();
	}
	
	/**
	 * 替换所有换行符。
	 * @param str 原字符串
	 * @return 替换结果
	 */
	public static String trim(String str){
		if(StringUtils.isEmpty(str)){
			return str;
		}
		return str.replaceAll("\r", "").replaceAll("\n", "");
	}
	
	/**
	 * 
	 * 进行url编码。
	 * @param str 原字符串
	 * @return 
	 */
	public static String urlEncode(String str){
		try {
			return URLEncoder.encode(str,"UTF-8");
		} catch (Exception e) {
			return str;
		}
	}
}
