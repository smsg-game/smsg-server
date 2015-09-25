package com.lodogame.ldsg.partner.model.dianjin;

import com.lodogame.ldsg.partner.model.PaymentObj;

public class DianJinPaymentObj_new extends PaymentObj {

	private String appId;
	private String productName;
	private String consumeSreamId;// 消费流水号
	private String cooOrderSerial;// 商户订单号
	private String uin;
	private String goodsId;
	private String goodsInfo;
	private String goodsCount;
	private String originalMoney;// 原价总和
	private String orderMoney;// 实际总价
	private String note;// 支付描述
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

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getConsumeSreamId() {
		return consumeSreamId;
	}

	public void setConsumeSreamId(String consumeSreamId) {
		this.consumeSreamId = consumeSreamId;
	}

	public String getCooOrderSerial() {
		return cooOrderSerial;
	}

	public void setCooOrderSerial(String cooOrderSerial) {
		this.cooOrderSerial = cooOrderSerial;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(String goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public String getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getOriginalMoney() {
		return originalMoney;
	}

	public void setOriginalMoney(String originalMoney) {
		this.originalMoney = originalMoney;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
