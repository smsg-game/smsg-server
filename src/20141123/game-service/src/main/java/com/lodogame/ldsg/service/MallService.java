package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.SystemMallBO;
import com.lodogame.model.SystemMallDiscountActivity;

public interface MallService {

	/**
	 * 购买道具-金币不足
	 */
	public final static int BUY_MALL_GOLD_NOT_ENOUGH = 2001;

	/**
	 * 购买道具-银币不足
	 */
	public final static int BUY_MALL_COPPER_NOT_ENOUGH = 2002;

	/**
	 * 购买道具-次数
	 */
	public final static int BUY_MALL_TIME_NOT_ENOUGH = 2003;

	/**
	 * 客户端和服务端的折扣率不同，不可购买
	 */
	public final static int DISCOUNT_NOT_MATCH = 2004;

	/**
	 * VIP等级不足，不可购买
	 */
	public final static int BUY_MALL_VIP_LEVEL_NOT_ENOUGH = 2005;

	/**
	 * 总购买次数超过限制，不可购买
	 */
	public final static int BUY_MALL_TOTAL_TIME_NOT_ENOUGH = 2006;

	/**
	 * 购买道具-契约碎片不足
	 */
	public final static int BUY_MALL_HERO_SHARD_NOT_ENOUGH = 2007;

	/**
	 * 获取商城商品列表
	 * 
	 * @return
	 */
	public List<SystemMallBO> getMallList(String userId, int type);

	/**
	 * 购买商品
	 * 
	 * @param userId
	 * @param mallId
	 * @param num
	 */
	public CommonDropBO buy(String userId, int mallId, int num, int discount);

	/**
	 * 检查是否在打折活动时间内
	 * 
	 * @return
	 */
	public SystemMallDiscountActivity checkDiscountIsOpen();
}
