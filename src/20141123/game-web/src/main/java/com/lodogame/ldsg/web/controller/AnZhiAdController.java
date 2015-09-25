package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.partner.model.anzhi.AnZhiPaymentObj;
import com.lodogame.ldsg.partner.sdk.AnZhiAdSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class AnZhiAdController {
	private static Logger LOG = Logger.getLogger(AnZhiAdController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/anZhiAdPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(AnZhiAdSdk.instance().getPartnerId());
		String responseStr = req.getParameter("data");
		try {
			if(StringUtils.isNotBlank(responseStr)){
				AnZhiPaymentObj data = new AnZhiPaymentObj();
				data.setData(responseStr);
				if (ps.doPayment(data)) {
					res.getWriter().print("success");
				}else{
					res.getWriter().print("fail");
				}
			}else{
				res.getWriter().print("fail");
			}
		} catch (Exception e) {
			LOG.error("anzhi payment error!",e);
		}
		return null;
	}
}
