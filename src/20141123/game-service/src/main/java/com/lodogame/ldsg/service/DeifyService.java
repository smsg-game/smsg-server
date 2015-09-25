package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.BattleStartBO;
import com.lodogame.ldsg.bo.DeifyRoomInfoBO;
import com.lodogame.ldsg.bo.DeifyTowerInfoBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.EventHandle;


public interface DeifyService {
	
	
	/**
	 * 武将激活化神节点-武将不存在
	 */
	public static final int ACTIVITENODE_HERO_NOT_EXIST = 2001;
	
	/**
	 * 武将激活化神节点-武将不是鬼将
	 */
	public static final int ACTIVITENODE_HERO_NOT_GHOST_GENERAL = 2002;
	
	/**
	 * 武将激活化神节点-该节点已激活
	 */
	public static final int ACTIVITENODE_NODE_IS_ACTIVITIED = 2003;
	
	/**
	 * 武将激活化神节点-前置节点未激活
	 */
	public static final int ACTIVITENODE_IHERNODE__NOT_GHOST_ACTIVITIED = 2004;
	
	/**
	 * 武将激活化神节点-神魂不足
	 */
	public static final int ACTIVITENODE_TOOL_IS_NOT_ENOUGH = 2005;
	
	/**
	 * 武将激活化神节点-鬼将等级不足
	 */
	public static final int ACTIVITENODE_HERO_LEVEL_NOT_ENOUGH = 2006;
	
	public static final int USER_LEVEL_NOT_ENOUGH = 2001;

	/**
	 * 占领修炼室 - 银币不足
	 */
	public static final int OCCUPY_COPPER_NOT_ENOUGH = 2001;

	/**
	 * 占领修炼室 - 体力不足
	 */
	public static final int OCCUPY_POWER_NOT_ENOUGH = 2002;
	
	/**
	 * 占领修炼室 - 修炼保护时间内，不可以占领
	 */
	public static final int ROOM_IS_PROTECTED = 2003;

	/**
	 * 占领修炼室 - 玩家正在修炼，要先取消修炼才可以占领
	 */
	public static final int OCCUPY_NOT_OPEN_TIME = 2004;

	/**
	 * 双倍修炼 - 金币不足
	 */
	public static final int DOUBLE_PROFIT_GOLD_NOT_ENOUGH = 2001;

	/**
	 * 已经开启了双倍，一场修炼中只能开启一次，
	 */
	public static final int DOBLE_PROFIT_IS_OPEN = 2002;

	/**
	 * 修炼保护 - 金币不足
	 */
	public static final int PROTECT_GOLD_NOT_ENOUGH = 2001;

	/**
	 * 修炼保护 - 已经开启了
	 */
	public static final int PROTECT_IS_OPEN = 2002;

	/**
	 * 用户等级不够
	 */
	public static final int USER_LEVEL_NOT_ENOUGHT = 2001;

	/**
	 * 没有到修炼塔开放时间
	 */
	public static final int NOT_TOWER_OPEN_TIME = 2002;
	
	/**
	 * 武将不存在
	 */
	public static final int HERO_NOT_EXIST = 2001;
	
	/**
	 * 体力不足
	 */
	public static final int POWER_NOT_ENOUGH = 2003;
	
	/**
	 * 锻造的神器不存在
	 */
	public static final int DEIFY_NOT_EXIST = 2001;
	
	/**
	 * 材料不足
	 */
	public static final int TOOL_NOT_ENOUGH = 2001;
	
	/**
	 * 银币不足
	 */
	public static final int COPPER_NOT_ENOUGH = 2002;
	
	/**
	 * 灵气不足
	 */
	public static final int ANIMA_NOT_ENOUGH = 2003;
	
	/**
	 * 神器已是最高等级
	 */
	public static final int DEIFY_MAX_LEVEL = 2004;
	
	/**
	 * 没有这个装备
	 */
	public static final int NOT_HAVE_EQUIP = 2005;
	
	/**
	 * 只有神器才能注灵
	 */
	public static final int ONLY_DEIFY_ANIMA = 2006;
	
	/**
	 * 购买延长修炼时间， 元宝不足
	 */
	public static final int BUY_DEIFY_TIME_GOLD_NOT_ENOUGH = 2001;
	
	/**
	 * 购买延长修炼时间 - 已经购买过延长修炼时间
	 */
	public static final int ALREADY_BUY_DEIFY_TIME = 2002;
	
	/**
	 * 强化材料不足
	 */
	public static final int UPGRADE_TOOL_NOT_ENOUGH = 2001;
	
	/**
	 * 增加成功率道具不足
	 */
	public static final int SUCCESS_TOOL_NOT_ENOUGH = 2002;
	
	/**
	 * 保护材料道具不足
	 */
	public static final int PRODUCT_TOOL_NOT_ENOUGH = 2003;

	/**
	 * 激活化神节点
	 * @return 
	 */
    public int activateNode(String userId ,String userHeroId ,int systemDeifyNodeId); 

	public Map<String, Object> getTowerList(String uid);

	public List<DeifyRoomInfoBO> getRoomList(String userId, int towerId);

	public Map<String, Object> occupy(String uid, int towerId, int roomId, EventHandle eventHandle);

	/**
	 * 开启双倍修炼
	 * @param type 
	 */
	public DeifyRoomInfoBO doubleProfit(String uid, int type);

	/**
	 * 开启修炼保护
	 */
	public DeifyRoomInfoBO protect(String uid, int type);

	public Map<String, Object> getDeifyStatus(String uid);

	List<DeifyTowerInfoBO> createTowerInfoBOList();

	public void checkLevel(String uid, int towerId);

	public BattleStartBO checkStatus(String userId);
	
	/**
	 * 用户神将列表
	 * @param uid
	 * @return
	 */
	public List<UserHeroBO> getDeifyPeopleList(String uid);
	
	/**
	 * 器灵列表
	 * @param uid
	 * @param heroId
	 * @return
	 */
	public List<UserForcesBO> getDeifyForcesList(String uid, int heroId, int type);
	
	public boolean deifyBattle(String uid, String uhid, int forcesId, EventHandle handle);
	
	/**
	 * 锻造预览
	 * @param uid
	 * @param userHeroID
	 * @return
	 */
	public Map<String, Object> forgePre(String uid, String userHeroID, int type);
	
	/**
	 * 锻造
	 * @param uid
	 * @param userHeroID
	 * @return
	 */
	public Map<String, Object> forge(String uid, String userHeroID, int type);
	
	/**
	 * 注灵预览
	 * @param uid
	 * @param userhEquipId
	 * @param type
	 * @return
	 */
	public Map<String, Object> animaPre(String uid, String userhEquipId, int type );
	
	/**
	 * 注灵
	 * @param uid
	 * @param userHeroID
	 * @param did
	 * @return
	 */
	public Map<String, Object> anima(String userId, String userEquipId, int successNum, int successToolId, int productNum, int productToolId);
	
	/**
	 * 购买延长修炼时间
	 */
	public DeifyRoomInfoBO buyDeifyTime(String uid, int type);

}
