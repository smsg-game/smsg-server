package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.BloodSacrificeBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.SystemBloodSacrificeLooseBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserHeroSkillBO;
import com.lodogame.ldsg.bo.UserHeroSkillTrainBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.SystemHero;
import com.lodogame.model.UserHero;

public interface HeroService {

	/**
	 * 布阵，上场武将个数超过限制
	 */
	public static final int CHANGE_POS_HERO_NUM_LIMIT = 2001;

	/**
	 * 布阵，阵上最少有一个武将
	 */
	public static final int CHANGE_POS_HERO_NUM_IS_ZERO = 2002;

	/**
	 * 布阵，同一人物的武将只可以上一个
	 */
	public static final int CHANGE_POS_SAME_HERO_EXIST = 2003;

	/**
	 * 武将进阶-等级不足
	 */
	public static final int UPGRADE_HERO_LEVEL_NOT_ENOUGH = 2001;

	/**
	 * 武将进阶-材料不足
	 */
	public static final int UPGRADE_HERO_TOOL_NOT_ENOUGH = 2002;

	/**
	 * 武将进阶-已到最大阶
	 */
	public static final int UPGRADE_HERO_IS_MAX_GRADE = 2003;

	/**
	 * 武将进阶-金币不足
	 */
	public static final int UPGRADE_HERO_NOT_ENOUGH_MONEY = 2004;

	/**
	 * 武将进阶-概率上的失败
	 */
	public static final int UPGRADE_HERO_RATE_FAILURE = 2005;

	/**
	 * 删除武将-武将在阵上
	 */
	public static final int DELETE_HERO_IS_IN_EMBATTLE = 2002;

	/**
	 * 删除武将-武将佩戴装备
	 */
	public static final int DELETE_HERO_IS_INSTALL_EQUIP = 2003;

	/**
	 * 吞噬失败，用户银币不足
	 */
	public static final int DEVOUR_HERO_COPPER_NOT_ENOUGH = 2004;

	/**
	 * 吞噬失败，武将等级已经达最大
	 */
	public static final int DEVOUR_HERO_HERO_LEVEL_OVER = 2005;

	/**
	 * 吞噬失败，武将等级高于用户等级
	 */
	public static final int DEVOUR_HERO_HERO_LEVEL_OVER_USER_LEVEL = 2006;

	/**
	 * 武将不存在
	 */
	public static final int HERO_NOT_EXIST = 2001;

	/**
	 * 升级技能-没有技能书
	 */
	public static final int HERO_SKILL_UPGRADE_TOOL_NOT_ENOUGH = 2001;

	/**
	 * 学习技能-没有技能书
	 */
	public static final int HERO_SKILL_STUDY_TOOL_NOT_ENOUGH = 2001;

	/**
	 * 学习技能-高级的不可以直接学习
	 */
	public static final int HERO_SKILL_STUDY_CAN_NOT_STUDY_ = 2002;

	/**
	 * 学习技能-已经学了相同的技能
	 */
	public static final int HERO_SKILL_STUDY_HAD_STUDY_SAME_SKILL = 2003;

	/**
	 * 学习技能-可学技能超过限制
	 */
	public static final int HERO_SKILL_STUDY_COUNT_OVER = 2004;

	/**
	 * 训练技能-银币不足
	 */
	public static final int HERO_SKILL_TRAIN_COPPER_NOT_ENOUGH = 2001;
	
	/**
	 * 转生-武将不满足下列转生条件：1）品质为5，等级达到100级，3）五星武将
	 */
	public static final int CONDITION_NOT_SATISFIED = 2001;
	
	/**
	 * 转生-武将等级不足
	 */
	public static final int HERO_LEVEL_NOT_ENOUGH = 2002;
	
	/**
	 * 转生-武将没有转生数据
	 */
	public static final int HERO_NO_REGENERATE_INFO = 2003;
		
	/**
	 * 转生-武将转生丹数量不足
	 */
	public static final int HERO_PILL_NOT_ENOUGH = 2004;
	
	/**
	 * 转生-武将鬼之合约数量不足
	 */
	public static final int HERO_CONTRACT_NOT_ENOUGH = 2005;
	
	/**
	 * 转生-武将数量不足
	 */
	public static final int HERO_NUM_NOT_ENOUGH = 2006;
	
	/**
	 * 武将传承传承-武将不存在
	 */
	public static final int INHERITPRE_GHERO_NOT_EXIST = 2001;
	
	/**
	 * 武将传承传承-被传承武将不存在
	 */
	public static final int INHERITPRE_IHERO_NOT_EXIST = 2002;
	
	/**
	 * 武将传承传承-被传承武将不存在
	 */
	public static final int INHERITPRE_HERO_CAN_NOT_CONTRACT = 2003;
	
	/**
	 * 武将传承-元宝数量不足
	 */
	public static final int INHERIT_GOLD_NOT_ENOUGH = 2001;
	
