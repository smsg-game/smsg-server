package com.lodogame.model;

import java.io.Serializable;

public class TavernDropTool implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 抽奖类型
	 */
	private int type;

	private int systemHeroId;

	/**
	 * 星级
	 */
	private int star;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

}
