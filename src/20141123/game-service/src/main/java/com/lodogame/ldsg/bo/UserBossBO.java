/**
 * UserBossBO.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-23
 */
@Compress
public class UserBossBO {

	@Mapper(name = "uid")
	private String userId;

//	@Mapper(name = "sid")
//	private int sceneId;

	@Mapper(name = "fid")
	private int forcesId;

//	@Mapper(name = "mid")
//	private int mapId;

	@Mapper(name = "ts")
	private int times;

	@Mapper(name = "cd")
	private long cooldown;

	@Mapper(name = "bdt")
	private long bossDisappearTime;

	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

//	public final int getSceneId() {
//		return sceneId;
//	}
//
//	public final void setSceneId(int sceneId) {
//		this.sceneId = sceneId;
//	}

	public final int getForcesId() {
		return forcesId;
	}

//	public int getMapId() {
//		return mapId;
//	}
//
//	public void setMapId(int mapId) {
//		this.mapId = mapId;
//	}

	public final void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

	public final int getTimes() {
		return times;
	}

	public final void setTimes(int times) {
		this.times = times;
	}

	public final long getCooldown() {
		return cooldown;
	}

	public final void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public long getBossDisappearTime() {
		return bossDisappearTime;
	}

	public void setBossDisappearTime(long bossDisappearTime) {
		this.bossDisappearTime = bossDisappearTime;
	}
}
