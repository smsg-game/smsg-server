package com.lodogame.model;

import java.util.Date;


public class Faction {

	private int factionId;
	
	private String factionName;
	
	private String factionMaster;
	
	private int memberNum;
	
	private int memberLimit;
	
	private String factionNotice;
	
	private Date createdTime;
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getFactionId() {
		return factionId;
	}

	public void setFactionId(int factionId) {
		this.factionId = factionId;
	}

	public String getFactionName() {
		return factionName;
	}

	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}

	public String getFactionMaster() {
		return factionMaster;
	}

	public void setFactionMaster(String factionMaster) {
		this.factionMaster = factionMaster;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public int getMemberLimit() {
		return memberLimit;
	}

	public void setMemberLimit(int memberLimit) {
		this.memberLimit = memberLimit;
	}

	public String getFactionNotice() {
		return factionNotice;
	}

	public void setFactionNotice(String factionNotice) {
		this.factionNotice = factionNotice;
	}
	
}
