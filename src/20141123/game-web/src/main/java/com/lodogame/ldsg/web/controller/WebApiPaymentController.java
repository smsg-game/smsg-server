package com.lodogame.ldsg.web.controller;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.easou.sso.sdk.service.ClientConfig;
import com.easou.sso.sdk.service.Md5SignUtil;
import com.lodogame.game.dao.PaymentOrderDao;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.constants.OrderStatus;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.Assert;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.partner.service.impl.EasouServiceImpl;
import com.lodogame.ldsg.sdk.GameApiSdk;
import com.lodogame.ldsg.service.WebApiService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.model.PaymentOrder;

/**
 * 游戏服务器WEB
 * 
 * @author CJ
 * 
 */
@Controller
public class WebApiPaymentController {

	private static final Logger LOG = Logger.getLogger(WebApiPaymentController.class);

	private static final Logger paymentLog = Logger.getLogger("paymentLog");

	private static final String crcKey = "$#@$%%eweqwlkfef";

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@Autowired
	private PaymentOrderDao paymentOrderDao;

	// private void checkTimestamp(String timestamp) {
	//
	// long timeout =
	// NumberUtils.toLong(ClientConfig.getProperty("ldsg.webapi.timeout",
	// "5000"));
	// long time = NumberUtils.toLong(timestamp);
	// long nowTimestamp = new Date().getTime();
	// long sub = nowTimestamp - time;
	// sub = Math.abs(sub);
	// if (sub > timeout) {
	// String message = "签名验证失败.请求链接失效.nowTimestamp[" + nowTimestamp +
	// "], timestamp[" + timestamp + "]";
	// throw new ServiceException(WebApiService.SIGN_CHECK_ERROR, message);
	// }
	//
	// }

