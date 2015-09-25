package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.partner.model.play.PlayPaymentObj;
import com.lodogame.ldsg.partner.sdk.PlaySdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class PlayController {
	private static Logger LOG = Logger.getLogger(PlayController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/playPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(PlaySdk.instance().getPartnerId());
		try {
			String payType = req.getParameter("payType");
			PlayPaymentObj obj = null;
			if(StringUtils.isNotEmpty(payType)
					&& "isagSmsPay".equals(payType)){//短信支付
				obj = new PlayPaymentObj();
				obj.setPayType(payType);
				obj.setResultCode(req.getParameter("resultCode"));
				obj.setCpparam(req.getParameter("cpparam"));
				obj.setResultMsg(req.getParameter("resultMsg"));
				obj.setCost(req.getParameter("cost"));
				obj.setValidatecode(req.getParameter("validatecode"));
				obj.setRequestTime(req.getParameter("requestTime"));
				if(ps.doPayment(obj)){
					res.addHeader("serialno",obj.getCpparam());
					res.getWriter().print("ResultCode:" + obj.getResultCode() + "<br />CpParam:" + obj.getCpparam());
				}
			}else if(StringUtils.isNotEmpty(payType) 
					&& StringUtils.isNotBlank(payType)){//普通支付
				obj = new PlayPaymentObj();
				obj.setSerialno(req.getParameter("serialno"));
				obj.setResultCode(req.getParameter("resultCode"));
				obj.setResultMsg(req.getParameter("resultMsg"));
				obj.setGameUserId(req.getParameter("gameUserId"));
				obj.setGameGold(req.getParameter("gameGold"));
				obj.setValidatecode(req.getParameter("validatecode"));
				obj.setPayType(req.getParameter("payType"));
				obj.setGameId(req.getParameter("gameId"));
				obj.setRequestTime(req.getParameter("requestTime"));
				obj.setResponseTime(req.getParameter("responseTime"));
				if(ps.doPayment(obj)){
					res.setHeader("serialno",obj.getSerialno());
					res.getWriter().print("serialno:"+obj.getSerialno());
				}
			}
		} catch (Exception e) {
			LOG.error("duoku payment error!",e);
		}
		return null;
	}
}
