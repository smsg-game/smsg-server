package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserForcesBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private int sceneId;

	@Mapper(name = "fid")
	private int forcesId;

	@Mapper(name = "stat")
	private int status;

	@Mapper(name = "ts")
	private int times;

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

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
