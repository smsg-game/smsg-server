package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.PkAwardDao;
import com.lodogame.game.dao.PkGroupAwardLogDao;
import com.lodogame.game.dao.RankScoreCfgDao;
import com.lodogame.game.dao.RuntimeDataDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.dao.impl.PkGroupAwardDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.AwardBO;
import com.lodogame.ldsg.bo.AwardDescBO;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.PkGroupAwardGrantBO;
import com.lodogame.ldsg.bo.PkGroupAwardLogBo;
import com.lodogame.ldsg.bo.PkGroupFirstBo;
import com.lodogame.ldsg.bo.PkInfoBO;
import com.lodogame.ldsg.bo.PkPlayerBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.LogTypes;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BaseEvent;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PkFightEvent;
import com.lodogame.ldsg.event.PkGetTitleEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.PkHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.PkAward;
import com.lodogame.model.PkGroupAward;
import com.lodogame.model.PkGroupAwardLog;
import com.lodogame.model.RankScoreCfg;
import com.lodogame.model.RuntimeData;
import com.lodogame.model.User;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserPkInfo;

public class PkServiceImpl implements PkService {

	private static final int MAX_RANK = 3000;

	private static final int ENTER_PK_LEVEL = 20;

	private static final int TIME_OUT = 2;

	private static final Map<Integer, String> map = new HashMap<Integer, String>();

	static {
		map.put(1, "20-39");
		map.put(2, "40-49");
		map.put(3, "50-59");
		map.put(4, "60-69");
		map.put(5, "70-79");
		map.put(6, "80-89");
		map.put(7, "90-99");
		map.put(8, "100-109");
		map.put(9, "110-119");
		map.put(10, "120-129");
		map.put(11, "130-139");
		map.put(12, "140-149");
		map.put(13, "150-150");
	}

	private final static Logger logger = Logger.getLogger(PkServiceImpl.class);

	@Autowired
	public UnSynLogService unSynLogService;

	@Autowired
	private VipService vipService;

	public UserPkInfoDao getUserPkInfoDao() {
		return userPkInfoDao;
	}

	public void setUserPkInfoDao(UserPkInfoDao userPkInfoDao) {
		this.userPkInfoDao = userPkInfoDao;
	}

	public RankScoreCfgDao getRankScoreCfgDao() {
		return rankScoreCfgDao;
	}

	public void setRankScoreCfgDao(RankScoreCfgDao rankScoreCfgDao) {
		this.rankScoreCfgDao = rankScoreCfgDao;
	}

	public UserPkInfoDao getUserPkInfoDaoRedisImpl() {
		return userPkInfoDaoRedisImpl;
	}

	public void setUserPkInfoDaoRedisImpl(UserPkInfoDao userPkInfoDaoRedisImpl) {
		this.userPkInfoDaoRedisImpl = userPkInfoDaoRedisImpl;
	}

	public PkAwardDao getPkAwardDao() {
		return pkAwardDao;
	}

	public void setPkAwardDao(PkAwardDao pkAwardDao) {
		this.pkAwardDao = pkAwardDao;
	}

	private boolean started = false;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	private UserPkInfoDao userPkInfoDao;

	private RankScoreCfgDao rankScoreCfgDao;

	private UserPkInfoDao userPkInfoDaoRedisImpl;

	@Autowired
	private EquipService equipService;

	private PkAwardDao pkAwardDao;

	@Autowired
	private BattleService battleService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private ConfigDataDao configDataDao;

	private String pkCacheKey;

	private String enterCacheKey;

	@Autowired
	private EventServcie eventServcie;

	@Autowired
	private ActivityTaskService activityTaskService;

	@Autowired
	private PkGroupAwardLogDao pkGroupAwardLogDao;

	@Autowired
	private PkGroupAwardDao pkGroupAwardDao;

	private Map<String, EventHandle> handles = new ConcurrentHashMap<String, EventHandle>();

