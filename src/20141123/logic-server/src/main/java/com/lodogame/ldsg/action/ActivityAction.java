package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserHeroExchangeDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.System30LoginRewardBO;
import com.lodogame.ldsg.bo.SystemActivityBO;
import com.lodogame.ldsg.bo.SystemHeroExchangeBO;
import com.lodogame.ldsg.bo.SystemReduceRebateBo;
import com.lodogame.ldsg.bo.TavernTebatelBo;
import com.lodogame.ldsg.bo.ToolExchangeCountBO;
import com.lodogame.ldsg.bo.UserActivityTaskBO;
import com.lodogame.ldsg.bo.UserActivityTaskRewardBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserLoginRewardInfoBO;
import com.lodogame.ldsg.bo.UserOnlineRewardBO;
import com.lodogame.ldsg.bo.UserPayRewardBO;
import com.lodogame.ldsg.bo.UserRecivePowerBO;
import com.lodogame.ldsg.bo.UserTotalDayPayRewardBO;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ToolUpdateEvent;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ActivityDrawService;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.model.UserHeroExchange;

/**
 * 活动action
 * 
 * @author jacky
 * 
 */

public class ActivityAction extends LogicRequestAction {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserHeroExchangeDao userHeroExchangeDao;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private ActivityDrawService activityDrawService;

	@Autowired
	private PushHandler pushHandler;

	private static final Logger LOG = Logger.getLogger(ActivityAction.class);

	/**
	 * 领取体力
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response recivePower() throws IOException {

		LOG.debug("领取体力.userId[" + getUid() + "]");

		int type = this.getInt("sq", 1);
		this.activityService.recivePower(getUid(), type);

		return this.render();
	}

	public Response getReciveInfo() {

		LOG.debug("获取领取体力信息.userId[" + getUid() + "]");

		List<UserRecivePowerBO> userRecivePowerInfothis = this.activityService.getUserRecivePowerInfo(getUid());

		set("rpl", userRecivePowerInfothis);
		set("st", System.currentTimeMillis());

		return this.render();

	}

	/**
	 * 签到
	 * 
	 * @return
	 */
	public Response checkIn() {

		CommonDropBO commonDropBO = this.activityService.checkIn(getUid());
		if (commonDropBO.getExp() > 0 || commonDropBO.getGold() > 0 || commonDropBO.getCopper() > 0) {
			this.pushHandler.pushUser(getUid());
		}

		set("dr", commonDropBO);

		return this.render();
	}

	/**
	 * 领取30天登入礼包
	 * 
	 * @return
	 */
	public Response receive30LoginReward() {

		String userId = this.getUid();
		int loginDay = getInt("day", 0);

		CommonDropBO commonDropBO = this.activityService.receiveLoginReward(userId, loginDay);
		if (commonDropBO.getExp() > 0 || commonDropBO.getGold() > 0 || commonDropBO.getCopper() > 0) {
			this.pushHandler.pushUser(getUid());
		}

		int gulri = this.activityService.checkLoginRewardHasGiven(userId);

		set("gulri", gulri);
		set("dr", commonDropBO);

		return this.render();
	}

	/**
	 * 获取用户30天登入礼包领取信息
	 * 
	 * @return
	 */
	public Response get30LoginRewardInfo() {

		String userId = this.getUid();
		List<UserLoginRewardInfoBO> ulriList = this.activityService.getUserLoginRewardInfo(userId);
		set("ulri", ulriList);
		return this.render();
	}

	/**
	 * 获取用户30天登入礼包信息
	 * 
	 * @return
	 */
	public Response get30LoginRewardGift() {
		List<System30LoginRewardBO> systemLoginRewardList = this.activityService.get30LoginRewardGift();

		set("slrl", systemLoginRewardList);
		return this.render();
	}

