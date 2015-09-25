package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 通用掉落信息BO
 * 
 * @author jacky
 * 
 */
@Compress
public class CommonDropBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 掉落英雄列表
	 */
	@Mapper(name = "hls")
	private List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

	/**
	 * 掉落装备列表
	 */
	@Mapper(name = "eqs")
	private List<UserEquipBO> userEquipBOList = new ArrayList<UserEquipBO>();

	/**
	 * 掉落材料列表
	 */
	@Mapper(name = "tls")
	private List<UserToolBO> userToolBOList = new ArrayList<UserToolBO>();

	/**
	 * 抽奖道具(抽奖用)
	 */
	@Mapper(name = "dls")
	private List<ActivityDrawToolBO> drawToolBOList = new ArrayList<ActivityDrawToolBO>();

	/**
	 * 经验
	 */
	@Mapper(name = "exp")
	private int exp;

	/**
	 * 金币
	 */
	@Mapper(name = "gd")
	private int gold;

	/**
	 * 银币
	 */
	@Mapper(name = "co")
	private int copper;

	/**
	 * VIP增加的经验
	 */
	@Mapper(name = "aexp")
	private int vipAddExp;

	/**
	 * VIP增加的银币
	 */
	@Mapper(name = "aco")
	private int vipAddCopper;

	/**
	 * 用户等级增长
	 */
	@Mapper(name = "ulv")
	private int levelUp;

	/**
	 * 用户基础攻击增长
	 */
	@Mapper(name = "pha")
	private int attackAdd;

	/**
	 * 用户基础防御增长
	 */
	@Mapper(name = "phd")
	private int defenseAdd;

	/**
	 * 用户基础生命增长
	 */
	@Mapper(name = "hp")
	private int lifeAdd;

	/**
	 * 获得积分，用于争霸赛
	 */
	@Mapper(name = "sc")
	private int score;

	/**
	 * 体力
	 */
	private int power;

	/**
	 * 最新排名，用于争霸赛
	 */
	@Mapper(name = "rk")
	private int rank;

	/**
	 * 总排名上升名次
	 */
	@Mapper(name = "urk")
	private int uprank;

	/**
	 * 最新组排名，用于争霸赛
	 */
	@Mapper(name = "grk")
	private int grank;

	/**
	 * 组排名上升名次
	 */
	@Mapper(name = "ugrk")
	private int upgrank;

	@Mapper(name = "hb")
	private int heroBag;

	@Mapper(name = "eb")
	private int equipBag;

	@Mapper(name = "mi")
	private int mind;

	/**
	 * vip等级
	 */
	private int vipLevel;

	@Mapper(name = "re")
	private int reputation;

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public int getUprank() {
		return uprank;
	}

	public void setUprank(int uprank) {
		this.uprank = uprank;
	}

	public int getUpgrank() {
		return upgrank;
	}

	public void setUpgrank(int upgrank) {
		this.upgrank = upgrank;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public List<ActivityDrawToolBO> getDrawToolBOList() {
		return drawToolBOList;
	}

	/**
	 * 增加一个掉落武将
	 * 
	 * @param userHeroBO
	 */
	public void addHero(UserHeroBO userHeroBO) {
		userHeroBOList.add(userHeroBO);
	}

	/**
	 * 增加一个掉落装备
	 * 
	 * @param userEquipBO
	 */
	public void addEquip(UserEquipBO userEquipBO) {
		userEquipBOList.add(userEquipBO);
	}

	/**
	 * 增加一个闪光道具
	 * 
	 * @param outId
	 */
	public void addFlashTool(ActivityDrawToolBO activityDrawToolBO) {
		drawToolBOList.add(activityDrawToolBO);
	}

	/**
	 * 是否需要推送用户数据
	 * 
	 * @return
	 */
	public boolean isNeedPushUser() {

		if (this.getCopper() > 0 || this.getGold() > 0 || this.getExp() > 0 || this.getPower() > 0) {
			return true;
		}

		if (this.getHeroBag() > 0 || this.getEquipBag() > 0) {
			return true;
		}

		if (this.getVipLevel() > 0) {
			return true;
		}

		if (this.getReputation() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 增加一个掉落装备
	 * 
	 * @param userToolBO
	 */
	public void addTool(UserToolBO userToolBO) {
		userToolBOList.add(userToolBO);
	}

	public int getLevelUp() {
		return levelUp;
	}

	public void setLevelUp(int levelUp) {
		this.levelUp = levelUp;
	}

	public int getAttackAdd() {
		return attackAdd;
	}

	public void setAttackAdd(int attackAdd) {
		this.attackAdd = attackAdd;
	}

	public int getDefenseAdd() {
		return defenseAdd;
	}

	public void setDefenseAdd(int defenseAdd) {
		this.defenseAdd = defenseAdd;
	}

	public int getLifeAdd() {
		return lifeAdd;
	}

	public void setLifeAdd(int lifeAdd) {
		this.lifeAdd = lifeAdd;
	}

	public List<UserHeroBO> getUserHeroBOList() {
		return userHeroBOList;
	}

	public List<UserEquipBO> getUserEquipBOList() {
		return userEquipBOList;
	}

	public List<UserToolBO> getUserToolBOList() {
		return userToolBOList;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public int getVipAddExp() {
		return vipAddExp;
	}

	public void setVipAddExp(int vipAddExp) {
		this.vipAddExp = vipAddExp;
	}

	public int getVipAddCopper() {
		return vipAddCopper;
	}

	public void setVipAddCopper(int vipAddCopper) {
		this.vipAddCopper = vipAddCopper;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
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

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getGrank() {
		return grank;
	}

	public int getMind() {
		return mind;
	}

	public void setMind(int mind) {
		this.mind = mind;
	}

	public void setGrank(int grank) {
		this.grank = grank;
	}

}
