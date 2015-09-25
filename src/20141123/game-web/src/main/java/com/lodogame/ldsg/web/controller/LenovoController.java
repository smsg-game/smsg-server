package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.partner.model.lenovo.TransData;
import com.lodogame.ldsg.partner.sdk.LenovoSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class LenovoController {

	private static Logger LOG = Logger.getLogger(LenovoController.class);
	
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/lenovoPayment.do", method = RequestMethod.POST)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(LenovoSdk.instance().getPartnerId());
		String retVal = FAILURE;
		String transdata = req.getParameter("transdata");
		String sign = req.getParameter("sign");
		
		if (!StringUtils.isBlank(transdata) && !StringUtils.isBlank(sign)) {
			TransData data = new TransData();
			data.setSign(sign);
			data.setTransData(transdata);
			if(ps.doPayment(data)){
				retVal = SUCCESS;
			}
		}
		
		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
}
