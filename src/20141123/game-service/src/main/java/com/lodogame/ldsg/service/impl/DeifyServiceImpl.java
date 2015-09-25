package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.DeifyRoomDao;
import com.lodogame.game.dao.ForcesDropToolDao;
import com.lodogame.game.dao.LogDao;
import com.lodogame.game.dao.SystemDeifyAttrDao;
import com.lodogame.game.dao.SystemDeifyDefineDao;
import com.lodogame.game.dao.SystemDeifyNodeDao;
import com.lodogame.game.dao.SystemDeifyTowerDao;
import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemHeroDao;
import com.lodogame.game.dao.SystemHeroDeifyUpgradeDao;
import com.lodogame.game.dao.SystemLevelExpDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.BattleStartBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DeifyBo;
import com.lodogame.ldsg.bo.DeifyRoomInfoBO;
import com.lodogame.ldsg.bo.DeifyTowerInfoBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ForcesStatus;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.DeifyEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.DeifyService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventServcie;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.DeifyRoomInfo;
import com.lodogame.model.DeifyTowerInfo;
import com.lodogame.model.ForcesDropTool;
import com.lodogame.model.SystemDeifyAttr;
import com.lodogame.model.SystemDeifyDefine;
import com.lodogame.model.SystemDeifyNode;
import com.lodogame.model.SystemDeifyTower;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemForces;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemHeroDeifyUpgrade;
import com.lodogame.model.SystemLevelExp;
import com.lodogame.model.SystemScene;
import com.lodogame.model.SystemTool;
import com.lodogame.model.User;
import com.lodogame.model.UserEquip;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserHero;

public class DeifyServiceImpl implements DeifyService {

	private static final Logger LOG = Logger.getLogger(DeifyServiceImpl.class);

	/**
	 * 修神塔开放等级
	 */
	private static final int ENTER_TOWER_LEVEL = 70;

	/**
	 * 占领修炼室需要消耗的体力
	 */
	private static final int OCCUPY_NEED_POWER_NUM = 5;

	private static final int DOUBLE_PROFIT_1_HOUR = 1;

	public static final int DOUBLE_PROFIT_OPEN = 1;
	public static final int DOUBLE_PROFIT_CLOSED = 2;

	/**
	 * 修炼塔在每天8点开放
	 */
	private static final int TOWER_OPEN_TIME = 8;

	/**
	 * 开启30分钟修炼保护
	 */
	private static final int PROTECT_1_HOUR = 1;

	private static final int ROOM_STATUS_EMPTY = 3;
	private static final int ROOM_STATUS_LOGIN_USERN = 1;
	private static final int ROOM_STATUS_NOT_LOGIN_USER = 2;

	/**
	 * 购买一小时的延长修炼时间
	 */
	private static final int BUY_DEIFY_TIME_1_HOUR = 1;

	private static final int BUY_DEIFY_TIME_3_HOUR = 2;

	private static final int NOT_BUY_DEIFY_TIME = 0;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private BattleService battleService;

	@Autowired
	private UserHeroDao userHeroDao;

	@Autowired
	private SystemHeroDao systemHeroDao;

	@Autowired
	private SystemDeifyNodeDao systemDeifyNodeDao;

	@Autowired
	private LogDao logDao;

	@Autowired
	private SystemHeroDeifyUpgradeDao systemHeroDeifyUpgradeDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private DeifyRoomDao deifyRoomDao;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemDeifyTowerDao systemDeifyTowerDao;

	@Autowired
	private SystemLevelExpDao systemLevelExpDao;

	@Autowired
	private SystemDeifyDefineDao systemDeifyDefineDao;

	@Autowired
	private SystemDeifyAttrDao systemDeifyAttrDao;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private SystemSceneDao systemSceneDao;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private ForcesDropToolDao forcesDropToolDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private UserEquipDao userEquipDao;

	@Autowired
	private SystemToolDao systemToolDao;

	@Autowired
	private EventServcie eventServcie;

	@Autowired
	private SystemEquipDao systemEquipDao;

	@Override
	public Map<String, Object> getTowerList(String uid) {
		checkEnterTowerCondition(uid);
		DeifyRoomInfo roomInfo = deifyRoomDao.getRoomByUid(uid);
		List<DeifyTowerInfoBO> towerInfoBOList = createTowerInfoBOList();

		Map<String, Object> rt = new HashMap<String, Object>();
		rt.put("twls", towerInfoBOList);

		if (roomInfo == null) {
			rt.put("utid", 0);
			rt.put("cd", 0);
		} else {
			rt.put("utid", roomInfo.getTowerId());
			rt.put("cd", roomInfo.getDeifyEndTime().getTime());
		}
		return rt;
	}

	@Override
	public List<DeifyTowerInfoBO> createTowerInfoBOList() {
		List<DeifyTowerInfo> towerInfoList = this.getAllTowerInfoList();
		List<DeifyTowerInfoBO> towerInfoBOList = new ArrayList<DeifyTowerInfoBO>();
		for (DeifyTowerInfo towerInfo : towerInfoList) {
			DeifyTowerInfoBO bo = new DeifyTowerInfoBO();
			BeanUtils.copyProperties(towerInfo, bo);
			towerInfoBOList.add(bo);
		}
		return towerInfoBOList;
	}

	private List<DeifyTowerInfo> getAllTowerInfoList() {

		Map<Integer, DeifyTowerInfo> m = new HashMap<Integer, DeifyTowerInfo>();

		Collection<DeifyRoomInfo> list = this.deifyRoomDao.getAllRoomInfoList();
		for (DeifyRoomInfo deifyRoomInfo : list) {

			int towerId = deifyRoomInfo.getTowerId();
			DeifyTowerInfo deifyTowerInfo = null;
			if (m.containsKey(towerId)) {
				deifyTowerInfo = m.get(towerId);
			} else {
				deifyTowerInfo = new DeifyTowerInfo();
				deifyTowerInfo.setTowerId(towerId);
				deifyTowerInfo.setOccupiedRoomNum(0);
				deifyTowerInfo.setRoomNum(0);
			}

			if (towerId != 4) {
				deifyTowerInfo.setRoomNum(deifyTowerInfo.getRoomNum() + 1);
				if (StringUtils.isNotEmpty(deifyRoomInfo.getUserId())) {
					deifyTowerInfo.setOccupiedRoomNum(deifyTowerInfo.getOccupiedRoomNum() + 1);
				}
			}

			m.put(towerId, deifyTowerInfo);

		}

		List<DeifyTowerInfo> deifyTowerInfoList = new ArrayList<DeifyTowerInfo>();
		for (DeifyTowerInfo deifyTowerInfo : m.values()) {
			deifyTowerInfoList.add(deifyTowerInfo);
		}

		return deifyTowerInfoList;

	}

	private void checkEnterTowerCondition(String uid) {
		User user = userService.get(uid);
		if (user.getLevel() < ENTER_TOWER_LEVEL) {
			String message = "玩家未到开放等级";
			throw new ServiceException(USER_LEVEL_NOT_ENOUGH, message);
		}

	}

