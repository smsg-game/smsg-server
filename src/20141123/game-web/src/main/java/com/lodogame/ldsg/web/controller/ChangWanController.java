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
import com.lodogame.ldsg.partner.model.changwan.ChangWanPaymentObj;
import com.lodogame.ldsg.partner.sdk.ChangWanSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class ChangWanController {
	private static Logger LOG = Logger.getLogger(ChangWanController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/cwPayment.do", method = RequestMethod.GET)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(ChangWanSdk.instance().getPartnerId());
		ChangWanPaymentObj data = new ChangWanPaymentObj();
		data.setServerid(req.getParameter("serverid"));
		data.setAmount(req.getParameter("amount"));
		data.setCustominfo(req.getParameter("custominfo"));
		data.setErrdesc(req.getParameter("errdesc"));
		data.setOpenid(req.getParameter("openid"));
		data.setOrdernum(req.getParameter("ordernum"));
		data.setPaytime(req.getParameter("paytime"));
		data.setPaytype(req.getParameter("paytype"));
		data.setSign(req.getParameter("sign"));
		data.setStatus(req.getParameter("status"));
		LOG.info("changwan:"+Json.toJson(data));
		try {
			if(ps.doPayment(data)){
				res.getWriter().print("1");
			}else{
				res.getWriter().print("103");
			}
		} catch (Exception e) {
			LOG.error("anzhi payment error!",e);
		}
		return null;
	}
}
