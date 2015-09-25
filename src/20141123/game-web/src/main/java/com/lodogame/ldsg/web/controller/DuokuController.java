package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.partner.model.duoku.DuokuPaymentObj;
import com.lodogame.ldsg.partner.sdk.DuoKuSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class DuokuController {
	private static Logger LOG = Logger.getLogger(DuokuController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/duoKuPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(DuoKuSdk.instance().getPartnerId());
		try {
			String cardtype = req.getParameter("cardtype");
			String amount = req.getParameter("amount");
			String orderid = req.getParameter("orderid");
			String result = req.getParameter("result");
			String aid = req.getParameter("aid");
			String client_secret = req.getParameter("client_secret");
			String timetamp = req.getParameter("timetamp");
			if(StringUtils.isNotBlank(orderid)
					&& StringUtils.isNotBlank(amount)
					&& StringUtils.isNotBlank(client_secret)){
				DuokuPaymentObj data = new DuokuPaymentObj();
				data.setAmount(amount);
				data.setAid(aid);
				data.setClient_secret(client_secret);
				data.setOrderid(orderid);
				data.setResult(result);
				data.setTimetamp(timetamp);
				data.setCardtype(cardtype);
				if (ps.doPayment(data)) {
					res.getWriter().print("SUCCESS");
				}else if("2".equals(data.getResult())){
					res.getWriter().print("SUCCESS");
				}else{
					res.getWriter().print("ERROR_FAIL");
				}
			}else{
				res.getWriter().print("ERROR_FAIL");
			}
		} catch (Exception e) {
			LOG.error("duoku payment error!",e);
		}
		return null;
	}
}
