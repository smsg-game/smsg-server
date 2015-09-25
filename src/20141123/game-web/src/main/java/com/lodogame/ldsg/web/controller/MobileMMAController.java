package com.lodogame.ldsg.web.controller;

import java.io.BufferedReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.lodogame.game.utils.mobileMM.Dom4jUtil;
import com.lodogame.ldsg.partner.model.mobilemm.MobileMMPaymentObj;
import com.lodogame.ldsg.partner.sdk.MobileMMASdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;

@Controller
public class MobileMMAController {
	private static Logger LOG = Logger.getLogger(MobileMMAController.class);
	
	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/mobileMMAPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		try {
			PartnerService ps = serviceFactory.getBean(MobileMMASdk.instance().getPartnerId());
			String param = req.getQueryString();
			LOG.info("param:" + param);
			String xmls = null;
			MobileMMPaymentObj data = new MobileMMPaymentObj();
			if (req.getContentLength() != -1) {
				char[] readerBuffer = new char[req.getContentLength()];
				BufferedReader bufferedReader = req.getReader();
				int portion = bufferedReader.read(readerBuffer);
				int amount = portion;
				while (amount < readerBuffer.length) {
					portion = bufferedReader.read(readerBuffer, amount,
							readerBuffer.length - amount);
					amount = amount + portion;
				}

				StringBuffer stringBuffer = new StringBuffer(
						(int) (readerBuffer.length * 1.5));
				for (int index = 0; index < readerBuffer.length; index++) {
					char c = readerBuffer[index];
					stringBuffer.append(c);
				}
				xmls = stringBuffer.toString();
				LOG.info("xmls:" + xmls);
				Map<String, String> maps = Dom4jUtil.parserXml(xmls);
				data.setActionID(maps.get("ActionID"));
				data.setActionTime(maps.get("ActionTime"));
				data.setAppID(maps.get("AppID"));
				data.setChannelID(maps.get("ChannelID"));
				data.setCheckID(maps.get("CheckID"));
				data.setDestAddress(maps.get("Dest_Address"));
				data.setExData(maps.get("ExData"));
				data.setFeeMsisdn(maps.get("FeeMSISDN"));
				data.setMd5Sign(maps.get("MD5Sign"));
				data.setMsgType(maps.get("MsgType"));
				data.setMsisdn(maps.get("MSISDN"));
				data.setOrderID(maps.get("OrderID"));
				data.setOrderType(maps.get("OrderType"));
				data.setPayCode(maps.get("PayCode"));
				data.setPrice(maps.get("Price"));
				data.setSendAddress(maps.get("Send_Address"));
				data.setSubsNumb(maps.get("SubsNumb"));
				data.setSubsSeq(maps.get("SubsSeq"));
				data.setTotalPrice(maps.get("TotalPrice"));
				data.setTradeID(maps.get("TradeID"));
				data.setVersion(maps.get("Version"));
				LOG.info("mobileMMA info:"+JSON.toJSONString(data));
				if (StringUtils.isNotEmpty(data.getOrderID())
						&& StringUtils.isNotEmpty(data.getMd5Sign())&&
						ps.doPayment(data)) {
					res.getWriter().print(getResponse(null));
				}else{
					res.getWriter().print("error");
				}
			}else{
				res.getWriter().print("error");
			}
		} catch (Exception e) {
			LOG.error("MobileMMA payment error!",e);
		}
		return null;
	}
	
	public String getResponse(String hRet) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SyncAppOrderResp><MsgType>SyncAppOrderResp</MsgType><Version>1.0.0</Version><hRet>0</hRet></SyncAppOrderResp>";
	}
}
