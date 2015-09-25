package com.lodogame.ldsg.partner.model.paypal;

import com.alibaba.fastjson.JSONObject;
import com.lodogame.ldsg.partner.model.PaymentObj;

public class PaypalPaymentObj extends PaymentObj {
	private JSONObject confirm;
	
	private JSONObject payment;
	
	private String orderId;
	
	private String paypalId;

	public JSONObject getConfirm() {
		return confirm;
	}

	public void setConfirm(JSONObject confirm) {
		this.confirm = confirm;
	}

	public JSONObject getPayment() {
		return payment;
	}

	public void setPayment(JSONObject payment) {
		this.payment = payment;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaypalId() {
		return paypalId;
	}

	public void setPaypalId(String paypalId) {
		this.paypalId = paypalId;
	}
}
