package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserHeroBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户武将ID
	 */
	@Mapper(name = "uhid")
	private String userHeroId;

	/**
	 * 用户ID
	 */
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 武将人物ID
	 */
	private int heroId;

	/**
	 * 系统武将ID
	 */
	@Mapper(name = "shid")
	private int systemHeroId;

	/**
	 * 生命
	 */
	@Mapper(name = "hp")
	private int life;

	/**
	 * 物攻
	 */
	@Mapper(name = "pha")
	private int physicalAttack;

	/**
	 * 物防
	 */
	@Mapper(name = "phd")
	private int physicalDefense;

	/**
	 * 主动技能
	 */
	private int plan;

	/**
	 * 普通攻击
	 */
	private int normalPlan;

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
	 * 天赋技能4
	 */
	private int skill5;

	/**
	 * 格挡
	 */
	@Mapper(name = "pry")
	private int parry;

	/**
	 * 暴击
	 */
	@Mapper(name = "cri")
	private int crit;

	/**
	 * 闪避
	 */
	@Mapper(name = "dk")
	private int dodge;

	/**
	 * 命中
	 */
	@Mapper(name = "hit")
	private int hit;

	/**
	 * 头像ID
	 */
	@Mapper(name = "img")
	private int imgId;

	/**
	 * 卡牌ID
	 */
	@Mapper(name = "cid")
	private int cardId;

	/**
	 * 武将名字
	 */
	@Mapper(name = "hn")
	private String name;

	/**
	 * 武将站位
	 */
	@Mapper(name = "pos")
	private int pos;

	/**
	 * 武将等级
	 */
	@Mapper(name = "lv")
	private int level;

	/**
	 * 武 将经验
	 */
	@Mapper(name = "exp")
	private int exp;

	/**
	 * 武将被吞噬的经验
	 */
	@Mapper(name = "dexp")
	private int dexp;

	/**
	 * 武将职业
	 */
	private int career;

	/**
	 * 武将出售价格
	 */
	@Mapper(name = "price")
	private int price;

	/**
	 * 武将卡牌的卡套
	 */
	@Mapper(name = "ol")
	private int outLine;

	/**
	 * 血祭等级
	 */
	@Mapper(name = "stage")
	private int stage;

	/**
	 * 血祭技能
	 */
	@Mapper(name = "bssid")
	private int bloodSacrificeSkillID;

	/**
	 * 武将锁定状态
	 */
	@Mapper(name = "ls")
	private int lockStatus;

	/**
	 * 专属装备技能ID
	 */
	@Mapper(name = "sskid")
	private int suitSkillId;
	
	/**
	 * 化神节点等级
	 */
	@Mapper(name = "sdfid")
	private int  deifyNodeLevel;
	
	/**
	 * 是否有专属神器
	 */
	@Mapper(name = "iw")
	private int isArms = 0;
	
	public int getIsArms() {
		return isArms;
	}

	public void setIsArms(int isArms) {
		this.isArms = isArms;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
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

	public int getSkill5() {
		return skill5;
	}

	public void setSkill5(int skill5) {
		this.skill5 = skill5;
	}

	public int getParry() {
		return parry;
	}

	public void setParry(int parry) {
		this.parry = parry;
	}

	public int getCrit() {
		return crit;
	}

	public void setCrit(int crit) {
		this.crit = crit;
	}

	public int getDodge() {
		return dodge;
	}

	public void setDodge(int dodge) {
		this.dodge = dodge;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getDexp() {
		return dexp;
	}

	public void setDexp(int dexp) {
		this.dexp = dexp;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getNormalPlan() {
		return normalPlan;
	}

	public void setNormalPlan(int normalPlan) {
		this.normalPlan = normalPlan;
	}

	public int getOutLine() {
		return outLine;
	}

	public void setOutLine(int outLine) {
		this.outLine = outLine;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getBloodSacrificeSkillID() {
		return bloodSacrificeSkillID;
	}

	public void setBloodSacrificeSkillID(int bloodSacrificeSkillID) {
		this.bloodSacrificeSkillID = bloodSacrificeSkillID;
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	public int getSuitSkillId() {
		return suitSkillId;
	}

	public void setSuitSkillId(int suitSkillId) {
		this.suitSkillId = suitSkillId;
	}

	public int getDeifyNodeLevel() {
		return deifyNodeLevel;
	}

	public void setDeifyNodeLevel(int deifyNodeLevel) {
		this.deifyNodeLevel = deifyNodeLevel;
	}

	
}
