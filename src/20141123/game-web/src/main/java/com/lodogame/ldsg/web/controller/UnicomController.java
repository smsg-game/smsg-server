package com.lodogame.ldsg.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

 

 import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

import org.apache.commons.lang.StringUtils;
 
 
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.unicom.UnicomPaymentObj;
import com.lodogame.ldsg.partner.sdk.UnicomSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller 
 public class UnicomController {
 	
 	private static Logger LOG = Logger.getLogger(UnicomController.class);
 	private String keys="bc4217a7b95d331b2108631e";
 	private String serverKey="d9dfa118b78c255ac2fb09791942e7";
 	public static final String paySuccess="0";
 	 
 
 	
 	   @Autowired
 	   private PartnerServiceFactory serviceFactory; 
 	   @RequestMapping(value = "/webApi/unicomPayment.do")
 	   public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) throws IOException {
 		//得到相应聚道的服务类
 		PartnerService ps = serviceFactory.getBean("8001");
 	    UnicomPaymentObj data = new UnicomPaymentObj();
 	    String payStatus=req.getParameter("result");
 	    if(payStatus.equals(paySuccess)){
 	     	data.setChannelId(req.getParameter("channelid"));
 		    data.setCompany(req.getParameter("company"));		
 		    data.setDate(req.getParameter("date"));
 	        data.setMoney(req.getParameter("money"));		 
 	        String type=req.getParameter("paytype");		 
 			if(StringUtils.isNotBlank(type)){
 	        //如果支付类型是szf或者是zfb的支付方式时对应的参数无需解码
 			if(type.equals("SZF") || type.equals("ZFB")){				 
 				data.setSoftGood(req.getParameter("softgood"));
 				data.setCustomer(req.getParameter("customer")); 
 				data.setErrorStr(req.getParameter("errorstr"));
 			}else{
 				data.setSoftGood(URLDecoder.decode(req.getParameter("softgood"),"utf-8"));
 				data.setCustomer(URLDecoder.decode(req.getParameter("customer"),"utf-8")); 
 				data.setErrorStr(URLDecoder.decode(req.getParameter("errorstr"),"utf-8"));
 			}
 		}
 			data.setPayType(req.getParameter("paytype"));
 			data.setGameCode(req.getParameter("gamecode"));
 			data.setPaymentId(req.getParameter("paymentid"));
 			data.setPkey(req.getParameter("pkey")); 
 			data.setCustomerOrderNo(req.getParameter("customorderno"));
 			data.setGameCode(req.getParameter("gamecode")); 
 			LOG.info("TongBuPaymentObj info:"+Json.toJson(data));
 		try {
 			if(ps.doPayment(data)){
 			  res.getWriter().print("0");
 			}else{
 				res.getWriter().print("1");
 			}
 		} catch (Exception e) {
 			LOG.error("UnicomPaymentObj payment error!",e);
 	}
 	   
	}else{
		res.getWriter().print("1");
	}
		return null;
 	}
} 
