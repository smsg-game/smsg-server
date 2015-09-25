package com.lodogame.ldsg.event;

import com.lodogame.model.UserEquip;

public class EquipUpdateEvent extends BaseEvent {
	
	public EquipUpdateEvent(String userId, String userEquipId) {
		this.userId = userId;
		this.data.put("userEquipId", userEquipId);
	}
	public EquipUpdateEvent(String userId, String userEquipId,String userHeroId) {
		this.userId = userId;
		this.data.put("userEquipId", userEquipId);
		this.data.put("userHeroId", userHeroId);
		
	}

	public EquipUpdateEvent(String userId, UserEquip userEquip) {
		this.userId = userId;
		this.data.put("userEquip", userEquip);
	}
}
