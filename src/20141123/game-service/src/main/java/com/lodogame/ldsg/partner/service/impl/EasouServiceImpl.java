package com.lodogame.ldsg.partner.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.easou.sso.sdk.AuthAPI;
import com.easou.sso.sdk.service.AuthBean;
import com.easou.sso.sdk.service.EucAPIException;
import com.easou.sso.sdk.service.EucApiResult;
import com.easou.sso.sdk.service.JUser;
import com.easou.sso.sdk.util.ServerBuildUrl;
import com.easou.sso.sdk.util.TradeInfo;
import com.lodogame.game.dao.PaymentOrderDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.OrderStatus;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.Assert;
import com.lodogame.ldsg.partner.model.PaymentObj;
import com.lodogame.ldsg.partner.sdk.EasouSdk;
import com.lodogame.ldsg.partner.service.BasePartnerService;
import com.lodogame.ldsg.sdk.GameApiSdk;
import com.lodogame.ldsg.service.WebApiService;
import com.lodogame.model.PaymentOrder;

public class EasouServiceImpl extends BasePartnerService {

	public static Logger LOG = Logger.getLogger(EasouServiceImpl.class);

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

	@Override
	public String createOrder(String partnerId, String serverId, String partnerUserId, BigDecimal amount, String tradeName, String pkgId, String qn) {

		pkgId = "sgsg_"+pkgId;
		TradeInfo info = createOrderInfo(partnerId, serverId, partnerUserId, amount, tradeName,pkgId,qn);
		info.setNotifyUrl(EasouSdk.instance().getPayBackUrl());
		info.setAppId(EasouSdk.instance().getAppId());
		info.setPartnerId(EasouSdk.instance().getEasouPartnerId());
		
		LOG.info("TradeInfo toJSONString" + JSON.toJSONString(info));
		
		return Json.toJson(info);
	}

	public String createOrder(String orderId, String partnerId, String serverId, String partnerUserId, BigDecimal amount, String tradeName, String qn) {

		Assert.notEmtpy(partnerId, PARAM_ERROR, "合作商ID不能为空");
		Assert.notEmtpy(serverId, PARAM_ERROR, "服务器ID不能为空");
		Assert.notEmtpy(partnerUserId, PARAM_ERROR, "合作商用户ID不能为空");
		Assert.notEmtpy(amount, PARAM_ERROR, "订单金额不能为空");

		String gameId = "ldsg";

		PaymentOrder paymentOrder = new PaymentOrder();

		if (StringUtils.isEmpty(orderId)) {
			orderId = IDGenerator.getID();
		}

		Date now = new Date();

		paymentOrder.setCreatedTime(now);
		paymentOrder.setGameId(gameId);
		paymentOrder.setOrderId(orderId);
		paymentOrder.setPartnerId(partnerId);
		paymentOrder.setPartnerUserId(partnerUserId);
		paymentOrder.setSeq(0);
		paymentOrder.setAmount(amount);
		paymentOrder.setFinishAmount(new BigDecimal(0));
		paymentOrder.setServerId(serverId);
		paymentOrder.setStatus(OrderStatus.STATUS_NEW);
		paymentOrder.setUpdatedTime(now);
		paymentOrder.setQn(qn);

		boolean success = this.paymentOrderDao.add(paymentOrder);
		if (!success) {
			throw new ServiceException(ServiceReturnCode.FAILD, "订单创建失败");
		}

		TradeInfo tradeInfo = new TradeInfo();
		tradeInfo.setExectInfo("");
		tradeInfo.setPayerId(partnerUserId);
		tradeInfo.setReqFee(String.valueOf(amount));
		tradeInfo.setTradeDesc(tradeName);
		tradeInfo.setTradeId(orderId);
		tradeInfo.setTradeName(tradeName);
		tradeInfo.setNotifyUrl(EasouSdk.instance().getPayBackUrl());
		tradeInfo.setAppId(EasouSdk.instance().getAppId());
		tradeInfo.setPartnerId(EasouSdk.instance().getEasouPartnerId());
		tradeInfo.setQn(qn);
		String url = ServerBuildUrl.buildPayUrl(tradeInfo);

		LOG.info(url);

		return url;
	}

	@Override
	public boolean doPayment(String orderId, String partnerUserId, boolean success, String partnerOrderId, BigDecimal finishAmount, Map<String, String> reqInfo) {

		PaymentOrder paymentOrder = this.paymentOrderDao.get(orderId);
		if (paymentOrder == null) {
			LOG.error("订单支付回调失败，订单不存在.orderId[" + orderId + "]");
			return false;
		}

		if (paymentOrder.getStatus() != OrderStatus.STATUS_NEW) {
			LOG.error("订单支付回调失败，订单不在可修改状态.orderId[" + orderId + "]");
			return false;
		}

		String extInfo = "";
		if (reqInfo != null) {
			extInfo = Json.toJson(reqInfo);
		}

		if (success) {// 成功
			int gold = (int) (finishAmount.doubleValue() * 10);

			// 更新订单状态
			if (this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_FINISH, partnerOrderId, finishAmount, extInfo)) {

				// 请求游戏服，发放游戏货币
				if (!GameApiSdk.getInstance().doPayment(paymentOrder.getPartnerId(), paymentOrder.getServerId(), partnerUserId, "", partnerOrderId, finishAmount, gold, "", "")) {
					// 如果失败，要把订单置为未支付
					this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_NOT_PAY, partnerOrderId, finishAmount, extInfo);
					return false;
				}
			}

		} else {
			this.paymentOrderDao.updateStatus(orderId, OrderStatus.STATUS_ERROR, partnerOrderId, finishAmount, extInfo);
		}

		return true;
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		// TODO Auto-generated method stub
		return false;
	}
}
