package com.lodogame.model;

/**
 * 商户配置信息
 * @author CJ
 *
 */
public class PartnerConfig {
	/**
	 * 商户ID
	 */
	public String partnerId;
	/**
	 * 商户名称
	 */
	public String partnerName;
	/**
	 * 登录URL
	 */
	public String loginUrl;
	/**
	 * 支付URL
	 */
	public String payUrl;
	
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
}
