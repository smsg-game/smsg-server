package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserBO implements Serializable {

	private final static int BAG_MAX = 800;

	private static final long serialVersionUID = 1L;

	private String userId;

	@Mapper(name = "pid")
	private long playerId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "lv")
	private int level;

	@Mapper(name = "gd")
	private long goldNum;

	@Mapper(name = "co")
	private long copper;

	@Mapper(name = "exp")
	private long exp;

	@Mapper(name = "od")
	private int orderNum;

	private Date orderAddTime;

	private Date regTime;

	private Date updatedTime;

	@Mapper(name = "fc")
	private int power;

	@Mapper(name = "fat")
	private long powerAddTime;

	@Mapper(name = "fai")
	private long powerAddInterval;

	@Mapper(name = "vl")
	private int vipLevel;

	@Mapper(name = "ve")
	private long vipExpired;

	@Mapper(name = "hbl")
	private int heroBagLimit;

	@Mapper(name = "ebl")
	private int equipBagLimit;

	@Mapper(name = "bci")
	private Map<String, Integer> buyCopperInfo;

	@Mapper(name = "pm")
	private int payAmount;

	/**
	 * 用户新手引导步骤
	 */
	@Mapper(name = "gs")
	private int guideStep;

	private int winCount;

	private int loseCount;
	
	@Mapper(name = "re")
	private int reputation;
	
	@Mapper(name = "md")
	private int mind;

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getLoseCount() {
		return loseCount;
	}

	public void setLoseCount(int loseCount) {
		this.loseCount = loseCount;
	}

	public int getGuideStep() {
		return guideStep;
	}

	public void setGuideStep(int guideStep) {
		this.guideStep = guideStep;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(long goldNum) {
		this.goldNum = goldNum;
	}

	public long getCopper() {
		return copper;
	}

	public void setCopper(long copper) {
		this.copper = copper;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public Date getOrderAddTime() {
		return orderAddTime;
	}

	public void setOrderAddTime(Date orderAddTime) {
		this.orderAddTime = orderAddTime;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public long getPowerAddTime() {
		return powerAddTime;
	}

	public void setPowerAddTime(long powerAddTime) {
		this.powerAddTime = powerAddTime;
	}

	public long getPowerAddInterval() {
		return powerAddInterval;
	}

	public void setPowerAddInterval(long powerAddInterval) {
		this.powerAddInterval = powerAddInterval;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public long getVipExpired() {
		return vipExpired;
	}

	public void setVipExpired(long vipExpired) {
		this.vipExpired = vipExpired;
	}

	public int getHeroBagLimit() {
		return heroBagLimit;
	}

	public void setHeroBagLimit(int heroBagLimit) {
		if (heroBagLimit > BAG_MAX) {
			heroBagLimit = BAG_MAX;
		}
		this.heroBagLimit = heroBagLimit;
	}

	public int getEquipBagLimit() {
		return equipBagLimit;
	}

	public void setEquipBagLimit(int equipBagLimit) {
		if (equipBagLimit > BAG_MAX) {
			equipBagLimit = BAG_MAX;
		}
		this.equipBagLimit = equipBagLimit;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Map<String, Integer> getBuyCopperInfo() {
		return buyCopperInfo;
	}

	public void setBuyCopperInfo(Map<String, Integer> buyCopperInfo) {
		this.buyCopperInfo = buyCopperInfo;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public int getMind() {
		return mind;
	}

	public void setMind(int mind) {
		this.mind = mind;
	}

	
}
