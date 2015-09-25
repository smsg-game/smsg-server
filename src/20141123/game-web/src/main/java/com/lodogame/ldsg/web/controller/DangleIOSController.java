package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.partner.model.dangle.DanglePaymentObj;
import com.lodogame.ldsg.partner.sdk.DangleIOSSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class DangleIOSController {
	private static Logger LOG = Logger.getLogger(DangleIOSController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/dlIOSPayment.do", method = RequestMethod.GET)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(DangleIOSSdk.instance().getPartnerId());
		DanglePaymentObj data = new DanglePaymentObj();
		data.setSignature(req.getParameter("signature"));
		data.setExt(req.getParameter("ext"));
		data.setMid(req.getParameter("mid"));
		data.setMoney(req.getParameter("money"));
		data.setOrder(req.getParameter("order"));
		data.setResult(req.getParameter("result"));
		data.setTime(req.getParameter("time"));
		LOG.info(data.getExt());
		try {
			if(ps.doPayment(data)){
				res.getWriter().print("success");
			}else{
				res.getWriter().print("error");
			}
		} catch (Exception e) {
			LOG.error("dangle payment error!",e);
		}
		return null;
	}
}
