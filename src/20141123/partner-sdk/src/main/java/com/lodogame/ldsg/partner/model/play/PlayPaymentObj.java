package com.lodogame.ldsg.partner.model.play;

import com.lodogame.ldsg.partner.model.PaymentObj;

public class PlayPaymentObj extends PaymentObj{
	
	private String serialno;
    private String resultCode;
    private String resultMsg;
    private String gameUserId;
    private String gameGold;
    private String validatecode;
    private String payType;
    private String gameId;
    private String requestTime;
    private String responseTime;
    private String cpparam;
    private String cost;
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getGameUserId() {
		return gameUserId;
	}
	public void setGameUserId(String gameUserId) {
		this.gameUserId = gameUserId;
	}
	public String getGameGold() {
		return gameGold;
	}
	public void setGameGold(String gameGold) {
		this.gameGold = gameGold;
	}
	public String getValidatecode() {
		return validatecode;
	}
	public void setValidatecode(String validatecode) {
		this.validatecode = validatecode;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getCpparam() {
		return cpparam;
	}
	public void setCpparam(String cpparam) {
		this.cpparam = cpparam;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
}
