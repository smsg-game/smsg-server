package com.lodogame.ldsg.partner.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.easou.sso.sdk.util.TradeInfo;
import com.lodogame.game.utils.MD5;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.OrderStatus;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.partner.model.PaymentObj;
import com.lodogame.ldsg.partner.model.mol.MolPaymentObj;
import com.lodogame.ldsg.partner.sdk.MolSdk;
import com.lodogame.ldsg.partner.service.BasePartnerService;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.sdk.GameApiSdk;
import com.lodogame.model.PaymentOrder;

public class MolServiceImpl extends BasePartnerService {
	private static final Logger logger = Logger.getLogger(MolServiceImpl.class);

	@Override
	public UserToken login(String token, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(PartnerService.PARAM_ERROR, "参数不正确");
		}

		checkSign(token, partnerId, serverId, timestamp, sign);

		try {
			String[] tokenArr = token.split(":");
			String uid = tokenArr[0];
			String session = tokenArr[1];

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
		}

		throw new ServiceException(PartnerService.LOGIN_VALID_FAILD, "登录验证失败");
	}

	@Override
	public boolean doPayment(String orderId, String partnerUserId, boolean success, String partnerOrderId, BigDecimal finishAmount, Map<String, String> reqInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doPayment(PaymentObj paymentObj) {
		if (paymentObj == null) {
			logger.error("paymentObj为空");
			return false;
		}

		MolPaymentObj cb = (MolPaymentObj) paymentObj;

		if (!MolSdk.instance().checkPayCallbackSign(cb)) {
			logger.error("签名不正确" + Json.toJson(paymentObj));
			return false;
		}

		PaymentOrder order = paymentOrderDao.get(cb.getCustomerId());

		logger.info("应用订单：" + Json.toJson(order));
		if (order == null) {
			logger.error("订单为空：" + Json.toJson(cb));
			return false;
		}

		if (order.getStatus() != OrderStatus.STATUS_NEW) {
			logger.error("订单已经完成" + Json.toJson(cb));
			return true;
		}

		BigDecimal finishAmount = new BigDecimal(cb.getAmount()).divide(new BigDecimal(100));

		if (order.getAmount().compareTo(finishAmount) != 0) {
			logger.error("订单金额不符：" + Json.toJson(cb));
			return false;
		}

		if (!cb.getPaymentStatusCode().equals("00")) {
			logger.error("充值失败：" + Json.toJson(cb));
			this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getPaymentId(), finishAmount, "");
			return false;
		}
		String extInfo = JSONObject.toJSONString(paymentObj);

		int gold = GameApiSdk.getInstance().getSystemGold(order.getPartnerId(),order.getServerId(), Double.toString(finishAmount.intValue()));
		// 更新订单状态
		if (this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_FINISH, cb.getPaymentId(), finishAmount, extInfo)) {

			// 请求游戏服，发放游戏货币
			if (!GameApiSdk.getInstance().doPayment(order.getPartnerId(), order.getServerId(), order.getPartnerUserId(), "", order.getOrderId(), finishAmount, gold, "", "")) {
				// 如果失败，要把订单置为未支付
				this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_NOT_PAY, cb.getPaymentId(), finishAmount, extInfo);
				logger.error("发货失败：" + Json.toJson(cb));
				return false;
			} else {
				logger.info("支付成功：" + Json.toJson(cb));
				return true;
			}
		}
		this.paymentOrderDao.updateStatus(order.getOrderId(), OrderStatus.STATUS_ERROR, cb.getPaymentId(), finishAmount, "");
		logger.error("充值失败：" + Json.toJson(cb));
		return false;
	}
	
	
	public String generalPaymentUrl(String orderId){
		if(StringUtils.isNotBlank(orderId)){
			PaymentOrder order = paymentOrderDao.get(orderId);
			if(order != null){
				try {
					String uriAPI = "https://api.mol.com/payout/payments";
					HttpPost httpPost = new HttpPost(uriAPI); // 建立HTTP
					String crcKey = "$#@$%%eweqwlkfef";
					String tiemstmpStr = new Date().getTime() + "";
					String sign = MD5.MD5Encode(tiemstmpStr + orderId + crcKey);
					int amount = order.getAmount().multiply(new BigDecimal(100)).intValue();
					String applicationCode = MolSdk.instance().getApplicationCode();
					String secretKey = MolSdk.instance().getAppSecret();
					String returnUrl = "http://wapi.sg.fantingame.com:8088/webApi/getOrderStatus.do"+ "?&timestamp="+ tiemstmpStr+ "&sign="+ sign+ "&orderNo=" + orderId;
					int gold = GameApiSdk.getInstance().getAllSystemGold(order.getPartnerId(),order.getServerId(),Double.toString(order.getAmount().doubleValue()));
					String desc = "充值"+gold+"元宝";
					String currencyCode = "USD";
					String version = "v1";
					String channelId = "1";
					String signValue = MD5.MD5Encode(amount + applicationCode + channelId
							+ currencyCode + orderId + desc + orderId
							+returnUrl + version + secretKey,"UTF-8");
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("applicationCode",
							applicationCode));
					parameters.add(new BasicNameValuePair("referenceId", orderId));
					parameters.add(new BasicNameValuePair("version", version));
					parameters.add(new BasicNameValuePair("channelId", channelId));
					parameters.add(new BasicNameValuePair("amount", amount + ""));
					parameters.add(new BasicNameValuePair("currencyCode", currencyCode));
					parameters.add(new BasicNameValuePair("returnUrl", returnUrl));
					parameters.add(new BasicNameValuePair("description",desc));
					parameters.add(new BasicNameValuePair("customerId", orderId));
					parameters.add(new BasicNameValuePair("signature", signValue));
					HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
					httpPost.setEntity(entity);
					HttpClient client = new DefaultHttpClient();
					client.getParams()
							.setParameter(
									CoreConnectionPNames.CONNECTION_TIMEOUT,
									5000);// 连接时间
					HttpResponse response = client.execute(httpPost);
					if(response.getStatusLine().getStatusCode() == 200){
						String resstr = EntityUtils.toString(response.getEntity());
						JSONObject jObject = JSONObject.parseObject(resstr);
						return jObject.getString("paymentUrl");
					}
				} catch (Exception e) {
					logger.error("general paymentUrl error!",e);
				}
			}
		}
		return null;
	}

	// @Override
	// public String createOrder(String partnerId, String serverId, String
	// partnerUserId, BigDecimal amount, String tradeName) {
	// TradeInfo info = createOrderInfo(partnerId, serverId, partnerUserId,
	// amount, tradeName);
	// // 以分为单位
	// // info.setReqFee(Integer.toString(new BigDecimal(100).multiply(new
	// // BigDecimal(info.getReqFee())).intValue()));
	// return Json.toJson(info);
	// }

}
