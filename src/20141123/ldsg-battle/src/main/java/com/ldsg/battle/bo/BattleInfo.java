package com.ldsg.battle.bo;

import java.util.List;

import com.ldsg.battle.ASObject;

public class BattleInfo {

	private List<ASObject> heroList;

	private List<ASObject> warcraftList;

	private double addRatio = 0;

	private int level;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getAddRatio() {
		return addRatio;
	}

	public void setAddRatio(double addRatio) {
		this.addRatio = addRatio;
	}

	public List<ASObject> getHeroList() {
		return heroList;
	}

	public void setHeroList(List<ASObject> heroList) {
		this.heroList = heroList;
	}

	public List<ASObject> getWarcraftList() {
		return warcraftList;
	}

	public void setWarcraftList(List<ASObject> warcraftList) {
		this.warcraftList = warcraftList;
	}

}
