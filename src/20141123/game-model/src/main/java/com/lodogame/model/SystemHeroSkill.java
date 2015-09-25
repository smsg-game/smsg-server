package com.lodogame.model;

import java.io.Serializable;

/**
 * 武将组合技能
 * 
 * @author jacky
 * 
 */
public class SystemHeroSkill implements Serializable {

	private static final long serialVersionUID = 3853738419054208383L;

	private int heroId;

	private int skillId;

	private int needHeroId1;

	private int needHeroId2;

	private int needHeroId3;

	private int needHeroId4;

	private int needHeroId5;

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getNeedHeroId1() {
		return needHeroId1;
	}

	public void setNeedHeroId1(int needHeroId1) {
		this.needHeroId1 = needHeroId1;
	}

	public int getNeedHeroId2() {
		return needHeroId2;
	}

	public void setNeedHeroId2(int needHeroId2) {
		this.needHeroId2 = needHeroId2;
	}

	public int getNeedHeroId3() {
		return needHeroId3;
	}

	public void setNeedHeroId3(int needHeroId3) {
		this.needHeroId3 = needHeroId3;
	}

	public int getNeedHeroId4() {
		return needHeroId4;
	}

	public void setNeedHeroId4(int needHeroId4) {
		this.needHeroId4 = needHeroId4;
	}

	public int getNeedHeroId5() {
		return needHeroId5;
	}

	public void setNeedHeroId5(int needHeroId5) {
		this.needHeroId5 = needHeroId5;
	}

}
