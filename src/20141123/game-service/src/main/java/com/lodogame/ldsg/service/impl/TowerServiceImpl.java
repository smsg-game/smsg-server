package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ConfigDataDao;
import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.game.dao.SystemFloorObjDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.dao.SystemTowerMapDao;
import com.lodogame.game.dao.SystemUserLevelDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserTowerDao;
import com.lodogame.game.dao.UserTowerFloorDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserTowerBO;
import com.lodogame.ldsg.bo.UserTowerMapDataBO;
import com.lodogame.ldsg.constants.ActivityTargetType;
import com.lodogame.ldsg.constants.ConfigKey;
import com.lodogame.ldsg.constants.LogTypes;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.TowerObjType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.helper.TowerHelper;
import com.lodogame.ldsg.service.ActivityTaskService;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.TowerService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.ForcesDropTool;
import com.lodogame.model.SystemFloorObj;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemScene;
import com.lodogame.model.SystemTowerMap;
import com.lodogame.model.SystemUserLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserTower;
import com.lodogame.model.UserTowerFloor;

public class TowerServiceImpl implements TowerService {

	private static final Logger logger = Logger.getLogger(TowerServiceImpl.class);

	private static final Logger statLogger = Logger.getLogger("stat");

	/**
	 * 最大层数
	 */
	protected static final int MAX_FLOOR = 100;

	/**
	 * 最大关卡数
	 */
	protected static final int MAX_STAGE = 20;

	@Autowired
	private UserTowerFloorDao userTowerFloorDaoCacheImpl;

	@Autowired
	private UserTowerDao userTowerDaoCacheImpl;

	@Autowired
	private SystemTowerMapDao systemTowerMapDaoMysqlImpl;

	@Autowired
	private SystemFloorObjDao systemFloorObjDaoMysqlImpl;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private BattleService battleService;

	@Autowired
	private ForcesDropToolDao forcesDropToolDao;

	@Autowired
	private SystemSceneDao systemSceneDao;

	@Autowired
	private SystemUserLevelDao systemUserLevelDao;

	@Autowired
	private VipService vipService;

	@Autowired
	private ConfigDataDao configDataDao;

	@Autowired
	public UnSynLogService unSynLogService;

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;
	
	@Autowired
	private ActivityTaskService activityTaskService;

	@Override
	public List<UserTowerBO> enter(String uid) {
		// 获取用户转生塔关卡信息并返回
		List<UserTower> userTowers = userTowerDaoCacheImpl.getUserTower(uid);
		// 组装成BO对象
		List<UserTowerBO> userTowerBOs = new ArrayList<UserTowerBO>();

		if (userTowers == null || userTowers.isEmpty()) {
			// 新建楼层数据
			userTowerDaoCacheImpl.newStage(uid, 1, 1);
			userTowers = userTowerDaoCacheImpl.getUserTower(uid);
		} else {
			// 删除最大楼层以外的楼层，用于兼容错误的数据
			clearStage(userTowers);
			UserTower ut = userTowers.get(0);
			// 不是同一天，则reset数据
			if (!DateUtils.isSameDay(ut.getCreatedTime(), new Date())) {
				return reset(uid, false);
			}
		}

		for (UserTower uts : userTowers) {
			packUserTowerBO(userTowerBOs, uts);
		}

		return userTowerBOs;
	}

	private void clearStage(List<UserTower> userTowers) {
		List<UserTower> delTmp = new ArrayList<UserTower>();
		for (UserTower bo : userTowers) {
			if (bo.getStage() > MAX_STAGE) {
				delTmp.add(bo);
			}
		}
		if (delTmp != null) {
			for (UserTower bo : delTmp) {
				userTowers.remove(bo);
			}
		}
	}

	private void packUserTowerBO(List<UserTowerBO> userTowerBOs, UserTower uts) {
		UserTowerBO bo = new UserTowerBO();
		bo.setFloor(uts.getFloor());
		bo.setStage(uts.getStage());
		bo.setStatus(uts.getStatus());
		bo.setTimes(uts.getTimes());
		userTowerBOs.add(bo);
	}

	@Override
	public List<UserTowerMapDataBO> enterFloor(String uid, int floor) {
		List<UserTowerMapDataBO> bos = null;
		// 进入宝塔关卡，判断是否有正在打的层数，如果有则直接返回该层数据
		List<UserTowerFloor> mds = userTowerFloorDaoCacheImpl.getUserTowerFloor(uid, floor);
		// 如果没有，则生成楼层数据，并返回
		if (mds == null || mds.isEmpty()) {
			bos = makeUserTowerFloorBO(uid, floor);
			// 将数据保存
			saveUserTowerMapDatas(uid, bos);
		} else {
			bos = packageUserTowerMapDataBO(mds);
		}
		return bos;
	}

