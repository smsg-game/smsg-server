package com.lodogame.ldsg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.partner.model.chinamobile.UserInfo;
import com.lodogame.ldsg.partner.sdk.ChinaMobileSdk;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class ChinaMobileLoginController {
	private static Logger LOG = Logger.getLogger(ChinaMobileLoginController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/chinaMobileLogin.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		try {
			String userId = req.getParameter("userId");
			String key = req.getParameter("key");
			String channelId = req.getParameter("channelId");
			String region = req.getParameter("region");
			String ua = req.getParameter("Ua");
			String p = req.getParameter("p");
			//userId=1133986783&key=JS000d85877001142672819143531&cpId=772241&cpServiceId=624110077858&channelId=10001000&p=&region=200&Ua=T9510E
			LOG.info("token:"+p+",userId:"+userId+",key:"+key+",channelId:"+channelId+",region:"+region+",ua:"+ua);
			if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(key)
					&& StringUtils.isNotBlank(channelId) && StringUtils.isNotBlank(region)){
				UserInfo userInfo = new UserInfo();
				userInfo.setChannelId(channelId);
				userInfo.setCpparam(p);
				userInfo.setKey(key);
				userInfo.setRegion(region);
				userInfo.setUserId(userId);
				userInfo.setUa(ua);
				ChinaMobileSdk.instance().getUserMap().put(p,userInfo);
				res.setContentType("text/plain; charset=UTF8\r");
				res.getWriter().println(0);
			}
		} catch (Exception e) {
			LOG.error("china mobile login error!",e);
		}
		return null;
	}
}
