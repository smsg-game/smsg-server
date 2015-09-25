/**
 * BossTeamBO.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-24
 */
@Compress
public class BossTeamBO implements Serializable {

	private static final long serialVersionUID = 1L;

	public BossTeamBO() {}

	/**
	 * @param captainId 用户的lodo_id
	 */
	public BossTeamBO(String teamId, long captainId, int captainLevel, String captainName, int captainPower, int memberNumber, int maxMemberNumber, List<UserHeroBO> userHeroList) {
		super();
		this.teamId = teamId;
		this.captainId = captainId;
		this.captainLevel = captainLevel;
		this.captainName = captainName;
		this.captainPower = captainPower;
		this.memberNumber = memberNumber;
		this.maxMemberNumber = maxMemberNumber;
		this.userHeroList = userHeroList;
	}

	@Mapper(name = "tid")
	private String teamId;

	/**
	 * 用户的 lodo_id
	 */
	@Mapper(name = "cid")
	private  long captainId;
	
	@Mapper(name = "cl")
	private int captainLevel;

	@Mapper(name = "cn")
	private String captainName;

	@Mapper(name = "cp")
	private int captainPower;

	@Mapper(name = "mn")
	private int memberNumber;

	@Mapper(name = "mmn")
	private int maxMemberNumber;
	
	@Mapper(name = "hrl")
	private List<UserHeroBO> userHeroList;

	public final String getTeamId() {
		return teamId;
	}

	public final void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public final int getCaptainLevel() {
		return captainLevel;
	}

	public final void setCaptainLevel(int captainLevel) {
		this.captainLevel = captainLevel;
	}

	public final String getCaptainName() {
		return captainName;
	}

	public final void setCaptainName(String captainName) {
		this.captainName = captainName;
	}

	public final int getCaptainPower() {
		return captainPower;
	}

	public final void setCaptainPower(int captainPower) {
		this.captainPower = captainPower;
	}

	public final int getMemberNumber() {
		return memberNumber;
	}

	public final void setMemberNumber(int memberNumber) {
		this.memberNumber = memberNumber;
	}

	public final int getMaxMemberNumber() {
		return maxMemberNumber;
	}

	public final void setMaxMemberNumber(int maxMemberNumber) {
		this.maxMemberNumber = maxMemberNumber;
	}

	public long getCaptainId() {
		return captainId;
	}

	public void setCaptainId(long captainId) {
		this.captainId = captainId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<UserHeroBO> getUserHeroList() {
		return userHeroList;
	}

	public void setUserHeroList(List<UserHeroBO> userHeroList) {
		this.userHeroList = userHeroList;
	}

	
}
