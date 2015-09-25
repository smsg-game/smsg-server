package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class BattleHeroBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户武将ID
	 */
	private String userHeroId;

	/**
	 * 系统武将ID
	 */
	@Mapper(name = "hid")
	private int systemHeroId;

	/**
	 * 最大生命
	 */
	@Mapper(name = "thp")
	private long maxLife;

	/**
	 * 生命
	 */
	@Mapper(name = "hp")
	private long life;

	/**
	 * 物攻
	 */
	@Mapper(name = "phy_a")
	private int physicalAttack;

	/**
	 * 物防
	 */
	@Mapper(name = "phy_d")
	private int physicalDefense;

	/**
	 * 主动技能
	 */
	@Mapper(name = "p")
	private int plan;

	/**
	 * 普通攻击
	 */
	@Mapper(name = "np")
	private int normalPlan;

	/**
	 * 速度
	 */
	@Mapper(name = "sp")
	private int speed;

	/**
	 * 策攻
	 */
	@Mapper(name = "p_a")
	private int strategyAttack;

	/**
	 * 策防
	 */
	@Mapper(name = "p_d")
	private int strategyDefense;

	/**
	 * 格挡
	 */
	@Mapper(name = "pa")
	private int parry;

	/**
	 * 暴击
	 */
	@Mapper(name = "cr")
	private int crit;

	/**
	 * 闪避
	 */
	@Mapper(name = "d")
	private int dodge;

	/**
	 * 命中
	 */
	@Mapper(name = "h")
	private int hit;

	/**
	 * 头像ID
	 */
	@Mapper(name = "iid")
	private int imgId;

	/**
	 * 系统兵符ID
	 */
	@Mapper(name = "sstid")
	private int soliderTokenId;

	/**
	 * 武将名字
	 */
	@Mapper(name = "hn")
	private String name;

	/**
	 * 武将站位
	 */
	@Mapper(name = "es")
	private int pos;

	/**
	 * 武将攻击类型
	 */
	@Mapper(name = "at")
	private int attackType;

	/**
	 * 武将等级
	 */
	@Mapper(name = "hl")
	private int level;

	@Mapper(name = "nt")
	private int nation;

	/**
	 * 技能列表(组合技 ska,skb,skc)
	 */
	@Mapper(name = "msklist")
	private List<MergeSkillBO> mergeSkillList;

	/**
	 * 技能列表 (包括天赋和被动 )
	 */
	@Mapper(name = "sklist")
	private List<Integer> skillList;

	/**
	 * 武将职业
	 */
	@Mapper(name = "car")
	private int career;

	/**
	 * 头像放大倍率
	 */
	@Mapper(name = "imr")
	private int imgageType = 1;

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public long getLife() {
		return life;
	}

	public void setLife(long life) {
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

	public int getNation() {
		return nation;
	}

	public void setNation(int nation) {
		this.nation = nation;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getStrategyAttack() {
		return strategyAttack;
	}

	public void setStrategyAttack(int strategyAttack) {
		this.strategyAttack = strategyAttack;
	}

	public int getStrategyDefense() {
		return strategyDefense;
	}

	public void setStrategyDefense(int strategyDefense) {
		this.strategyDefense = strategyDefense;
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

	public int getSoliderTokenId() {
		return soliderTokenId;
	}

	public void setSoliderTokenId(int soliderTokenId) {
		this.soliderTokenId = soliderTokenId;
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

	public int getAttackType() {
		return attackType;
	}

	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public List<MergeSkillBO> getMergeSkillList() {
		return mergeSkillList;
	}

	public void setMergeSkillList(List<MergeSkillBO> mergeSkillList) {
		this.mergeSkillList = mergeSkillList;
	}

	public void addMergeSkill(MergeSkillBO mergeSkillBO) {
		if (this.mergeSkillList == null) {
			this.mergeSkillList = new ArrayList<MergeSkillBO>();
		}

		this.mergeSkillList.add(mergeSkillBO);
	}

	public void addSkill(Integer skillId) {
		if (this.skillList == null) {
			this.skillList = new ArrayList<Integer>();
		}
		skillList.add(skillId);
	}

	public List<Integer> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<Integer> skillList) {
		this.skillList = skillList;
	}

	public int getImgageType() {
		return imgageType;
	}

	public void setImgageType(int imgageType) {
		this.imgageType = imgageType;
	}

	public int getNormalPlan() {
		return normalPlan;
	}

	public void setNormalPlan(int normalPlan) {
		this.normalPlan = normalPlan;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public long getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(long maxLife) {
		this.maxLife = maxLife;
	}

}
