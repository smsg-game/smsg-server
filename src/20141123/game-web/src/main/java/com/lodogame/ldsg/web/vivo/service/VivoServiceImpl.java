package com.lodogame.ldsg.web.vivo.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.easou.sso.sdk.util.TradeInfo;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.OrderStatus;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.partner.model.PaymentObj;
import com.lodogame.ldsg.partner.service.BasePartnerService;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.sdk.GameApiSdk;
import com.lodogame.ldsg.web.vivo.sdk.VivoPaymentObj;
import com.lodogame.ldsg.web.vivo.sdk.VivoSdk;
import com.lodogame.model.PaymentOrder;


public class VivoServiceImpl extends BasePartnerService {

	private final static Logger logger = Logger.getLogger(VivoServiceImpl.class);
	
	
	@Override
	public UserToken login(String token, String partnerId, String serverId,
			long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

//		checkSign(token, partnerId, serverId, timestamp, sign);
		
		logger.info("Vivo token:" + token);
		try {
		String uid = VivoSdk.instance().getUid(token);
		if (uid != null) {
			UserToken userToken = GameApiSdk.getInstance().loadUserToken(uid, partnerId, serverId, "0", params);
			return userToken;
		}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
		}

		throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
	}

	@Override
	public boolean doPayment(String orderId, String partnerUserId,
			boolean success, String partnerOrderId, BigDecimal finishAmount,
			Map<String, String> reqInfo) {
		
		return false;
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}

		VivoPaymentObj cb = (VivoPaymentObj) paymentObj;
		if (!VivoSdk.instance().checkPayCallbackSign(cb)) {
			logger.error("签名不正确" + Json.toJson(paymentObj));
			return true;
		}
		
		logger.info("game id:" + cb.getCpOrderNumber());
		PaymentOrder order = paymentOrderDao.get( cb.getCpOrderNumber());

		logger.info("应用订单：" + Json.toJson(order));
		if (order == null) {
			logger.error("订单为空：" + Json.toJson(cb));
			return false;
		}

		if (order.getStatus() == OrderStatus.STATUS_FINISH) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}
		
		// 以分为单位
		BigDecimal finishAmount = new BigDecimal(cb.getOrderAmount()).divide(new BigDecimal(100));
		if (!cb.getTradeStatus().equals("0000")) {
			logger.error("充值失败：" + Json.toJson(cb));
			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getOrderNumber(), finishAmount, "");
			return true;
		}

		int gold = (int) (order.getAmount().doubleValue() * 10);
//		// 更新订单状态
//		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getVivoOrder(), finishAmount, "")) {
//			GameServer gameServer = serverListDao.getServerByServerIdAndPartenerId(order.getServerId(), order.getPartnerId());
//			// 请求游戏服，发放游戏货币
//			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), order.getPartnerUserId(), "", order.getOrderId(), finishAmount, gold, "", "",gameServer)) {
//				// 如果失败，要把订单置为未支付
//				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getVivoOrder(), finishAmount, "");
//				logger.error("发货失败：" + Json.toJson(cb));
//				return false;
//			} else {
//				logger.info("支付成功：" + Json.toJson(cb));
//				return true;
//			}
//		}
//		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getVivoOrder(), finishAmount, "");
//		logger.error("充值失败：" + Json.toJson(cb));		
//		return false;
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getOrderNumber(),
				finishAmount, "")) {

			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), order.getPartnerUserId(),
					"", order.getOrderId(), finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getOrderNumber(),
						finishAmount, "");
				logger.error("发货失败：" + Json.toJson(cb));
				return false;
			} else {
				logger.info("支付成功：" + Json.toJson(cb));
				return true;
			}
		}
		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getOrderNumber(), finishAmount,
				"");
		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}
	
	@Override
	public String createOrder(String partnerId, String serverId,
			String partnerUserId, BigDecimal amount, String tradeName,String pkgId, String qn) {
		String tradeNameDecode = tradeName;
		try {
			tradeNameDecode = new String(tradeNameDecode.getBytes("ISO_8859-1"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		logger.info("Vivo createOrder tradeNameDecode = " + tradeNameDecode);
		TradeInfo info = createOrderInfo(partnerId, serverId, partnerUserId,
				amount, tradeName,pkgId,qn);
		info.setNotifyUrl("http://wapi.andr.sg.xworldgame.com:8088/webApi/vivoPayment.do");
//		info.setNotifyUrl("http://203.195.190.121:8089/webApi/vivoPayment.do");
		logger.info("vivo orderInfo:" + info.getNotifyUrl());
		createServerOrder(info);
		return Json.toJson(info);
	}
	
//	@Override
//	public String createOrder(String partnerId, String serverId,
//			String partnerUserId, BigDecimal amount, String tradeName, String qn) {
//		String tradeNameDecode = tradeName;
//		try {
//			tradeNameDecode = new String(tradeNameDecode.getBytes("ISO_8859-1"));
//		} catch (UnsupportedEncodingException e) {
//			logger.error(e);
//		}
//		logger.info("Vivo createOrder tradeNameDecode = " + tradeNameDecode);
//		TradeInfo info = createOrderInfo(partnerId, serverId, partnerUserId,
//				amount, tradeName, qn);
//		info.setNotifyUrl("http://wapi.andr.hw.fantingame.com:80/webApi/vivoPayment.do");
////		info.setNotifyUrl("http://203.195.190.121:8089/webApi/vivoPayment.do");
//		logger.info("vivo orderInfo:" + info.getNotifyUrl());
//		
//		createServerOrder(info);
//		
//		return Json.toJson(info);
//	}
	
	private void createServerOrder(TradeInfo info) {
		VivoSdk.instance().createServerOrder(info);
	}

}
