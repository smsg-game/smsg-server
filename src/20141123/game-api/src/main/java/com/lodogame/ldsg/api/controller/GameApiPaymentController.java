package com.lodogame.ldsg.api.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.model.SystemGoldSet;

/**
 * 游戏服务器API
 * 
 * @author CJ
 * 
 */
@Controller
public class GameApiPaymentController {

	private static final Logger LOG = Logger.getLogger(UserController.class);

	@Autowired
	private GameApiService gameApiService;
	
	/**
	 * 获取套餐列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/gameApi/getPaySettings.do")
	public ModelAndView getPaySettings(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		List<SystemGoldSet> list = gameApiService.getPaySettings();
		map.put("list", list);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}
	
	@RequestMapping("/gameApi/payment.do")
	public ModelAndView payment(HttpServletRequest request) {

		Map<String, Object> map = new HashMap<String, Object>();
		int rc = ServiceReturnCode.SUCCESS;
		try {

			String partnerId = request.getParameter("partnerId");
			String serverId = request.getParameter("serverId");
			String partnerUserId = request.getParameter("partnerUserId");
			String channel = request.getParameter("channel");
			String orderId = request.getParameter("orderId");
			String strAmount = request.getParameter("amount");
			String strGold = request.getParameter("gold");
			String userIp = request.getParameter("userIp");
			String remark = request.getParameter("remark");

			BigDecimal amount = new BigDecimal(Double.parseDouble(strAmount));
			int gold = Integer.parseInt(strGold);

			gameApiService.doPayment(partnerId, serverId, partnerUserId, channel, orderId, amount, gold, userIp, remark);

		} catch (ServiceException se) {
			LOG.error(se.getMessage(), se);
			rc = se.getCode();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			rc = ServiceReturnCode.FAILD;
		}

		map.put("rc", rc);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}
}
