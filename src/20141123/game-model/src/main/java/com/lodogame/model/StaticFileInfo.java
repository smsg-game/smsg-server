package com.lodogame.model;

/**
 * 客户端文件
 * @author liaocheng
 *
 */
public class StaticFileInfo {
	
	/**
	 * 文件版本
	 */
	private String version;
	
	/**
	 * 文件的绝对路径
	 */
	private String absUrl;
	
	/**
	 * 相对路径
	 */
	private String relativeUrl;
	
	/**
	 * 标记位，0：不是测试版本；1：是测试版本
	 */
	private int is_test;
	
	/**
	 * 文件的 MD5 编码
	 */
	private String md5;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAbsUrl() {
		return absUrl;
	}

	public void setAbsUrl(String absUrl) {
		this.absUrl = absUrl;
	}

	public String getRelativeUrl() {
		return relativeUrl;
	}

	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	public int getIs_test() {
		return is_test;
	}

	public void setIs_test(int is_test) {
		this.is_test = is_test;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	

}
