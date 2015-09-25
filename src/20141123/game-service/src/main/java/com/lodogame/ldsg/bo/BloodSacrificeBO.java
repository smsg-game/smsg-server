package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class BloodSacrificeBO implements Serializable  {

	private static final long serialVersionUID = -5522191282476405515L;
	
	/**
	 * 增加的血量比例
	 */
	@Mapper(name = "al")
	private int addLife;
	
	/**
	 * 血祭后可增加攻击的百分比
	 */
	@Mapper(name = "apa")
	private int addPhysicalAttack;
	
	/**
	 * 血祭后可增加防御的百分比
	 */
	@Mapper(name = "apd")
	private int addPhysicalDefense;
	
	/**
	 * 血祭后可获得的技能
	 */
	@Mapper(name = "bssid")
	private int BSSkillID;
	
	/**
	 * 血祭后卡牌的卡套
	 */
	@Mapper(name = "ol")
	private int outline;
	
	/**
	 * 血祭后卡牌的卡套
	 */
	@Mapper(name = "as")
	private int addStage;
	
	/**
	 * 掉落道具列表
	 */
	@Mapper(name = "dr")
	private List<DropToolBO> dropToolBO;

	public int getAddLife() {
		return addLife;
	}

	public void setAddLife(int addLife) {
		this.addLife = addLife;
	}

	public int getAddPhysicalAttack() {
		return addPhysicalAttack;
	}

	public void setAddPhysicalAttack(int addPhysicalAttack) {
		this.addPhysicalAttack = addPhysicalAttack;
	}

	public int getAddPhysicalDefense() {
		return addPhysicalDefense;
	}

	public void setAddPhysicalDefense(int addPhysicalDefense) {
		this.addPhysicalDefense = addPhysicalDefense;
	}

	public int getBSSkillID() {
		return BSSkillID;
	}

	public void setBSSkillID(int bSSkillID) {
		BSSkillID = bSSkillID;
	}

	public int getOutline() {
		return outline;
	}

	public void setOutline(int outline) {
		this.outline = outline;
	}

	public List<DropToolBO> getDropToolBO() {
		return dropToolBO;
	}

	public void setDropToolBO(List<DropToolBO> dropToolBO) {
		this.dropToolBO = dropToolBO;
	}

	public int getAddStage() {
		return addStage;
	}

	public void setAddStage(int addStage) {
		this.addStage = addStage;
	}
	
}
