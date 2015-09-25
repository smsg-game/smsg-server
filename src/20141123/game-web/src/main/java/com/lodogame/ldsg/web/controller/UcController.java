package com.lodogame.ldsg.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.uc.PayCallbackResponse;
import com.lodogame.ldsg.partner.sdk.UcSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class UcController {

	private static Logger LOG = Logger.getLogger(UcController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/ucPayment.do", method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView ucCallBack(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(UcSdk.instance().getPartnerId());
		String retVal = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
			String ln;
			StringBuffer stringBuffer = new StringBuffer();
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			
			String paramsStr = stringBuffer.toString();
			
			LOG.info("[UcController ucCallBack 接收到的参数 : ]"+paramsStr);
			
			PayCallbackResponse rsp = Json.toObject(paramsStr, PayCallbackResponse.class);
			
			if(paramsStr.contains("cpOrderId")){
				rsp.setOwnFlag(true);
			}

			if (ps.doPayment(rsp)) {
				retVal = "SUCCESS";
			} else {
				// retVal = "FAILURE";
				retVal = "SUCCESS";
			}

		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
			retVal = "SUCCESS";
		}

		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
}