	/**
	 * 查询订单
	 */
	@RequestMapping("/webApi/getOrder.do")
	public ModelAndView getOrder(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> map = new HashMap<String, Object>();

		String timestamp = req.getParameter("timestamp");
		String sign = req.getParameter("sign");
		String orderNo = req.getParameter("orderNo");

		int rc = 1000;

		String checkSign = EncryptUtil.getMD5(timestamp + orderNo + crcKey);

		if (!StringUtils.equalsIgnoreCase(sign, checkSign)) {
			rc = 2001;
		} else {
			PaymentOrder paymentOrder = this.paymentOrderDao.get(orderNo);
			if (paymentOrder != null) {
				map.put("order", paymentOrder);
			} else {
				rc = 2002;
			}
		}

		map.put("rc", rc);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}
	
	
	/**
	 * 查询订单
	 */
	@RequestMapping("/webApi/getOrderStatus.do")
	public ModelAndView getOrderStatus(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> map = new HashMap<String, Object>();

		String timestamp = req.getParameter("timestamp");
		String sign = req.getParameter("sign");
		String orderNo = req.getParameter("orderNo");

		int rc = 1000;

		String checkSign = EncryptUtil.getMD5(timestamp + orderNo + crcKey);

		if (!StringUtils.equalsIgnoreCase(sign, checkSign)) {
			rc = 2001;
		} else {
			PaymentOrder paymentOrder = this.paymentOrderDao.get(orderNo);
			if (paymentOrder != null) {
				if(paymentOrder.getStatus() == 1){
					map.put("status","payment success!");
				}else{
					map.put("status","payment fail!");
				}
			} else {
				rc = 2002;
			}
		}

		map.put("rc", rc);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	/**
	 * 补单
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/webApi/fillOrder.do")
	public ModelAndView fillOrder(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> map = new HashMap<String, Object>();

		String timestamp = req.getParameter("timestamp");
		String sign = req.getParameter("sign");
		String orderNo = req.getParameter("orderNo");
		String partnerOrderId = req.getParameter("partnerOrderId");

		int rc = 1000;

		String checkSign = EncryptUtil.getMD5(timestamp + orderNo + partnerOrderId + crcKey);

		if (!StringUtils.equalsIgnoreCase(sign, checkSign)) {
			rc = 2001;
		} else {
			PaymentOrder paymentOrder = this.paymentOrderDao.get(orderNo);
			if (paymentOrder != null) {

				if (paymentOrder.getStatus() == OrderStatus.STATUS_NEW) {
					String orderId = paymentOrder.getOrderId();
					BigDecimal finishAmount = paymentOrder.getAmount();
					int gold = GameApiSdk.getInstance().getSystemGold(paymentOrder.getPartnerId(),paymentOrder.getServerId(), Double.toString(finishAmount.intValue()));
					// 更新订单状态
					if (this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_FINISH, partnerOrderId, finishAmount, "")) {

						// 请求游戏服，发放游戏货币
						if (!GameApiSdk.getInstance().doPayment(paymentOrder.getPartnerId(), paymentOrder.getServerId(), paymentOrder.getPartnerUserId(), "", paymentOrder.getOrderId(), finishAmount,
								gold, "", "compensate")) {
							// 如果失败，要把订单置为未支付
							this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_NOT_PAY, orderId, finishAmount, "");
							LOG.error("发货失败");
							rc = 2003;
						} else {
							LOG.info("支付成功");
						}
					}
				} else {
					rc = 2004;
				}

			} else {
				rc = 2002;
			}
		}

		map.put("rc", rc);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/webApi/createOrder.do")
	public ModelAndView createOrder(HttpServletRequest req, HttpServletResponse res) {
		String timestamp = req.getParameter("timestamp");
		String partnerId = req.getParameter("partnerId");
		String serverId = req.getParameter("serverId");
		String partnerUserId = req.getParameter("partnerUserId");
		String amount = req.getParameter("amount");
		String tradeName = req.getParameter("tradeName");
		String sign = req.getParameter("sign");
		String qn = req.getParameter("esqn");
		String pkgId = req.getParameter("pkgId");

		PartnerService ps = serviceFactory.getBean(partnerId);
		Map<String, Object> map = new HashMap<String, Object>();
		int rc = ServiceReturnCode.SUCCESS;
		try {

			Assert.notEmtpy(timestamp, WebApiService.PARAM_ERROR, "时间戳不能为空");
			Assert.notEmtpy(sign, WebApiService.PARAM_ERROR, "检验key不能为空");
			LOG.info("createOrder,partnerId:" + partnerId + ",amount:" + amount);
			
			String createOrderParams="";
			StringBuilder params = new StringBuilder();
			Enumeration paramNames = req.getParameterNames();
			while ((paramNames != null) && (paramNames.hasMoreElements())) {
				String paramName = (String) paramNames.nextElement();
				String[] values = req.getParameterValues(paramName);
				if ((values == null) || (values.length == 0))
					continue;
				if (values.length == 1)
					params.append(paramName + "=" + values[0] + "&");
				else {
					for (int i = 0; i < values.length; i++) {
						params.append(paramName + "=" + values[i] + "&");
					}
				}
			}
			if (params.length() > 0) {
				createOrderParams = params.substring(0, params.length() - 1);
			}
			
			LOG.info("createOrder params : " + createOrderParams);
			
			// String key = ClientConfig.getProperty("ldsg.webapi.secertKey");

			// this.checkTimestamp(timestamp);

			// String serverSign = EncryptUtil.getMD5((partnerId + serverId +
			// partnerUserId + timestamp + key));
			// /*
			// * if (false && !StringUtils.equalsIgnoreCase(sign, serverSign)) {
			// * String message = "签名验证失败.partnerId[" + partnerId +
			// "], serverId["
			// * + serverId + "], partnerUserId[" + partnerUserId + "], amount["
			// +
			// * amount + "], timestamp[" + timestamp + "], sign[" + sign + "]";
			// * throw new ServiceException(WebApiService.SIGN_CHECK_ERROR,
			// * message); }
			// */

			String data;
			// if (StringUtils.isNotBlank(pkgId)) {
			// //1
			// data = ps.createOrder(partnerId, serverId, partnerUserId, new
			// BigDecimal(amount), tradeName, pkgId);
			// } else if (StringUtils.isEmpty(qn)) {
			// //2
			// data = ps.createOrder(partnerId, serverId, partnerUserId, new
			// BigDecimal(amount), tradeName);
			// } else {
			// 1

			if (pkgId == null) {
				pkgId = "";
			}

			if (qn == null) {
				qn = "";
			}

			data = ps.createOrder(partnerId, serverId, partnerUserId, new BigDecimal(amount), tradeName, pkgId, qn);
			// }
			
			LOG.info("createOrder response jsonData : " + data);

			map.put("data", data);

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

	@RequestMapping(value = "/webApi/payment.do", method = RequestMethod.POST)
	public ModelAndView payment(HttpServletRequest req, HttpServletResponse res) {

		String invoice = req.getParameter("invoice");
		String tradeId = req.getParameter("tradeId");
		String paidFee = req.getParameter("paidFee");
		String tradeStatus = req.getParameter("tradeStatus");
		String payerId = req.getParameter("payerId");
		String appId = req.getParameter("appId");
		String reqFee = req.getParameter("reqFee");
		String sign = req.getParameter("sign");
		String tradeName = req.getParameter("tradeName");
		String notifyDatetime = req.getParameter("notifyDatetime");
		String partnerId = req.getParameter("partnerId");
		PartnerService ps = serviceFactory.getBean(partnerId);

		paymentLog.info("invoice[" + invoice + "], tradeId[" + tradeId + "], paidFee[" + paidFee + "], tradeStatus[" + tradeStatus + "], payerId[" + payerId + "], sign[" + sign + "], tradeName["
				+ tradeName + "], notifyDatetime[" + notifyDatetime + "],partnerId["+partnerId+"]");

		String retVal = "NOT_OK";

		Map<String, String> map = new HashMap<String, String>();
		map.put("invoice", invoice);
		map.put("tradeId", tradeId);
		map.put("paidFee", paidFee);
		map.put("tradeStatus", tradeStatus);
		map.put("payerId", payerId);
		map.put("appId", appId);
		map.put("reqFee", reqFee);
		map.put("tradeName", tradeName);
		map.put("notifyDatetime", notifyDatetime);

		if (Md5SignUtil.doCheck(map, sign, ClientConfig.getProperty("secertKey"))) {

			boolean success = false;
			if (StringUtils.equalsIgnoreCase(tradeStatus, "TRADE_SUCCESS")) {
				success = true;
			}

			BigDecimal finishAmount = new BigDecimal(paidFee);

			if (ps.doPayment(tradeId, payerId, success, invoice, finishAmount, map)) {
				retVal = "OK";
			}

		} else {
			retVal = "ILLEGAL_SIGN";
		}

		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}
	//ios 宜搜
	@RequestMapping(value = "/webApi/paymentIOS.do", method = RequestMethod.POST)
	public ModelAndView paymentIOS(HttpServletRequest req, HttpServletResponse res) {

		String invoice = req.getParameter("invoice");
		String tradeId = req.getParameter("tradeId");
		String paidFee = req.getParameter("paidFee");
		String tradeStatus = req.getParameter("tradeStatus");
		String payerId = req.getParameter("payerId");
		String appId = req.getParameter("appId");
		String reqFee = req.getParameter("reqFee");
		String sign = req.getParameter("sign");
		String tradeName = req.getParameter("tradeName");
		String notifyDatetime = req.getParameter("notifyDatetime");
		String partnerId = req.getParameter("partnerId");
		PartnerService ps = serviceFactory.getBean(appId);//appId映射
		paymentLog.info("invoice[" + invoice + "], tradeId[" + tradeId + "], paidFee[" + paidFee + "], tradeStatus[" + tradeStatus + "], payerId[" + payerId + "], sign[" + sign + "], tradeName["
				+ tradeName + "], notifyDatetime[" + notifyDatetime + "],partnerId["+partnerId+"],servicename:"+ps.getClass().getName());

		String retVal = "NOT_OK";

		Map<String, String> map = new HashMap<String, String>();
		map.put("invoice", invoice);
		map.put("tradeId", tradeId);
		map.put("paidFee", paidFee);
		map.put("tradeStatus", tradeStatus);
		map.put("payerId", payerId);
		map.put("appId", appId);
		map.put("reqFee", reqFee);
		map.put("tradeName", tradeName);
		map.put("notifyDatetime", notifyDatetime);

		if (Md5SignUtil.doCheck(map, sign, ClientConfig.getProperty("IOSSecretKey"))) {

			boolean success = false;
			if (StringUtils.equalsIgnoreCase(tradeStatus, "TRADE_SUCCESS")) {
				success = true;
			}

			BigDecimal finishAmount = new BigDecimal(paidFee);

			if (ps.doPayment(tradeId, payerId, success, invoice, finishAmount, map)) {
				retVal = "OK";
			}

		} else {
			retVal = "ILLEGAL_SIGN";
		}

		ModelAndView modelView = new ModelAndView("empty", "output", retVal);

		return modelView;
	}

	@RequestMapping(value = "/webApi/testPayment.do", method = RequestMethod.GET)
	public ModelAndView testPayment(HttpServletRequest req, HttpServletResponse res) {

		String partnerOrderId = req.getParameter("partnerOrderId");
		String serverId = req.getParameter("serverId");
		String amount = req.getParameter("amount");
		String partnerUserId = req.getParameter("partnerUserId");
		String partnerId = "1004";
		EasouServiceImpl ps = (EasouServiceImpl) serviceFactory.getBean(partnerId);

		int rc = 1000;
		String msg = "充值成功";

		Map<String, String> map = new HashMap<String, String>();
		map.put("partnerOrderId", partnerOrderId);
		map.put("amount", amount);
		map.put("serverId", serverId);
		map.put("partnerUserId", partnerUserId);

		String orderNo = "";

		try {

			checkParam(partnerOrderId, serverId, amount, partnerUserId);

			BigDecimal finishAmount = new BigDecimal(amount);

			orderNo = IDGenerator.getID();
			ps.createOrder(orderNo, partnerId, serverId, partnerUserId, finishAmount, "", "bgf1278_12296_001");

			if (!ps.doPayment(orderNo, partnerUserId, true, partnerOrderId, finishAmount, map)) {
				rc = 2001;
				msg = "充值失败";
			}

		} catch (ServiceException se) {
			LOG.error("T充值失败.code[" + se.getCode() + "], msg[" + se.getMessage() + "]");
			rc = se.getCode();
			msg = se.getMessage();
		}

		Map<String, Object> retVal = new HashMap<String, Object>();

		retVal.put("rc", rc);
		retVal.put("msg", msg);
		retVal.put("orderNo", orderNo);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(retVal);
		modelView.setView(view);
		return modelView;
	}

	private void checkParam(String partnerOrderId, String serverId, String amount, String partnerUserId) {

		if (StringUtils.isEmpty(partnerOrderId)) {
			throw new ServiceException(3001, "partnerOrderId is empty");
		}

		if (StringUtils.isEmpty(serverId)) {
			throw new ServiceException(3001, "serverId is empty");
		}

		if (StringUtils.isEmpty(amount)) {
			throw new ServiceException(3001, "amount is empty");
		}

		if (StringUtils.isEmpty(partnerUserId)) {
			throw new ServiceException(3001, "partnerUserId is empty");
		}
	}
}
