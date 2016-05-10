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
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.vivo.sdk.VivoPaymentObj;


/**
 * 步步高
 * 
 * @author yezp
 */
@Controller
public class VivoController {
	private static Logger LOG = Logger.getLogger(VivoController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/vivoPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean("1026");
		try {
			String respCode = req.getParameter("respCode");
			String respMsg = req.getParameter("respMsg");
			String signMethod = req.getParameter("signMethod");
			String signature = req.getParameter("signature");
			String tradeType = req.getParameter("tradeType");
			String tradeStatus = req.getParameter("tradeStatus");
			String cpId = req.getParameter("cpId");
			String appId = req.getParameter("appId");
			String uid = req.getParameter("uid");
			String cpOrderNumber = req.getParameter("cpOrderNumber");
			String orderNumber = req.getParameter("orderNumber");
			String orderAmount = req.getParameter("orderAmount");
			String extInfo = req.getParameter("extInfo");
			String payTime = req.getParameter("payTime");
			if (StringUtils.isNotBlank(respCode) && StringUtils.isNotBlank(orderNumber) 
					&& StringUtils.isNotBlank(orderAmount)) {
				VivoPaymentObj paymentObj = new VivoPaymentObj(respCode, respMsg, signMethod, signature, tradeType, tradeStatus, cpId, appId, uid, cpOrderNumber, orderNumber, orderAmount, extInfo, payTime);
				LOG.error("Vivo payment callback:" + Json.toJson(paymentObj));
				if (ps.doPayment(paymentObj)) {
					res.getWriter().print("SUCCESS");
				} else if(respCode.equals("0000")) {
					res.getWriter().print("SUCCESS");
				} else {
					res.getWriter().print("ERROR_FAIL");
				}				
			} else{
				res.getWriter().print("ERROR_FAIL");
			}		
		} catch (Exception e) {
			LOG.error("vivo payment error!", e);
		}
		return null;
	}
	
}
