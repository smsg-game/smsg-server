package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserTavernBO;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.TavernDrawEvent;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TavernService;

/**
 * 任务相关action
 * 
 * @author jacky
 * 
 */

public class TavernAction extends LogicRequestAction {

	private static final Logger logger = Logger.getLogger(TavernAction.class);

	@Autowired
	private TavernService tavernService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private PushHandler pushHandler;

	/**
	 * 酒馆抽武将
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response draw() {

		int type = this.getInt("tp", 0);
		int times = this.getInt("ts", 1);

		logger.debug("用户酒馆抽奖.uid[" + getUid() + "], type[" + type + "]");

		tavernService.draw(getUid(), type, times, new EventHandle() {

			public boolean handle(Event event) {

				if (event instanceof TavernDrawEvent) {

					List<String> userHeroIdList = (List<String>) event.getObject("userHeroIdList");

					List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
					for (String userHeroId : userHeroIdList) {
						UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
						userHeroBOList.add(userHeroBO);
					}

					set("hs", userHeroBOList);
					UserTavernBO userTavernBO = (UserTavernBO) event.getObject("userTavernBO");
					set("ti", userTavernBO);

					pushHandler.pushUser(getUid());

				}

				return true;
			}
		});

		return this.render();
	}
}
