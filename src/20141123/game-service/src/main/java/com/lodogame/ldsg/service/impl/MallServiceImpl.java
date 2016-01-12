package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemMallDao;
import com.lodogame.game.dao.SystemMallDiscountDao;
import com.lodogame.game.dao.UserMallInfoDao;
import com.lodogame.game.dao.UserMallLogDao;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.SystemMallBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.constants.MallType;
import com.lodogame.ldsg.constants.MoneyType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.MallService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemMall;
import com.lodogame.model.SystemMallDiscountActivity;
import com.lodogame.model.SystemMallDiscountItems;
import com.lodogame.model.User;
import com.lodogame.model.UserMallInfo;
import com.lodogame.model.UserMallLog;

public class MallServiceImpl implements MallService {

	private static final Logger LOG = Logger.getLogger(MallServiceImpl.class);

	@Autowired
	private SystemMallDiscountDao systemMallDiscountDao;

	@Autowired
	private SystemMallDao systemMallDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserMallLogDao userMallLogDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private UserMallInfoDao userMallInfoDao;

	@Override
	public List<SystemMallBO> getMallList(String userId, int type) {
		List<SystemMallDiscountItems> discountItems = new ArrayList<SystemMallDiscountItems>();

		SystemMallDiscountActivity activity = checkDiscountIsOpen();
		if (activity != null) {
			discountItems = systemMallDiscountDao.getDiscountItems(activity.getActivityId());
		}

		List<SystemMall> systemMallList = this.systemMallDao.getList();

		List<SystemMallBO> systemMallBOList = new ArrayList<SystemMallBO>();

		Date now = new Date();

		for (SystemMall systemMall : systemMallList) {

			SystemMallBO bo = BOHelper.createSystemMallBO(systemMall);

			bo.setDailyRemainder(-1);
			bo.setTotalRemainder(-1);

			if (type == MallType.MALL_TYPE_NORMAL) {

				if (!systemMall.getTag().equals("1") && !systemMall.getTag().equals("2")) {// 普通商城
					continue;
				}

			} else if (type == MallType.MALL_TYPE_VIP) {// VIP商城
				if (!systemMall.getTag().equals("3")) {
					continue;
				}
			}

			if (systemMall.getDailyMaxNum() > 0 || systemMall.getMaxNum() > 0) {

				UserMallInfo userMallInfo = this.userMallInfoDao.get(userId, systemMall.getMallId());
				int dayBuyNum = 0;
				int totalBuyNum = 0;
				if (userMallInfo != null) {
					totalBuyNum = userMallInfo.getTotalBuyNum();
					if (DateUtils.isSameDay(userMallInfo.getUpdatedTime(), now)) {
						dayBuyNum = userMallInfo.getDayBuyNum();
					}
				}

				if (systemMall.getDailyMaxNum() > 0) {
					bo.setDailyRemainder(systemMall.getDailyMaxNum() - dayBuyNum);
				}

				if (systemMall.getMaxNum() > 0) {
					bo.setTotalRemainder(systemMall.getMaxNum() - totalBuyNum);
				}
			}

			if (activity != null) { // 如果在打折活动期间，则设置折扣率，没有折扣的商品折折扣率设置为100
				bo.setDiscount(getDiscount(discountItems, systemMall.getMallId()));
			} else {
				bo.setDiscount(100); // 不在打折活动期间，所有商品折扣率设置为100
			}

			systemMallBOList.add(bo);
		}

		return systemMallBOList;
	}

	@Override
	public CommonDropBO buy(String userId, int mallId, int num, int discount) {

		SystemMall systemMall = this.systemMallDao.get(mallId);

		User user = userService.get(userId);

		UserMallInfo userMallInfo = this.userMallInfoDao.get(userId, systemMall.getMallId());
		int dayBuyNum = 0;
		int totalBuyNum = 0;
		if (userMallInfo != null) {
			totalBuyNum = userMallInfo.getTotalBuyNum();
			if (DateUtils.isSameDay(userMallInfo.getUpdatedTime(), new Date())) {
				dayBuyNum = userMallInfo.getDayBuyNum();
			}
		}

		this.checkBuyCondition(userId, num, systemMall, totalBuyNum, dayBuyNum);

		// 需要的钱
		int needMoney = this.checkDiscount(userId, mallId, systemMall.getAmount(), num, discount);

		int moneyType = systemMall.getMoneyType();
		this.reduceMoney(userId, moneyType, needMoney, user.getLevel());

		int toolType = systemMall.getToolType();
		int toolId = systemMall.getToolId();
		int toolNum = systemMall.getToolNum() * num;

		CommonDropBO commonDropBO = new CommonDropBO();

		List<DropToolBO> dropToolBOList = this.toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_BUY_MALL);

		for (DropToolBO dropToolBO : dropToolBOList) {
			this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
		}

		// 做日志
		this.buyMallLog(userId, user.getLevel(), mallId, moneyType, num, toolNum, needMoney, totalBuyNum, dayBuyNum);

