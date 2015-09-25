/**
 * BossBattleReportBO.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-27
 */
@Compress
public class BossBattleReportBO {

	@Mapper(name = "rp")
	private String report;

	@Mapper(name = "fid")
	private int bossForceId;

	@Mapper(name = "tp")
	private int type;

	@Mapper(name = "rf")
	private int result;

	@Mapper(name = "dr")
	private CommonDropBO drop;

	@Mapper(name = "bgid")
	private int backgroundId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "pid")
	private long playerId;

	@Mapper(name = "uid")
	private String userId;
	
	@Mapper(name = "hero")
	private UserHeroBO hero;

	public BossBattleReportBO() {}
	
	public BossBattleReportBO(int bossForceId, int type, int backgroundId, String userId, String username, long playerId, UserHeroBO hero) {
		super();
		this.bossForceId = bossForceId;
		this.type = type;
		this.backgroundId = backgroundId;
		this.userId = userId;
		this.username = username;
		this.playerId = playerId;
		this.hero = hero;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public int getBossForceId() {
		return bossForceId;
	}

	public void setBossForceId(int bossForceId) {
		this.bossForceId = bossForceId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public CommonDropBO getDrop() {
		return drop;
	}

	public void setDrop(CommonDropBO drop) {
		this.drop = drop;
	}

	public int getBackgroundId() {
		return backgroundId;
	}

	public void setBackgroundId(int backgroundId) {
		this.backgroundId = backgroundId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserHeroBO getHero() {
		return hero;
	}

	public void setHero(UserHeroBO hero) {
		this.hero = hero;
	}

}