	/**
	 * 武将传承-传承武将不满足条件
	 */
	public static final int INHERIT_GIVING_HERO_NOT_MATCH = 2002;
	
	/**
	 * 武将传承-被传承武将不满足条件
	 */
	public static final int INHERIT_INHERIT_HERO_NOT_MATCH = 2003;
	
	/**
	 * 武将传承-武将不存在
	 */
	public static final int INHERIT_HERO_NOT_EXIST = 2004;
	
	
	/**
	 * 武将传承-鬼之合约不足
	 */
	public static final int INHERIT_HERO_NOT_ENOUGH_CONTRACT = 2005;
	
	/**
	 * 武将传承-传承武将不能被鬼化
	 */
	public static final int INHERIT_HERO_CAN_NOT_CONTRACT = 2006;
	
	/**
	 * 武将血祭-被传承血祭不满足条件
	 */
	public static final int BLOOD_SACRIFICE_HERO_NOT_MATCH = 2001;
	
	/**
	 * 武将血祭-被传承血祭的英雄达到了最高血祭等级
	 */
	public static final int BLOOD_SACRIFICE_IS_MAX_STAGE = 2002;
	
	
	/**
	 *  武将血祭--材料不足
	 */
	public static final int BLOOD_SACRIFICE_TOOL_NOT_ENOUGH = 2001;
	
	/**
	 * 武将血祭--血祭英雄不存在
	 */
	public static final int BLOOD_SACRIFICE_HERO_NOT_EXIST = 2001;
	
	
	/**
	 *  武将血祭--英雄不能血祭自己
	 */
	public static final int BLOOD_SACRIFICE_HERO_REPETTION = 2003;
	
	
	/**
	 *  武将血祭--血祭的材料不能是鬼将
	 */
	public static final int BLOOD_SACRIFICE_HAS_GHOST_GENERAL = 2004;
	
	/**
	 *  武将锁定--武将已被锁定
	 */
	public static final int LOCK_HERO_HAS_LOCKED = 2002;
	
	/**
	 *  武将锁定--武将星级不够
	 */
	public static final int LOCK_HERO_STAR_NOT_ENOUGH = 2003;
	
	/**
	 *  武将锁定--武将未被锁定
	 */
	public static final int LOCK_HERO_WAS_UNLOCKED = 2002;
	
	/**
	 *  武将锁定--武将被锁定,不能用于消耗或卖出
	 */
	public static final int HERO_HAS_LOCKED = 2007;
	
	/**
	 * 获取用户武将BO列表
	 * 
	 * @param userId
	 * @param type
	 *            0表示获取所有 1表示获取上阵武将
	 * @return
	 */
	public List<UserHeroBO> getUserHeroList(String userId, int type);

	/**
	 * 获取用户武将BO
	 * 
	 * @return
	 */
	public UserHeroBO getUserHeroBO(String userId, String userHeroId);

	/**
	 * 获取武将
	 * 
	 * @param userHeroId
	 * @return
	 */
	public UserHero get(String userId, String userHeroId);

	/**
	 * 改变武将站位
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param pos
	 * @param handle
	 * @return
	 */
	@Transactional
	public boolean changePos(String userId, String userHeroId, int pos, EventHandle handle);

	/**
	 * 进阶预览
	 * 
	 * @param userHeroId
	 * @return
	 */
	public UserHeroBO upgradePre(String userId, String userHeroId);

	/**
	 * 吞噬预览
	 * 
	 * @param userHeroId
	 * @param targetUserHeroIdList
	 * @return {'userHeroBO': $userHeroBO, 'copper': $copper}
	 */
	public Map<String, Object> devourPre(String userId, String userHeroId, List<String> targetUserHeroIdList);

	/**
	 * 武将进阶
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param userHeroIdList
	 *            消耗 的武将
	 * @param handle
	 * @return
	 */
	public boolean upgrade(String userId, String userHeroId, List<String> userHeroIdList, boolean force, EventHandle handle);

	/**
	 * 武将出售
	 * 
	 * @param userId
	 * @param userHeroIdList
	 * @param handle
	 * @return
	 */
	public boolean sell(String userId, List<String> userHeroIdList, EventHandle handle);

	/**
	 * 吞噬武将
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param targetUserHeroIdList
	 * @return
	 */
	public boolean devour(String userId, String userHeroId, List<String> targetUserHeroIdList, EventHandle handle);

	/**
	 * 获取用户的战斗武将模型列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<BattleHeroBO> getUserBattleHeroBOList(String userId);

	/**
	 * 添加用户武将
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @return
	 */
	public boolean addUserHero(String userId, String userHeroId, int systemHeroId, int pos, int useType);

	/**
	 * 添加用户武将
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @return
	 */
	@Transactional
	public boolean addUserHero(String userId, Map<String, Integer> heroIdMap, int useType);

