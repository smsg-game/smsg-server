package com.lodogame.ldsg.web.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.lodogame.ldsg.partner.model.ipay.SignHelper;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
@Controller
public class IpayController {
private static Logger LOG = Logger.getLogger(IpayController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;
	
	public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCApMdoweZ/kw9lyQMjmjFGucW8Dz5T9Dlg2JFNHH00zIstRtHaCPoonzZEwNusC9dNE39WP3p5P1OwX+eILXpmi/d5k76FOVnINRsouljslWwKMVYKLumeGd2TB4YI2E2eKIoHvULyrfiUx1xAXkajTAN/JpF7V51us5sv0Fwr7wIDAQAB";


	@RequestMapping(value = "/webApi/ipayPayment.do", method = RequestMethod.POST)
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean("1025");
		String transdata = req.getParameter("transdata"); 
		String sign =  req.getParameter("sign"); 
		String signtype = req.getParameter("signtype"); 
		
		LOG.info("ipay收到支付回调,trandata="+transdata+",sign="+sign+",signtype="+signtype);
		
		/*
		 * 调用验签接口
		 * 
		 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
		 */
		if (SignHelper.verify(transdata, sign, PLATP_KEY)) {
			LOG.info("校验成功");
			JSONObject json = JSONObject.parseObject(transdata);
			String orderId = json.getString("cporderid");
			String partnerUserId = json.getString("appuserid");
			String partnerOrderId = json.getString("transid");
			BigDecimal finishAmount = json.getBigDecimal("money");
			int result = json.getIntValue("result");
			boolean success = false;
			if(result==0){
				success = true;
			}
			try {
				if(ps.doPayment(orderId, partnerUserId, success, partnerOrderId, finishAmount, null)){
					LOG.info("ipay发货成功~~");
					res.getWriter().print("SUCCESS");
				}else{
					LOG.info("ipay发货失败~~");
					res.getWriter().print("FAILURE");
				}
			} catch (Exception e) {
				try {
					res.getWriter().print("FAILURE");
				} catch (IOException e1) {
					LOG.error("ipay payment error!",e1);
				}
				LOG.error("ipay payment error!",e);
			}
		} else {
			LOG.info("校验失败");
		}
		return null;
	}
}
