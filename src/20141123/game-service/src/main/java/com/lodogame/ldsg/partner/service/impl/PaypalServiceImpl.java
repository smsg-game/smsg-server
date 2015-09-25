package com.lodogame.ldsg.partner.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.easou.sso.sdk.util.TradeInfo;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.partner.model.PaymentObj;
import com.lodogame.ldsg.partner.service.BasePartnerService;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.sdk.GameApiSdk;

public class PaypalServiceImpl extends BasePartnerService {
	private static final Logger logger = Logger.getLogger(PaypalServiceImpl.class);

	@Override
	public UserToken login(String token, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		checkSign(token, partnerId, serverId, timestamp, sign);

		try {
			return  GameApiSdk.getInstance().loadUserToken(token, partnerId, serverId, "0", params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
		}
	}

	@Override
	public boolean doPayment(String orderId, String partnerUserId, boolean success, String partnerOrderId, BigDecimal finishAmount, Map<String, String> reqInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
//		if (paymentObj == null) {
//			logger.error("paymentObj为空");
//			return false;
//		}
//		PaypalPaymentObj cb = (PaypalPaymentObj) paymentObj;
//		cb = PaypalSdk.instance().checkPayCallbackSign(cb);
//		if (cb == null) {
//			logger.error("签名不正确" + Json.toJson(paymentObj));
//			return false;
//		}
//		logger.info("game encode id:" + cb.getOrderId());
//		String orderId = null;
//		try {
//			PrivateKey privateKey = RSAHelper.getPrivateKey(PaypalSdk.instance().getPrivateKey());
//			// 加解密类
//			Cipher cipher = Cipher.getInstance("RSA");
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			byte[] deBytes = cipher.doFinal(Base64.decode(cb.getOrderId()));
//			orderId = new String(deBytes);
//			logger.info("game decode id:" + orderId);
//		} catch (Exception e) {
//			logger.error("decode error!", e);
//		}
//		PaymentOrder order = paymentOrderDao.get(orderId);
//
//		logger.info("应用订单：" + Json.toJson(order));
//		if (order == null) {
//			logger.error("订单为空：" + Json.toJson(cb));
//			return false;
//		}
//
//		if (order.getStatus() == OrderStatus.STATUS_FINISH) {
//			logger.error("订单已经完成" + Json.toJson(cb));
//			return true;
//		}
//		// 以分为单位
//		BigDecimal finishAmount = order.getAmount();
//		if (!"approved".equals(cb.getConfirm().getJSONObject("response").getString("state"))) {
//			logger.error("充值失败：" + Json.toJson(cb));
//			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, order.getOrderId(), finishAmount, "");
//			return false;
//		}
//
//		int gold = GameApiSdk.getInstance().getSystemGold(order.getPartnerId(),order.getServerId(), Double.toString(finishAmount.intValue()));
//		// 更新订单状态
//		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, orderId, finishAmount, "")) {
//
//			// 请求游戏服，发放游戏货币
//			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), order.getPartnerUserId(), "", order.getOrderId(), finishAmount, gold, "", "")) {
//				// 如果失败，要把订单置为未支付
//				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, orderId, finishAmount, "");
//				logger.error("发货失败：" + Json.toJson(cb));
//				return false;
//			} else {
//				logger.info("支付成功：" + Json.toJson(cb));
//				return true;
//			}
//		}
//		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, orderId, finishAmount, "");
//		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}

	@Override
	public String createOrder(String partnerId, String serverId, String partnerUserId, BigDecimal amount, String tradeName, String pkgId, String qn) {
		pkgId = "sgsg_" + pkgId;
		TradeInfo info = createOrderInfo(partnerId, serverId, partnerUserId, amount, tradeName, pkgId, qn);
//		try {
//			PublicKey publicKey = RSAHelper.getPublicKey(GooGlePlaySdk.instance().getPublicKey());
//			// 加解密类
//			Cipher cipher = Cipher.getInstance("RSA");// Cipher.getInstance("RSA/ECB/PKCS1Padding");
//			// 明文
//			byte[] plainText = info.getTradeId().getBytes();
//			// 加密
//			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//			byte[] enBytes = cipher.doFinal(plainText);
//			info.setTradeId(Base64.encode(enBytes));
//		} catch (Exception e) {
//			logger.error("encode error!", e);
//		}
		return Json.toJson(info);
	}

}
