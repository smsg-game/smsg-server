package com.lodogame.ldsg.web.controller;

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

import com.lodogame.ldsg.partner.model.xiaomi.XiaomiPaymentObj;
import com.lodogame.ldsg.partner.sdk.XiaomiSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class XiaomiController {
	private static Logger LOG = Logger.getLogger(XiaomiController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/xiaomiPayment.do", method = RequestMethod.GET)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		
		String orderSource = BaseController.getOrderSource(req);
		LOG.info("XiaomiController pay callback orderSource : " + orderSource);
		
		PartnerService ps = serviceFactory.getBean(XiaomiSdk.instance().getPartnerId());
		XiaomiPaymentObj data = new XiaomiPaymentObj();
		Map<String, Object> map = new HashMap<String, Object>();
		data.setAppId(req.getParameter("appId"));
		data.setCpOrderId(req.getParameter("cpOrderId"));
		data.setCpUserInfo(req.getParameter("cpUserInfo"));
		data.setOrderId(req.getParameter("orderId"));
		data.setOrderStatus(req.getParameter("orderStatus"));
		data.setPayFee(req.getParameter("payFee"));
		data.setPayTime(req.getParameter("payTime"));
		data.setProductCode(req.getParameter("productCode"));
		data.setProductCount(req.getParameter("productCount"));
		data.setProductName(req.getParameter("productName"));
		data.setSignature(req.getParameter("signature"));
		data.setUid(req.getParameter("uid"));
		
		// add by hlz
//		data.setOrderConsumeType(req.getParameter("orderConsumeType"));
//		data.setPartnerGiftConsume(Long.valueOf(req.getParameter("partnerGiftConsume")));

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
	

}
