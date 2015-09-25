package com.ldsg.battle.vo;

public class AttackVO {

	/**
	 * 反击判断值
	 */
	private int counter = 0;

	/**
	 * 暴击判断值
	 */
	private int critValue;

	/**
	 * 是否命中
	 */
	private int hit;

	/**
	 * 被攻击者的位置
	 */
	private String position = "0";

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getCritValue() {
		return critValue;
	}

	public void setCritValue(int critValue) {
		this.critValue = critValue;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
