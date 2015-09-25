package com.lodogame.model;

/**
 * API的下载URL
 * @author zyz
 *
 */
public class PartnerApkUrl {
	/**
	 * 商户ID
	 */
	private String partnerId;
	
	/**
	 * 下载地址
	 */
	private String url;

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
