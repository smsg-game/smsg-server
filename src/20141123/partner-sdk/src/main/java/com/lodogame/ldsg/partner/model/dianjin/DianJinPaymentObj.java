package com.lodogame.ldsg.partner.model.dianjin;

import com.lodogame.ldsg.partner.model.PaymentObj;

public class DianJinPaymentObj extends PaymentObj{
	
	private String appId;
	
	private String uin;
	
	private String urechargeId;
	
	private String extra;
	
	private String rechargeMoney;
	
	private String rechargeGoldCount;
	
	private String payStatus;
	
	private String createTime;
	
	private String sign;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public String getUrechargeId() {
		return urechargeId;
	}

	public void setUrechargeId(String urechargeId) {
		this.urechargeId = urechargeId;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(String rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public String getRechargeGoldCount() {
		return rechargeGoldCount;
	}

	public void setRechargeGoldCount(String rechargeGoldCount) {
		this.rechargeGoldCount = rechargeGoldCount;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
