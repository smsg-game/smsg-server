package com.lodogame.ldsg.partner.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.easou.sso.sdk.util.TradeInfo;
import com.lodogame.game.dao.ActiveCodeDao;
import com.lodogame.game.dao.PackageExtinfoDao;
import com.lodogame.game.dao.PackageInfoDao;
import com.lodogame.game.dao.PackageSettingsDao;
import com.lodogame.game.dao.PaymentOrderDao;
import com.lodogame.game.dao.ServerStatusDao;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.OrderStatus;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.Assert;
import com.lodogame.ldsg.helper.PaymentOrderHelper;
import com.lodogame.ldsg.service.WebApiService;
import com.lodogame.model.DataFileSign;
import com.lodogame.model.PackageExtinfo;
import com.lodogame.model.PackageInfo;
import com.lodogame.model.PackageInfoBO;
import com.lodogame.model.PackageSettings;
import com.lodogame.model.PaymentOrder;
import com.lodogame.model.ServerStatus;

public abstract class BasePartnerService implements PartnerService {

	private static final Logger LOG = Logger.getLogger(BasePartnerService.class);

	@Autowired
	protected ActiveCodeDao activeCodeDao;

	@Autowired
	protected PackageInfoDao packageInfoDao;

	@Autowired
	protected PackageSettingsDao packageSettingsDao;

	@Autowired
	protected PackageExtinfoDao packageExtinfoDao;

	@Autowired
	protected PaymentOrderDao paymentOrderDao;

	@Autowired
	protected ServerStatusDao serverStatusDao;

	@Override
	public boolean isActive(String uuid, String partnerId) {

		if (StringUtils.isBlank(uuid) || StringUtils.isBlank(partnerId)) {
			throw new ServiceException(WebApiService.PARAM_ERROR, "参数不正确");
		}

		return this.activeCodeDao.isActive(uuid, partnerId);
	}

	@Override
	public boolean active(String uuid, String code, String partnerId) {

		if (StringUtils.isBlank(uuid)) {
			throw new ServiceException(WebApiService.PARAM_ERROR, "参数不正确");
		}

		return this.activeCodeDao.active(uuid, code, partnerId);
	}

	@Override
	public String createOrder(String partnerId, String serverId, String partnerUserId, BigDecimal amount, String tradeName, String pkgId, String qn) {
		TradeInfo tradeInfo = createOrderInfo(partnerId, serverId, partnerUserId, amount, tradeName, pkgId, qn);
		return Json.toJson(tradeInfo);
	}

	protected TradeInfo createOrderInfo(String partnerId, String serverId, String partnerUserId, BigDecimal amount, String tradeName, String pkgId, String qn) {
		Assert.notEmtpy(partnerId, PARAM_ERROR, "合作商ID不能为空");
		Assert.notEmtpy(serverId, PARAM_ERROR, "服务器ID不能为空");
		Assert.notEmtpy(partnerUserId, PARAM_ERROR, "合作商用户ID不能为空");
		Assert.notEmtpy(amount, PARAM_ERROR, "订单金额不能为空");

		String gameId = "ldsg";

		PaymentOrder paymentOrder = new PaymentOrder();
		PaymentOrder lastPaymentOrder = this.paymentOrderDao.getLastOrder(gameId, partnerId);
		int seq = 1;
		if (lastPaymentOrder != null) {
			seq = lastPaymentOrder.getSeq() + 1;
		}
		String orderId = PaymentOrderHelper.getOrderId(gameId, partnerId, seq);

		Date now = new Date();

		paymentOrder.setCreatedTime(now);
		paymentOrder.setGameId(gameId);
		paymentOrder.setOrderId(orderId);
		paymentOrder.setPartnerId(partnerId);
		paymentOrder.setPartnerUserId(partnerUserId);
		paymentOrder.setSeq(seq);
		paymentOrder.setAmount(amount);
		paymentOrder.setFinishAmount(new BigDecimal(0));
		paymentOrder.setServerId(serverId);
		paymentOrder.setStatus(OrderStatus.STATUS_NEW);
		paymentOrder.setUpdatedTime(now);
        String json=Json.toJson(paymentOrder);
        LOG.info("创建的订单信息"+json);
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
		tradeInfo.setQn(qn);
		tradeInfo.setPkgId(pkgId);
		return tradeInfo;
	}