	@Override
	public List<DeifyRoomInfoBO> getRoomList(String userId, int towerId) {
		List<DeifyRoomInfoBO> boList = new ArrayList<DeifyRoomInfoBO>();
		List<DeifyRoomInfo> roomInfoList = deifyRoomDao.getRoomListByTowerId(towerId);
		for (DeifyRoomInfo room : roomInfoList) {
			if (towerId == 4) {
				if (userId.equals(room.getUserId())) {
					DeifyRoomInfoBO bo = createDeifyRoomInfoBO(userId, room);
					boList.add(bo);
					break;
				}
				continue;
			}

			DeifyRoomInfoBO bo = createDeifyRoomInfoBO(userId, room);
			boList.add(bo);
		}

		if (towerId == 4 && boList.size() == 0) {
			DeifyRoomInfo roomInfo = new DeifyRoomInfo();
			roomInfo.setRoomId(1);
			DeifyRoomInfoBO infoBO = createDeifyRoomInfoBO(userId, roomInfo);
			boList.add(infoBO);
		}

		return boList;
	}

	/**
	 * 
	 * @param userId
	 *            登陆用户的 userId
	 */
	private DeifyRoomInfoBO createDeifyRoomInfoBO(String userId, DeifyRoomInfo info) {
		DeifyRoomInfoBO bo = new DeifyRoomInfoBO();
		String roomUserId = info.getUserId();
		bo.setRoomId(info.getRoomId());

		if (StringUtils.isBlank(roomUserId) == false) {
			User user = userService.get(roomUserId);
			bo.setDeifyStartTime(info.getDeifyStartTime().getTime());
			bo.setDeifyEndTime(info.getDeifyEndTime().getTime());
			bo.setDoubleProfitEndTime(info.getDoubleProfitEndTime().getTime());
			bo.setLevel(user.getLevel());
			bo.setProtectEndTime(info.getProtectEndTime().getTime());
			bo.setUserId(roomUserId);
			bo.setVip(user.getVipLevel());
			bo.setName(user.getUsername());
		}

		if (isProtected(info) == true) {
			bo.setProtectStatus(1);
		} else {
			bo.setProtectStatus(2);
		}

		if (isDoubleProfit(info) == true) {
			bo.setDoubleProfitStatus(1);
		} else {
			bo.setDoubleProfitStatus(2);
		}

		boolean isEmptyRoom = isEmptyRoom(info);
		if (isEmptyRoom == true) {
			bo.setRoomStatus(ROOM_STATUS_EMPTY);
		} else if (userId.equals(roomUserId)) {
			bo.setRoomStatus(ROOM_STATUS_LOGIN_USERN);
		} else {
			bo.setRoomStatus(ROOM_STATUS_NOT_LOGIN_USER);
		}

		List<UserHeroBO> userHeroList = heroService.getUserHeroList(roomUserId, 1);
		List<Integer> systemHeroIdList = new ArrayList<Integer>();
		for (UserHeroBO heroBO : userHeroList) {
			systemHeroIdList.add(heroBO.getSystemHeroId());
		}
		bo.setSystemHeroIdList(systemHeroIdList);

		return bo;
	}

	private boolean isEmptyRoom(DeifyRoomInfo roomInfo) {

		if (roomInfo == null) {
			return false;
		}

		if (StringUtils.isBlank(roomInfo.getUserId())) {
			return true;
		}
		return false;
	}

	private boolean isRoomExpired(DeifyRoomInfo roomInfo) {
		Date now = new Date();
		Date deifyEndTime = roomInfo.getDeifyEndTime();
		if (!StringUtils.isBlank(roomInfo.getUserId()) && now.after(deifyEndTime)) {
			return true;
		}
		return false;
	}

	private boolean isProtected(DeifyRoomInfo roomInfo) {
		Date protectStartTime = roomInfo.getProtectStartTime();
		Date protectEndTime = roomInfo.getProtectEndTime();

		// 每次占领一个房间（不管是不是空房间，都会把保护开始时间置为占领时的时间，把保护结束时间置为占领时间之后的30秒（因为有30秒的系统保护时间）
		// 因此，如果保护结束时间和保护开始时间只差大于30秒，说明在这次修炼中曾经开启过保护
		int secondDiff = DateUtils.getSecondDiff(protectStartTime, protectEndTime);
		if (secondDiff > 30) {
			return true;
		}
		return false;
	}

	private boolean isDoubleProfit(DeifyRoomInfo roomInfo) {
		Date doubleProfitStartTime = roomInfo.getDoubleProfitStartTime();
		Date doubleProfitEndTime = roomInfo.getDoubleProfitEndTime();

		// 每次占领一个房间（不管是不是空房间，都会把双倍的开始时间和结束时间置为占领时候的时间
		// 因此，如果双倍的开始时间和结束时间不相同，则说明在这次修炼时曾经开启过双倍
		if (doubleProfitStartTime.equals(doubleProfitEndTime) == false) {
			return true;
		}

		return false;
	}

	@Override
	public Map<String, Object> occupy(final String uid, final int towerId, final int roomId, final EventHandle eventHandle) {
		checkOccupyCondition(uid, towerId, roomId);
		DeifyRoomInfo previousRoom = deifyRoomDao.getRoomByUid(uid);
		if (previousRoom != null) {
			cancelDeify(previousRoom);
		}

		// 要占领的房间
		final DeifyRoomInfo room = getRoomToOccupy(towerId, roomId);
		final DeifyRoomInfo roomToOccupy = new DeifyRoomInfo();
		BeanUtils.copyProperties(room, roomToOccupy);
		SystemDeifyTower systemDeifyTower = systemDeifyTowerDao.get(towerId);

		// 空房间
		if (isEmptyRoom(room) == true) {

			DeifyRoomInfo occupiedRoom = occupyARoom(uid, room);

			Map<String, Object> rt = new HashMap<String, Object>();
			rt.put("cd", occupiedRoom.getDeifyEndTime().getTime());
			rt.put("utid", occupiedRoom.getTowerId());
			rt.put("rol", getRoomList(uid, towerId));

			pushTowerList(uid);
			pushRoomList(uid, towerId);
			pushBattleStart();

			userService.reduceCopper(uid, systemDeifyTower.getCopper(), ToolUseType.REDUCE_DEIFY_OCCUPY);

			return rt;

		} else {

			User user = userService.get(uid);

			if (user.getPower() < OCCUPY_NEED_POWER_NUM) {
				String message = "用户体力不足，不可以占领修炼室 uid[" + uid + "]";
				throw new ServiceException(DeifyService.OCCUPY_POWER_NOT_ENOUGH, message);
			}

			BattleBO attack = battleService.createBattleBO(uid);
			BattleBO defense = battleService.createBattleBO(room.getUserId());

			battleService.fight(attack, defense, 10, new EventHandle() {

				@Override
				public boolean handle(Event event) {
					if (event instanceof BattleResponseEvent) {
						int flag = event.getInt("flag");
						if (flag == 1) {
							DeifyRoomInfo roomInfo = occupyARoom(uid, room);
							event.setObject("cd", roomInfo.getDeifyEndTime().getTime());
							event.setObject("utid", roomInfo.getTowerId());
							int deifyProfit = getDeifyProfit(roomToOccupy);
							LOG.info("=========== deifyProfit[" + deifyProfit + "]");
							String userId = roomToOccupy.getUserId();

							if (deifyProfit != 0) {
								userService.addMind(userId, deifyProfit, ToolUseType.ADD_DEIFY_MIND);
							}

							pushUserInfo(userId);
							pushDeifyStatus(roomToOccupy);

						} else {
							event.setObject("cd", 0);
							event.setObject("utid", 0);

						}

						event.setObject("rol", getRoomList(uid, towerId));
						event.setObject("atu", userService.get(uid).getUsername());
						event.setObject("defu", userService.get(roomToOccupy.getUserId()).getUsername());
						event.setObject("bgid", 1);
						event.setObject("aod", 1);
						eventHandle.handle(event);
					}

					pushReport(roomToOccupy, event);
					pushBattleStart();
					return true;
				}

			});

			userService.reduceCopper(uid, systemDeifyTower.getCopper(), ToolUseType.REDUCE_DEIFY_OCCUPY);
			userService.reducePower(uid, OCCUPY_NEED_POWER_NUM, ToolUseType.REDUCE_DEIFY_OCCUPY);
			pushBattleStart();
			pushTowerList(uid);
			pushRoomList(uid, towerId);

		}

		return null;

	}

