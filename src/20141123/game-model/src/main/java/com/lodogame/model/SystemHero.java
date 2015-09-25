package com.lodogame.model;

import java.io.Serializable;

/**
 * 系统武将
 * 
 * @author jacky
 * 
 */
public class SystemHero implements Serializable {

	private static final long serialVersionUID = -5030957883739690497L;

	/**
	 * 系统武将ID
	 */
	private int systemHeroId;

	/**
	 * 武将星数
	 */
	private int heroStar;

	/**
	 * 武将人物唯一ID
	 */
	private int heroId;

	/**
	 * 武将名称
	 */
	private String heroName;

	/**
	 * 武将品质
	 */
	private int heroColor;

	/**
	 * 武将职业
	 */
	private int career;

	/**
	 * 普通攻击
	 */
	private int normalPlan;

	/**
	 * 主动技能
	 */
	private int plan;

	/**
	 * 天赋技能1
	 */
	private int skill1;

	/**
	 * 天赋技能2
	 */
	private int skill2;

	/**
	 * 天赋技能3
	 */
	private int skill3;

	/**
	 * 天赋技能4
	 */
	private int skill4;

	/**
	 * 头像id
	 */
	private int imgId;

	/**
	 * 卡牌ID
	 */
	private int cardId;

	/**
	 * 可兑换的兵符碎片数
	 */
	private int shardNum;

	/**
	 * 最大等级
	 */
	private int maxLevel;

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getHeroStar() {
		return heroStar;
	}

	public void setHeroStar(int heroStar) {
		this.heroStar = heroStar;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public int getHeroColor() {
		return heroColor;
	}

	public void setHeroColor(int heroColor) {
		this.heroColor = heroColor;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getSkill1() {
		return skill1;
	}

	public void setSkill1(int skill1) {
		this.skill1 = skill1;
	}

	public int getSkill2() {
		return skill2;
	}

	public void setSkill2(int skill2) {
		this.skill2 = skill2;
	}

	public int getSkill3() {
		return skill3;
	}

	public void setSkill3(int skill3) {
		this.skill3 = skill3;
	}

	public int getSkill4() {
		return skill4;
	}

	public void setSkill4(int skill4) {
		this.skill4 = skill4;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getShardNum() {
		return shardNum;
	}

	public void setShardNum(int shardNum) {
		this.shardNum = shardNum;
	}

	public int getNormalPlan() {
		return normalPlan;
	}

	public void setNormalPlan(int normalPlan) {
		this.normalPlan = normalPlan;
	}

}