	@Override
	public void enterPk(String uid, EventHandle handle, int isGroup) {
		User user = userService.get(uid);

		int pkLevel = this.configDataDao.getInt(ConfigKey.USER_PK_NEED_LIMIT, ENTER_PK_LEVEL);

		if (user.getLevel() < pkLevel) {
			throw new ServiceException(ENTER_PK_LEVEL_NOT_ENOUGH, "玩家等级不足");
		}
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(uid);
		if (userPkInfo == null) {
			// UserPkInfo lastInfo = userPkInfoDao.getLastUserPkInfo();
			// if (lastInfo == null || lastInfo.getRank() < MAX_RANK) {
			String id = IDGenerator.getID();
			handles.put(id, handle);
			pushEnterReq(id, uid, isGroup);
			// }
		} else {
			Date updatePkTime = userPkInfo.getUpdatePkTime();
			Date now = new Date();
			if (!DateUtils.isSameDay(updatePkTime, now)) {
				userPkInfo.setPkTimes(0);
				userPkInfo.setUpdatePkTime(now);
				userPkInfoDao.update(userPkInfo);
			}
			Event event = new BaseEvent();
			List<PkPlayerBO> pkPlayers = loadPlayers(uid, isGroup);
			PkGroupAwardLog pkGroupAwardLog = pkGroupAwardLogDao.get(uid);
			PkGroupAwardGrantBO pkGroupAwardGrantBO = new PkGroupAwardGrantBO();
			if (pkGroupAwardLog != null) {
				pkGroupAwardGrantBO.setGid(pkGroupAwardLog.getGroupId());
				pkGroupAwardGrantBO.setGrank(pkGroupAwardLog.getGrank());
				pkGroupAwardGrantBO.setIsget(pkGroupAwardLog.getIsGet() == 1 ? 2 : 1);// 2:以领取
			} else {// 用户没有领取可以领取
				pkGroupAwardGrantBO.setGid(-1);
				pkGroupAwardGrantBO.setGrank(-1);
				pkGroupAwardGrantBO.setIsget(0);
			}

			event.setObject("pkInfoBo", packPkInfoBo(userPkInfo));
			event.setObject("pkPlayers", pkPlayers);
			event.setObject("ig", pkGroupAwardGrantBO);
			handle.handle(event);
		}
	}

	private String getEnterCallbackKey() {
		synchronized (ENTER_REQUEST_POOL_KEY) {
			if (enterCacheKey == null) {
				enterCacheKey = ENTER_REQUEST_POOL_KEY + IDGenerator.getID();
			}
		}
		return enterCacheKey;
	}

	private void pushEnterReq(String handlerId, String uid, int isGroup) {
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("uid", uid);
		request.put("handlerId", handlerId);
		request.put("isGroup", isGroup);
		JedisUtils.pushMsg(getEnterCallbackKey(), Json.toJson(request));
	}

	@Override
	public PkInfoBO getUserPkInfo(String uid) {
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(uid);
		if (userPkInfo == null) {
			return null;
		}
		PkInfoBO bo = packPkInfoBo(userPkInfo);
		return bo;
	}

	private PkInfoBO packPkInfoBo(UserPkInfo userPkInfo) {
		PkInfoBO bo = new PkInfoBO();
		bo.setRank(userPkInfo.getRank());
		bo.setScore(userPkInfo.getScore());
		bo.setGid(PkHelper.getGroup(userPkInfo.getUser_level()));
		bo.setGRank(getUserGrank(userPkInfo.getUserId(), PkHelper.getGroup(userPkInfo.getUser_level())));

		Date now = new Date();
		if (userPkInfo.getUpdatePkTime() != null && !DateUtils.isSameDay(userPkInfo.getUpdatePkTime(), now)) {
			userPkInfo.setPkTimes(0);
			userPkInfo.setUpdatePkTime(now);
			userPkInfoDao.update(userPkInfo);
		} else {
			bo.setPkTimes(userPkInfo.getPkTimes());
		}

		if (userPkInfo.getLastBuyTime() != null && !DateUtils.isSameDay(userPkInfo.getLastBuyTime(), now)) {
			userPkInfo.setBuyPkTimes(0);
			bo.setBuyPkTimes(0);
		} else {
			userPkInfo.setBuyPkTimes(userPkInfo.getBuyPkTimes());
			bo.setBuyPkTimes(userPkInfo.getBuyPkTimes());
		}

		User user = userService.get(userPkInfo.getUserId());
		bo.setPlayerId(user.getLodoId());
		return bo;
	}

	@Override
	public List<PkPlayerBO> loadPlayers(String uid, int isGroup) {
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(uid);

		// boolean useMoney = userPkInfo.getSeeType() == 1;
		// if (userPkInfo.getRank() <= 50) {
		// useMoney = false;
		// }

		if (isGroup == 0) {
			List<Integer> range = getRange(PkHelper.getGroup(userPkInfo.getUser_level()));
			List<UserPkInfo> list = userPkInfoDao.getGroupRanks(range.get(0), range.get(1));
			if (list.size() <= 11) {
				return packPkPlayerBO(filterList(list, userPkInfo.getRank()));
			}
			return packPkPlayerBO(filterList(list, userPkInfo.getRank()));
		} else {
			List<Integer> attackAbleList = new ArrayList<Integer>();
			PkHelper.setAttackAbleList(attackAbleList, userPkInfo.getRank());
			return packPkPlayerBO(userPkInfoDao.getRanks(attackAbleList));
		}

	}

	/**
	 * 过滤组排名，只拿当前玩家的前10名
	 * 
	 * @param original
	 * @param rank
	 * @return
	 */
	private List<UserPkInfo> filterList(List<UserPkInfo> original, int rank) {
		List<UserPkInfo> list = new ArrayList<UserPkInfo>();
		int index = 0;
		if (original != null && original.size() > 0) {
			for (int i = 0; i < original.size(); i++) {
				if (rank == original.get(i).getRank()) {
					index = i + 1;
					break;
				}
			}
			if (index <= 11) {// 用户总排名
				for (int i = 0; i < 11; i++) {
					if (list.size() == 11 || i == original.size()) {
						break;
					}
					UserPkInfo userPkInfo = original.get(i);
					userPkInfo.setgRank(i + 1);
					list.add(userPkInfo);
				}
			} else {
				index = (index - 11);
				for (; list.size() != 11; index++) {
					UserPkInfo userPkInfo = original.get(index);
					userPkInfo.setgRank(index);
					list.add(userPkInfo);

				}
			}
		}
		return list;
	}

