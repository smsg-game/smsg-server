package com.lodogame.model;

import java.io.Serializable;

/**
 * 用户装备洗练
 * 
 * @author zyz
 * 
 */
public class UserEquipPolish implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6509645960500157486L;

	private String userId;
	
	private String userEquipId;
	
	private int attack;
	
	private int life;
	
	private int defense;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEquipId() {
		return userEquipId;
	}

	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}
	
	
}
