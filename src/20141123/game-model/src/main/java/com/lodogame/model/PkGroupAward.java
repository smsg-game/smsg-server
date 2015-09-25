package com.lodogame.model;

/**
 * 组奖励数据
 * @author Candon
 *
 */
public class PkGroupAward {
	
	private int groupId;
	private int rank;
	private String toolIds;
	private int copper;
	private String title;
	
	public int getCopper() {
		return copper;
	}
	public void setCopper(int copper) {
		this.copper = copper;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getToolIds() {
		return toolIds;
	}
	public void setToolIds(String toolIds) {
		this.toolIds = toolIds;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
