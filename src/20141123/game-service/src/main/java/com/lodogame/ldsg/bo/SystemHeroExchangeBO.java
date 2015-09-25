package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemHeroExchangeBO {

	@Mapper(name = "eid")
	private int systemHeroExchangeId;

	private int week;

	@Mapper(name = "shid")
	private int systemHeroId;

	@Mapper(name = "tid")
	private int toolId;

	@Mapper(name = "tn")
	private int toolNum;
	
	@Mapper(name = "life")
	private int life;

	@Mapper(name = "pa")
	private int physicalAttack;

	@Mapper(name = "pd")
	private int physicalDefense;

	public int getSystemHeroExchangeId() {
		return systemHeroExchangeId;
	}

	public void setSystemHeroExchangeId(int systemHeroExchangeId) {
		this.systemHeroExchangeId = systemHeroExchangeId;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getPhysicalAttack() {
		return physicalAttack;
	}

	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	public int getPhysicalDefense() {
		return physicalDefense;
	}

	public void setPhysicalDefense(int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}

}
