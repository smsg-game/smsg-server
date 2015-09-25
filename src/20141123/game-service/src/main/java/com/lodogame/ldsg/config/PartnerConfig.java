package com.lodogame.ldsg.config;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 合作商配置，根据合作商返回合作商英文代码，用于在程序中获取响应的处理对象
 * @author chenjian
 *
 */
public class PartnerConfig {
	
	private final static String DEFAULT_PARTNER = "easouPartner";
	private static PartnerConfig cfg;
	
	private Map<String, String> partners;
	
	private Properties prop;
	
	public static PartnerConfig ins() {
		synchronized (Config.class) {
			if (cfg == null) {
				cfg = new PartnerConfig();
			}
		}
		return cfg;
	}
	
	private PartnerConfig(){
		try {
			initConfig();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void reload(){
		try {
			initConfig();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void initConfig() throws IOException {
		prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("partner.properties"));
		partners = new HashMap<String, String>();
		Set<Object> keys = prop.keySet();
		for(Object key : keys){
			partners.put((String)key, prop.getProperty((String)key));
		}
	}
	
	public String getPartnerName(String partnerId){
		if(StringUtils.isBlank(partnerId)){
			return DEFAULT_PARTNER;
		}
		return partners.containsKey(partnerId) ? partners.get(partnerId) : DEFAULT_PARTNER;
	}
	
}
