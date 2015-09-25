package com.lodogame.ldsg.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ToolService;

/**
 * 道具action
 * 
 * @author jacky
 * 
 */

public class ToolAction extends LogicRequestAction {

	@Autowired
	private ToolService toolService;

	@Autowired
	private PushHandler pushHandler;

	private static final Logger logger = Logger.getLogger(ActivityAction.class);

	/**
	 * 打开宝箱
	 * 
	 * @return
	 */
	public Response openGiftBox() {

		final String userId = getUid();

		int toolId = this.getInt("tid", 0);

		logger.debug("打开宝箱.userId[" + userId + "], toolId[" + toolId + "]");

		CommonDropBO commonDropBO = this.toolService.openGiftBox(userId, toolId, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return true;
			}
		});

		CommonDropBO CommonDropLoosenBO = this.toolService.checkOpenBoxLooseTool(userId, toolId);
		
		set("ldr", CommonDropLoosenBO);
		set("dr", commonDropBO);
		set("tid", toolId);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}
		
		return this.render();
	}


	/**
	 * 打开宝箱
	 * 
	 * @return
	 */
	public Response loadTools() {

		List<UserToolBO> userToolBOList = this.toolService.getUserToolList(getUid());
		set("tls", userToolBOList);

		return this.render();
	}

}
