package com.lodogame.ldsg.event;

import java.util.List;

import com.lodogame.ldsg.bo.UserTavernBO;

public class TavernDrawEvent extends BaseEvent {
	public TavernDrawEvent(String userId, List<String> userHeroIdList, UserTavernBO userTavernBO) {
		this.userId = userId;
		this.data.put("userHeroIdList", userHeroIdList);
		this.data.put("userTavernBO", userTavernBO);
	}
}
