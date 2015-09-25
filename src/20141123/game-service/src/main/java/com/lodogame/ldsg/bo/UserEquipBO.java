package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.model.UserEquipPolish;

@Compress
public class UserEquipBO {

	/**
	 * 用户装备ID(唯一ID)
	 */
	@Mapper(name = "ueid")
	private String userEquipId;

	/**
	 * 系统装备ID
	 */
	@Mapper(name = "seid")
	private int equipId;

	/**
	 * 穿戴的武将ID
	 */
	@Mapper(name = "uhid")
	private String userHeroId;

	/**
	 * 装备等级
	 */
	@Mapper(name = "elv")
	private int equipLevel;

	/**
	 * 装备名字
	 */
	private String equipName;

	/**
	 * 装备类型
	 */
	@Mapper(name = "etype")
	private int equipType;

	/**
	 * 生命值
	 */
	@Mapper(name = "life")
	private int life;

	/**
	 * 物攻值
	 */
	@Mapper(name = "pha")
	private int physicsAttack;

	/**
	 * 物防
	 */
	@Mapper(name = "phd")
	private int physicsDefense;

	/**
	 * 出售价格
	 */
	@Mapper(name = "pric")
	private int price;

	/**
	 * 强化成功概率
	 */
	@Mapper(name = "ur")
	private int upgradetRate;
	
	/**
	 * 洗练副属性
	 */
	@Mapper(name = "uep")
	private UserEquipPolishBO userEquipPolishBO;
	
	/**
	 * 星级（神器专用）
	 */
	@Mapper(name = "st")
	private int start;
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getUserEquipId() {
		return userEquipId;
	}

	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getEquipLevel() {
		return equipLevel;
	}

	public void setEquipLevel(int equipLevel) {
		this.equipLevel = equipLevel;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getPhysicsAttack() {
		return physicsAttack;
	}

	public void setPhysicsAttack(int physicsAttack) {
		this.physicsAttack = physicsAttack;
	}

	public int getPhysicsDefense() {
		return physicsDefense;
	}

	public void setPhysicsDefense(int physicsDefense) {
		this.physicsDefense = physicsDefense;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getUpgradetRate() {
		return upgradetRate;
	}

	public void setUpgradetRate(int upgradetRate) {
		this.upgradetRate = upgradetRate;
	}

	public UserEquipPolishBO getUserEquipPolishBO() {
		return userEquipPolishBO;
	}

	public void setUserEquipPolishBO(UserEquipPolishBO userEquipPolishBO) {
		this.userEquipPolishBO = userEquipPolishBO;
	}

}