		return commonDropBO;
	}

	private void buyMallLog(String userId, int userLevel, int mallId, int moneyType, int num, int toolNum, int needMoney, int totalBuyNum, int dayBuyNum) {

		// 做购买日志
		UserMallLog userMallLog = new UserMallLog();
		userMallLog.setUserId(userId);
		userMallLog.setMallId(mallId);
		userMallLog.setNum(toolNum);
		userMallLog.setCreatedTime(new Date());
		this.userMallLogDao.add(userMallLog);
		// 插入运营日志
		if (moneyType == MoneyType.GOLD) {
			unSynLogService.userMallLog(userId, userLevel, mallId, num, needMoney, 0, new Date());
		} else {
			unSynLogService.userMallLog(userId, userLevel, mallId, num, 0, needMoney, new Date());
		}

		// 购买次数记录
		this.userMallInfoDao.add(userId, mallId, totalBuyNum + num, dayBuyNum + num);
	}

	private void checkBuyCondition(String userId, int num, SystemMall systemMall, int totalBuyNum, int dayBuyNum) {

		if (systemMall == null) {
			String message = "购买商品出错，商品不存在.userId[" + userId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		int mallId = systemMall.getMallId();

		if (num < 1 || num>100) {
			String message = "购买商品出错，商品数量异常.userId[" + userId + "], num[" + num + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (systemMall.getDailyMaxNum() >= 0 && dayBuyNum + num > systemMall.getDailyMaxNum()) {
			String message = "购买商品出错，购买数量超出当日购买限额.userId[" + userId + "], maillId[" + mallId + "]";
			LOG.error(message);
			throw new ServiceException(BUY_MALL_TIME_NOT_ENOUGH, message);
		}

		if (systemMall.getMaxNum() >= 0 && totalBuyNum + num > systemMall.getMaxNum()) {
			String message = "购买商品出错，购买数量超出总的购买限额.userId[" + userId + "], maillId[" + mallId + "]";
			LOG.error(message);
			throw new ServiceException(BUY_MALL_TOTAL_TIME_NOT_ENOUGH, message);
		}

		if (systemMall.getNeedVipLevel() > 0) {// vip等级不足
			UserBO userBO = this.userService.getUserBO(userId);
			if (userBO.getVipLevel() < systemMall.getNeedVipLevel()) {
				String message = "购买商品出错，vip等级不足.userId[" + userId + "], maillId[" + mallId + "]";
				LOG.error(message);
				throw new ServiceException(BUY_MALL_VIP_LEVEL_NOT_ENOUGH, message);
			}
		}

	}

	/**
	 * 判断打折，返回需要的总价
	 * 
	 * @param userId
	 * @param mallId
	 * @param amount
	 * @param num
	 * @param clientDiscount
	 * @return
	 */
	public int checkDiscount(String userId, int mallId, int amount, int num, int clientDiscount) {

		// 检查是否在打折活动期间，是的话按照折扣率和客户端发送来的折扣率比较。 如果不在打折期间或者该物品没有折扣，则服务端默认为折扣率为100

		int needMoney = 0;
		int serverDiscount = 100;// 服务端的折扣

		SystemMallDiscountActivity activity = checkDiscountIsOpen();

		if (activity != null) {

			SystemMallDiscountItems discountItem = systemMallDiscountDao.getDiscountItem(activity.getActivityId(), mallId);

			if (discountItem != null) {
				serverDiscount = discountItem.getDiscount();
			}

		}

		if (serverDiscount != clientDiscount) {
			String message = "购买商品出错，服务端和客户端的折扣率不相同.userId[" + userId + "] mallId[" + mallId + "] discount[" + clientDiscount + "]";
			LOG.error(message);
			throw new ServiceException(DISCOUNT_NOT_MATCH, message);
		}

		needMoney = (int) (amount * serverDiscount * 0.01) * num;

		return needMoney;

	}

	/**
	 * 购买商品扣钱
	 * 
	 * @param userId
	 * @param moneyType
	 * @param needMoney
	 */
	public void reduceMoney(String userId, int moneyType, int needMoney, int userLevel) {

		if (moneyType == MoneyType.GOLD) {
			if (!this.userService.reduceGold(userId, needMoney, ToolUseType.REDUCE_BUY_MALL, userLevel)) {
				String message = "购买商品出错，金币不足.userId[" + userId + "], moneyType[" + moneyType + "]";
				LOG.error(message);
				throw new ServiceException(BUY_MALL_GOLD_NOT_ENOUGH, message);
			}
		} else if (moneyType == MoneyType.COPPER) {
			if (!this.userService.reduceCopper(userId, needMoney, ToolUseType.REDUCE_BUY_MALL)) {
				String message = "购买商品出错，银币不足.userId[" + userId + "], moneyType[" + moneyType + "]";
				LOG.error(message);
				throw new ServiceException(BUY_MALL_COPPER_NOT_ENOUGH, message);
			}
		} else if (moneyType == MoneyType.HERO_SHARD) {
			if (!this.toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.TOOL_ID_HERO_SHARD, needMoney, ToolUseType.REDUCE_BUY_MALL)) {
				String message = "购买商品出错，契约碎片不足.userId[" + userId + "], moneyType[" + moneyType + "]";
				LOG.error(message);
				throw new ServiceException(BUY_MALL_HERO_SHARD_NOT_ENOUGH, message);
			}
		} else {
			String message = "购买商品出错，商品购买货币异常.userId[" + userId + "], moneyType[" + moneyType + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

	}

	public SystemMallDiscountActivity checkDiscountIsOpen() {

		List<SystemMallDiscountActivity> activities = systemMallDiscountDao.getAllActivity();
		if (activities.size() == 0) {
			return null;
		}

		Date now = new Date();
		for (SystemMallDiscountActivity activity : activities) {
			if (now.after(activity.getEndTime()) || now.before(activity.getStartTime())) {
				// 不在打折活动时间内
				continue;
			} else {
				return activity;
			}
		}

		return null;
	}

	private int getDiscount(List<SystemMallDiscountItems> discountItems, int mallId) {
		for (SystemMallDiscountItems item : discountItems) {
			if (item.getMallId() == mallId) {
				return item.getDiscount();
			}
		}

		return 100;

	}
}
