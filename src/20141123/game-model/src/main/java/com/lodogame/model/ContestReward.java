package com.lodogame.model;

import java.io.Serializable;

public class ContestReward implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private int rewardId;
	
	private String description;
	
	private String dropToolIds;

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDropToolIds() {
		return dropToolIds;
	}

	public void setDropToolIds(String dropToolIds) {
		this.dropToolIds = dropToolIds;
	}

}
