package com.lodogame.ldsg.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.SystemMallBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.MallService;

/**
 * 商城action
 * 
 * @author jacky
 * 
 */

public class MallAction extends LogicRequestAction {

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private MallService mallService;

	private static final Logger logger = Logger.getLogger(MallAction.class);

	/**
	 * 获取商城商品列表
	 * 
	 * @return
	 */
	public Response getMallList() {

		logger.debug("获取商城商品列表.uid[" + getUid() + "]");

		int type = this.getInt("tp", 1);

		List<SystemMallBO> systemMallBOList = this.mallService.getMallList(this.getUid(), type);

		set("ml", systemMallBOList);
		set("tp", type);

		return this.render();
	}

	/**
	 * 获取商城道具折扣列表
	 */
	// public Response getDiscount() {
	//
	// }

	/**
	 * 购买商城物品
	 * 
	 * @return
	 */
	public Response buy() {

		String userId = this.getUid();

		int mallId = this.getInt("mid", 0);
		int num = this.getInt("nm", 0);
		int discount = this.getInt("dc", 100);

		logger.debug("购买商品.uid[" + getUid() + "], mallId[" + mallId + "], num[" + num + "]");

		CommonDropBO commonDropBO = this.mallService.buy(userId, mallId, num, discount);

		this.pushHandler.pushUser(userId);

		set("dr", commonDropBO);

		return this.render();
	}
}
