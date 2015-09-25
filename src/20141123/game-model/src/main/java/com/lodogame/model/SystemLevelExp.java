package com.lodogame.model;

import java.io.Serializable;

/**
 * 武将经验等级配置
 * 
 * @author jacky
 * 
 */
public class SystemLevelExp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 经验
	 */
	private int exp;

	/**
	 * 等级
	 */
	private int level;

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
