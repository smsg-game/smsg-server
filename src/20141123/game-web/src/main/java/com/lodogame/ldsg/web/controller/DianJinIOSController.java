package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.partner.model.dianjin.DianJinPaymentObj;
import com.lodogame.ldsg.partner.model.dianjin.DianJinPaymentObj_new;
import com.lodogame.ldsg.partner.sdk.DianJinIOSSdk;
import com.lodogame.ldsg.partner.sdk.DianJinNewIOSSdk;
import com.lodogame.ldsg.partner.sdk.DianJinNewSdk;
import com.lodogame.ldsg.partner.sdk.DianJinSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.partner.service.impl.DianJinNewIOSServiceImpl;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class DianJinIOSController {
	private static Logger LOG = Logger.getLogger(DianJinIOSController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/djIOSPayment.do",method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps=null;
		String s=req.getParameter("App_Id");
		if (StringUtils.isNotBlank(s)) {
			 ps = serviceFactory.getBean(DianJinIOSSdk.instance().getPartnerId());
			 DianJinPaymentObj data=new DianJinPaymentObj();
			 data.setAppId(s);
			 data.setCreateTime(req.getParameter("Create_Time"));
				data.setExtra(req.getParameter("Extra"));
				data.setRechargeMoney(req.getParameter("Recharge_Money"));
				data.setPayStatus(req.getParameter("Pay_Status"));
				data.setRechargeGoldCount(req.getParameter("Recharge_Gold_Count"));
				data.setSign(req.getParameter("Sign"));
				data.setUin(req.getParameter("Uin"));
				data.setUrechargeId(req.getParameter("Urecharge_Id"));
				LOG.info("DianJinPaymentObj info:"+Json.toJson(data));
				try {
					if(StringUtils.isNotBlank(data.getSign()) 
							&& StringUtils.isNotBlank(data.getPayStatus()) 
							&& StringUtils.isNotBlank(data.getUrechargeId()) && ps.doPayment(data)){
						res.getWriter().print("Success");
					}else{
						res.getWriter().print("Error");
					}
				} catch (Exception e) {
					LOG.error("DianJinPaymentObj payment error!",e);
				}
		}else {
			 ps = serviceFactory.getBean("2015");
			 DianJinPaymentObj_new  data=new DianJinPaymentObj_new();
			 JSONObject jsonObject=new JSONObject();
				data.setCreateTime(req.getParameter("CreateTime"));
				data.setProductName(req.getParameter("ProductName"));
				data.setConsumeSreamId(req.getParameter("ConsumeStreamId"));
				data.setCooOrderSerial(req.getParameter("CooOrderSerial"));
				data.setGoodsId(req.getParameter("GoodsId"));
				data.setGoodsInfo(req.getParameter("GoodsInfo"));
				data.setGoodsCount(req.getParameter("GoodsCount"));
				data.setOriginalMoney(req.getParameter("OriginalMoney"));
				data.setOrderMoney(req.getParameter("OrderMoney"));
				data.setNote(req.getParameter("Note"));
				data.setPayStatus(req.getParameter("PayStatus"));
				data.setSign(req.getParameter("Sign"));
				data.setUin(req.getParameter("Uin"));
				LOG.info("DianJinPaymentObj info:"+Json.toJson(data));
				try {
					if(StringUtils.isNotBlank(data.getSign()) 
							&& StringUtils.isNotBlank(data.getPayStatus()) 
							&& StringUtils.isNotBlank(data.getCooOrderSerial()) && ps.doPayment(data)){
						jsonObject.put("ErrorCode", 1);
						jsonObject.put("ErrorDesc","接受成功");
						res.getWriter().print(jsonObject);
					}else{
						jsonObject.put("ErrorCode",0);
						jsonObject.put("ErrorDesc","接受失败");
						res.getWriter().print(jsonObject);
					}
				} catch (Exception e) {
					LOG.error("DianJinPaymentObj payment error!",e);
				}
		}
		return null;
	}
	
}
