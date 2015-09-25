package com.lodogame.ldsg.partner.model.xiaomi;

import com.lodogame.ldsg.partner.model.PaymentObj;

public class XiaomiPaymentObj extends PaymentObj {
	private String appId;
	private String cpOrderId;
	private String cpUserInfo;
	private String uid;
	private String orderId;
	private String orderStatus;
	private String payFee;
	private String productCode;
	private String productName;
	private String productCount;
	private String payTime;
	private String signature;
	
	// add by hlz
//	private String orderConsumeType; // 订单类型：10：普通订单11：直充直消订单
//	private long partnerGiftConsume; //使用游戏券金额 （如果订单使用游戏券则有,long型），如果有则参与签名

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCpOrderId() {
		return cpOrderId;
	}

	public void setCpOrderId(String cpOrderId) {
		this.cpOrderId = cpOrderId;
	}

	public String getCpUserInfo() {
		return cpUserInfo;
	}

	public void setCpUserInfo(String cpUserInfo) {
		this.cpUserInfo = cpUserInfo;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

//	public String getOrderConsumeType() {
//		return orderConsumeType;
//	}
//
//	public void setOrderConsumeType(String orderConsumeType) {
//		this.orderConsumeType = orderConsumeType;
//	}
//
//	public long getPartnerGiftConsume() {
//		return partnerGiftConsume;
//	}
//
//	public void setPartnerGiftConsume(long partnerGiftConsume) {
//		this.partnerGiftConsume = partnerGiftConsume;
//	}
	
	

}
