package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.bo.DeifyRoomInfoBO;
import com.lodogame.ldsg.bo.DeifyTowerInfoBO;
import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.handler.DeifyPushHandler;
import com.lodogame.ldsg.service.DeifyService;

public class DeifyPushHandlerImpl extends BasePushHandler implements DeifyPushHandler{

	@Autowired
	private DeifyService deifyService;
	
	@Override
	public void pushTowerList(String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DeifyTowerInfoBO> towerInfoBOList = deifyService.createTowerInfoBOList();
		param.put("uid", userId);
		param.put("twls", towerInfoBOList);
		this.push("Deify.pushTowerList", param);
	}

	@Override
	public void pushDeifyStatus(String userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", userId);
		this.push("Deify.pushDeifyStatus", param);
	}

	@Override
	public void pushRoomList(String userId, int towerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DeifyRoomInfoBO> roomList = deifyService.getRoomList(userId, towerId);
		param.put("uid", userId);
		param.put("rol", roomList);
		param.put("twid", towerId);
		this.push("Deify.pushRoomList", param);
	}

	@Override
	public void pushProtected(String userId, int towerId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DeifyRoomInfoBO> roomList = deifyService.getRoomList(userId, towerId);
		param.put("uid", userId);
		param.put("twid", towerId);
		param.put("rol", roomList);
		this.push("Deify.pushProtected", param);
	}

	@Override
	public void pushReport(String userId, Map<String, String> param) {
		Map<String, Object> pushParam = new HashMap<String, Object>();
		int type = Integer.parseInt(param.get("type"));
		pushParam.put("uid", userId);
		pushParam.put("type", type);
		pushParam.put("cd", Integer.parseInt(param.get("cd")));
		pushParam.put("snum", Integer.parseInt(param.get("snum")));
		pushParam.put("twid", Integer.parseInt(param.get("twid")));
		pushParam.put("rid", Integer.parseInt(param.get("rid")));
		
		if (type != 3) {
			pushParam.put("rf", Integer.parseInt(param.get("rf")));
			pushParam.put("rp", param.get("rp"));
			pushParam.put("tp", Integer.parseInt(param.get("tp")));
			pushParam.put("bgid" , Integer.parseInt(param.get("bgid")));
			pushParam.put("atu", param.get("atu"));
			pushParam.put("defu", param.get("defu"));
			pushParam.put("utid", Integer.parseInt(param.get("utid")));
			pushParam.put("aod", Integer.parseInt(param.get("aod")));
		}
		this.push("Deify.pushReport", pushParam);
	}
	
	

	
	
	
	
}
