package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.kuaibo.KuaiBoPaymentObj;
import com.lodogame.ldsg.partner.sdk.KuaiBoSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class KuaiBoController {
	private static Logger LOG = Logger.getLogger(KuaiBoController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/kuaiBoPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(KuaiBoSdk.instance().getPartnerId());
		try {
			KuaiBoPaymentObj data = new KuaiBoPaymentObj();
			data.setEncString(req.getParameter("EncString"));
			data.setMerId(req.getParameter("MerId"));
			data.setMoney(req.getParameter("Money"));
			data.setNote(req.getParameter("Note"));
			data.setOrderId(req.getParameter("OrderId"));
			data.setPaymentFee(req.getParameter("PaymentFee"));
			data.setPaymentStatusCode(req.getParameter("PaymentStatusCode"));
			data.setTranCode(req.getParameter("TranCode"));
			LOG.info("KuaiBo payment info:"+Json.toJson(data));
			if(StringUtils.isNotBlank(data.getOrderId())
					&& StringUtils.isNotBlank(data.getPaymentFee())
					&& StringUtils.isNotBlank(data.getEncString())){
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