	private void saveUserTowerMapDatas(String uid, List<UserTowerMapDataBO> bos) {
		if (bos != null) {
			for (UserTowerMapDataBO bo : bos) {
				UserTowerFloor data = new UserTowerFloor();
				Date now = new Date();
				data.setMapId(bo.getMapId());
				data.setFloor(bo.getFloor());
				data.setCreatedTime(now);
				data.setObjId(bo.getObjId());
				data.setObjType(bo.getObjType());
				data.setPassed(bo.getPassed());
				data.setPos(bo.getPosition());
				data.setUpdatedTime(now);
				data.setUserId(uid);
				userTowerFloorDaoCacheImpl.saveUserTowerFloor(data);
				bo.setUuid(DateUtils.getDateStr(now.getTime(), "yyyy-MM-dd HH:mm:ss"));
			}
		}
	}

	/**
	 * 创建用户楼层数据，用于第一次进入某个楼层时使用
	 * 
	 * @param uid
	 * @param floor
	 * @return
	 */
	private List<UserTowerMapDataBO> makeUserTowerFloorBO(String uid, int floor) {
		// 随机获得地图数据
		SystemTowerMap map = TowerHelper.getRandomOneMapData();
		// 获取可用的站位索引
		String blockMask = map.getBlockMask();
		Set<Integer> enableMaskIdxs = TowerHelper.getEnableMasks(blockMask, map);
		// 随机生成怪物
		List<SystemFloorObj> forcesList = TowerHelper.getRandomForces(floor);
		// 随机生成宝箱
		List<SystemFloorObj> boxList = TowerHelper.getRandomBox(floor);
		// 随机指定宝箱位置
		return TowerHelper.creteUserTowerMapDataBOs(floor, enableMaskIdxs, forcesList, boxList, map);
	}

	private List<UserTowerMapDataBO> packageUserTowerMapDataBO(List<UserTowerFloor> mds) {
		List<UserTowerMapDataBO> bos = new ArrayList<UserTowerMapDataBO>();
		for (UserTowerFloor utf : mds) {
			UserTowerMapDataBO bo = new UserTowerMapDataBO();
			bo.setMapId(utf.getMapId());
			bo.setFloor(utf.getFloor());
			bo.setObjId(utf.getObjId());
			bo.setObjType(utf.getObjType());
			bo.setPassed(utf.getPassed());
			bo.setPosition(utf.getPos());
			bo.setUuid(EncryptUtil.getMD5(DateUtils.getDateStr(utf.getUpdatedTime().getTime(), "yyyy-MM-dd HH:mm:ss")));
			bos.add(bo);
		}
		return bos;
	}

	@Override
	public void useTool(String uid) {
		// 减少火把
		toolService.reduceTool(uid, ToolType.MATERIAL, ToolId.TOOL_ID_TORCH, 1, ToolUseType.REDUCE_MOVE_TOWER);
	}

	@Override
	public void pickUpBox(String uid, int floor, int pos, int tid) {

		UserTowerFloor utf = userTowerFloorDaoCacheImpl.getUserTowerFloor(uid, floor, pos, tid);
		if (utf == null || utf.getObjType() != TowerObjType.BOX) {
			throw new ServiceException(ServiceReturnCode.FAILD, "宝箱不存在");
		}
		if (utf.getPassed() == 1) {
			throw new ServiceException(ServiceReturnCode.FAILD, "宝箱已经被打开");
		}

		reduceTool(uid);

		// return toolService.openGiftBox(uid, tid, eventHandle);
		this.toolService.addTool(uid, ToolType.GIFT_BOX, tid, 1, ToolUseType.ADD_TOWER_PICK_UP);
		userTowerFloorDaoCacheImpl.updateObjStatus(uid, floor, pos, 1);
	}

	@Override
	public List<UserTowerBO> reset(String uid, boolean useMoney) {

		User user = this.userService.get(uid);
		int vipLevel = user.getVipLevel();

		if (useMoney) {

			int resetTowerTimesLimit = this.vipService.getResetTowerTimesLimit(vipLevel);
			int userDayResetTowerTimes = this.userDailyGainLogDao.getUserDailyGain(uid, UserDailyGainLogType.RESET_TOWER);

			if (userDayResetTowerTimes >= resetTowerTimesLimit) {// 次数不足
				String message = "重置转生塔失败，次数不足.userId[" + uid + "], resetForcesTimesLimit[" + resetTowerTimesLimit + "], userDayResetForcesTimes[" + userDayResetTowerTimes + "]";
				logger.info(message);
				throw new ServiceException(VIP_LEVEL_NOT_ENOUGH, message);
			}

			int resetTowerGold = configDataDao.getInt(ConfigKey.RESET_TOWER_GOLD, 200);

			if (!userService.reduceGold(uid, resetTowerGold, ToolUseType.REDUCE_RESET_TOWER, user.getLevel())) {
				throw new ServiceException(VIP_LEVEL_NOT_ENOUGH, "金币不足");
			}

			this.userDailyGainLogDao.addUserDailyGain(uid, UserDailyGainLogType.RESET_TOWER, 1);
		}

		// 恢复关卡打过的状态
		userTowerDaoCacheImpl.resetTowerStage(uid);
		// 清空楼层数据
		userTowerFloorDaoCacheImpl.clearTowerFloorData(uid);
		// 返回新的关卡信息
		return enter(uid);
	}

