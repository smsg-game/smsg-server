package com.lodogame.ldsg.handler.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserWarInfoDao;
import com.lodogame.ldsg.bo.CityBo;
import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.handler.WarPushHandler;
import com.lodogame.ldsg.service.WarService;

public class WarPushHandlerImpl extends BasePushHandler implements WarPushHandler {

	@Autowired
	private WarService warService;

	@Autowired
	private UserWarInfoDao userWarInfoDao;

	@Override
	public void pushAllCity() {
		Collection<String> lists = userWarInfoDao.getWarUserIdList();
		Map<String, Object> pushParam = new HashMap<String, Object>();
		List<CityBo> list = warService.getAllCity();
		pushParam.put("cl", list);
		for (String userId : lists) {
			pushParam.put("uid", userId);
			this.push("War.pushAllCity", pushParam);
		}
	}

	@Override
	public void pushWarEnd() {
		Map<String, Object> pushParam = new HashMap<String, Object>();

		Collection<String> lists = userWarInfoDao.getWarUserIdList();
		for (String userId : lists) {
			pushParam.put("uid", userId);
			this.push("War.pushWarEnd", pushParam);
		}

	}

	@Override
	public void pushDefenseNum(String userId, Map<String, String> params) {
		Map<String, Object> pushParam = new HashMap<String, Object>();
		pushParam.put("uid", userId);
		pushParam.put("dn", params.get("defenseNum"));
		this.push("War.pushDefenseNum", pushParam);

	}

	@Override
	public void pushBattle(String userId, Map<String, String> params) {

		Map<String, Object> pushParam = new HashMap<String, Object>();

		pushParam.put("rid", params.get("rid"));
		pushParam.put("acid", params.get("acid"));
		pushParam.put("dcid", params.get("dcid"));
		pushParam.put("dpo", params.get("dpo"));
		pushParam.put("occ", params.get("occ"));
		pushParam.put("uid", userId);
		pushParam.put("auid", params.get("attackUserId"));
		pushParam.put("aun", params.get("attackUsername"));
		pushParam.put("duid", params.get("defenseUserId"));
		pushParam.put("dun", params.get("defenseUsername"));
		pushParam.put("rf", Integer.parseInt(params.get("rf")));
		pushParam.put("rp", params.get("report"));
		pushParam.put("tp", 6);
		pushParam.put("isc", 0);

		this.push("War.pushBattle", pushParam);

	}

}
