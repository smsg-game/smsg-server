package com.lodogame.ldsg.partner.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.easou.sso.sdk.AuthAPI;
import com.easou.sso.sdk.service.AuthBean;
import com.easou.sso.sdk.service.EucAPIException;
import com.easou.sso.sdk.service.EucApiResult;
import com.easou.sso.sdk.service.JUser;
import com.easou.sso.sdk.util.TradeInfo;
import com.lodogame.game.dao.PaymentOrderDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.OrderStatus;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.partner.model.PaymentObj;
import com.lodogame.ldsg.partner.model.mobilemm.MobileMMPaymentObj;
import com.lodogame.ldsg.partner.sdk.MobileMMSdk;
import com.lodogame.ldsg.partner.service.BasePartnerService;
import com.lodogame.ldsg.sdk.GameApiSdk;
import com.lodogame.ldsg.service.WebApiService;
import com.lodogame.model.PaymentOrder;

public class MobileMMServiceImpl extends BasePartnerService {

	public static Logger logger = Logger.getLogger(MobileMMServiceImpl.class);

	@Autowired
	private PaymentOrderDao paymentOrderDao;

	@Override
	public UserToken login(String token, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(WebApiService.PARAM_ERROR, "参数不正确");
		}

		checkSign(token, partnerId, serverId, timestamp, sign);

		try {
			EucApiResult<AuthBean> result = AuthAPI.validate(token, null, null);
			JUser juser = null;
			if (result == null || result.getResult() == null) {
				if (juser == null) {
					// TODO 测试代码
					throw new ServiceException(WebApiService.LOGIN_VALID_FAILD, "登录验证失败");
				}
			}
			juser = result.getResult().getUser();
			return GameApiSdk.getInstance().loadUserToken(juser.getId().toString(), partnerId, serverId, juser.getQn(), params);
		} catch (EucAPIException e) {
			throw new ServiceException(WebApiService.LOGIN_VALID_FAILD, "登录验证失败");
		}

	}
	
//	@Override
//	public String createOrder(String partnerId, String serverId, String partnerUserId, BigDecimal amount, String tradeName,String pkgId) {
//		TradeInfo info = createOrderInfo(partnerId, serverId, partnerUserId, amount, tradeName,pkgId);
//		return Json.toJson(info);
//	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		//logger.info("订单信息是:"+Json.toJson(paymentObj));
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}

		MobileMMPaymentObj cb = (MobileMMPaymentObj) paymentObj;
		if (!MobileMMSdk.instance().checkPayCallbackSign(cb)) {
			logger.error("签名不正确" + Json.toJson(paymentObj));
			return false;
		}
		logger.info("game id:" + cb.getExData());
		PaymentOrder order = paymentOrderDao.get(cb.getExData());
		logger.info("应用订单：" + Json.toJson(order));
		if (order == null) {
			logger.error("订单为空：" + Json.toJson(cb));
			return false;
		}

		if (order.getStatus() == OrderStatus.STATUS_FINISH) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}

		BigDecimal finishAmount = new BigDecimal(cb.getTotalPrice()).divide(new BigDecimal(100));
		int gold = (int) (finishAmount.doubleValue() * 10);
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH,cb.getOrderID(), finishAmount, "")) {

			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), order.getPartnerUserId(), cb.getOrderID(), order.getOrderId(), finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY,cb.getOrderID(), finishAmount, "");
				logger.error("发货失败：" + Json.toJson(cb));
				return false;
			} else {
				logger.info("支付成功：" + Json.toJson(cb));
				return true;
			}
		}
		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR,cb.getOrderID(), finishAmount, "");
		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}

	@Override
	public boolean doPayment(String orderId, String partnerUserId,
			boolean success, String partnerOrderId, BigDecimal finishAmount,
			Map<String, String> reqInfo) {
		// TODO Auto-generated method stub
		return false;
	}
}
