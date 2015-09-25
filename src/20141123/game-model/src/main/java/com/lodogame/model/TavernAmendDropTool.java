package com.lodogame.model;

import java.io.Serializable;

/**
 * 修正掉落武将
 * 
 * @author jacky
 * 
 */
public class TavernAmendDropTool implements Serializable {

	private static final long serialVersionUID = 1L;

	private int type;

	private int systemHeroId;

	private int lowerNum;

	private int upperNum;

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

	public int getLowerNum() {
		return lowerNum;
	}

	public void setLowerNum(int lowerNum) {
		this.lowerNum = lowerNum;
	}

	public int getUpperNum() {
		return upperNum;
	}

	public void setUpperNum(int upperNum) {
		this.upperNum = upperNum;
	}

}
