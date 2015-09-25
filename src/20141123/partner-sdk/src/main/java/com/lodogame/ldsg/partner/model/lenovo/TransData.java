package com.lodogame.ldsg.partner.model.lenovo;

import com.lodogame.ldsg.partner.model.PaymentObj;

public class TransData extends PaymentObj {
	private String sign;
	private String transData;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTransData() {
		return transData;
	}

	public void setTransData(String transData) {
		this.transData = transData;
	}
}