	@Override
	public PackageInfoBO checkVersion(String version, String fr, String mac, String partnerId, String ip) {

		checkServerOpen(fr, mac, partnerId, ip);
		checkVersionExpire(version, partnerId);

		int apkBigVer = 0;
		int apkSmallVer = 0;
		int resBigVer = 0;
		int resSmallVer = 0;
		// 版本参数是否合法
		boolean versionInvalid = false;
		if (version != null && !StringUtils.isBlank(version)) {
			String[] verArr = version.split("\\.");
			if (verArr.length == 4) {
				apkBigVer = Integer.parseInt(verArr[0]);
				apkSmallVer = Integer.parseInt(verArr[1]);
				resBigVer = Integer.parseInt(verArr[2]);
				resSmallVer = Integer.parseInt(verArr[3]);
			} else {
				versionInvalid = true;
			}
		} else {
			versionInvalid = true;
		}

		if (versionInvalid) {
			throw new ServiceException(PARAM_ERROR, "版本参数错误");
		}

		PackageInfo apkPackage = packageInfoDao.getLast(0, partnerId);
		PackageInfo resPackage = packageInfoDao.getLast(1, partnerId);
		PackageInfoBO bo = new PackageInfoBO();
		bo.setVersion("");
		bo.setApkUrl("");
		bo.setResUrl("");
		bo.setDescribe("");

		if (apkPackage == null && resPackage == null) {
			return bo;
		}

		// 检测APK包升级
		if (apkPackage != null) {
			if (apkPackage.getIsTest() == 1) {
				String arkFrs = apkPackage.getFrs();
				// 用户不在灰度测试范围内，则获取普通的最后一个包
				if (StringUtils.isBlank(fr) || !arkFrs.contains(fr)) {
					apkPackage = packageInfoDao.getLastByTest(0, 0, partnerId);
				}
			}

			// 版本小于当前版本，表示APK包为旧的，则直接返回APK包内容
			if (apkPackage != null && needUpgradeApk(apkBigVer, apkSmallVer, apkPackage.getVersion())) {
				bo.setVersion(apkPackage.getVersion());
				bo.setApkUrl(apkPackage.getFullUrl());
				bo.setDescribe(apkPackage.getDescription());
				return bo;
			}
		}

		// 检测资源包升级
		if (resPackage != null && resPackage.getIsTest() == 1) {
			String resFrs = resPackage.getFrs();
			// 用户不在灰度测试范围内，则获取普通的最后一个包
			if (StringUtils.isBlank(fr) || !resFrs.contains(fr)) {
				resPackage = packageInfoDao.getLastByTest(1, 0, partnerId);
			}
		}

		if (resPackage == null) {
			return bo;
		}

		// 资源版本大于当前版本，无需升级，则直接返回
		if (!needUpgradeRes(resBigVer, resSmallVer, resPackage.getVersion())) {
			bo.setVersion(resPackage.getVersion());
			return bo;
		}

		String versions = resPackage.getVersions();

		// 如果兼容版本为空，则直接返回完整包URL
		if (StringUtils.isBlank(versions)) {
			bo.setVersion(resPackage.getVersion());
			bo.setResUrl(resPackage.getFullUrl());
			return bo;
		}

		// 判断是否为兼容包
		String[] versionArr = versions.split(",");
		boolean isSupportVersion = false;
		for (int i = 0; i < versionArr.length; i++) {
			if (versionArr[i].equals(version)) {
				isSupportVersion = true;
				break;
			}
		}

		if (isSupportVersion) {
			bo.setResUrl(resPackage.getUpgradeUrl());
		} else {
			bo.setResUrl(resPackage.getFullUrl());
		}

		return bo;
	}

