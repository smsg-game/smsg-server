package com.lodogame.ldsg.web.vivo.sdk;

import com.lodogame.ldsg.partner.model.PaymentObj;

/**
 * 步步高回调数据
 * 
 * @author yezp
 */
public class VivoPaymentObj extends PaymentObj{

	private String respCode;
	private String respMsg;
	private String signMethod;
	private String signature;
	private String tradeType ;
	private String tradeStatus ;
	private String cpId ;
	private String appId ;
	private String uid ;
	private String cpOrderNumber ;
	private String orderNumber ;
	private String orderAmount;
	private String extInfo ;
	private String payTime ;
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public String getSignMethod() {
		return signMethod;
	}
	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCpOrderNumber() {
		return cpOrderNumber;
	}
	public void setCpOrderNumber(String cpOrderNumber) {
		this.cpOrderNumber = cpOrderNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	/**
	 * @param respCode
	 * @param respMsg
	 * @param signMethod
	 * @param signature
	 * @param tradeType
	 * @param tradeStatus
	 * @param cpId
	 * @param appId
	 * @param uid
	 * @param cpOrderNumber
	 * @param orderNumber
	 * @param orderAmount
	 * @param extInfo
	 * @param payTime
	 */
	public VivoPaymentObj(String respCode, String respMsg, String signMethod, String signature, String tradeType,
			String tradeStatus, String cpId, String appId, String uid, String cpOrderNumber, String orderNumber,
			String orderAmount, String extInfo, String payTime) {
		super();
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.signMethod = signMethod;
		this.signature = signature;
		this.tradeType = tradeType;
		this.tradeStatus = tradeStatus;
		this.cpId = cpId;
		this.appId = appId;
		this.uid = uid;
		this.cpOrderNumber = cpOrderNumber;
		this.orderNumber = orderNumber;
		this.orderAmount = orderAmount;
		this.extInfo = extInfo;
		this.payTime = payTime;
	}
	/**
	 * 
	 */
	public VivoPaymentObj() {
		super();
	}

}
