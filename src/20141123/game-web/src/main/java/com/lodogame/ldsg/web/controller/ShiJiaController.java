package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.partner.model.shijia.ShiJiaPaymentObj;
import com.lodogame.ldsg.partner.sdk.ShiJiaSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class ShiJiaController {
	private static Logger LOG = Logger.getLogger(ShiJiaController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/shiJiaPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(ShiJiaSdk.instance().getPartnerId());
		try {
			String uid = req.getParameter("uid");
			String amount = req.getParameter("amount");
			String billno = req.getParameter("billno");
			String server_id = req.getParameter("server_id");
			String extrainfo = req.getParameter("extrainfo");
			String sign = req.getParameter("sign");
			String timestamp = req.getParameter("timestamp");
			LOG.info("shijia orderId:"+extrainfo+",sign:"+sign);
			if(StringUtils.isNotBlank(uid)
					&& StringUtils.isNotBlank(billno)
					&& StringUtils.isNotBlank(sign)
					&& StringUtils.isNotBlank(amount)
					&& StringUtils.isNotBlank(timestamp)){
				ShiJiaPaymentObj data = new ShiJiaPaymentObj();
				data.setAmount(amount);
				data.setBillno(billno);
				data.setExtrainfo(extrainfo);
				data.setServer_id(server_id);
				data.setSign(sign);
				data.setTimestamp(timestamp);
				data.setUid(uid);
				if (ps.doPayment(data)) {
					res.getWriter().print("success");
				}else{
					res.getWriter().print("fail");
				}
			}else{
				res.getWriter().print("fail");
			}
		} catch (Exception e) {
			LOG.error("shijia payment error!",e);
		}
		return null;
	}
}
