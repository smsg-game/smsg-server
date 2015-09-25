package com.easou.sso.sdk.example;

import com.easou.sso.sdk.PayAPI;
import com.easou.sso.sdk.service.PayBean;

public class PayExample {

	public static void main(String[] args) {
		PayBean bean = PayAPI.getUserCurrency("999");
		System.out.println(bean.getEbNum());
	}
}
