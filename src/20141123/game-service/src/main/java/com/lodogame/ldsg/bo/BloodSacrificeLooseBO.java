package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class BloodSacrificeLooseBO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Mapper(name = "hls")
	private List<ToolExchangeHeroBO> heroBOList = new ArrayList<ToolExchangeHeroBO>();

	@Mapper(name = "eqs")
	private List<ToolExchangeEquipBO> equipBOList = new ArrayList<ToolExchangeEquipBO>();

	@Mapper(name = "tls")
	private List<ToolExchangeToolBO> toolBOList = new ArrayList<ToolExchangeToolBO>();

	/**
	 * 金币
	 */
	@Mapper(name = "gd")
	private int gold;

	@Mapper(name = "co")
	private int copper;

	/**
	 * 体力
	 */
	@Mapper(name = "pw")
	private int power;

	@Mapper(name = "exp")
	private int exp;

	@Mapper(name = "hb")
	private int heroBag;

	@Mapper(name = "eb")
	private int equipBag;

	public List<ToolExchangeHeroBO> getHeroBOList() {
		return heroBOList;
	}

	public List<ToolExchangeEquipBO> getEquipBOList() {
		return equipBOList;
	}

	public void setEquipBOList(List<ToolExchangeEquipBO> equipBOList) {
		this.equipBOList = equipBOList;
	}

	public List<ToolExchangeToolBO> getToolBOList() {
		return toolBOList;
	}

	public void setToolBOList(List<ToolExchangeToolBO> toolBOList) {
		this.toolBOList = toolBOList;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getHeroBag() {
		return heroBag;
	}

	public void setHeroBag(int heroBag) {
		this.heroBag = heroBag;
	}

	public int getEquipBag() {
		return equipBag;
	}

	public void setEquipBag(int equipBag) {
		this.equipBag = equipBag;
	}
}
