package com.lodogame.ldsg.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Config {

	private static Config cfg;

	private String signKey;
	private String account;
	private String password;
	private boolean isGameServer;
	private boolean isDebug;
	private boolean openArena;
	private int initGold;
	private int initCopper;
	private boolean isServerClose;
	private String serverCloseNotice;
	private String getServerListUrl;
	private int isHK;
	private String loginUrl;

	private Properties prop;

	public static Config ins() {
		synchronized (Config.class) {
			if (cfg == null) {
				cfg = new Config();
			}
		}
		return cfg;
	}

	private Config() {
		try {
			initConfig();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void initConfig() throws IOException {
		prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("webconfig.properties"));
		isHK = Integer.valueOf(prop.getProperty("server.isHK", "0"));
		signKey = prop.getProperty("signKey", "test");
		account = prop.getProperty("account", "webadmin");
		password = prop.getProperty("password", "webadmin");
		isGameServer = NumberUtils.toInt(prop.getProperty("server.isGameServer", "0")) == 1;
		initGold = NumberUtils.toInt(prop.getProperty("user.initGold", "0"));
		initCopper = NumberUtils.toInt(prop.getProperty("user.initCopper", "1000"));
		isServerClose = NumberUtils.toInt(prop.getProperty("server.isServerClose", "0")) == 1;
		serverCloseNotice = prop.getProperty("server.closeNotice", "");
		serverCloseNotice = new String(serverCloseNotice.getBytes("ISO8859-1"), "utf8");
		getServerListUrl = prop.getProperty("getServerListUrl", "http://admin.sg.xworldgame.com/web/get_server_list");
		loginUrl = prop.getProperty("loginUrl", "http://wapi.andr.sg.xworldgame.com:8088");
//		getServerListUrl = prop.getProperty("getServerListUrl", "http://admin.ldsg.lodogame.com:8088/adminApi/serverList.do");
//		loginUrl = prop.getProperty("loginUrl", "sg.fantingame.com:8088");
		String debug = prop.getProperty("debug", "false");
		isDebug = debug.equals("true");
		openArena = "true".equals(prop.getProperty("openArena", "true"));
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isGameServer() {
		return isGameServer;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public int getInitGold() {
		return initGold;
	}

	public int getInitCopper() {
		return initCopper;
	}

	public boolean isServerClose() {
		return isServerClose;
	}

	public String getServerCloseNotice() {
		return serverCloseNotice;
	}

	public boolean isOpenArena() {
		return openArena;
	}

	public void setOpenArena(boolean openArena) {
		this.openArena = openArena;
	}
	
	public int getIsHK() {
		return isHK;
	}

	public void setIsHK(int isHK) {
		this.isHK = isHK;
	}

	public String getGetServerListUrl() {
		return getServerListUrl;
	}

	public String getLoginUrl() {
		return loginUrl;
	}
}
