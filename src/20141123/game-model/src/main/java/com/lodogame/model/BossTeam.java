/**
 * BossTeam.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public class BossTeam {

	public static final int MAX_MEBMER_NUMBER = 3;
	public static final int MIN_MEBMER_NUMBER = 2;

	public static final int STATUS_WAITING = 0;
	public static final int STATUS_COUNTDOWN = 1;
	public static final int STATUS_FIGHTING = 2;
	public static final int STATUS_FINISH = 3;

	private String id;
	private Vector<String> members = new Vector<String>(MAX_MEBMER_NUMBER);
	private String captainId;
	private int forcesId;
	private Date createdTime;

	/**
	 * 已准备好进行封魔的玩家列表
	 */
	private Vector<String> preparedMemebers = new Vector<String>(MAX_MEBMER_NUMBER);

	private int status;

	public BossTeam(int forcesId, String userId, Date createdTime) {

		this.id = UUID.randomUUID().toString().replaceAll("-", "");

		this.forcesId = forcesId;
		this.captainId = userId;
		this.members.add(userId);
		this.createdTime = createdTime;

		this.status = STATUS_WAITING;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object team) {
		if (this == team)
			return true;
		if (team == null)
			return false;
		if (getClass() != team.getClass())
			return false;
		BossTeam other = (BossTeam) team;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * 获得已准备进行封魔的小队成员数量
	 * 
	 * @return
	 */
	public int getPreparedMemberNumber() {
		return this.preparedMemebers.size();
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * 记录已准备好进行封魔的用户
	 * 
	 * @param userId
	 *            用户编号
	 */
	public void addPreparedMember(String userId) {
		if (this.preparedMemebers.contains(userId)) {
			return;
		}
		this.preparedMemebers.add(userId);
	}

	public boolean isAllMembersReady() {
		return this.preparedMemebers.size() == members.size();
	}

	public boolean isFighting() {
		return this.status == STATUS_FIGHTING;
	}

	public boolean containsMember(String userId) {
		return members.contains(userId);
	}

	public List<String> getMembers() {
		return Collections.unmodifiableList(members);
	}

	public synchronized boolean addMember(String userId) {

		if (members.contains(userId)) {
			return true;
		}

		if (members.size() >= MAX_MEBMER_NUMBER) {
			return false;
		}

		return members.add(userId);
	}

	public boolean removeMember(String userId) {
		return members.removeElement(userId);
	}

	public int getTeamMemberCount() {
		return this.members.size();
	}

	public boolean shiftCaptain() {
		if (members.size() == 0)
			return false;

		setCaptainId(members.get(0));

		return true;
	}

	public void clean() {
		this.captainId = null;
		this.members.clear();
		this.status = STATUS_FINISH;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaptainId() {
		return captainId;
	}

	public void setCaptainId(String captainId) {
		this.captainId = captainId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

}
