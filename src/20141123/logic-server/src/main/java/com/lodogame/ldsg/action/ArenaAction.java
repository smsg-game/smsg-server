package com.lodogame.ldsg.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ArenaRankBO;
import com.lodogame.ldsg.bo.ArenaRegBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ArenaService;

/**
 * 百人斩action
 * 
 * @author jacky
 * 
 */

public class ArenaAction extends LogicRequestAction {

	@Autowired
	private ArenaService arenaService;

	@Autowired
	private PushHandler pushHandler;

	private static final Logger logger = Logger.getLogger(ArenaAction.class);

	/**
	 * 获取商城商品列表
	 * 
	 * @return
	 */
	public Response enter() {

		String userId = getUid();

		logger.debug("进入百人斩界面.uid[" + userId + "]");

		this.arenaService.enter(userId);

		ArenaRegBO arenaRegBO = this.arenaService.getRegBO(userId);

		List<ArenaRankBO> arenaRankBOList = this.arenaService.getRankList();

		set("rkl", arenaRankBOList);
		set("uinfo", arenaRegBO);

		return this.render();
	}

	public Response quit() {

		String userId = getUid();

		logger.debug("退出百人斩界面.uid[" + getUid() + "]");

		this.arenaService.quit(userId);

		return this.render();
	}

	public Response encourage() {

		String userId = getUid();

		logger.debug("进入百人斩界面.uid[" + userId + "]");

		this.arenaService.encourage(userId);

		this.pushHandler.pushUser(userId);

		return this.render();
	}

	public Response matcher() {

		String userId = getUid();

		logger.debug("继续战斗匹配.uid[" + getUid() + "]");

		this.arenaService.startMatcher(userId);

		return this.render();
	}

}
