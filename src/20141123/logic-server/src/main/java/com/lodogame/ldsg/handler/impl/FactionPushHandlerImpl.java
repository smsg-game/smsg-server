package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.handler.FactionPushHandler;

public class FactionPushHandlerImpl extends BasePushHandler implements FactionPushHandler {

	@Override
	public void pushFactionApplyTips(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		this.push("Faction.pushFactionApplyTips", params);
	}

	@Override
	public void pushDissolveFaction(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		this.push("Faction.pushDissolveFaction", params);
	}

}