	/**
	 * 比较APK版本号，是否需要升级APK
	 * 
	 * @param apkBigVer
	 * @param apkSmallVer
	 * @param fullVer
	 * @return
	 */
	protected boolean needUpgradeApk(int apkBigVer, int apkSmallVer, String fullVer) {
		String[] verArr = fullVer.split("\\.");
		int apkBigVerTmp = Integer.parseInt(verArr[0]);
		int apkSmallVerTmp = Integer.parseInt(verArr[1]);
		return apkBigVer < apkBigVerTmp || (apkBigVer == apkBigVerTmp && apkSmallVer < apkSmallVerTmp);
	}

	/**
	 * 比较资源包版本号
	 * 
	 * @param resBigVer
	 * @param resSmallVer
	 * @param fullVer
	 * @return
	 */
	protected boolean needUpgradeRes(int resBigVer, int resSmallVer, String fullVer) {
		String[] verArr = fullVer.split("\\.");
		int resBigVerTmp = Integer.parseInt(verArr[2]);
		int resSmallVerTmp = Integer.parseInt(verArr[3]);
		return resBigVer < resBigVerTmp || (resBigVer == resBigVerTmp && resSmallVer < resSmallVerTmp);
	}

	protected void checkVersionExpire(String version, String partnerId) {

		PackageExtinfo info = packageExtinfoDao.getByVersion(version, partnerId);
		if (info != null && info.getIsExpire() == 1) {
			PackageSettings settings = packageSettingsDao.get();
			throw new ServiceException(WebApiService.VERSION_EXPIRE, settings.getExpirePackageMessage());
		}
	}

	/**
	 * 判断能否进游戏
	 * 
	 * @param imei
	 */
	protected void checkServerOpen(String imei, String mac, String partnerId, String ip) {

		ServerStatus serverStatus = this.serverStatusDao.getServerStatus(partnerId);

		if (serverStatus != null && serverStatus.getStatus() == 1) {

			LOG.info("imei[" + imei + "], " + "mac[" + mac + "], partnerId[" + partnerId + "], ip[" + ip + "]");

			if (this.activeCodeDao.isBlackImei(imei, partnerId)) {
				return;
			}

			if (this.activeCodeDao.isBlackImei(mac, partnerId)) {
				return;
			}

			if (this.serverStatusDao.isWhiteIp(partnerId, ip)) {
				return;
			}

			throw new ServiceException(WebApiService.SERVER_CLOSE, Config.ins().getServerCloseNotice());
		}
	}

	protected void checkSign(String token, String partnerId, String serverId, long timestamp, String sign) {
		String serverSign = EncryptUtil.getMD5((token + partnerId + serverId + timestamp + Config.ins().getSignKey()));
		System.out.println(serverSign.toLowerCase());
		System.out.println(sign.toLowerCase());
		if (!serverSign.toLowerCase().equals(sign.toLowerCase())) {
			throw new ServiceException(WebApiService.SIGN_CHECK_ERROR, "签名校验失败");
		}
	}

	@Override
	public String checkDataFile(String fListStr, String fr, String partnerId) {
		String dataUrl = "";
		if (StringUtils.isBlank(fListStr) || StringUtils.isBlank(fr)) {
			return "";
		}
		List<DataFileSign> list = null;

		// 如果在白名单内，则返回测试的签名列表
		// if(this.activeCodeDao.isBlackImei(fr, partnerId)){
		// list = dataFileSignDao.getSignList(DataFileSignType.TEST_SIGN);
		// if(list == null || list.isEmpty()){
		// list = dataFileSignDao.getSignList(DataFileSignType.NO_TEST_SIGN);
		// }
		// }else{
		// list = dataFileSignDao.getSignList(DataFileSignType.NO_TEST_SIGN);
		// }

		return dataUrl;
	}
}
