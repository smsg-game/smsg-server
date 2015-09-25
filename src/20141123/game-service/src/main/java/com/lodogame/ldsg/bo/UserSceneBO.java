package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserSceneBO {

	private String userId;

	@Mapper(name = "sid")
	private int sceneId;

	@Mapper(name = "pf")
	private int passFlag;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(int passFlag) {
		this.passFlag = passFlag;
	}

}
