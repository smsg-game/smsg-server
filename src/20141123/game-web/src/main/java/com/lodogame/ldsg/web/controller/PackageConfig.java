package com.lodogame.ldsg.web.controller;

import java.io.IOException;
import java.util.Properties;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 用于读取及动态更新 package.properties 配置文件
 * @author chengevo
 *
 */
public class PackageConfig {
	private static PackageConfig ins;
	private static Properties prop;
	
	private String packagePath;
	private String packageUrl;
	private String ips;
	
	public static PackageConfig instance() {
		synchronized (PackageConfig.class) {
			if (ins == null) {
				ins = new PackageConfig();
			}
		}
		return ins;
	}
	
	private PackageConfig() {
		loadProperties();
	}
	
	public void reload() {
		loadProperties();
	}
	
	private void loadProperties() {
		try {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("package.properties"));
			packagePath = prop.getProperty("package.path");
			packageUrl = prop.getProperty("package.url");
			ips = prop.getProperty("ips");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	public String getPackageUrl() {
		return packageUrl;
	}
	public void setPackageUrl(String packageUrl) {
		this.packageUrl = packageUrl;
	}
	public String getIps() {
		return ips;
	}
	public void setIps(String ips) {
		this.ips = ips;
	}
}
