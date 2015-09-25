package com.lodogame.ldsg.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import alex.zhrenjie04.wordfilter.WordFilterUtil;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.MessageType;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.User;

public class MessageServiceImpl implements MessageService {

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserService userService;

	@Override
	public void sendPkMsg(String userId, String username) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("username", username);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_PK_WIN));
		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendGainHeroMsg(String userId, String username, String heroName, int star, String place) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("username", username);
		params.put("heroName", heroName);
		params.put("place", place);
		params.put("star", String.valueOf(star));
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_GAIN_HERO));
		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendGainEquipMsg(String userId, String username, String equipName) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("username", username);
		params.put("equipName", equipName);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_GAIN_EQUIP));
		sendMsg(params, Priority.MESSAGE);

	}

	private void sendMsg(Map<String, String> params, int priority) {

		Command command = new Command();
		command.setCommand(CommandType.COMMAND_PUSH_MESSAGE);
		command.setType(CommandType.PUSH_ALL);
		command.setParams(params);
		command.setPriority(priority);

		commandDao.add(command);

	}

	@Override
	public void sendSystemMsg(String content) {
		this.sendSystemMsg(content, "all");
	}

	@Override
	public void sendHornMsg(String userId, String content) {

		if (!toolService.reduceTool(userId, ToolType.HORN, ToolId.TOOL_HORN_ID, 1, ToolUseType.REDUCE_SEND_HORN_MSG)) {
			String message = "发送小喇叭失败，没有足够的道具.userId[" + userId + "]";
			throw new ServiceException(SEND_HORN_MSG_NOT_ENOUGH_TOOL, message);
		}

		content = WordFilterUtil.filterHtml(content, '*').getFilteredContent();

		User user = userService.get(userId);

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user.getUsername());
		params.put("content", content);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_HORN_MSG));

		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendTopUserLoginMsg(String userId, String title, String username) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("title", title);
		params.put("username", username);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_TOP_USER_LOGIN));

		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendGainToolMsg(String userId, String username, String toolName, String place, String title) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("username", username);
		params.put("toolName", toolName);
		params.put("title", title);
		params.put("place", place);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_GAIN_TOOL));
		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendRankUserLoginMsg(String userId, String title, String username) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("title", title);
		params.put("username", username);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_RANK_USER_LOGIN));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendVipLevelMsg(String userId, String username, int level) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("level", String.valueOf(level));
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_VIP_LEVEL));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendSceneMsg(String userId, String username, String scene) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("scene", scene);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_SCENE));

		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendHeroTowerMsg(String userId, String username, String heroName) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("heroName", heroName);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_HERO_TOWER));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendArenaMsg(String rank1, String username1, String rank2, String username2, String rank3, String username3) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("rank1", rank1);
		params.put("username1", username1);
		params.put("rank2", rank2);
		params.put("username2", username2);
		params.put("rank3", rank3);
		params.put("username3", username3);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_ARENA_RANK));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendPkUserLogin(String title, String username) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("title", title);
		params.put("username", username);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_PK_USER_LOGIN));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendPkUserLogout(String title, String username) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("title", title);
		params.put("username", username);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_PK_USER_LOGOUT));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendPkTotalFirst(String userId, String attack, String defense) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("attack", attack);
		params.put("defense", defense);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_PK_ONE));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendPkGroupFirst(String userId, String attack, String defense, String group) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("attack", attack);
		params.put("defense", defense);
		params.put("group", group);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_PK_GROUP_ONE));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendPkGetTitle(String userId, String username, String title) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("title", title);
		params.put("msgType", String.valueOf(MessageType.MEESSAGE_TYPE_PK_GET_TITLE));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendPkGoWin(String username, String title) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("username", username);
		params.put("title", title);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_PK_GO_WIN));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendSystemMsg(String content, String partnerIds) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("content", content);
		params.put("partnerIds", partnerIds);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_SYSTEM_MSG));
		sendMsg(params, Priority.HIGH);

	}

	@Override
	public void sendOpenGiftBoxMsg(String userId, String username, String title, String place, String toolName) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("userId", userId);
		params.put("username", username);
		params.put("title", title);
		params.put("place", place);
		params.put("toolName", toolName);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_OPEN_GIFT_BOX));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendWarAttackRankCreate(String username) {

		Map<String, String> params = new HashMap<String, String>();

		params.put("username", username);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_ATTACK_RANK_CREATE));

		sendMsg(params, Priority.MESSAGE);
	}

	@Override
	public void sendPkGoWinF(String title, String username, int times) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("title", title);
		params.put("username", username);
		params.put("times", String.valueOf(times));
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_PK_GO_WIN_FIFTY));

		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendWarMajorCity(String countryName, String username) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("country", countryName);
		params.put("username", username);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_WAR_MAJOR_CITY));
		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendWarDefense(String countryName, String username, String cityName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("country", countryName);
		params.put("username", username);
		params.put("cityname", cityName);
		params.put("msgType", String.valueOf(MessageType.MESSAGE_TYPE_WAR_DEFENSE_CITY));
		sendMsg(params, Priority.MESSAGE);

	}

	@Override
	public void sendDeifyForgeMsg(String username, String userHeroName, String equipName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("userHeroName", userHeroName);
		params.put("equipName", equipName);
		params.put("msgType", String.valueOf(MessageType.DEIFY_FROGE_TYPE_MSG));
		sendMsg(params, Priority.MESSAGE);
		
	}

}
