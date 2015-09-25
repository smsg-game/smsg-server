package com.ldsg.battle.vo;

public class BuffVO {

	private int effectUID;

	/**
	 * 是否是一个掉血的buff
	 */
	private boolean isReduceLife;

	/**
	 * 是否是一个加血的buff
	 */
	private boolean isAddLife;

	/**
	 * 当前血量
	 */
	private long life;

	/**
	 * buff所有者位置
	 */
	private String position;

	/**
	 * 持续回合数
	 */
	private int round;

	/**
	 * 触发时机
	 */
	private int triggerType;

	/**
	 * buff数值
	 */
	private long value;

	public int getEffectUID() {
		return effectUID;
	}

	public void setEffectUID(int effectUID) {
		this.effectUID = effectUID;
	}

	public boolean isReduceLife() {
		return isReduceLife;
	}

	public void setReduceLife(boolean isReduceLife) {
		this.isReduceLife = isReduceLife;
	}

	public long getLife() {
		return life;
	}

	public void setLife(long life) {
		this.life = life;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public int getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(int triggerType) {
		this.triggerType = triggerType;
	}

	public boolean isAddLife() {
		return isAddLife;
	}

	public void setAddLife(boolean isAddLife) {
		this.isAddLife = isAddLife;
	}

}
