package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.AwardBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.event.EventHandle;

/**
 * 道具service
 * 
 * @author jacky
 * 
 */
public interface ToolService {

	/**
	 * 打开宝箱失败，宝箱不足
	 */
	public final static int ERROR_OPEN_GIFT_BOX_NOT_ENOUGH = 2001;

	/**
	 * 打开宝箱失败，没有钥匙
	 */
	public final static int ERROR_OPEN_GIFT_BOX_HAS_NOT_KEY = 2002;

	/**
	 * 增加道具
	 * 
	 * @param userId
	 * @param toolType
	 * @param toolId
	 * @param toolNum
	 * @param useType
	 * @return
	 */
	public boolean addTool(String userId, int toolType, int toolId, int toolNum, int useType);

	/**
	 * 消费道具
	 * 
	 * @param userId
	 * @param toolType
	 * @param toolId
	 * @param toolNum
	 * @param useType
	 * @return
	 */
	public boolean reduceTool(String userId, int toolType, int toolId, int toolNum, int useType);

	/**
	 * 获取用户道具列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserToolBO> getUserToolList(String userId);

	/**
	 * 给道具
	 * 
	 * @param userId
	 * @param toolType
	 * @param toolId
	 * @param toolNum
	 * @param useType
	 * @return
	 */
	// public DropToolBO giveTool(String userId, int toolType, int toolId, int
	// toolNum, int useType);

	/**
	 * 获取用户单个道具BO
	 * 
	 * @param userId
	 * @param toolId
	 * @return
	 */
	public UserToolBO getUserToolBO(String userId, int toolId);

	/**
	 * 给多个道具
	 * 
	 * @param userId
	 * @param toolType
	 * @param toolId
	 * @param toolNum
	 * @param useType
	 * @return
	 */
	public List<DropToolBO> giveTools(String userId, int toolType, int toolId, int toolNum, int useType);

	/**
	 * 打开宝箱
	 * 
	 * @param userId
	 * @param toolId
	 * @return
	 */
	public CommonDropBO openGiftBox(String userId, int toolId, EventHandle handle);

	/**
	 * 将掉落 道具追加到掉落数据封将对象上
	 * 
	 * @param userId
	 * @param commonDropBO
	 * @param dropToolBO
	 */
	public void appendToDropBO(String userId, CommonDropBO commonDropBO, DropToolBO dropToolBO);

	/**
	 * 开宝箱的时候需要的钥匙
	 * 
	 * @param userId
	 * @param toolId
	 */
	public CommonDropBO checkOpenBoxLooseTool(String userId, int toolId);

	/**
	 * 跟住道具类型拿用户的道具列表
	 * 
	 * @param userId
	 * @param toolType
	 * @return
	 */
	public List<UserToolBO> getToolBoByToolType(String userId, int toolType);

	/**
	 * 是否道具
	 * 
	 * @param toolType
	 * @return
	 */
	public boolean isTool(int toolType);
}
