package com.lodogame.ldsg.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.alibaba.fastjson.JSONObject;
import com.lodogame.ldsg.partner.model.mol.MolPaymentObj;
import com.lodogame.ldsg.partner.sdk.MolSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.partner.service.impl.MolServiceImpl;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class MolController {
	private static Logger LOG = Logger.getLogger(MolController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/molPayment.do", method = RequestMethod.POST)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(MolSdk.instance().getPartnerId());
		Map<String, Object> map = new HashMap<String, Object>();
		MolPaymentObj data = new MolPaymentObj();
		data.setAmount(req.getParameter("amount"));
		data.setApplicationCode(req.getParameter("applicationCode"));
		data.setChannelId(req.getParameter("channelId"));
		data.setCurrencyCode(req.getParameter("currencyCode"));
		data.setCustomerId(req.getParameter("customerId"));
		data.setPaymentId(req.getParameter("paymentId"));
		data.setPaymentStatusCode(req.getParameter("paymentStatusCode"));
		data.setReferenceId(req.getParameter("referenceId"));
		data.setSignature(req.getParameter("signature"));
		data.setVersion(req.getParameter("version"));
		try {
			data.setPaymentStatusDate(URLDecoder.decode(req.getParameter("paymentStatusDate"),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LOG.info("accept payment info:"+JSONObject.toJSONString(req.getParameterMap()));
		LOG.info("mol payment info:"+JSONObject.toJSONString(data));
		if (ps.doPayment(data)) {
			map.put("errcode", 200);
		}else{
			map.put("errcode", 3515);
		}

		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		ModelAndView modelView = new ModelAndView();
		modelView.setView(view);
		return modelView;
	}
	
	@RequestMapping(value = "/webApi/generalUrl.do", method = RequestMethod.POST)
	public ModelAndView generalUrl(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(MolSdk.instance().getPartnerId());
		Map<String, Object> map = new HashMap<String, Object>();
		String orderId = req.getParameter("orderId");
		MolServiceImpl molServiceImpl = (MolServiceImpl)ps;
		String url = molServiceImpl.generalPaymentUrl(orderId);
		map.put("url", url);
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		ModelAndView modelView = new ModelAndView();
		modelView.setView(view);
		return modelView;
	}
}
