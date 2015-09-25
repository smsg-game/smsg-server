package com.lodogame.model;

/**
 * 专属神器属性
 * @author Candon
 *
 */
public class SystemDeifyAttr {
	private int id;
	private int deifyId;
	private int deifyLevel;
	private int attack;
	private int defense;
	private int life;
	private String upgradeTool;
	private int succLower;
	private int succUpper;
	private int failLower;
	private int failUpper;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDeifyId() {
		return deifyId;
	}
	public void setDeifyId(int deifyId) {
		this.deifyId = deifyId;
	}
	public int getDeifyLevel() {
		return deifyLevel;
	}
	public void setDeifyLevel(int deifyLevel) {
		this.deifyLevel = deifyLevel;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public String getUpgradeTool() {
		return upgradeTool;
	}
	public void setUpgradeTool(String upgradeTool) {
		this.upgradeTool = upgradeTool;
	}
	public int getSuccLower() {
		return succLower;
	}
	public void setSuccLower(int succLower) {
		this.succLower = succLower;
	}
	public int getSuccUpper() {
		return succUpper;
	}
	public void setSuccUpper(int succUpper) {
		this.succUpper = succUpper;
	}
	public int getFailLower() {
		return failLower;
	}
	public void setFailLower(int failLower) {
		this.failLower = failLower;
	}
	public int getFailUpper() {
		return failUpper;
	}
	public void setFailUpper(int failUpper) {
		this.failUpper = failUpper;
	}
	
}
