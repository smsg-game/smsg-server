package com.lodogame.ldsg.domain;

import com.lodogame.ldsg.partner.model.PaymentObj;
import com.lodogame.ldsg.partner.sdk.AppleSdk;
import com.lodogame.ldsg.partner.service.PartnerService;

public class AppleVerfiyRunnable implements Runnable{
	
	private PartnerService ps;
	
	private PaymentObj paymentObj;
	
	public AppleVerfiyRunnable(PartnerService ps,PaymentObj paymentObj){
		this.ps = ps;
		this.paymentObj = paymentObj;
	}

	@Override
	public void run() {
		if(ps != null && paymentObj != null){
			//如果失败加入重试
			if(!ps.doPayment(paymentObj)){
				AppleSdk.instance().getExecutorService().execute(this);
			}
		}
	}

}
