package com.lodogame.model;

import java.io.Serializable;

public class PkAward implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int awardId;
	private String name;
	private int score;
	private String tools;
	private String imgId;
	private String description;
	public int getAwardId() {
		return awardId;
	}
	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getTools() {
		return tools;
	}
	public void setTools(String tools) {
		this.tools = tools;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
}
