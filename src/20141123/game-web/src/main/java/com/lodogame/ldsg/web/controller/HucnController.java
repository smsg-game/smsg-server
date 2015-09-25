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
import com.lodogame.ldsg.partner.model.hucn.HucnPaymentObj;
import com.lodogame.ldsg.partner.sdk.HuCnSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class HucnController {
	private static Logger LOG = Logger.getLogger(HucnController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/hucnPayment.do", method = RequestMethod.POST)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(HuCnSdk.instance().getPartnerId());
		HucnPaymentObj data = new HucnPaymentObj();
		data.setAccount(req.getParameter("account"));
		data.setAmount(req.getParameter("amount"));
		data.setOrderid(req.getParameter("orderid"));
		data.setResult(req.getParameter("result"));
		data.setChannel(req.getParameter("channel"));
		data.setMsg(req.getParameter("msg"));
		data.setExtrainfo(req.getParameter("extrainfo"));
		data.setSign(req.getParameter("sign"));
		LOG.info("HucnPaymentInfo:"+Json.toJson(data));
		try {
			if(ps.doPayment(data)){
				res.getWriter().print(data.getExtrainfo());
			}else{
				res.getWriter().print("-1");
			}
		} catch (Exception e) {
			LOG.error("anzhi payment error!",e);
		}
		return null;
	}
}
