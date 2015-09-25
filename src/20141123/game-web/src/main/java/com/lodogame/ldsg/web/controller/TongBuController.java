package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.tongbu.TongBuPaymentObj;
import com.lodogame.ldsg.partner.sdk.TongBuSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class TongBuController {
	private static Logger LOG = Logger.getLogger(TongBuController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/tongbuPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(TongBuSdk.instance().getPartnerId());
		TongBuPaymentObj data = new TongBuPaymentObj();
		data.setAmount(req.getParameter("amount"));
		data.setDebug(req.getParameter("debug"));
		data.setPartner(req.getParameter("partner"));
		data.setPaydes(req.getParameter("paydes"));
		data.setSign(req.getParameter("sign"));
		data.setSource(req.getParameter("source"));
		data.setTradeNo(req.getParameter("trade_no"));
//		data.setAmount("100");
//		data.setDebug("1");
//		data.setPartner("131268");
//		data.setPaydes("TBPay");
//		data.setSign("1a944203dbf6531c13c9f879b838abfd");
//		data.setSource("tongbu");
//		data.setTradeNo("0120052014010700000090");
		LOG.info("TongBuPaymentObj info:"+Json.toJson(data));
		try {
			if(ps.doPayment(data)){
				res.getWriter().print("{\"status\":\"success\"}");
			}else{
				res.getWriter().print("-1");
			}
		} catch (Exception e) {
			LOG.error("TongBuPaymentObj payment error!",e);
		}
		return null;
	}
}
