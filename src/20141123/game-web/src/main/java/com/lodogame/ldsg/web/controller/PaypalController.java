package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lodogame.ldsg.partner.model.paypal.PaypalPaymentObj;
import com.lodogame.ldsg.partner.sdk.EasouIOSSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class PaypalController {
	private static Logger LOG = Logger.getLogger(PaypalController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/paypalPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(EasouIOSSdk.instance().getEasouPartnerId());
		String confirm = req.getParameter("confirm");
		String payment = req.getParameter("payment");
		confirm = "{\"response\": {\"state\":\"approved\",\"id\":\"PAY-1CH415761F1235919KIQMTGY\",\"create_time\": \"2014-02-12T22:29:49Z\",\"intent\": \"sale\"},\"client\": {\"platform\": \"Android\",\"paypal_sdk_version\": \"2.2.2\",\"product_name\": \"PayPal-Android-SDK\",\"environment\": \"mock\"},\"response_type\": \"payment\"}";
		payment = "{\"short_description\": \"KIEd84pJXyZyGQ6Z5+DwMxPqQi1jsqF9xghXrqWcztzRnFQC+zNFykWQz3Iox+nhW89eTB+KKrceImq8KxOLOVnKb+SHtKVi/YoZGq7uFjRK8X+ZRinCIZmDFMoIUYrjqC+wkPoW0KEEklrWkfh5hlYQjaCZwgI8NN8iVgrtU9M=\",\"amount\": \"3\",\"intent\": \"sale\",\"currency_code\": \"USD\"}";
		try {
			if(StringUtils.isNotEmpty(confirm)
					&& StringUtils.isNotEmpty(payment)){
				PaypalPaymentObj paymentObj = new PaypalPaymentObj();
				paymentObj.setConfirm(JSONObject.parseObject(confirm));
				paymentObj.setPayment(JSONObject.parseObject(payment));
				LOG.info("paypalPaymentObj:"+JSON.toJSONString(paymentObj));
				if(ps.doPayment(paymentObj)){
					res.getOutputStream().print("0");
				}else{
					res.getOutputStream().print("1");
				}
			}else{
				res.getOutputStream().print("1");
			}
		} catch (Exception e) {
			LOG.error("paypalPaymentObj error!",e);
		}
		return null;
	}
}