	/**
	 * 初始化宝塔数据
	 */
	public void init() {
		loadSystemTowerData();
	}

	@Override
	public void loadSystemTowerData() {
		TowerHelper.init();
		// 初始化地图数据
		List<SystemTowerMap> towerMaps = systemTowerMapDaoMysqlImpl.getAll();
		TowerHelper.setSystemTowerMaps(towerMaps);
		// 初始化对象数据
		List<SystemFloorObj> objs = systemFloorObjDaoMysqlImpl.getAll();
		for (SystemFloorObj obj : objs) {
			if (obj.getObjType() == TowerObjType.MONSTER) {
				TowerHelper.addForces(obj.getFloor(), obj);
			} else {
				TowerHelper.addBox(obj.getFloor(), obj);
			}
		}
	}

	@Override
	public void towerFight(final String uid, final int floor, final int pos, final int forcesId, final EventHandle eventHandle) {

		final int curStage = (floor - 1) / 5 + 1;
		// 验证怪物是否存在
		final UserTowerFloor utf = userTowerFloorDaoCacheImpl.getUserTowerFloor(uid, floor, pos, forcesId);
		if (utf == null || utf.getObjType() != TowerObjType.MONSTER) {
			throw new ServiceException(ServiceReturnCode.FAILD, "怪物不存在");
		}

		if (utf.getPassed() == 1) {
			throw new ServiceException(ServiceReturnCode.FAILD, "怪物已经被打开");
		}

		reduceTool(uid);

		// 读取怪物信息
		BattleBO attack = new BattleBO();
		List<BattleHeroBO> userBattleBattleHeroBO = this.heroService.getUserBattleHeroBOList(uid);
		attack.setBattleHeroBOList(userBattleBattleHeroBO);

		// 读取用户武将信息
		List<BattleHeroBO> battleHeroBOList = this.forcesService.getForcesHeroBOList(forcesId);
		BattleBO defense = new BattleBO();
		defense.setBattleHeroBOList(battleHeroBOList);

		// 用户背包检测
		// userService.checkUserHeroBagLimit(uid);
		// userService.checkUserEquipBagLimit(uid);

		final SystemForces systemForces = this.systemForcesDao.get(forcesId);
		if (systemForces == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "攻打部队失败,怪物不存在.userId[" + uid + "], forcesId[" + forcesId + "]");
		}

		// TODO 缓存读取
		SystemScene systemScene = this.systemSceneDao.get(systemForces.getSceneId());
		final int imgId = systemScene.getImgId();
		// final int imgId = 1;

		final User user = this.userService.get(uid);
		// 开始战斗
		battleService.fight(attack, defense, 1, new EventHandle() {

			public boolean handle(Event event) {

				if (event instanceof BattleResponseEvent) {
					try {
						int flag = event.getInt("flag");

						// 统计
						statLogger.info("action[attackForces], userId[" + uid + "], userLevel[" + user.getLevel() + "], forcesId[" + forcesId + "], flag[" + flag + "]");

						if (flag == 1) {// 打怪打赢了

							// 处理怪物部队通过

							// 获取掉落
							CommonDropBO forcesDropBO = pickUpForcesDropToolList(user, forcesId);
							event.setObject("forcesDropBO", forcesDropBO);
							// 检测是否通过关卡，通过则生成新的关卡数据，判断是否通关是根据当前怪物是否BOSS以及楼层是否可以整除5
							SystemFloorObj floorForces = TowerHelper.getFloorForces(floor, forcesId);
							if (floorForces.getIsBoss() == 1) {
								if (floor % 5 == 0) {
									int nextStage = floor / 5 + 1;
									userTowerDaoCacheImpl.newStage(uid, nextStage, floor + 1);
									// 更新当前关卡状态为打过
									userTowerDaoCacheImpl.updateTowerStatus(uid, curStage, 1, 1);
								} else {
									// 当为BOSS时，表示可进入下一层，更新关卡当前楼层为下一层
									if (floor + 1 < MAX_FLOOR) {
										userTowerDaoCacheImpl.updateStageFloor(uid, curStage, floor + 1);
									}
								}
							}

							// 更新位置状态，表示已经打过当前位置怪物
							userTowerFloorDaoCacheImpl.updateObjStatus(uid, floor, pos, 1);
						} else {
							// 失败后,更新当前楼层状态,为失败
							userTowerDaoCacheImpl.updateTowerStatus(uid, curStage, -1, 0);
						}
						// 活跃度奖励
						activityTaskService.updateActvityTask(uid, ActivityTargetType.TOWER_BATTLE, 1);
						
						// 写日志
						unSynLogService.userBattleLog(uid, user.getLevel(), String.valueOf(forcesId), LogTypes.BATTLE_TYPE_TOWER, flag, new Date());
						event.setObject("bgid", imgId);

						// 继续广播出去
						eventHandle.handle(event);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}

				return true;
			}
		});

	}

