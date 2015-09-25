package com.lodogame.ldsg.partner.model.unicom;

import com.lodogame.ldsg.partner.model.PaymentObj;

public class UnicomPaymentObj extends PaymentObj {
	
	private String ChannelId;//聚道id
	private String SoftGood; //游戏商品名
	private int result; //支付结果
	private String PaymentId;//订单号
	private String ErrorStr;//执行订单为空
	private String company;//公司名
	private String customer;//注册用户id
	private String money;//游戏商品价格
	private String date;//支付时间
	private String Pkey;//用户数据校验
	private String PayType;//支付类型
	private String CustomerOrderNo;//自定义订单号
	private String GameCode;//游戏id
	public String getChannelId() {
		return ChannelId;
	}
	public void setChannelId(String channelId) {
		ChannelId = channelId;
	}
	public String getSoftGood() {
		return SoftGood;
	}
	public void setSoftGood(String softGood) {
		SoftGood = softGood;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getPaymentId() {
		return PaymentId;
	}
	public void setPaymentId(String paymentId) {
		PaymentId = paymentId;
	}
	public String getErrorStr() {
		return ErrorStr;
	}
	public void setErrorStr(String errorStr) {
		ErrorStr = errorStr;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPkey() {
		return Pkey;
	}
	public void setPkey(String pkey) {
		Pkey = pkey;
	}
	public String getPayType() {
		return PayType;
	}
	public void setPayType(String payType) {
		PayType = payType;
	}
	public String getCustomerOrderNo() {
		return CustomerOrderNo;
	}
	public void setCustomerOrderNo(String customerOrderNo) {
		CustomerOrderNo = customerOrderNo;
	}
	public String getGameCode() {
		return GameCode;
	}
	public void setGameCode(String gameCode) {
		GameCode = gameCode;
	}
	public UnicomPaymentObj(String channelId, String softGood, int result,
			String paymentId, String errorStr, String company, String customer,
			String money, String date, String pkey, String payType,
			String customerOrderNo, String gameCode) {
		super();
		ChannelId = channelId;
		SoftGood = softGood;
		this.result = result;
		PaymentId = paymentId;
		ErrorStr = errorStr;
		this.company = company;
		this.customer = customer;
		this.money = money;
		this.date = date;
		Pkey = pkey;
		PayType = payType;
		CustomerOrderNo = customerOrderNo;
		GameCode = gameCode;
	}
	public UnicomPaymentObj() {
		super();
	}

	
	
}
