package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemBloodSacrificeEquipBO implements Serializable{

	private static final long serialVersionUID = 1L;

	@Mapper(name = "ueid")
	private String userEquipId;
	
	@Mapper(name = "num")
	private int exchangeNum = 1;

	public String getUserEquipId() {
		return userEquipId;
	}

	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}
	
	

}
