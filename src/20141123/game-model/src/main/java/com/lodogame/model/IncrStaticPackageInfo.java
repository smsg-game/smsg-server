package com.lodogame.model;

import java.io.Serializable;

/**
 * 增量静态文件包
 * @author chengevo
 *
 */
public class IncrStaticPackageInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用于生成增量包的新版本号
	 */
	private String oldVersion;
	
	/**
	 * 用于生成增量包的旧版本号
	 */
	private String newVersion;
	private String url;
	public String getOldVersion() {
		return oldVersion;
	}
	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}