	private void pushUserInfo(String userId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_USER_INFO);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);

		commandDao.add(command);
	}

	private DeifyRoomInfo getRoomToOccupy(final int towerId, final int roomId) {
		DeifyRoomInfo room = new DeifyRoomInfo();
		if (towerId == 4) {
			room.setTowerId(towerId);
			room.setRoomId(1);
			room.setId(IDGenerator.getID());
		} else {
			room = deifyRoomDao.getRoom(towerId, roomId);
		}
		return room;
	}

	private void pushBattleStart() {
		Map<String, String> params = new HashMap<String, String>();
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_BATTLE_START);
		command.setType(CommandType.PUSH_ALL);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}

	private DeifyRoomInfo occupyARoom(String uid, DeifyRoomInfo deifyRoomInfo) {

		deifyRoomInfo.setUserId(uid);
		Date now = new Date();
		deifyRoomInfo.setDeifyStartTime(now);
		deifyRoomInfo.setDeifyEndTime(DateUtils.add(now, Calendar.HOUR, 1));
		deifyRoomInfo.setProtectStartTime(now);
		deifyRoomInfo.setProtectEndTime(DateUtils.add(now, Calendar.SECOND, 30));
		deifyRoomInfo.setDoubleProfitStartTime(now);
		deifyRoomInfo.setDoubleProfitStartTime(now);
		deifyRoomInfo.setDoubleProfitEndTime(now);

		if (deifyRoomInfo.getTowerId() != 4) {
			deifyRoomDao.update(deifyRoomInfo);
		} else {
			deifyRoomDao.add(deifyRoomInfo);
		}

		return deifyRoomInfo;
	}

	private void checkOccupyCondition(String uid, int towerId, int roomId) {

		checkOpenTime();

		SystemDeifyTower systemDeifyTower = systemDeifyTowerDao.get(towerId);
		User user = userService.get(uid);
		if (user.getCopper() < systemDeifyTower.getCopper()) {
			String message = "用户银币不足，不可以占领修炼室 uid[" + uid + "]";
			throw new ServiceException(DeifyService.OCCUPY_COPPER_NOT_ENOUGH, message);
		}

		DeifyRoomInfo room = deifyRoomDao.getRoom(towerId, roomId);
		if (isEmptyRoom(room) == false) {
			Date protectEndTime = room.getProtectEndTime();
			Date now = new Date();

			if (protectEndTime != null && now.before(protectEndTime)) {
				String message = "该房间还在修炼保护时间内，不可以占领, towerId[" + towerId + "] roomId[" + roomId + "]";
				throw new ServiceException(DeifyService.ROOM_IS_PROTECTED, message);
			}
		}

	}

	@Override
	public int activateNode(String userId, String userHeroId, int systemDeifyNodeId) {

		// 查看武将是否存在
		UserHero userHero = this.userHeroDao.get(userId, userHeroId);

		if (userHero == null) {
			String message = "激活节点失败,用户武将不存在.userHeroId[" + userHeroId + "]";
			LOG.error(message);
			throw new ServiceException(ACTIVITENODE_HERO_NOT_EXIST, message);
		}

		// 少于120级不能化神
		if (userHero.getHeroLevel() < 120) {
			String message = "激活节点失败,用户武将等级不够.userHeroId[" + userHeroId + "]";
			LOG.error(message);
			throw new ServiceException(ACTIVITENODE_HERO_LEVEL_NOT_ENOUGH, message);
		}

		// 节点已被激活
		if (userHero.getDeifyNodeLevel() >= systemDeifyNodeId) {
			String message = "激活节点失败,该节点已经激活.userHeroId[" + userHeroId + "]";
			LOG.error(message);
			throw new ServiceException(ACTIVITENODE_NODE_IS_ACTIVITIED, message);
		}

		// 前置节点未激活
		if (systemDeifyNodeId > 1 && ((systemDeifyNodeId - userHero.getDeifyNodeLevel()) != 1)) {
			String message = "激活节点失败,该节点的前置节点为激活.userHeroId[" + userHeroId + "]" + "systemDeifyNodeid[" + systemDeifyNodeId + "]";
			LOG.error(message);
			throw new ServiceException(ACTIVITENODE_IHERNODE__NOT_GHOST_ACTIVITIED, message);
		}

		// 指定武将是否是鬼将
		SystemHero iSystemHero = systemHeroDao.get(userHero.getSystemHeroId());
		if (iSystemHero.getHeroColor() != 6) {
			String message = "激活节点失败,用户武将不是鬼将.userHeroId[" + userHeroId + "]";
			LOG.error(message);
			throw new ServiceException(ACTIVITENODE_HERO_NOT_GHOST_GENERAL, message);
		}

		// 获取化神节点信息
		SystemHero systemHero = this.systemHeroDao.get(userHero.getSystemHeroId());
		SystemDeifyNode systemDeityNode = this.systemDeifyNodeDao.getSystemDeifyNodeById(systemHero.getHeroId(), systemDeifyNodeId);
		if (systemDeityNode == null) {
			String message = "激活节点失败,节点信息不存在.userHeroId[" + userHeroId + "]" + "systemDeifyNodeid[" + systemDeifyNodeId + "]";
			LOG.error(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		// 扣除道具
		boolean success = userService.reduceMind(userId, systemDeityNode.getNeedGodSoulNum(), ToolUseType.REDUCE_ACTIVITY_NODE);
		if (!success) {
			String message = "激活化神节点失败，神魂不足.userId[" + userId + "], ToolId[" + ToolId.TOOL_MIND_ID + "], toolNum[" + systemDeityNode.getNeedGodSoulNum() + "]";
			LOG.error(message);
			throw new ServiceException(ACTIVITENODE_TOOL_IS_NOT_ENOUGH, message);
		}

		logDao.toolLog(userId, ToolType.MIND, ToolId.TOOL_MIND_ID, systemDeityNode.getNeedGodSoulNum(), ToolUseType.REDUCE_ACTIVITY_NODE, -1, "", true);

		// 更改的等级
		this.userHeroDao.upgradeDeifyNodeLevel(userId, userHeroId, systemDeifyNodeId);

		// 化神
		if (systemDeifyNodeId == 6) {
			SystemHeroDeifyUpgrade systemHeroDeifyUpgrade = this.systemHeroDeifyUpgradeDao.getSystemHeroDeifyUpgradeById(userHero.getSystemHeroId());
			int exp = userHero.getHeroExp() - systemLevelExpDao.getHeroExp(120).getExp();

			SystemLevelExp systemLevelExp = this.systemLevelExpDao.getHeroLevel(exp);
			int level = systemLevelExp.getLevel();
			this.userHeroDao.update(userId, userHero.getUserHeroId(), systemHeroDeifyUpgrade.getAfterDeifySystemHeroId(), level, exp);
		}

		User user = this.userService.get(userId);

		return user.getMind();
	}

	/**
	 * 获取修炼获得的神魂点数
	 * 
	 * @param uid
	 * @return
	 */
	private int getDeifyProfit(DeifyRoomInfo roomInfo) {
		if (isEmptyRoom(roomInfo) == true) {
			return 0;
		}
		int deifyTime = getDeifyMin(roomInfo);
		Date doubleProfitStartTime = roomInfo.getDoubleProfitStartTime();
		Date doubleProfitEndTime = roomInfo.getDoubleProfitEndTime();

		Date now = new Date();
		int doubleProfitTime = 0;

		if (now.after(doubleProfitEndTime)) {
			doubleProfitTime = DateUtils.getMinuteDiff(doubleProfitStartTime, doubleProfitEndTime);
		} else {
			doubleProfitTime = DateUtils.getMinuteDiff(doubleProfitStartTime, now);
		}

		SystemDeifyTower tower = systemDeifyTowerDao.get(roomInfo.getTowerId());

		int output = tower.getOutput();

		int mindNum = output * deifyTime + output * doubleProfitTime;

		int buyDeifyTimeType = getBuyDeifyTimeType(roomInfo);
		if (buyDeifyTimeType == BUY_DEIFY_TIME_1_HOUR) {
			mindNum = (int) Math.ceil(mindNum * 0.9);
		} else if (buyDeifyTimeType == BUY_DEIFY_TIME_3_HOUR) {
			mindNum = (int) Math.ceil(mindNum * 0.8);
		}

		return mindNum;

	}

	private int getDeifyMin(DeifyRoomInfo roomInfo) {
		Date deifyStartTime = roomInfo.getDeifyStartTime();
		Date now = new Date();

		int deifyTime = DateUtils.getMinuteDiff(deifyStartTime, now);
		return deifyTime;
	}

	@Override
	public DeifyRoomInfoBO doubleProfit(String uid, int type) {

		int towerId = 0;
		DeifyRoomInfo room = deifyRoomDao.getRoomByUid(uid);
		if (room != null) {
			towerId = room.getTowerId();
		}

		checkDoubleProfitCondition(uid, towerId, type);

		if (!userService.reduceGold(uid, getDoubleProfitNeedGoldNum(towerId, type), ToolUseType.DEIFY_DOUBLE_PROFIT, userService.get(uid).getLevel())) {
			String message = "双倍修炼失败，金币不足 uid[" + uid + "]";
			throw new ServiceException(DeifyService.DOUBLE_PROFIT_GOLD_NOT_ENOUGH, message);
		}

		Date startTime = new Date();
		Date endTime = getDoubleProfitEndTime(startTime, type);
		room.setDoubleProfitStartTime(startTime);
		room.setDoubleProfitEndTime(endTime);
		deifyRoomDao.update(room);

		return createDeifyRoomInfoBO(uid, room);
	}

	private Date getDoubleProfitEndTime(Date startTime, int type) {
		if (type == DOUBLE_PROFIT_1_HOUR) {
			return DateUtils.add(startTime, Calendar.HOUR, 1);
		} else {
			return DateUtils.add(startTime, Calendar.HOUR, 2);
		}
	}

	private void checkDoubleProfitCondition(String uid, int towerId, int type) {
		User user = userService.get(uid);
		if (user.getGoldNum() < getDoubleProfitNeedGoldNum(towerId, type)) {
			String message = "双倍修炼失败，金币不足 uid[" + uid + "]";
			throw new ServiceException(DeifyService.DOUBLE_PROFIT_GOLD_NOT_ENOUGH, message);
		}

		DeifyRoomInfo room = deifyRoomDao.getRoomByUid(uid);
		if (isDoubleProfit(room) == true) {
			String message = "已经开启了双倍，一场修炼中只能开启一次 uid[" + uid + "]";
			throw new ServiceException(DeifyService.DOBLE_PROFIT_IS_OPEN, message);
		}
	}

	private int getDoubleProfitNeedGoldNum(int towerId, int type) {
		SystemDeifyTower systemDeifyTower = systemDeifyTowerDao.get(towerId);
		int doubleOneHourPrice = systemDeifyTower.getDoubleOutputPrice();
		if (type == DOUBLE_PROFIT_1_HOUR) {
			return doubleOneHourPrice;
		} else {
			return (int) Math.ceil(doubleOneHourPrice * 2 * 0.95);
		}
	}

	@Override
	public DeifyRoomInfoBO protect(String uid, int type) {

		int towerId = 0;
		DeifyRoomInfo room = deifyRoomDao.getRoomByUid(uid);
		if (room == null) {
			return null;
		}

		towerId = room.getTowerId();

		checkProtectCondition(uid, towerId, type);

		if (!userService.reduceGold(uid, getProtectNeedGold(towerId, type), ToolUseType.DEIFY_PROTECT, userService.get(uid).getLevel())) {
			String message = "开启修炼保护失败，金币不足 uid[" + uid + "]";
			throw new ServiceException(DeifyService.PROTECT_GOLD_NOT_ENOUGH, message);
		}

		Date startTime = new Date();
		Date endTime = getProtectEndTime(startTime, type);
		room.setProtectStartTime(startTime);
		room.setProtectEndTime(endTime);
		deifyRoomDao.update(room);

		pushProtected(uid, towerId);

		return createDeifyRoomInfoBO(uid, room);
	}

	/**
	 * 检查开启修炼保护的条件
	 */
	private void checkProtectCondition(String uid, int towerId, int type) {

		User user = userService.get(uid);
		if (user.getGoldNum() < getProtectNeedGold(towerId, type)) {
			String message = "开启修炼保护失败，金币不足 uid[" + uid + "]";
			throw new ServiceException(DeifyService.PROTECT_GOLD_NOT_ENOUGH, message);
		}

		DeifyRoomInfo room = deifyRoomDao.getRoomByUid(uid);
		if (isProtected(room) == true) {
			String message = "已经开启了修炼保护，一场修炼中只能开启一次 uid[" + uid + "]";
			throw new ServiceException(DeifyService.PROTECT_IS_OPEN, message);
		}

	}

	private int getProtectNeedGold(int towerId, int type) {
		SystemDeifyTower systemDeifyTower = systemDeifyTowerDao.get(towerId);
		int protectOneHourPrice = systemDeifyTower.getProtectTimePrice();
		if (type == PROTECT_1_HOUR) {
			return protectOneHourPrice;
		} else {
			return (int) Math.ceil(protectOneHourPrice * 2 * 0.9);
		}
	}

	private Date getProtectEndTime(Date startTime, int type) {
		if (type == PROTECT_1_HOUR) {
			return DateUtils.add(startTime, Calendar.MINUTE, 60);
		} else {
			return DateUtils.add(startTime, Calendar.MINUTE, 60 * 2);
		}
	}

	@Override
	public Map<String, Object> getDeifyStatus(String uid) {
		Map<String, Object> rt = new HashMap<String, Object>();
		DeifyRoomInfo roomInfo = deifyRoomDao.getRoomByUid(uid);
		if (roomInfo == null || isEmptyRoom(roomInfo) == true) {
			rt.put("st", 0);
			rt.put("snum", 0);
			return rt;
		} else {
			int mindNum = getDeifyProfit(roomInfo);
			rt.put("st", 1);
			rt.put("snum", mindNum);
			return rt;
		}
	}

	private void cancelDeify(DeifyRoomInfo room) {

		String uid = room.getUserId();
		int towerId = room.getTowerId();

		int deifyEarnMind = 0;
		if (room != null) {
			deifyEarnMind = getDeifyProfit(room);
			room.setUserId(null);
			this.deifyRoomDao.update(room);
		}

		if (deifyEarnMind != 0) {
			userService.addMind(uid, deifyEarnMind, ToolUseType.ADD_DEIFY_MIND);
		}

		pushTowerList(uid);
		pushRoomList(uid, towerId);
		pushUserInfo(uid);
		pushBattleStart();
	}

	private void pushTowerList(String userId) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_DEIFY_PUSH_TOWER_LIST);
		command.setType(CommandType.PUSH_ALL);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", userId);
		command.setParams(params);
		commandDao.add(command);
	}

	private void pushDeifyStatus(DeifyRoomInfo room) {
		String userId = room.getUserId();
		pushTowerList(userId);
		pushRoomList(userId, room.getTowerId());

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_DEIFY_FINISHED);
		command.setType(CommandType.PUSH_USER);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", userId);
		command.setParams(params);
		commandDao.add(command);
	}

	private void pushRoomList(String userId, int towerId) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_DEIFY_PUSH_ROOM_LIST);
		command.setType(CommandType.PUSH_ALL);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", userId);
		params.put("towerId", towerId + "");
		command.setParams(params);
		commandDao.add(command);
	}

	private void pushProtected(String userId, int towerId) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_DEIFY_PUSH_PROTECTED);
		command.setType(CommandType.PUSH_ALL);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", userId);
		params.put("towerId", towerId + "");
		command.setParams(params);
		commandDao.add(command);
	}

	private void pushReport(DeifyRoomInfo roomInfo, Event event) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_DEIFY_PUSH_REPORT);
		command.setType(CommandType.PUSH_USER);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", roomInfo.getUserId());

		if (event.getInt("flag") == 1) {
			params.put("type", "2");
			params.put("utid", "0");
		} else if (event.getInt("flag") == 3) {
			params.put("type", "1");
			params.put("utid", roomInfo.getTowerId() + "");
		}

		params.put("cd", getDeifyMin(roomInfo) + "");
		params.put("snum", getDeifyProfit(roomInfo) + "");
		params.put("rf", event.getString("flag"));
		params.put("rp", event.getString("report"));
		params.put("tp", "10");
		params.put("twid", roomInfo.getTowerId() + "");
		params.put("rid", roomInfo.getRoomId() + "");
		params.put("bgid", "1");
		params.put("atu", event.getString("atu"));
		params.put("defu", event.getString("defu"));
		params.put("aod", 2 + "");
		command.setParams(params);
		commandDao.add(command);
	}

	public void init() {

		if (Config.ins().isGameServer()) {

			Thread deifyThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {

						try {
							Collection<DeifyRoomInfo> roomList = deifyRoomDao.getAllRoomInfoList();
							for (DeifyRoomInfo room : roomList) {
								if (isRoomExpired(room)) {
									LOG.debug("towerId[" + room.getTowerId() + "] roomId [" + room.getRoomId() + "]");
									String userId = room.getUserId();
									Map<String, String> reportData = createPushReportDate(room);
									cancelDeify(room);
									pushUserInfo(userId);
									pushReport(reportData);
								}
							}
						} catch (Throwable t) {
							LOG.error(t.getMessage(), t);
						}

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}

			});

			deifyThread.setName("deify-thread");
			deifyThread.start();

		}
	}

	private void pushReport(Map<String, String> params) {
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_DEIFY_PUSH_REPORT);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);
		commandDao.add(command);
	}

	private Map<String, String> createPushReportDate(DeifyRoomInfo room) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", room.getUserId());
		params.put("type", "3");
		params.put("cd", getDeifyMin(room) + "");
		params.put("snum", getDeifyProfit(room) + "");
		params.put("twid", room.getTowerId() + "");
		params.put("rid", room.getRoomId() + "");
		return params;
	}

	@Override
	public void checkLevel(String uid, int towerId) {
		int currentHour = DateUtils.getHour();
		if (currentHour < TOWER_OPEN_TIME) {
			String message = "未到开放时间";
			throw new ServiceException(DeifyService.NOT_TOWER_OPEN_TIME, message);
		}

		SystemDeifyTower systemDeifyTower = systemDeifyTowerDao.get(towerId);
		int enterLevel = systemDeifyTower.getOpenLevel();
		User user = userService.get(uid);
		if (user.getLevel() < enterLevel) {
			String message = "进入修炼塔，用户等级不够 userId[" + uid + "] towerId[" + towerId + "]";
			throw new ServiceException(DeifyService.USER_LEVEL_NOT_ENOUGHT, message);
		}
	}

	private void checkOpenTime() {
		int currentHour = DateUtils.getHour();
		if (currentHour < TOWER_OPEN_TIME) {
			String message = "未到开放时间";
			throw new ServiceException(DeifyService.OCCUPY_NOT_OPEN_TIME, message);
		}

	}

	@Override
	public BattleStartBO checkStatus(String userId) {
		DeifyRoomInfo room = deifyRoomDao.getRoomByUid(userId);
		BattleStartBO bo = new BattleStartBO();
		bo.setType(4);
		if (room == null || isEmptyRoom(room)) {
			bo.setIsStart(1);
			bo.setIsEnd(0);
		} else {
			bo.setIsStart(0);
			bo.setIsEnd(1);
		}

		return bo;

	}

	@Override
	public List<UserHeroBO> getDeifyPeopleList(String uid) {

		User user = userService.get(uid);
		if (user.getLevel() < 120) {
			throw new ServiceException(DeifyService.USER_LEVEL_NOT_ENOUGHT, "需主公等级120，请努力提升等级吧！！");
		}

		return this.heroService.getDeifyPeopleList(uid);
	}

	@Override
	public List<UserForcesBO> getDeifyForcesList(String uid, int heroId, int type) {
		SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDao.getByHeroIdAndType(heroId, type);
		List<UserForcesBO> userForcesBOs = this.forcesService.getUserForcesList(uid, systemDeifyDefine.getSceneId());
		return userForcesBOs;
	}

	@Override
	public boolean deifyBattle(final String userId, final String userHeroId, final int forcesId, final EventHandle handle) {
		BattleBO attack = new BattleBO();

		// 怪物列表
		List<BattleHeroBO> battleHeroBOList = this.forcesService.getForcesHeroBOList(forcesId);

		// 英雄列表
		List<BattleHeroBO> userBattleBattleHeroBO = new ArrayList<BattleHeroBO>();
		userBattleBattleHeroBO.add(heroService.getUserBattleHeroBO(userId, userHeroId));

		attack.setBattleHeroBOList(userBattleBattleHeroBO);

		BattleBO defense = new BattleBO();
		defense.setBattleHeroBOList(battleHeroBOList);

		// userService.checkUserHeroBagLimit(uid);
		userService.checkUserEquipBagLimit(userId);
		final SystemForces systemForces = this.systemForcesDao.get(forcesId);
		if (systemForces == null) {
			throw new ServiceException(ServiceReturnCode.FAILD, "攻打怪物部队失败,怪物不存在.userId[" + userId + "], forcesId[" + forcesId + "]");
		}

		SystemScene systemScene = this.systemSceneDao.get(systemForces.getSceneId());
		final int imgId = systemScene.getImgId();

		final UserForces userForces = this.userForcesDao.get(userId, forcesId);

		if (userForces != null && DateUtils.isSameDay(userForces.getUpdatedTime(), new Date()) && userForces.getTimes() >= systemForces.getTimesLimit() && systemForces.getTimesLimit() != 0) {
			throw new ServiceException(ServiceReturnCode.FAILD, "攻打怪物部队失败,攻打次数不足.userId[" + userId + "], forcesId[" + forcesId + "]");
		}

		int needPower = systemForces.getNeedPower();
		final User user = this.userService.get(userId);
		if (user.getPower() < needPower) {
			String message = "攻打怪物出错，体力不足.userId[" + userId + "], forcesId[" + forcesId + "]";
			throw new ServiceException(POWER_NOT_ENOUGH, message);
		}

		if (!this.userService.reducePower(userId, needPower, systemForces.getForcesType())) {
			String message = "攻打怪物异常,更新用户体力出错.userId[" + userId + "], forcesId[" + forcesId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		battleService.fight(attack, defense, 11, new EventHandle() {

			public boolean handle(Event event) {

				if (event instanceof BattleResponseEvent) {

					int flag = event.getInt("flag");

					if (flag == 1 || flag == 2) {// 打怪打赢了
						// 更新FB次数
						handleForcesTimes(userId, forcesId);

						// 获取掉落
						CommonDropBO forcesDropBO = getDeifyCommonDropBO(userId, forcesId, RandomUtils.nextInt(10000));
						event.setObject("forcesDropBO", forcesDropBO);
					} else {
						// 返还体力
						int giveBackPower = systemForces.getNeedPower();
						if (giveBackPower > 0) {
							userService.addPower(userId, giveBackPower, ToolUseType.ADD_FORCES_BACK, null);
						}
					}

					event.setObject("bgid", imgId);

					// 继续广播出去
					handle.handle(event);
				}
				return true;
			}
		});

		return true;
	}

	private void handleForcesTimes(String userId, int forcesId) {

		UserForces userForces = this.userForcesDao.get(userId, forcesId);

		if (userForces != null) {// 不为空,攻打次数+1
			Date now = new Date();
			int times = userForces.getTimes();
			if (!DateUtils.isSameDay(now, userForces.getUpdatedTime())) {
				times = 1;
			} else {
				times += 1;
			}
			// 攻打次数加1
			this.userForcesDao.updateTimes(userId, forcesId, times);

		} else {// 为空，创建

			Date now = new Date();

			SystemForces systemForces = this.systemForcesDao.get(forcesId);
			userForces = new UserForces();
			userForces.setCreatedTime(now);
			userForces.setForcesId(forcesId);
			userForces.setForcesType(systemForces.getForcesType());
			userForces.setSceneId(systemForces.getSceneId());
			userForces.setStatus(ForcesStatus.STATUS_PASS);
			userForces.setTimes(1);
			userForces.setUpdatedTime(now);
			userForces.setUserId(userId);

			this.userForcesDao.add(userForces);
		}
	}

	/*
	 * 器灵挑战掉落（专用）
	 */
	private CommonDropBO getDeifyCommonDropBO(String userId, int forcesId, int random) {
		CommonDropBO commonDropBO = new CommonDropBO();
		List<ForcesDropTool> forcesDropTools = this.forcesDropToolDao.getForcesDropToolList(forcesId);
		for (ForcesDropTool forcesDropTool : forcesDropTools) {

			if (!DropToolHelper.isDrop(forcesDropTool.getLowerNum(), forcesDropTool.getUpperNum(), random)) {
				continue;
			}

			List<DropToolBO> dropToolBOList = toolService.giveTools(userId, forcesDropTool.getToolType(), forcesDropTool.getToolId(), forcesDropTool.getToolNum(), ToolUseType.ADD_FORCES);
			if (dropToolBOList == null) {
				continue;
			}

			for (DropToolBO dropToolBO : dropToolBOList) {
				this.toolService.appendToDropBO(userId, commonDropBO, dropToolBO);
			}

		}
		return commonDropBO;
	}

	@Override
	public Map<String, Object> forgePre(String userId, String userHeroID, int type) {
		return getForcesToolMap(userId, userHeroID, type);
	}

	private Map<String, Object> getForcesToolMap(String userId, String userHeroID, int type) {
		User user = this.userService.get(userId);
		Map<String, Object> resuleMap = new HashMap<String, Object>();
		resuleMap.put("ina", 0);
		UserHero userHero = this.userHeroDao.get(userId, userHeroID);

		SystemHero systemHero = this.systemHeroDao.get(userHero.getSystemHeroId());
		SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDao.getByHeroIdAndType(systemHero.getHeroId(), type);
		if (null == systemDeifyDefine) {
			throw new ServiceException(DEIFY_NOT_EXIST, "要锻造的神器不存在！");
		}
		List<UserEquipBO> userEquips = this.equipService.getUserHeroEquipList(userId, userHeroID);

		// 武将在阵上
		if (userEquips != null && userEquips.size() > 0) {
			for (UserEquipBO ueb : userEquips) {
				if (ueb.getEquipLevel() == 140 && ueb.getEquipType() == systemDeifyDefine.getEquipType()) {
					resuleMap.put("ina", 1);
					resuleMap.put("eid", ueb.getEquipId());
					break;
				}
			}
		}
		int needCopper = 0;
		int animaNum = 0;

		UserToolBO userTool = null;

		List<DropToolBO> dropToolBOs = DropToolHelper.parseDropTool(systemDeifyDefine.getToolIds());
		for (DropToolBO dropToolBO : dropToolBOs) {
			if (dropToolBO.getToolType() == ToolType.COPPER || dropToolBO.getToolType() == ToolType.GOLD) {
				needCopper = dropToolBO.getToolNum();
			} else if (dropToolBO.getToolType() == ToolType.MATERIAL) {
				userTool = this.toolService.getUserToolBO(userId, dropToolBO.getToolId());
				animaNum = dropToolBO.getToolNum();
				SystemTool systemTool = this.systemToolDao.get(dropToolBO.getToolId());
				resuleMap.put("ann", systemTool.getName());
				resuleMap.put("tid", systemTool.getToolId());
			} else if (dropToolBO.getToolType() == ToolType.EQUIP) {
				SystemEquip systemEquip = this.systemEquipDao.get(dropToolBO.getToolId());
				if (systemEquip != null) {
					resuleMap.put("nen", systemEquip.getEquipName());
				} else {
					resuleMap.put("nen", "");
				}
			}
		}

		resuleMap.put("did", systemDeifyDefine.getDeifyId());
		resuleMap.put("an", userTool != null ? userTool.getToolNum() : 0);
		resuleMap.put("han", animaNum);
		resuleMap.put("cop", user.getCopper());
		resuleMap.put("hcop", needCopper);
		return resuleMap;
	}

	@Override
	public Map<String, Object> forge(String userId, String userHeroID, int type) {

		User user = this.userService.get(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ig", 0);
		Map<String, Object> resultMap = getForcesToolMap(userId, userHeroID, type);
		int isHaveArms = Integer.parseInt(resultMap.get("ina").toString());
		int needAnimaNum = Integer.parseInt(resultMap.get("han").toString());
		int needCopper = Integer.parseInt(resultMap.get("hcop").toString());
		int tool_id = Integer.parseInt(resultMap.get("tid").toString());

		if (isHaveArms == 0) {
			throw new ServiceException(TOOL_NOT_ENOUGH, "锻造需要的武器没有!");
		}

		if (!userService.reduceCopper(userId, needCopper, ToolUseType.DEIFY_FORGE)) {
			throw new ServiceException(COPPER_NOT_ENOUGH, "银币不足！");
		}

		if (!toolService.reduceTool(userId, ToolType.MATERIAL, tool_id, needAnimaNum, ToolUseType.DEIFY_FORGE)) {
			userService.addCopper(userId, needCopper, ToolUseType.DEIFY_FORGE);
			throw new ServiceException(ANIMA_NOT_ENOUGH, "灵气不足！");
		}

		UserHero userHero = this.userHeroDao.get(userId, userHeroID);
		SystemHero systemHero = this.systemHeroDao.get(userHero.getSystemHeroId());
		SystemDeifyDefine systemDeifyDefine = this.systemDeifyDefineDao.getByHeroIdAndType(systemHero.getHeroId(), type);
		List<UserEquipBO> userEquips = this.equipService.getUserHeroEquipList(userId, userHeroID);
		String userEquipId = "";
		// 武将在阵上
		if (userEquips != null && userEquips.size() > 0) {
			for (UserEquipBO ueb : userEquips) {
				if (ueb.getEquipLevel() == 140 && ueb.getEquipType() == systemDeifyDefine.getEquipType()) {
					userEquipId = ueb.getUserEquipId();
					break;
				}
			}
		}
		if (StringUtils.isEmpty(userEquipId)) {
			throw new ServiceException(TOOL_NOT_ENOUGH, "锻造需要的武器没有!");
		}

		userEquipDao.updateUserDeify(userId, systemDeifyDefine.getDeifyId(), 0, userEquipId);

		DeifyBo deifyBo = equipService.getUserHeroEquipDeifyList(userHero.getUserId(), systemDeifyDefine.getDeifyId(), 0);

		DeifyEvent deifyEvent = new DeifyEvent(user.getUsername(), systemHero.getHeroName(), systemDeifyDefine.getDeifyName());
		eventServcie.dispatchEvent(deifyEvent);

		List<DropToolBO> dropToolBOs = DropToolHelper.parseDropTool(systemDeifyDefine.getToolIds());
		for (DropToolBO dropToolBO : dropToolBOs) {
			if (dropToolBO.getToolType() == ToolType.MATERIAL) {
				map.put("nan", dropToolBO.getToolNum());
				map.put("ntid", dropToolBO.getToolId());
			}
		}

		map.put("ig", 1);

		List<UserToolBO> successTools = this.toolService.getToolBoByToolType(userId, ToolType.DEIFY_UPGRADE_PROBABILITY_TOOL_TYPE);
		List<UserToolBO> productTools = this.toolService.getToolBoByToolType(userId, ToolType.DEIFY_UPGRADE_PRODUCT_TOOL_TYPE);

		UserHeroBO userHeroBO = this.heroService.getUserHeroBO(userId, userHeroID);
		UserEquipBO userEquipBO = this.equipService.getUserEquipBO(userId, userEquipId);

		map.put("hbo", userHeroBO);
		map.put("eq", userEquipBO);
		map.put("da", deifyBo);
		map.put("sls", successTools);
		map.put("pls", productTools);
		return map;
	}

	@Override
	public Map<String, Object> animaPre(String userId, String userEquipId, int type) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserEquip userEquip = this.equipService.getUserEquip(userId, userEquipId);

		if (null == userEquip) {
			throw new ServiceException(DEIFY_NOT_EXIST, "神器不存在！");
		}
		int upgradeNum = 0;
		DeifyBo deifyBo = equipService.getUserHeroEquipDeifyList(userId, userEquip.getEquipId(), userEquip.getEquipLevel());
		SystemDeifyAttr systemDeifyAttr = this.systemDeifyAttrDao.get(userEquip.getEquipId(), userEquip.getEquipLevel());
		DropToolBO dropToolBO = null;
		if (systemDeifyAttr != null && !StringUtils.isEmpty(systemDeifyAttr.getUpgradeTool())) {
			dropToolBO = DropToolHelper.parseDropTool(systemDeifyAttr.getUpgradeTool()).get(0);
			UserToolBO userToolBO = this.toolService.getUserToolBO(userId, dropToolBO.getToolId());
			upgradeNum = null == userToolBO ? 0 : userToolBO.getToolNum();
		}

		List<UserToolBO> successTools = this.toolService.getToolBoByToolType(userId, ToolType.DEIFY_UPGRADE_PROBABILITY_TOOL_TYPE);
		List<UserToolBO> productTools = this.toolService.getToolBoByToolType(userId, ToolType.DEIFY_UPGRADE_PRODUCT_TOOL_TYPE);
		resultMap.put("da", deifyBo);
		resultMap.put("ul", dropToolBO);
		resultMap.put("hul", upgradeNum);
		resultMap.put("sls", successTools);
		resultMap.put("pls", productTools);

		return resultMap;
	}

	@Override
	public Map<String, Object> anima(String userId, String userEquipId, int successNum, int successToolId, int productNum, int productToolId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserEquip userEquip = this.userEquipDao.get(userId, userEquipId);

		if (null == userEquip) {
			throw new ServiceException(NOT_HAVE_EQUIP, "没有这个装备userEquipId[" + userEquipId + "]");
		}

		SystemDeifyAttr systemDeifyAttr = this.systemDeifyAttrDao.get(userEquip.getEquipId(), userEquip.getEquipLevel());
		if (null == systemDeifyAttr) {
			throw new ServiceException(ONLY_DEIFY_ANIMA, "只有神器才可以注灵userEquipId[" + userEquipId + "]");
		}

		if (userEquip.getEquipLevel() == systemDeifyAttrDao.maxLevel(userEquip.getEquipId())) {
			throw new ServiceException(DEIFY_MAX_LEVEL, "神器已是最高等级");
		}

		List<DropToolBO> dropToolBOs = DropToolHelper.parseDropTool(systemDeifyAttr.getUpgradeTool());
		for (DropToolBO dropToolBO : dropToolBOs) {
			int toolNum = dropToolBO.getToolNum();
			int toolId = dropToolBO.getToolId();

			UserToolBO userToolBO = this.toolService.getUserToolBO(userId, toolId);

			if (null == userToolBO) {
				userToolBO = new UserToolBO();
				userToolBO.setToolNum(0);
			}

			if (userToolBO.getToolNum() < toolNum) {
				throw new ServiceException(UPGRADE_TOOL_NOT_ENOUGH, "强化材料不足！");
			}
		}
		UserToolBO successTool = null;
		if (successNum != 0) {
			successTool = this.toolService.getUserToolBO(userId, successToolId);

			if (null == successTool || successTool.getToolNum() < successNum) {
				throw new ServiceException(SUCCESS_TOOL_NOT_ENOUGH, "增加成功率道具不足！");
			}
			SystemTool systemTool = this.systemToolDao.get(successToolId);
			successTool.setProbability(systemTool.getRate());
		}

		UserToolBO productTool = null;
		if (productNum != 0) {
			productTool = this.toolService.getUserToolBO(userId, productToolId);
			if (null == productTool || productTool.getToolNum() < productNum) {
				throw new ServiceException(PRODUCT_TOOL_NOT_ENOUGH, "保护材料道具不足！");
			}

			SystemTool systemTool = this.systemToolDao.get(productToolId);
			productTool.setProbability(systemTool.getRate());
		}

		for (DropToolBO dropToolBO : dropToolBOs) {
			int toolNum = dropToolBO.getToolNum();
			int toolId = dropToolBO.getToolId();
			int toolType = dropToolBO.getToolType();

			if (!toolService.reduceTool(userId, toolType, toolId, toolNum, ToolUseType.DEIFY_ANIMA)) {
				throw new ServiceException(UPGRADE_TOOL_NOT_ENOUGH, "强化材料道具不足！");
			} else {
				resultMap.put("ugid", toolId);
				resultMap.put("unm", toolNum);
			}
		}

		if (productNum > 0) {
			if (!toolService.reduceTool(userId, ToolType.DEIFY_UPGRADE_PROBABILITY_TOOL_TYPE, productToolId, productNum, ToolUseType.DEIFY_ANIMA)) {
				throw new ServiceException(SUCCESS_TOOL_NOT_ENOUGH, "保护材料道具不足！");
			}
		}

		if (successNum > 0) {
			if (!toolService.reduceTool(userId, ToolType.DEIFY_UPGRADE_PRODUCT_TOOL_TYPE, successToolId, successNum, ToolUseType.DEIFY_ANIMA)) {
				throw new ServiceException(PRODUCT_TOOL_NOT_ENOUGH, "增加成功率材料不足！");
			}
		}
		int isSuccss = 0;
		int equipLevel = systemDeifyAttr.getDeifyLevel();
		int suceeRandom = RandomUtils.nextInt(100);

		// 使用道具增加的成功率
		int succPro = 0;

		// 使用道具增加的保护率
		int failPro = 0;
		if (successTool != null) {
			succPro = successTool.getProbability() * successNum;
		}
		if (productTool != null) {
			failPro = productTool.getProbability() * productNum;
		}

		if (suceeRandom >= systemDeifyAttr.getSuccLower() && suceeRandom <= systemDeifyAttr.getSuccUpper() + succPro) {
			isSuccss = 1;
			equipLevel++;
			systemDeifyAttr = this.systemDeifyAttrDao.get(userEquip.getEquipId(), equipLevel);
			userEquipDao.updateEquipLevel(userId, userEquipId, 1, 7);
		} else {
			int failRandom = RandomUtils.nextInt(100);
			if (failRandom >= systemDeifyAttr.getFailLower() && failRandom <= systemDeifyAttr.getFailUpper() - failPro) {
				if (userEquip.getEquipLevel() > 0) {
					isSuccss = 3;
					equipLevel--;
					systemDeifyAttr = this.systemDeifyAttrDao.get(userEquip.getEquipId(), equipLevel);
					userEquipDao.updateEquipLevel(userId, userEquipId, -1, 7);
				}
			} else {
				isSuccss = 2;
			}
		}

		DeifyBo deifyBo = equipService.getUserHeroEquipDeifyList(userId, userEquip.getEquipId(), equipLevel);

		int upgradeNum = 0;
		DropToolBO dropToolBO = null;
		if (systemDeifyAttr != null && !StringUtils.isEmpty(systemDeifyAttr.getUpgradeTool())) {
			dropToolBO = DropToolHelper.parseDropTool(systemDeifyAttr.getUpgradeTool()).get(0);
			UserToolBO userToolBO = this.toolService.getUserToolBO(userId, dropToolBO.getToolId());
			upgradeNum = null == userToolBO ? 0 : userToolBO.getToolNum();
		}

		List<UserToolBO> successTools = this.toolService.getToolBoByToolType(userId, ToolType.DEIFY_UPGRADE_PROBABILITY_TOOL_TYPE);
		List<UserToolBO> productTools = this.toolService.getToolBoByToolType(userId, ToolType.DEIFY_UPGRADE_PRODUCT_TOOL_TYPE);
		UserHeroBO userHeroBO = null;
		if (!StringUtils.isEmpty(userEquip.getUserHeroId())) {
			userHeroBO = this.heroService.getUserHeroBO(userId, userEquip.getUserHeroId());
		}
		UserEquipBO userEquipBO = this.equipService.getUserEquipBO(userId, userEquipId);

		resultMap.put("hbo", userHeroBO);
		resultMap.put("eq", userEquipBO);
		resultMap.put("iss", isSuccss);
		resultMap.put("da", deifyBo);
		resultMap.put("ul", dropToolBO);
		resultMap.put("hul", upgradeNum);
		resultMap.put("sls", successTools);
		resultMap.put("pls", productTools);

		return resultMap;
	}

	@Override
	public DeifyRoomInfoBO buyDeifyTime(String uid, int type) {

		DeifyRoomInfo room = deifyRoomDao.getRoomByUid(uid);
		if (room == null) {
			return null;
		}

		checkBuyDeifyTimeCondition(room, type);

		reduceBuyDeifyTimeGold(room, type);

		Date startTime = room.getDeifyStartTime();
		Date endTime = getBuyDeifyEndTIme(startTime, type);
		room.setDeifyEndTime(endTime);
		deifyRoomDao.update(room);

		return createDeifyRoomInfoBO(uid, room);

	}

	private void reduceBuyDeifyTimeGold(DeifyRoomInfo room, int type) {
		String uid = room.getUserId();
		User user = userService.get(uid);

		int buyDeifyTimeNeedGold = getBuyDeifyTimeNeedGold(room.getTowerId(), type);
		boolean isSuccess = userService.reduceGold(uid, buyDeifyTimeNeedGold, ToolUseType.REDUCE_BUY_DEIFY_TIME, user.getLevel());
		if (isSuccess == false) {
			String message = "购买延长修炼时间，元宝不足 uid[" + uid + "]";
			throw new ServiceException(DeifyService.BUY_DEIFY_TIME_GOLD_NOT_ENOUGH, message);
		}
	}

	private void checkBuyDeifyTimeCondition(DeifyRoomInfo room, int type) {
		String uid = room.getUserId();

		if (hasBuyDeifyTime(room)) {
			String message = "已经购买过延长修炼时间";
			throw new ServiceException(DeifyService.ALREADY_BUY_DEIFY_TIME, message);
		}

		int buyDeifyTimeNeedGold = getBuyDeifyTimeNeedGold(room.getTowerId(), type);
		User user = userService.get(uid);
		if (user.getGoldNum() < buyDeifyTimeNeedGold) {
			String message = "购买延长修炼时间，元宝不足 uid[" + uid + "]";
			throw new ServiceException(DeifyService.BUY_DEIFY_TIME_GOLD_NOT_ENOUGH, message);
		}
	}

	private boolean hasBuyDeifyTime(DeifyRoomInfo room) {
		Date deifyStartTime = room.getDeifyStartTime();
		Date deifyEndTime = room.getDeifyEndTime();
		int minuteDiff = DateUtils.getMinuteDiff(deifyStartTime, deifyEndTime);

		// 如果开始时间和结束时间相差大于60分钟，说明已经购买了延长修炼时间
		if (minuteDiff > 60) {
			return true;
		}
		return false;
	}

	private int getBuyDeifyTimeType(DeifyRoomInfo room) {
		Date deifyStartTime = room.getDeifyStartTime();
		Date deifyEndTime = room.getDeifyEndTime();
		int minuteDiff = DateUtils.getMinuteDiff(deifyStartTime, deifyEndTime);

		if (60 < minuteDiff && minuteDiff <= 120) {
			return BUY_DEIFY_TIME_1_HOUR;
		} else if (minuteDiff > 120) {
			return BUY_DEIFY_TIME_3_HOUR;
		} else {
			return NOT_BUY_DEIFY_TIME;
		}
	}

	private int getBuyDeifyTimeNeedGold(int towerId, int type) {
		SystemDeifyTower systemDeifyTower = systemDeifyTowerDao.get(towerId);
		int buyDeifyTimePrice = systemDeifyTower.getBuyDeifyTimePrice();

		if (type == BUY_DEIFY_TIME_1_HOUR) {
			return buyDeifyTimePrice;
		} else {
			return (int) Math.ceil(buyDeifyTimePrice * 2.6);
		}
	}

	private Date getBuyDeifyEndTIme(Date startTime, int type) {
		if (type == BUY_DEIFY_TIME_1_HOUR) {
			return DateUtils.add(startTime, Calendar.HOUR, 2);
		} else {
			return DateUtils.add(startTime, Calendar.HOUR, 4);
		}
	}

}
