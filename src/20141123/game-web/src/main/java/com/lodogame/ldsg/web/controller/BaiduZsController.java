package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.baidu.BaiduZsPaymentObj;
import com.lodogame.ldsg.partner.sdk.BaiduZsSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class BaiduZsController {
	private static Logger LOG = Logger.getLogger(BaiduZsController.class);

	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/baiduZsPayment.do", method = RequestMethod.POST)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(BaiduZsSdk.instance().getPartnerId());
		String retVal = FAILURE;
		String transdata = req.getParameter("transdata");
		String sign = req.getParameter("sign");
		LOG.info("transdata: " + transdata);
		BaiduZsPaymentObj data = Json.toObject(transdata, BaiduZsPaymentObj.class);
		if (BaiduZsSdk.instance().vertifySign(transdata, sign) && ps.doPayment(data)) {
			retVal = SUCCESS;
		}

		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
}
