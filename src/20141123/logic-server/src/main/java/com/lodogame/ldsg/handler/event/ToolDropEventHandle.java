package com.lodogame.ldsg.handler.event;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ToolDropEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemTool;
import com.lodogame.model.User;

public class ToolDropEventHandle implements EventHandle {

	private static final Logger LOG = Logger.getLogger(ToolDropEventHandle.class);

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemToolDao systemToolDao;

	@Override
	public boolean handle(Event event) {

		if (!(event instanceof ToolDropEvent)) {
			return true;
		}

		String userId = event.getString("userId");
		String toolName = event.getString("toolName");
		int toolId = event.getInt("toolId");
		int useType = event.getInt("useType");

		if (StringUtils.isEmpty(toolName)) {
			SystemTool tool = systemToolDao.get(toolId);
			if (tool == null) {
				LOG.error("获取道具失败，道具不存在.toolId[" + toolId + "]");
				return false;
			}
			toolName = tool.getName();
		}

		User user = userService.get(userId);
		int isFlash = event.getInt("isFlash");

		if (toolId == ToolId.GUI_TOOL_ID && useType == ToolUseType.ADD_FORCES) {

			messageService.sendGainToolMsg(userId, user.getUsername(), toolName, "转生塔", "鬼将即将诞生");

		} else if (isFlash == 1 && useType == ToolUseType.ADD_ACTIVITY_DRAW) {

			String title = "";
			if (toolId == ToolId.GUI_TOOL_ID) {
				title = "，鬼将即将诞生";
			}
			messageService.sendGainToolMsg(userId, user.getUsername(), toolName, "抽奖活动中", title);
		}

		return true;
	}

	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}
}
