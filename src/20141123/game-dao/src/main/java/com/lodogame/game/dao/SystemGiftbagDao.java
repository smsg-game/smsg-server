package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.GiftbagDropTool;
import com.lodogame.model.SystemGiftbag;

public interface SystemGiftbagDao {

	/**
	 * 获取首充礼包
	 * 
	 * @return
	 */
	public SystemGiftbag getFirstPayGiftbag();

	/**
	 * 获取VIP礼包
	 * 
	 * @param vipLevel
	 * @return
	 */
	public SystemGiftbag getVipGiftbag(int vipLevel);

	/**
	 * 获取礼包码礼包
	 * 
	 * @param subType
	 * @param giftBagType 
	 * @return
	 */
	public SystemGiftbag getCodeGiftBag(int subType, int giftBagType);

	/**
	 * 获取礼包掉落
	 * 
	 * @param giftbagId
	 * @return
	 */
	public List<GiftbagDropTool> getGiftbagDropToolList(int giftbagId);

	/**
	 * 获取在线礼包
	 * 
	 * @param subType
	 * @return
	 */
	public SystemGiftbag getOnlineGiftBag(int subType);

	/**
	 * 获取单笔充值奖励礼包
	 * @param giftBagType
	 * @param type
	 * @return
	 */
	SystemGiftbag getOncePayReward(int type, int subType);

	/**
	 * 获取累计充值奖励礼包
	 * @param type
	 * @param subtype
	 * @return
	 */
	public SystemGiftbag getTotalPayReward(int type, int subtype);
	

}
