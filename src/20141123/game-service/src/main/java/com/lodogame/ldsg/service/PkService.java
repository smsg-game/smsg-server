package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.AwardBO;
import com.lodogame.ldsg.bo.AwardDescBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.PkGroupAwardLogBo;
import com.lodogame.ldsg.bo.PkGroupFirstBo;
import com.lodogame.ldsg.bo.PkInfoBO;
import com.lodogame.ldsg.bo.PkPlayerBO;
import com.lodogame.ldsg.event.EventHandle;

/**
 * 争霸赛业务接口
 * 
 * @author CJ
 * 
 */
public interface PkService {

	/**
	 * 请求的key
	 */
	public final static String PK_REQUEST_POOL_KEY = "pk_request_pool_key";

	public final static String ENTER_REQUEST_POOL_KEY = "enter_request_pool_key";

	public final static int ENTER_PK_LEVEL_NOT_ENOUGH = 2001;

	/**
	 * 英雄背包不足
	 */
	public final static int EXCHANGE_HERO_BAG_NOT_ENOUGH = 2002;

	/**
	 * 装备背包不足
	 */
	public final static int EXCHANGE_EQUIP_BAG_NOT_ENOUGH = 2003;

	// 挑战次数不足
	public final static int FIGHT_TIMES_NOT_ENOUGH = 2004;

	// 剩余积分不足
	public final static int EXCHANGE_SCORE_NOT_ENOUGH = 2004;

	/**
	 * 元宝不足
	 */
	public final static int ATTACK_ABLE_JUMP_GOLD_NOT_ENOUGH = 2001;

	/**
	 * 已经飞跃 过
	 */
	public final static int ATTACK_ABLE_JUMP_HAS_JUMP = 2003;

	/**
	 * 排名过高
	 */
	public final static int ATTACK_ABLE_JUMP_RANK_TOOL_HIGHT = 2002;

	/**
	 * 元宝不足
	 */
	public final static int BUY_PK_TIMES_GOLD_NOT_ENOUGH = 2001;

	public static final String[] TITLE_ZH_CN = { "", "万人敌", "一骑当千", "以一敌百", "以一当十", "勇猛过人" };
	public static final String[] TITLE_ZH_HK = { "", "萬人敵", "一騎當千", "以一敵百", "以一當十", "勇猛過人" };

	/**
	 * 获取用户PK信息
	 * 
	 * @param uid
	 * @return
	 */
	public PkInfoBO getUserPkInfo(String uid);

	/**
	 * 获取当前用户可攻击目标列表
	 * 
	 * @param uid
	 * @param isGroup
	 * @return
	 */
	public List<PkPlayerBO> loadPlayers(String uid, int isGroup);

	/**
	 * 获取排行榜前十排名
	 * 
	 * @return
	 */
	public List<PkPlayerBO> loadTopPlayers();

	/**
	 * 兑换奖励
	 * 
	 * @param awardId
	 *            奖励ID
	 * @return
	 */
	public AwardBO exchange(String userId, int awardId);

	/**
	 * 批量兑换奖励
	 * 
	 * @param awardId
	 *            奖励 ID
	 * @param num
	 *            一次批量兑换要对换奖励的个数
	 */
	public AwardBO batchExchange(String userId, int awardId, int num);

	/**
	 * 挑战接口,根据UID来进行挑战
	 * 
	 * @param uid
	 * @param targetPid
	 * @param handle
	 * @return
	 */
	public boolean fight(String uid, long targetPid, EventHandle handle);

	/**
	 * 进入争霸赛
	 * 
	 * @param uid
	 * @param isGroup
	 *            (0：全部，1：组)
	 * @return
	 */
	public void enterPk(String uid, EventHandle handle, int isGroup);

	/**
	 * 获取奖励列表
	 * 
	 * @return
	 */
	public List<AwardDescBO> getAwardList();

	// /**
	// * 争霸排名跳跃
	// *
	// * @param userId
	// * @return
	// */
	// public List<PkPlayerBO> attackAbleJump(String userId);

	/**
	 * 购买挑战次数
	 * 
	 * @param uid
	 * @return
	 */
	public boolean buyPkTimes(String uid);

	/**
	 * 是否是组第一名
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isGroupFirst(String userId);

	/**
	 * 所有组第一名
	 * 
	 * @param beginLevel
	 * @param endLevel
	 * @return
	 */
	public List<PkGroupFirstBo> getGrankFirst();

	/**
	 * 组排名前10
	 * 
	 * @param gid
	 * @return
	 */
	public List<PkPlayerBO> getGrankTen(int gid);

	/**
	 * 总排名前10
	 * 
	 * @return
	 */
	public List<PkPlayerBO> getTotalTen();

	/**
	 * 我的组排名
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserGrank(String userId, int pid);

	/**
	 * 奖励历史（只看起前一天）
	 * 
	 * @param groupId
	 * @return
	 */
	public List<PkGroupAwardLogBo> getAwardLogBos(int groupId);

	/**
	 * 领取奖励
	 * 
	 * @param userId
	 */
	public CommonDropBO updateisGet(String userId);
}
