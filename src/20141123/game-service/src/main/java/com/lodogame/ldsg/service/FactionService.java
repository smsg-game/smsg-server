package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.FactionBO;
import com.lodogame.model.Faction;

/**
 * 帮派 service
 * 
 * @author zyz
 * 
 */
public interface FactionService {

	/**
	 * 创建帮派，用户等级不够
	 */
	public final static int CREATE_FACTION_LEVEL_NOT_ENOUGH = 2001;

	/**
	 * 创建帮派，银币不足
	 */
	public final static int CREATE_FACTION_COPPER_NOT_ENOUGH = 2002;
	
	/**
	 * 创建帮派，名字为空
	 */
	public final static int CREATE_FACTION_NAME_IS_NULL = 2003;
	
	/**
	 * 创建帮派，名字过长
	 */
	public final static int CREATE_FACTION_NAME_IS_TOO_LONG = 2004;
	
	/**
	 * 创建帮派，名字含非法字符
	 */
	public final static int CREATE_FACTION_NAME_IS_ILLEGAL = 2005;
	
	/**
	 * 创建帮派，创建者已有帮派
	 */
	public final static int CREATE_FACTION_IS_IN_FACTION = 2006;
	
	/**
	 * 创建帮派，创建名字已经有了
	 */
	public final static int CREATE_FACTION_NAME_IS_REPETITION = 2007;
	
	
	/**
	 * 创建帮派，建帮令部足
	 */
	public final static int CREATE_FACTION_ORDER_NOT_ENOUGHT= 2008;
	
	
	/**
	 * 申请加入帮派，用户等级不够
	 */
	public final static int APPLY_FACTION_LEVEL_NOT_ENOUGH = 2001;

	/**
	 * 申请加入帮派，用户已有帮派
	 */
	public final static int APPLY_FACTION_IS_IN_FACTION = 2002;
	
	/**
	 * 申请加入帮派，用户已在申请列表
	 */
	public final static int APPLY_FACTION_IS_IN_APPLY_LIST = 2003;
	
	
	/**
	 * 批准拒绝申请加入帮派，用户不是帮主
	 */
	public final static int APPROVE_FACTION_USER_NOT_MASTER = 2001;

	/**
	 * 批准拒绝申请加入帮派，玩家已有帮派
	 */
	public final static int APPROVE_FACTION_IS_IN_FACTION = 2002;
	
	/**
	 * 批准拒绝申请加入帮派，帮派已满员
	 */
	public final static int APPROVE_FACTION_MEMBER_IS_LIMIT = 2003;

	/**
	 * 退出帮派，帮主不能退出
	 */
	public final static int QUIT_FACTION_USER_IS_MASTER = 2001;

	/**
	 * 开除帮派成员，用户不是帮主
	 */
	public final static int KICK_FACTION_USER_IS_NOT_MASTER = 2001;
	
	/**
	 * 开除帮派成员，帮主不能被开除
	 */
	public final static int KICK_FACTION_KICKED_IS_MASTER = 2002;
	

	/**
	 * 保存公告失败，用户不是帮主
	 */
	public final static int SAVE_FACTION_NOTICE_USER_IS_NOT_MASTER = 2001;
	
	/**
	 * 保存公告失败，用户不是帮主
	 */
	public final static int SAVE_FACTION_NOTICE_IS_NULL = 2002;
	
	/**
	 * 保存公告失败，用户不是帮主
	 */
	public final static int SAVE_FACTION_NOTICE_IS_TOO_LONG = 2003;
	
	/**
	 * 保存公告失败，用户不是帮主
	 */
	public final static int SAVE_FACTION_NOTICE_IS_ILLEGA = 2004;
	
	/**
	 * 创建帮派
	 * 
	 * @param userId
	 * @param factionName
	 * @return
	 */
	public void createFaction(String userId, String factionName);

	/**
	 * 进入帮派界面
	 * 
	 * @param userId
	 * @param factionName
	 * @return
	 */
	public Map<String,Object> enter(String userId);
	
	/**
	 * 申请加入帮派
	 * 
	 * @param userId
	 * @param factionId
	 * @return
	 */
	public void applyForFaction(String userId, int factionId);
	
	/**
	 * 批准是否加入
	 * 
	 * @param userId
	 * @param factionId
	 * @param flag
	 * @return
	 */
	public void approveAddFaction(String userId, String pid, int flag);
	
	/**
	 * 退出帮派
	 * 
	 * @param userId
	 * @param factionId
	 * @return
	 */
	public void quitFaction(String userId);

	/**
	 * 开除成员
	 * @param userId
	 * @param pid
	 */
	public void kickFaction(String userId, String pid);

	/**
	 * 保存帮派公告
	 * @param userId
	 * @param factionNotice
	 */
	public void saveFactionNotice(String userId, String factionNotice);

	/**
	 * 获取指定页帮派列表
	 * @param userId
	 * @param pageNum
	 */
	public List<FactionBO> getFactionListByPage(String userId, int pageNum);
	
}
