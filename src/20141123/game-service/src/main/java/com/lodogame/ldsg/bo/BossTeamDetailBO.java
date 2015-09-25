/**
 * BossTeamDetailBO.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-24
 */
@Compress
public class BossTeamDetailBO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Mapper(name = "hls")
	private List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
	
	@Mapper(name = "ic")
	private int captain;

	@Mapper(name = "pid")
	private long playerId;
	
	@Mapper(name = "un")
	private String userName;

	@Mapper(name = "ul")
	private int userLevel;

	@Mapper(name = "po")
	private int power;
	
	public BossTeamDetailBO() {}

	public BossTeamDetailBO(List<UserHeroBO> userHeroBOList, boolean isCaptain, long playerId, String userName, int userLevel, int power) {
		super();
		this.userHeroBOList = userHeroBOList;
		setCaptain(isCaptain);
		this.playerId = playerId;
		this.userName = userName;
		this.userLevel = userLevel;
		this.power = power;
	}


	public List<UserHeroBO> getUserHeroBOList() {
		return userHeroBOList;
	}

	public void setUserHeroBOList(List<UserHeroBO> userHeroBOList) {
		this.userHeroBOList = userHeroBOList;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getCaptain() {
		return captain;
	}

	public void setCaptain(int isCaptain) {
		this.captain = isCaptain;
	}
	
	public void setCaptain(boolean isCaptain) {
		this.captain = isCaptain ? 1 : 0;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

}