	/**
	 * 查询武将兑换列表
	 * 
	 * @return
	 */
	public Response getHeroExchangeList() {

		String userId = this.getUid();

		UserHeroExchange userHeroExchange = this.userHeroExchangeDao.get(userId);
		int week = DateUtils.getWeekOfYear();
		if (userHeroExchange != null && userHeroExchange.getSystemWeek() == week) {// 如果用户刷新了,并且有效，则用该
																					// 组
			week = userHeroExchange.getUserWeek();
		}

		List<SystemHeroExchangeBO> systemHeroExchangeBOList = this.activityService.getSystemHeroExchangeBOList(week);

		int needMoney = 20;
		if (userHeroExchange != null && DateUtils.isSameDay(userHeroExchange.getUpdatedTime(), new Date())) {
			needMoney = userHeroExchange.getTimes() * 5 + 20;
		}

		set("ncm", needMoney);
		set("hels", systemHeroExchangeBOList);

		return this.render();
	}

	public Response receiveGiftBag() {

		String userId = getUid();
		String code = this.getString("code", "");

		LOG.debug("领取礼包码礼包.userId[" + userId + "], code[" + code + "]");

		CommonDropBO commonDropBO = this.activityService.receiveGiftCodeGiftBag(userId, code, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response receiveVipGiftBag() {

		String userId = getUid();

		LOG.debug("领取VIP礼包.userId[" + userId + "]");

		CommonDropBO commonDropBO = this.activityService.receiveVipGiftBag(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response receiveFirstPayGiftBag() {

		String userId = getUid();

		LOG.debug("领取首充礼包.userId[" + userId + "]");

		CommonDropBO commonDropBO = this.activityService.receiveFirstPayGiftBag(userId, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response receiveRookieGuideGiftBag() {
		int giftBagId = getInt("gbid", 0);

		CommonDropBO commonDropBO = this.activityService.receiveRookieGuideGiftBag(getUid(), giftBagId, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				return false;
			}
		});

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(getUid());
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response getGiftBagStatus() {

		String userId = this.getUid();
		int type = this.getInt("tp", 1);

		int status = this.activityService.getGiftBagStatus(userId, type);

		this.set("st", status);
		this.set("tp", type);

		return this.render();
	}

	/**
	 * 武将兑换
	 * 
	 * @return
	 */
	public Response heroExchange() {

		int heroExchangeId = getInt("eid", 0);

		UserHeroBO userHeroBO = this.activityService.heroExchange(getUid(), heroExchangeId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				if (event instanceof ToolUpdateEvent) {
					// pushHandler.pushToolList(getUid());
				}

				return true;
			}
		});

		set("hero", userHeroBO);

		return this.render();
	}

	/**
	 * 刷新可兑换武将列表
	 * 
	 * @return
	 */
	public Response refreshHeroList() {

		String userId = this.getUid();

		this.activityService.refreshHeroExchange(userId);

		this.pushHandler.pushUser(userId);

		return this.getHeroExchangeList();

	}

	/**
	 * 领取在线礼包
	 * 
	 * @return
	 */
	public Response receiveOnlineGiftBag() {

		String userId = this.getUid();

		CommonDropBO commonDropBO = this.activityService.receiveOnlineReward(userId);

		UserOnlineRewardBO userOnlineRewardBO = this.activityService.getUserOnlineRewardBO(userId);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("olgb", userOnlineRewardBO);

		return this.getHeroExchangeList();

	}

	/**
	 * 查询活跃度任务列表
	 * 
	 * @return
	 */
	public Response getActivityTaskList() {

		String userId = this.getUid();

		List<UserActivityTaskBO> userActivityTaskBOList = this.activityTaskService.getUserActivityTaskListBO(userId);
		List<UserActivityTaskRewardBO> userActivityTaskRewardBOList = this.activityTaskService.getUserActivityTaskRewardBOList(userId);
		int point = this.activityTaskService.getUserActivityPoint(userId);

		set("atls", userActivityTaskBOList);
		set("rwls", userActivityTaskRewardBOList);
		set("pt", point);

		return this.render();
	}

	/**
	 * w
	 * 
	 * @return
	 */
	public Response receiveActivityReward() {

		String userId = this.getUid();
		int activityTaskRewardId = this.getInt("arid", 0);

		CommonDropBO commonDropBO = this.activityTaskService.receive(userId, activityTaskRewardId);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response receiveOncePayReward() {

		String userId = this.getUid();
		int aid = this.getInt("aid", 0);

		CommonDropBO commonDropBO = this.activityService.receiveOncePayReward(userId, aid);
		UserPayRewardBO userPayRewardBO = this.activityService.getUserOncePayRewardById(userId, aid);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("uprl", userPayRewardBO);

		return this.render();
	}

	public Response getUserOncePayRewards() {

		String userId = this.getUid();

		List<UserPayRewardBO> list = this.activityService.getUserOncePayRewardList(userId);

		set("uprl", list);

		return this.render();
	}

	public Response getUserTotalPayRewards() {
		String userId = getUid();
		List<UserPayRewardBO> list = this.activityService.getUserTotalPayRewardList(userId);
		set("uprl", list);
		return this.render();
	}

	public Response receiveTotalPayReward() {

		String userId = this.getUid();
		int aid = this.getInt("aid", 0);

		CommonDropBO commonDropBO = this.activityService.receiveTotalPayReward(userId, aid);
		UserPayRewardBO userPayReward = this.activityService.getUserTotalPayRewardById(userId, aid);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("uprl", userPayReward);

		return this.render();
	}

	/**
	 * 物品兑换
	 */
	public Response toolExchange() {

		String userId = this.getUid();
		int toolExchangeId = this.getInt("toolExchangeId", 0);
		int num = this.getInt("num", 0);
		Map<String, Object> rtMap = activityService.toolExchange(userId, toolExchangeId, num);
		set("tr", rtMap.get("tr"));
		set("te", rtMap.get("te"));

		set("ext", rtMap.get("ext"));

		this.pushHandler.pushUser(userId);
		return this.render();
	}

	/**
	 * 在进入物品兑换界面时，统计用户已经兑换的次数
	 */
	public Response toolExchangeCount() {

		String userId = this.getUid();
		List<ToolExchangeCountBO> boList = activityService.toolExchangeCount(userId);
		set("extc", boList);
		set("ecl", this.activityService.getToolExchangeBoList());
		return this.render();
	}

	/**
	 * 七天登陆-获取奖励列表
	 */
	public Response getLoginRewardList() {
		Map<String, Object> loginRewardList = activityService.getLoginRewardList(getUid());
		set("tls", loginRewardList.get("tls"));
		set("sts", loginRewardList.get("sts"));
		set("day", loginRewardList.get("day"));

		return this.render();
	}

	/**
	 * 七天登录-领取奖励
	 */
	public Response getLoginReward() {
		Map<String, Object> loginReward = activityService.getLoginReward(getUid());

		set("dr", loginReward.get("dr"));
		set("day", loginReward.get("day"));

		return this.render();
	}

	/**
	 * 领取限时在线奖励
	 */
	public Response getLimOnlineReward() {
		CommonDropBO dropBO = activityService.getLimOnlineReward(getUid());

		set("dr", dropBO);
		return this.render();
	}

	/**
	 * 获取限时在线奖品列表
	 */
	public Response getLimOnlineRewardList() {
		List<DropToolBO> rewardList = activityService.getLimOnlineRewardList();

		set("tls", rewardList);

		return this.render();
	}

	/**
	 * 获取活动配置表
	 */
	public Response getActivityConfigData() {

		int type = this.getInt("tp", 1);

		if (type == 1) {
			set("ecl", this.activityService.getToolExchangeBoList());
		} else if (type == 2) {
			set("drl", this.activityDrawService.getSystemActivityDrawShowBOList());
		}

		set("tp", type);

		return this.render();
	}

	/**
	 * 查询积天充值任务列表
	 * 
	 * @return
	 */
	public Response getUserTotalDayPayRewards() {

		String userId = this.getUid();

		// 积天充值的奖励列表
		List<UserTotalDayPayRewardBO> userTotalDayPayRewardListBO = this.activityService.getUserTotalDayPayRewardList(userId);
		int totalDayPayRewardIsOver = this.activityService.checkTotalDayPayRewardIsOver(userTotalDayPayRewardListBO);

		set("atrbl", userTotalDayPayRewardListBO);
		set("tdprio", totalDayPayRewardIsOver);

		return this.render();
	}

	/**
	 * 领取积天充值
	 * 
	 * @return
	 */
	public Response receiveTotalDayPayReward() {
		String userId = this.getUid();
		int rid = this.getInt("rid", 0);

		CommonDropBO commonDropBO = this.activityService.receiveTotalDayPayReward(userId, rid);
		if (commonDropBO.getExp() > 0 || commonDropBO.getGold() > 0 || commonDropBO.getCopper() > 0) {
			this.pushHandler.pushUser(getUid());
		}

		int totalDayPayRewardIsOver = this.activityService.checkTotalDayPayRewardIsOver(userId);
		UserTotalDayPayRewardBO userTotalDayPayRewardBO = new UserTotalDayPayRewardBO();
		userTotalDayPayRewardBO.setRewardId(rid);
		userTotalDayPayRewardBO.setStatus(2);
		set("dr", commonDropBO);
		set("tdprio", totalDayPayRewardIsOver);
		set("utdpr", userTotalDayPayRewardBO);

		return this.render();
	}

	/**
	 * 获取活动面板信息
	 * 
	 * @return
	 */
	public Response getDisplayAcitvity() {

		List<SystemActivityBO> list = this.activityService.getDisplayActivityBOList(getUid());

		set("acl", list);

		return this.render();
	}

	/**
	 * 招募返利
	 * 
	 * @return
	 */
	public Response getTavernRebates() {
		String userId = getUid();
		int type = getInt("tp", 0);
		List<TavernTebatelBo> list = this.activityService.getTavernRebates(userId, type);
		List<Integer> integers = this.activityService.getTavernTimes(userId, type);
		set("strs", list);
		set("ats", integers.get(0));
		set("bts", integers.get(1));
		return this.render();
	}

	/**
	 * 领取招募返利
	 * 
	 * @return
	 */
	public Response receiveTavernRebates() {
		String userId = getUid();
		int id = getInt("id", 0);
		Map<String, Object> map = this.activityService.receiveTavernRebates(userId, id);
		int type = Integer.parseInt((map.get("tp").toString()));
		List<Integer> integers = this.activityService.getTavernTimes(userId, type);
		CommonDropBO commonDropBO = (CommonDropBO) map.get("dr");

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("strs", map.get("strs"));
		set("ats", integers.get(0));
		set("bts", integers.get(1));
		return this.render();
	}

	/**
	 * 消耗返利
	 * 
	 * @return
	 */
	public Response getReduceRebate() {
		String userId = getUid();
		int type = getInt("tp", 0);
		List<SystemReduceRebateBo> list = this.activityService.getReduceRebates(userId, type);
		set("srrs", list);
		set("amt", this.activityService.getReduceGoldByType(userId, type));
		return this.render();
	}

	/**
	 * 领取消耗返利
	 * 
	 * @return
	 */
	public Response receiveReduceRebates() {
		String userId = getUid();
		int id = getInt("id", 0);
		Map<String, Object> map = this.activityService.receiveReduceRebates(userId, id);
		int type = Integer.parseInt((map.get("tp").toString()));

		CommonDropBO commonDropBO = (CommonDropBO) map.get("dr");

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		set("srrs", map.get("srrs"));
		set("amt", this.activityService.getReduceGoldByType(userId, type));
		return this.render();
	}
	
	/*
	 * 繁体版评价后通过邮件发送奖励
	 */
	public Response comment() {
		activityService.comment(getUid());
		return this.render();
	}

}
