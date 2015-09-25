package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.DeifyBo;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.UserEquip;

/**
 * 装备service
 * 
 * @author jacky
 * 
 */
public interface EquipService {

	/**
	 * 装备出售错误，装备已经在武将身上
	 */
	public final static int SELL_EQUIP_IS_INSTALLED = 2002;

	/**
	 * 强化失败，银币不足
	 */
	public final static int UPGRADE_EQUIP_COPPER_NOT_ENOUGH = 2001;

	/**
	 * 强化失败，武将等级不足
	 */
	public final static int UPGRADE_EQUIP_HERO_LEVEL_NOT_ENOUGH = 2004;

	/**
	 * 强化失败，装备等级高于用户等级
	 */
	public final static int UPGRADE_EQUIP_LEVEL_OVER_USER_LEVEL = 2003;

	/**
	 * 装备强化失败(正常的概率上的失败)
	 */
	public final static int UPGRADE_EQUIP_NORMAL_PROBABILITY_FAILED = 2002;

	/**
	 * 装备合成，等级不足
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_LEVEL = 2001;

	/**
	 * 装备合成，材料不足
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_TOOL = 2002;

	/**
	 * 装备合成(金币)，金币不足
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_GOLD = 2004;

	/**
	 * 装备合成(金币)，VIP不够
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_VIP = 2003;

	/**
	 * 穿戴装备，职业不符
	 */
	public final static int INSTALL_EQUIP_CAREER_ERROR = 2002;
	
	/**
	 * 穿戴装备，神将不符
	 */
	public final static int INSTALL_EQUIP_DEIFY_ERROR = 2003;
	
	/**
	 * 装备洗练，洗练石不够
	 */
	public final static int POLISH_EQUIP_POLISH_STONE_NOT_ENOUGH = 2001;
	
	/**
	 * 装备洗练，银币不足
	 */
	public final static int POLISH_EQUIP_COPPER_NOT_ENOUGH = 2002;
	
	/**
	 * 装备洗练，金币不够
	 */
	public final static int POLISH_EQUIP_GOLD_NOT_ENOUGH = 2003;
	
	/**
	 * 装备洗练，洗练装备的品质不足
	 */
	public final static int POLISH_EQUIP_COLOR_NOT_ENOUGH = 2004;
	
	/**
	 * 装备洗练，主将等级不足
	 */
	public final static int POLISH_EQUIP_USER_LEVEL_NOT_ENOUGH = 2005;
	
	/**
	 * 保存装备洗练，装备副属性不存在
	 */
	public final static int SAVEPOLISH_EQUIP_NOT_HAVE_TEMP = 2002;

	/**
	 * 获取用户装备列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEquipBO> getUserEquipList(String userId);

	/**
	 * 获取用户某种类型的装备
	 * 
	 * @param userId
	 * @param equipId
	 * @return
	 */
	public List<UserEquip> getUserEquipList(String userId, int equipId);

	/**
	 * 获取用户某个武将的装备列表
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public List<UserEquipBO> getUserHeroEquipList(String userId, String userHeroId);
	
	/**
	 * 获取用户某个武将的神器
	 * 
	 * @param userId
	 * @param deifyId
	 * @return
	 */
	public DeifyBo getUserHeroEquipDeifyList(String userId, int deifyId, int equipLevel);


	/**
	 * 穿戴装备
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param userHeroId
	 * @param equipType
	 * @param handle
	 * @return
	 */
	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId, int equipType, EventHandle handle);

	/**
	 * 装备升级
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public boolean upgrade(String userId, String userEquipId, EventHandle handle);

	/**
	 * 装备升级预览
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public Map<String, Object> upgradePre(String userId, String userEquipId);

	/**
	 * 装备出售
	 * 
	 * @param userId
	 * @param userEquipIdList
	 * @return
	 */
	public boolean sell(String userId, List<String> userEquipIdList, EventHandle handle);

	/**
	 * 获取用户装备
	 * 
	 * @param userEquipId
	 * @return
	 */
	public UserEquip getUserEquip(String userId, String userEquipId);

	/**
	 * 获取用户装备
	 * 
	 * @param equipId
	 * @return
	 */
	public SystemEquip getSysEquip(int equipId);

	/**
	 * 获取用户装备BO对象
	 * 
	 * @param userEquipId
	 * @return
	 */
	public UserEquipBO getUserEquipBO(String userId, String userEquipId);

	/**
	 * 添加用户装备
	 * 
	 * @param userId
	 * @param equipId
	 * @param useType
	 *            日志类型
	 * @return
	 */
	public boolean addUserEquip(String userId, String userEquipId, int equipId, int useType);

	/**
	 * 获取所有系统装备定义
	 * 
	 * @return
	 */
	public List<SystemEquip> getAllSystemEquip();

	/**
	 * 合成装备
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param useMoney
	 * @param handle
	 * @return
	 */
	public boolean mergeEquip(String userId, String userEquipId, boolean useMoney, EventHandle handle);

	/**
	 * 打造装备预览
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public UserEquipBO mergeEquipPre(String userId, String userEquipId);

	/**
	 * 获取用户装备长度
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserEquipCount(String userId);

	/**
	 * 批量增加装备
	 * 
	 * @param userId
	 * @param equipIdMap
	 * @param useType
	 * @return
	 */
	public boolean addUserEquips(String userId, Map<String, Integer> equipIdMap, int useType);

	/**
	 * 在用户的一组装备中获取等级最低的装备
	 */
	public UserEquip getLowestLevel(List<UserEquip> userEquipList);

	/**
	 * 根据DropToolBO 列表 创建 UserEquipBO 列表
	 */
	public List<UserEquipBO> createUserEquipBOList(String userId, List<DropToolBO> boList);

	/**
	 * 一键强化
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public int autoUpgrade(String userId, String userEquipId, List<Integer> addLevelList);
	
	/**
	 * 装备洗练
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param polishType
	 * @return
	 */
	public Map<String, Object>  polish(String userId, String userEquipId, int polishType);

	/**
	 *  保存装备洗练副属性
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param polishType
	 * @return
	 */
	public Map<String, Object> savePolish(String uid, String userEquipId);
}
