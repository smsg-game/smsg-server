package com.lodogame.model;

import java.io.Serializable;

/**
 * 上传的静态文件包信息
 * @author liaocheng
 *
 */
public class StaticPackageInfo implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String signature;
	
	/**
	 * 是否测试版本，0否，1是
	 */
	private int isTest;
	
	/**
	 * 下载地址，不包含 ip
	 */
	private String url;
	
	/**
	 * 版本号
	 * @return
	 */
	private String version;
	
	/**
	 * 白名单，只有白名单上的用户可以下载测试版的文件
	 * @return
	 */
	private String whiteList;
	
	private String describe;

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getIsTest() {
		return isTest;
	}

	public void setIsTest(int isTest) {
		this.isTest = isTest;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
	}
	
	
}