	/**
	 * 组ID拿组的等级区间
	 * 
	 * @param gid
	 * @return
	 */
	private List<Integer> getRange(int gid) {

		List<Integer> list = new ArrayList<Integer>();
		String[] range = map.get(gid).split("-");
		list.add(Integer.parseInt(range[0]));
		list.add(Integer.parseInt(range[1]));

		return list;
	}

	// @Override
	// public List<PkPlayerBO> attackAbleJump(String userId) {
	//
	// UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
	// if (userPkInfo.getRank() <= 50) {// 小于50名不能飞跃
	// String message = "小于50名不能使用飞跃功能, userId[" + userId + "]";
	// throw new ServiceException(ATTACK_ABLE_JUMP_RANK_TOOL_HIGHT, message);
	// }
	//
	// if (userPkInfo.getSeeType() == 1) {// 已经使用了飞跃
	// String message = "飞跃功能不可以重复使用.userId[" + userId + "]";
	// throw new ServiceException(ATTACK_ABLE_JUMP_HAS_JUMP, message);
	// }
	//
	// User user = this.userService.get(userId);
	// if (!this.userService.reduceGold(userId, ATTACK_ABLE_JUMP_NEED_MONEY,
	// ToolUseType.REDUCE_PK_RANK_JUMP, user.getLevel())) {
	// String message = "不能使用飞跃功能，元宝不足.userId[" + userId + "]";
	// throw new ServiceException(ATTACK_ABLE_JUMP_GOLD_NOT_ENOUGH, message);
	// }
	//
	// userPkInfo.setSeeType(1);
	// this.userPkInfoDao.update(userPkInfo);
	//
	// return this.loadPlayers(userId);
	// }
	private List<PkPlayerBO> packPkPlayerBO(List<UserPkInfo> list) {
		List<PkPlayerBO> boList = new ArrayList<PkPlayerBO>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				UserPkInfo tmp = list.get(i);
				if (tmp == null) {
					continue;
				}
				User user = userService.get(tmp.getUserId());
				PkPlayerBO pkPlayerBO = new PkPlayerBO();
				pkPlayerBO.setNickname(user.getUsername());
				pkPlayerBO.setLevel(user.getLevel());
				pkPlayerBO.setPlayerId(user.getLodoId());
				pkPlayerBO.setRank(tmp.getRank());
				pkPlayerBO.setScore(tmp.getScore());
				pkPlayerBO.setVipLevel(user.getVipLevel());
				pkPlayerBO.setGRank(getUserGrank(user.getUserId(), PkHelper.getGroup(user.getLevel())));
				List<UserHeroBO> userHeroBoList = heroService.getUserHeroList(tmp.getUserId(), 1);
				pkPlayerBO.setHeroList(userHeroBoList);
				boList.add(pkPlayerBO);
			}
		}
		return boList;
	}

	@Override
	public List<PkPlayerBO> loadTopPlayers() {
		List<UserPkInfo> list = userPkInfoDao.getByRankRange(1, 10);
		return packPkPlayerBO(list);
	}

	// TODO 增加积分对话日志
	@Override
	public AwardBO exchange(String userId, int awardId) {
		PkAward pkAward = pkAwardDao.getById(awardId);
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		int remainder = userPkInfo.getScore() - pkAward.getScore();
		AwardBO awardBo = null;
		if (remainder >= 0) {
			logger.debug("userId:" + userId + ", award id: " + awardId + ", score:" + pkAward.getScore() + ", remainder score:" + remainder);
			List<DropToolBO> dropTools = DropToolHelper.parseDropTool(pkAward.getTools());
			checkBag(userId, dropTools);
			awardBo = pickUpAwardToolList(userId, dropTools);
			userPkInfo.setScore(remainder);
			userPkInfoDao.update(userPkInfo);
		} else {
			throw new ServiceException(EXCHANGE_SCORE_NOT_ENOUGH, "剩余积分不足");
		}
		return awardBo;
	}

	/**
	 * 批量兑换
	 */
	public AwardBO batchExchange(String userId, int awardId, int num) {

		if (num <= 0) {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据库常，传了负数");
		}

		PkAward pkAward = pkAwardDao.getById(awardId);
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		int remainder = userPkInfo.getScore() - pkAward.getScore() * num;
		AwardBO awardBO = null;
		if (remainder >= 0) {
			logger.debug("userId:" + userId + ", award id: " + awardId + ", score:" + pkAward.getScore() + "*" + num + ", remainder score:" + remainder);
			List<DropToolBO> dropTools = DropToolHelper.parseDropTool(pkAward.getTools());

			/**
			 * DropToolBO 对象中包含了兑换一次可获得的物品数量， 因此要计算一次批量转换可以兑换多少
			 */
			for (DropToolBO dropToolBo : dropTools) {
				int toolNum = dropToolBo.getToolNum();
				dropToolBo.setToolNum(toolNum * num);
			}

			checkBag(userId, dropTools);
			awardBO = pickUpAwardToolList(userId, dropTools);
			userPkInfo.setScore(remainder);
			userPkInfoDao.update(userPkInfo);
		} else {
			throw new ServiceException(EXCHANGE_SCORE_NOT_ENOUGH, "剩余积分不足");
		}
		return awardBO;
	}

	/**
	 * 兑换时检测背包
	 * 
	 * @param userId
	 * @param dropTools
	 */
	private void checkBag(String userId, List<DropToolBO> dropTools) {
		int userHeroCount = this.heroService.getUserHeroCount(userId);
		int userEquipCount = 0;
		List<UserEquipBO> userEquips = this.equipService.getUserEquipList(userId);
		if (userEquips != null) {
			userEquipCount = userEquips.size();
		}
		UserExtinfo userExtinfo = this.userExtinfoDao.get(userId);
		int heroBagMax = InitDefine.HERO_BAG_INIT;
		int equipBagMax = InitDefine.EQUIP_BAG_INIT;
		if (userExtinfo != null) {
			heroBagMax = userExtinfo.getHeroMax();
			equipBagMax = userExtinfo.getEquipMax();
		}

		int heroCount = 0;
		int equipCount = 0;
		for (DropToolBO dropToolBO : dropTools) {
			int type = dropToolBO.getToolType();
			switch (type) {
			case ToolType.HERO:
				heroCount++;
				break;
			case ToolType.EQUIP:
				equipCount++;
				break;
			}
		}

		if (heroCount != 0 && heroCount + userHeroCount > heroBagMax) {
			throw new ServiceException(EXCHANGE_HERO_BAG_NOT_ENOUGH, "武将背包不足");
		}

		if (equipCount != 0 && equipCount + userEquipCount > equipBagMax) {
			throw new ServiceException(EXCHANGE_EQUIP_BAG_NOT_ENOUGH, "装备背包不足");
		}
	}

	private AwardBO pickUpAwardToolList(String userId, List<DropToolBO> dropTools) {

		AwardBO awardBo = new AwardBO();
		List<UserHeroBO> userHeroList = new ArrayList<UserHeroBO>();
		List<UserEquipBO> userEquipList = new ArrayList<UserEquipBO>();
		List<UserToolBO> userToolList = new ArrayList<UserToolBO>();

		for (DropToolBO dropToolBO : dropTools) {

			List<DropToolBO> dropToolBOList = pickUpAwardTool(userId, dropToolBO);

			if (dropToolBOList == null) {
				continue;
			}

			for (DropToolBO bo : dropToolBOList) {

				int type = bo.getToolType();
				switch (type) {
				case ToolType.HERO:
					UserHeroBO userHeroBO = this.heroService.getUserHeroBO(userId, bo.getValue());
					userHeroList.add(userHeroBO);
					break;
				case ToolType.EQUIP:
					UserEquipBO userEquipBO = this.equipService.getUserEquipBO(userId, bo.getValue());
					userEquipList.add(userEquipBO);
					break;
				case ToolType.COPPER:
					userService.pushUser(userId);
					awardBo.setCopper(bo.getToolNum());
					break;
				case ToolType.MATERIAL:
				case ToolType.GIFT_BOX:
				case ToolType.HERO_SHARD:
					UserToolBO userToolBO = new UserToolBO();
					userToolBO.setUserId(userId);
					userToolBO.setToolId(bo.getToolId());
					userToolBO.setToolType(bo.getToolType());
					userToolBO.setToolNum(bo.getToolNum());
					userToolList.add(userToolBO);
					break;
				default:
					break;
				}
			}

		}
		awardBo.setUserEquipBOList(userEquipList);
		awardBo.setUserHeroBOList(userHeroList);
		awardBo.setUserToolBOList(userToolList);
		return awardBo;
	}

	/**
	 * 给具体的奖励
	 * 
	 * @param userId
	 * @param forcesDropTool
	 */
	private List<DropToolBO> pickUpAwardTool(String userId, DropToolBO dropToolBO) {

		int toolType = dropToolBO.getToolType();
		int toolId = dropToolBO.getToolId();
		int toolNum = dropToolBO.getToolNum();

		return toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_SCORE_EXCHANGE);
	}

	private UserPkInfo checkPkTime(User user) {
		UserPkInfo pkInfo = userPkInfoDao.getByUserId(user.getUserId());
		int timesLimit = vipService.getPkTimesLimit(user.getVipLevel());
		if (pkInfo.getPkTimes() < timesLimit) {
			// pkInfo.setPkTimes(pkInfo.getPkTimes() + 1);
		} else {
			throw new ServiceException(FIGHT_TIMES_NOT_ENOUGH, "挑战次数不足");
		}
		return pkInfo;
	}

	@Override
	public boolean fight(final String uid, final long targetPid, final EventHandle handle) {
		final User user = userService.get(uid);
		final UserPkInfo userPkInfo = checkPkTime(user);

		BattleBO attack = new BattleBO();
		// 英雄列表
		List<BattleHeroBO> attackBO = this.heroService.getUserBattleHeroBOList(uid);
		attack.setUserLevel(user.getLevel());
		attack.setBattleHeroBOList(attackBO);

		final User defenseUser = userService.getByPlayerId(Long.toString(targetPid));
		BattleBO defense = new BattleBO();
		// 英雄列表
		List<BattleHeroBO> defenseBO = this.heroService.getUserBattleHeroBOList(defenseUser.getUserId());
		defense.setBattleHeroBOList(defenseBO);
		defense.setUserLevel(defenseUser.getLevel());

		this.activityTaskService.updateActvityTask(uid, ActivityTargetType.PK, 1);

		if (userPkInfo.getSeeType() == 1) {
			userPkInfo.setSeeType(0);
			this.userPkInfoDao.update(userPkInfo);
		}

		battleService.fight(attack, defense, 2, new EventHandle() {
			public boolean handle(Event event) {
				boolean isWin = false;
				if (event instanceof BattleResponseEvent) {

					int pkTimes = userPkInfo.getPkTimes();
					pkTimes = pkTimes + 1;
					userPkInfoDao.addPkTimes(uid, 1);// 挑战不管胜负挑战次数都要加1次

					int flag = event.getInt("flag");
					if (flag == 1) {// 挑战打赢了
						userPkInfoDao.updateTimes(user.getUserId());
						UserPkInfo targetPkInfo = userPkInfoDao.getByUserId(defenseUser.getUserId());
						int rank = targetPkInfo.getRank();

						PkFightEvent pkFightEvent = new PkFightEvent(user.getUserId(), user.getUsername(), defenseUser.getUsername(), rank, user.getLevel(), defenseUser.getLevel(), targetPkInfo.getUserId());
						eventServcie.dispatchEvent(pkFightEvent);

						isWin = true;
					} else {
						userPkInfoDao.clearTimes(user.getUserId());
					}

					CommonDropBO dropBO = updatePkInfo(userPkInfo, defenseUser.getUserId(), isWin);
					userExtinfoDao.updateFightRecode(uid, true);
					userExtinfoDao.updateFightRecode(defenseUser.getUserId(), false);
					event.setObject("forcesDropBO", dropBO);
					// 保存战报
					// String report = event.getString("report");
					// battleService.saveReport(uid, defenseUser.getUserId(), 2,
					// flag, report, true);
					unSynLogService.userBattleLog(uid, user.getLevel(), defenseUser.getUserId(), LogTypes.BATTLE_TYPE_PK, flag, new Date());
					event.setObject("bgid", 1);
					event.setObject("dun", defenseUser.getUsername());
					event.setObject("pkt", pkTimes);
					handle.handle(event);
				}
				return true;
			}
		});

		return true;
	}

	@SuppressWarnings("unchecked")
	private void checkEnterResponse() {
		String responseStr = JedisUtils.blockPopMsg(TIME_OUT, getEnterCallbackKey());
		if (StringUtils.isBlank(responseStr)) {
			return;
		}
		Map<String, Object> response = Json.toObject(responseStr, Map.class);
		String handlerId = (String) response.get("handlerId");
		String uid = (String) response.get("uid");
		int isGroup = (Integer) response.get("isGroup");
		enterHandler(handlerId, uid, isGroup);
	}

	private void enterHandler(String handlerId, String uid, int isGroup) {
		UserPkInfo lastInfo = userPkInfoDao.getLastUserPkInfo();
		EventHandle handle = handles.remove(handlerId);
		Event event = new BaseEvent();
		// if (lastInfo == null || lastInfo.getRank() < MAX_RANK) {
		User user = userService.get(uid);
		UserPkInfo userPkInfo = new UserPkInfo();
		userPkInfo.setPkTimes(0);
		userPkInfo.setScore(0);
		userPkInfo.setUserId(uid);
		userPkInfo.setTimes(0);
		userPkInfo.setUser_level(user.getLevel());
		userPkInfo.setUsername(user.getUsername());
		userPkInfo.setUpdatePkTime(new Date());
		try {
			if (lastInfo == null) {
				userPkInfo.setRank(1);
				userPkInfoDao.addFirst(userPkInfo);
			} else {
				userPkInfoDao.add(userPkInfo);
			}
		} catch (Exception e) {
			handle.handle(event);
		}
		userPkInfo = userPkInfoDao.getByUserId(uid);
		// userPkInfo.setgRank(getUserGrank(uid,
		// PkHelper.getGroup(user.getLevel())));

		PkGroupAwardGrantBO pkGroupAwardGrantBO = new PkGroupAwardGrantBO();

		// 用户没有领取可以领取
		pkGroupAwardGrantBO.setGid(-1);
		pkGroupAwardGrantBO.setGrank(-1);
		pkGroupAwardGrantBO.setIsget(0);

		List<PkPlayerBO> pkPlayers = loadPlayers(uid, isGroup);
		event.setObject("pkInfoBo", packPkInfoBo(userPkInfo));
		event.setObject("pkPlayers", pkPlayers);
		event.setObject("ig", pkGroupAwardGrantBO);
		handle.handle(event);

		// }
	}

	/**
	 * 更新用户的排名信息
	 * 
	 * @param uid
	 * @param targetUserId
	 * @return
	 */
	private CommonDropBO updatePkInfo(final UserPkInfo attackPkInfo, final String targetUserId, boolean isWin) {

		int pid = PkHelper.getGroup(attackPkInfo.getUser_level());
		int grank = getUserGrank(attackPkInfo.getUserId(), pid);

		CommonDropBO dropBO = new CommonDropBO();
		UserPkInfo targetPkInfo = userPkInfoDao.getByUserId(targetUserId);

		int rank = targetPkInfo.getRank();
		if (rank > MAX_RANK) {
			rank = MAX_RANK;
		}
		RankScoreCfg config = rankScoreCfgDao.getByRank(rank);

		int score = config.getScore();

		if (isWin) {
			boolean isChange = false;
			if (userPkInfoDao.changeRank(attackPkInfo.getUserId(), targetUserId)) {
				isChange = true;
				dropBO.setRank(targetPkInfo.getRank());
				dropBO.setUprank(attackPkInfo.getRank() - targetPkInfo.getRank());
				int newGroupRank = getUserGrank(attackPkInfo.getUserId(), pid);
				dropBO.setGrank(newGroupRank);
				dropBO.setUpgrank(grank - newGroupRank);
			}

			attackPkInfo.setScore(targetPkInfo.getScore() + score);
			if (!isChange) {
				dropBO.setRank(attackPkInfo.getRank());
				dropBO.setGrank(grank);
				dropBO.setUpgrank(0);
			}
			dropBO.setScore(score);

		} else {
			score = config.getScore() / 2;
			attackPkInfo.setScore(targetPkInfo.getScore() + score);
			dropBO.setRank(0);
			dropBO.setGrank(grank);
			dropBO.setUpgrank(0);
			dropBO.setScore(score);
		}

		userPkInfoDao.addScore(attackPkInfo.getUserId(), score);
		return dropBO;
	}

	@Override
	public boolean buyPkTimes(String uid) {

		UserPkInfo userPkInfo = this.userPkInfoDao.getByUserId(uid);
		// if (userPkInfo.getPkTimes() > 0) {
		// String message = "购买争霸次数失败，还有剩余争霸次数.userId[" + uid + "]";
		// throw new ServiceException(BUY_PK_TIMES_PK_TIMES_NOT_ZERO, message);
		// }

		int buyTimes = userPkInfo.getBuyPkTimes() + 1;
		if (!DateUtils.isSameDay(new Date(), userPkInfo.getLastBuyTime())) {
			buyTimes = 1;
		}

		int needMoney = (buyTimes - 1) * 5 + 10;

		User user = this.userService.get(uid);

		if (!this.userService.reduceGold(uid, needMoney, ToolUseType.REDUCE_BUY_PK_TIMES, user.getLevel())) {
			String message = "购买争霸次数失败，元宝不足.userId[" + uid + "]";
			throw new ServiceException(BUY_PK_TIMES_GOLD_NOT_ENOUGH, message);
		}

		this.userPkInfoDao.buyPkTimes(uid, buyTimes);

		return true;
	}

	public void init() {
		if (started) {
			return;
		}

		if (!Config.ins().isGameServer()) {
			return;
		}

		started = true;

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						checkEnterResponse();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}

		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000 * 60 * 10);
						addScore();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}

		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (DateUtils.getHour() >= 21) {

							String date = DateUtils.getDate();
							if (!pkAwardDao.isAwardSended(date)) {

								pkAwardDao.addAwardSendLog(date);

								rewardPkGroupRankAward();
							} else {
								logger.debug("争霸奖励已经发放");
							}
						} else {
							logger.debug("未到争霸奖励发放时间");
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

					try {
						Thread.sleep(1000 * 60 * 1);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}

				}
			}

		}).start();

	}

	private void addScore() {
		List<UserPkInfo> list = userPkInfoDao.getByRankRange(1, 100000);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				UserPkInfo info = list.get(i);
				if (info == null) {
					continue;
				}
				int rank = info.getRank();
				if (info.getRank() > MAX_RANK) {
					rank = MAX_RANK;
				}
				RankScoreCfg config = rankScoreCfgDao.getByRank(rank);
				info.setScore(info.getScore() + config.getScore());
				// userPkInfoDao.update(info);
				userPkInfoDao.addScore(info.getUserId(), config.getScore());
			}
		}
	}

	@Override
	public List<AwardDescBO> getAwardList() {
		List<AwardDescBO> boList = new ArrayList<AwardDescBO>();
		List<PkAward> list = pkAwardDao.getAll();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				PkAward pkAward = list.get(i);
				AwardDescBO bo = new AwardDescBO();
				bo.setAwardId(pkAward.getAwardId());
				bo.setName(pkAward.getName());
				bo.setScore(pkAward.getScore());
				bo.setImgId(pkAward.getImgId());
				bo.setDescription(pkAward.getDescription());

				List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(pkAward.getTools());
				if (!dropToolBOList.isEmpty()) {
					bo.setToolId(dropToolBOList.get(0).getToolId());
					bo.setToolType(dropToolBOList.get(0).getToolType());
				} else {
					bo.setToolId(0);
					bo.setToolType(0);
				}

				boList.add(bo);
			}
		}
		return boList;
	}

	@Override
	public boolean isGroupFirst(String userId) {
		User user = userService.get(userId);
		List<Integer> range = getRange(PkHelper.getGroup(user.getLevel()));
		List<UserPkInfo> list = userPkInfoDao.getGroupRanks(range.get(0), range.get(1));
		if (list != null && list.size() > 0) {
			if (list.get(0).getUserId().equals(userId)) {
				return true;
			}
		}
		return false;
	}

	public int getUserGrank(String userId, int pid) {
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		List<Integer> range = getRange(PkHelper.getGroup(userPkInfo.getUser_level()));
		List<UserPkInfo> list = userPkInfoDao.getGroupRanks(range.get(0), range.get(1));
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRank() == userPkInfo.getRank()) {
				userPkInfo.setgRank(i + 1);
				break;
			}
		}
		return userPkInfo.getgRank();
	}

	@Override
	public List<PkGroupFirstBo> getGrankFirst() {
		List<PkGroupFirstBo> list = new ArrayList<PkGroupFirstBo>();
		for (int i = 13; i >= 1; i--) {
			List<Integer> range = getRange(i);
			UserPkInfo userPkInfo = userPkInfoDao.getGroupFirstRanks(range.get(0), range.get(1));
			if (userPkInfo != null) {

				User user = userService.get(userPkInfo.getUserId());
				PkGroupFirstBo pkGroupFirstBo = new PkGroupFirstBo();
				pkGroupFirstBo.setGroup(PkHelper.getGroup(userPkInfo.getUser_level()));
				pkGroupFirstBo.setUsername(user.getUsername());

				list.add(pkGroupFirstBo);
			}
		}
		return list;
	}

	@Override
	public List<PkPlayerBO> getGrankTen(int gid) {
		List<PkPlayerBO> bos = new ArrayList<PkPlayerBO>();
		List<Integer> range = getRange(gid);
		List<UserPkInfo> list = userPkInfoDao.getGrankTen(range.get(0), range.get(1));
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				UserPkInfo userPkInfo = list.get(i);
				User user = userService.get(userPkInfo.getUserId());
				PkPlayerBO pkPlayerBO = new PkPlayerBO();
				pkPlayerBO.setGRank(i + 1);
				pkPlayerBO.setRank(userPkInfo.getRank());

				pkPlayerBO.setLevel(userPkInfo.getUser_level());
				pkPlayerBO.setNickname(userPkInfo.getUsername());
				pkPlayerBO.setVipLevel(user.getVipLevel());
				List<UserHeroBO> userHeroBoList = heroService.getUserHeroList(user.getUserId(), 1);
				pkPlayerBO.setHeroList(userHeroBoList);
				bos.add(pkPlayerBO);
			}
		}
		return bos;
	}

	@Override
	public List<PkPlayerBO> getTotalTen() {
		List<PkPlayerBO> bos = new ArrayList<PkPlayerBO>();
		List<UserPkInfo> list = userPkInfoDao.getTotalTen();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				UserPkInfo userPkInfo = list.get(i);
				User user = userService.get(userPkInfo.getUserId());
				PkPlayerBO pkPlayerBO = new PkPlayerBO();
				pkPlayerBO.setLevel(userPkInfo.getUser_level());
				pkPlayerBO.setNickname(userPkInfo.getUsername());
				pkPlayerBO.setVipLevel(user.getVipLevel());
				pkPlayerBO.setRank(userPkInfo.getRank());
				List<UserHeroBO> userHeroBoList = heroService.getUserHeroList(user.getUserId(), 1);
				pkPlayerBO.setHeroList(userHeroBoList);
				bos.add(pkPlayerBO);
			}
		}
		return bos;
	}

	@Override
	public List<PkGroupAwardLogBo> getAwardLogBos(int groupId) {

		List<PkGroupAwardLog> list = pkGroupAwardLogDao.getList(groupId);
		List<PkGroupAwardLogBo> pkGroupAwardLogBos = new ArrayList<PkGroupAwardLogBo>();
		if (list != null && list.size() > 0) {
			for (PkGroupAwardLog entity : list) {
				PkGroupAwardLogBo pkGroupAwardLogBo = new PkGroupAwardLogBo();

				List<DropToolBO> dropToolBOList = new ArrayList<DropToolBO>();

				PkGroupAward pkGroupAward = pkGroupAwardDao.getGroupAward(entity.getGroupId(), entity.getGrank());

				List<DropToolBO> dropToolBOs = DropToolHelper.parseDropTool(pkGroupAward.getToolIds());
				dropToolBOList.addAll(dropToolBOs);
				DropToolBO bo = new DropToolBO(ToolType.COPPER, ToolType.COPPER, pkGroupAward.getCopper());
				dropToolBOList.add(bo);
				pkGroupAwardLogBo.setDropToolBOList(dropToolBOList);
				pkGroupAwardLogBo.setGrank(entity.getGrank());
				// pkGroupAwardLogBo.setTitle(entity.getTitle());
				pkGroupAwardLogBo.setUsername(entity.getUsername());
				pkGroupAwardLogBos.add(pkGroupAwardLogBo);
			}
		}
		return pkGroupAwardLogBos;
	}

	@Override
	public CommonDropBO updateisGet(String userId) {

		CommonDropBO commonDropBO = new CommonDropBO();
		User user = userService.get(userId);
		PkGroupAwardLog pkGroupAwardLog = pkGroupAwardLogDao.get(userId);
		if (null == pkGroupAwardLog) {
			throw new ServiceException(ServiceReturnCode.NOT_AWARD, "没有奖励.userId[" + userId + "]");
		}
		if (pkGroupAwardLog.getIsGet() == 1) {
			throw new ServiceException(ServiceReturnCode.AWARD_RECEIVE, "奖励已经领取了.userId[" + userId + "]");
		}

		if (!pkGroupAwardLogDao.updateGet(userId, new Date())) {
			throw new ServiceException(ServiceReturnCode.AWARD_RECEIVE, "奖励已经领取了.userId[" + userId + "]");
		}

		PkGroupAward pkGroupAward = pkGroupAwardDao.getGroupAward(pkGroupAwardLog.getGroupId(), pkGroupAwardLog.getGrank());
		if (!StringUtils.isBlank(pkGroupAward.getToolIds())) {
			List<DropToolBO> dropToolBOs = DropToolHelper.parseDropTool(pkGroupAward.getToolIds());
			if (dropToolBOs != null && dropToolBOs.size() > 0) {

				// 领取奖励时检测背包
				checkBag(userId, dropToolBOs);

				for (DropToolBO dropToolBO : dropToolBOs) {
					int toolType = dropToolBO.getToolType();
					int toolId = dropToolBO.getToolId();
					int toolNum = dropToolBO.getToolNum();

					List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_PK_GROUP_RANK);

					for (DropToolBO dropBO : dropBOList) {
						this.toolService.appendToDropBO(userId, commonDropBO, dropBO);
					}
				}
			}
		}

		// 给银币
		toolService.giveTools(userId, ToolType.COPPER, 0, pkGroupAward.getCopper(), ToolUseType.ADD_PK_GROUP_RANK);

		commonDropBO.setCopper(pkGroupAward.getCopper());

		// 获得称号跑马灯
		PkGetTitleEvent pkGetTitleEvent = new PkGetTitleEvent(userId, user.getUsername(), pkGroupAwardLog.getTitle());
		eventServcie.dispatchEvent(pkGetTitleEvent);

		return commonDropBO;

	}

	// 发放组排名奖励
	private void rewardPkGroupRankAward() {

		// 删除前天的记录
		pkGroupAwardLogDao.deleteRecord();

		pkGroupAwardLogDao.updateHostory();

		PkGroupAwardLog pkGroupAwardLog;
		List<UserPkInfo> userPkInfos = new ArrayList<UserPkInfo>();

		for (int i = 1; i <= 13; i++) {
			List<Integer> range = getRange(i);
			userPkInfos = userPkInfoDao.getGrankTen(range.get(0), range.get(1));
			if (userPkInfos != null && userPkInfos.size() > 0) {
				for (int j = 0; j < userPkInfos.size(); j++) {

					pkGroupAwardLog = new PkGroupAwardLog();
					PkGroupAward pkGroupAward = pkGroupAwardDao.getGroupAward(i, j + 1);
					pkGroupAwardLog.setPastTime(DateUtils.getAfterTime(new Date(), 60 * 60 * 24));
					pkGroupAwardLog.setGrank(j + 1);
					pkGroupAwardLog.setGroupId(i);
					pkGroupAwardLog.setIsGet(0);
					pkGroupAwardLog.setTitle(StringUtils.isBlank(pkGroupAward.getTitle()) ? "" : pkGroupAward.getTitle());
					pkGroupAwardLog.setUserId(userPkInfos.get(j).getUserId());
					pkGroupAwardLog.setUsername(userPkInfos.get(j).getUsername());

					pkGroupAwardLogDao.add(pkGroupAwardLog);
				}
			}
		}
	}
}