	private void reduceTool(final String uid) {
		if (!toolService.reduceTool(uid, ToolType.MATERIAL, ToolId.TOOL_ID_TORCH, 1, ToolUseType.REDUCE_MOVE_TOWER)) {
			throw new ServiceException(TORCE_NOT_ENOUGH, "火把不足");
		}
	}

	private CommonDropBO pickUpForcesDropToolList(User user, int forcesId) {

		CommonDropBO forcesDropBO = new CommonDropBO();

		int oldLevel = user.getLevel();

		SystemUserLevel systemUserLevel = this.systemUserLevelDao.get(user.getLevel());
		int oldAttack = systemUserLevel.getAttack();
		int oldDefense = systemUserLevel.getDefense();

		int rand = RandomUtils.nextInt(10000);

		logger.debug("获取宝塔怪物部队掉落道具.userId[" + user.getUserId() + "], forcesId[" + forcesId + "]");
		List<ForcesDropTool> forcesDropToolList = this.forcesDropToolDao.getForcesDropToolList(forcesId);
		for (ForcesDropTool forcesDropTool : forcesDropToolList) {

			List<DropToolBO> dropToolBOList = this.pickUpForcesDropTool(user, forcesDropTool, rand);
			if (dropToolBOList == null) {
				continue;
			}

			for (DropToolBO dropToolBO : dropToolBOList) {
				if (dropToolBO.getToolType() == ToolType.EXP) {
					forcesDropBO.setVipAddExp((int) (vipService.getExpAddRatio(user.getVipLevel()) * dropToolBO.getToolNum()));
				} else if (dropToolBO.getToolType() == ToolType.COPPER) {
					forcesDropBO.setVipAddCopper((int) (vipService.getCopperAddRatio(user.getVipLevel()) * dropToolBO.getToolNum()));
				}

				this.toolService.appendToDropBO(user.getUserId(), forcesDropBO, dropToolBO);
			}

		}

		int newLevel = user.getLevel();
		if (newLevel > oldLevel) {
			int levelUp = newLevel - oldLevel;
			systemUserLevel = this.systemUserLevelDao.get(newLevel);
			forcesDropBO.setLevelUp(levelUp);
			forcesDropBO.setAttackAdd(systemUserLevel.getAttack() - oldAttack);
			forcesDropBO.setDefenseAdd(systemUserLevel.getDefense() - oldDefense);
		}

		return forcesDropBO;
	}

	/**
	 * 给具体的掉落，需计算概率
	 * 
	 * @param userId
	 * @param forcesDropTool
	 */
	private List<DropToolBO> pickUpForcesDropTool(User user, ForcesDropTool forcesDropTool, int rand) {

		List<DropToolBO> dropToolBOList = null;

		int toolType = forcesDropTool.getToolType();
		int toolId = forcesDropTool.getToolId();
		int toolNum = forcesDropTool.getToolNum();
		int lowerNum = forcesDropTool.getLowerNum();
		int upperNum = forcesDropTool.getUpperNum();

		if (!DropToolHelper.isDrop(lowerNum, upperNum, rand)) {
			logger.debug("该道具未掉落.toolType[" + toolType + "]");
			return null;
		}

		// vip加成
		if (toolType == ToolType.COPPER) {
			toolNum += vipService.getCopperAddRatio(user.getVipLevel()) * toolNum;
		} else if (toolType == ToolType.EXP) {
			toolNum += vipService.getExpAddRatio(user.getVipLevel()) * toolNum;
		}

		if (toolType == ToolType.GOLD && !userService.checkUserGoldGainLimit(user.getUserId(), toolNum)) {
			return null;
		}

		dropToolBOList = toolService.giveTools(user.getUserId(), toolType, toolId, toolNum, ToolUseType.ADD_FORCES);

		return dropToolBOList;
	}
}