	/**
	 * 修正阵法,如果阵法和当前阵法不一致的话
	 * 
	 * @param userId
	 * @param posMap
	 */
	@Transactional
	public void amendEmbattle(String userId, Map<Integer, String> posMap);

	/**
	 * 获取用户武将个数
	 * 
	 * @param userId
	 */
	public int getUserHeroCount(String userId);

	/**
	 * 获取系统武将
	 * 
	 * @param systemHeroId
	 * @return
	 */
	public SystemHero getSysHero(int systemHeroId);

	/**
	 * 获取所有系统武将数据
	 * 
	 * @return
	 */
	public List<SystemHero> getAllSystemHero();

	/**
	 * 根据武将等级倒序获取列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<UserHeroBO> listByHeroLevelDesc(int offset, int size);

	/**
	 * 用户武将学习技能
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param toolId
	 * @return
	 */
	public boolean studySkill(String userId, String userHeroId, int toolId);

	/**
	 * 技能训练
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param lockHeroSkillIdList
	 * @return
	 */
	public boolean trainSkill(String userId, String userHeroId, List<Integer> lockHeroSkillIdList);

	/**
	 * 技能训练保存
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public boolean trainSkillSave(String userId, String userHeroId);

	/**
	 * 技能训练放弃
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public boolean trainSkillCancel(String userId, String userHeroId);

	/**
	 * 获取用户武将技能BO列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserHeroSkillBO> getUserHeroSkillBOList(String userId);

	/**
	 * 获取用户武将技能BO列表(指定武将 )
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public List<UserHeroSkillBO> getUserHeroSkillBOList(String userId, String userHeroId);

	/**
	 * 获取用户武将技能训练列表
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public List<UserHeroSkillTrainBO> getUserHeroSkillTrainBOList(String userId, String userHeroId);

	/**
	 * 技能升级
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param userHeroSkillId
	 * @return
	 */
	public boolean upgradeSkill(String userId, String userHeroId, int userHeroSkillId);

	/**
	 * 遗忘技能
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param userHeroSkillId
	 * @return
	 */
	public boolean forgetSkill(String userId, String userHeroId, int userHeroSkillId);

	/**
	 * 武将兑换成武将碎片
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public boolean exchangeShard(String userId, String userHeroId, EventHandle handle);

	/**
	 * 根据 UserHero 获取对应的 hero_id
	 * @param userHero
	 * @return
	 */
	public int getHeroId(int userHeroId);

	/**
	 * 鬼之契约转生
	 * @return
	 */
	public boolean contractTransform(String userId, String userHeroId);
	
	/**
	 * 相同武将转生
	 */
	public boolean heroTransform(String userId, String userHeroId, List<String> userHeroIdList);

	/**
	 * 转生预览 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public UserHeroBO transformPre(String userId, String userHeroId);
	
	/**
	 * 根据 DropToolBO 列表创建 UserHeroBO
	 * @param boList
	 * @return
	 */
	public List<UserHeroBO> createUserHeroBOList(String userId, List<DropToolBO> boList);

	/**
	 * 武将传承
	 * @param givingHeroId 传承武将 ID
	 * @param inheritHeroId 被传承武将 ID
	 * @param type 传承类型，0：普通传承，1：元宝传承
	 * @return
	 */
	public Map<String, Object> inherit(String uid, String givingHeroId, String inheritHeroId, int type);

	/**
	 * 武将传承预览
	 * @param uid
	 * @param givingHeroId
	 * @param inheritHeroId
	 * @param type
	 * @return
	 */
	public Map<String, Object> inheritPre(String uid, String givingHeroId, String inheritHeroId, int type);
	
	/**
	 * 进阶预览
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public BloodSacrificeBO bloodSacrificePre(String userId, String userHeroId);
	
	/**
	 * 进阶
	 * 
	 * @param uid
	 * @param userHeroId
	 * @param useUserHeroId
	 * @return
	 */
	public SystemBloodSacrificeLooseBO bloodSacrifice(String uid, String userHeroId, String useUserHeroId, EventHandle handle);
	
	/**
	 * 锁定英雄
	 * 
	 * @param uid
	 * @param userHeroId
	 * @param useUserHeroId
	 * @return
	 */
	public boolean lockHero(String uid, String userHeroId);
	
	/**
	 * 解除锁定英雄
	 * 
	 * @param uid
	 * @param userHeroId
	 * @param useUserHeroId
	 * @return
	 */
	public boolean unlockHero(String uid, String userHeroId);
	
	/**
	 * 用户神将列表
	 * @param uid
	 * @return
	 */
	public List<UserHeroBO> getDeifyPeopleList(String uid);
	
	/**
	 * 获取用户的战斗武将模型
	 * @param uid
	 * @param userHeroId
	 * @return
	 */
	public BattleHeroBO getUserBattleHeroBO(String uid, String userHeroId);
	

}
