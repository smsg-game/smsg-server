package com.lodogame.ldsg.partner.model.kuaiyong;

import com.lodogame.ldsg.partner.model.PaymentObj;
import com.lodogame.ldsg.partner.sdk.KuaiYongSdk;

public class KuaiYongPaymentObj extends PaymentObj {
	private String notifyData;
	private String orderid;
	private String dealseq;
	private String uid;
	private String subject;
	private String v;
	private String sign;
	private String payresult;
	private String fee;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotifyData() {
		return notifyData;
	}

	public void setNotifyData(String notifyData) {
		this.notifyData = notifyData;
	}

	public String getDealseq() {
		return dealseq;
	}

	public void setDealseq(String dealseq) {
		this.dealseq = dealseq;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getPayresult() {
		return payresult;
	}

	public void setPayresult(String payresult) {
		this.payresult = payresult;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
}
