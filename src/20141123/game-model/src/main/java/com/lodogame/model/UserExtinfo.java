package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserExtinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	/**
	 * 购买银币次数
	 */
	private int buyCopperTimes;

	/**
	 * 最后购买银币时间
	 */
	private Date lastBuyCopperTime;

	/**
	 * 购买体力次数
	 */
	private int buyPowerTimes = 0;

	/**
	 * 最后购买体力时间
	 */
	private Date lastBuyPowerTime;

	/**
	 * 卡牌背包上阴
	 */
	private int heroMax;

	/**
	 * 装备背包上限
	 */
	private int equipMax;

	/**
	 * 用户新手引导步骤
	 */
	private int guideStep;

	/**
	 * 打赢争霸赛的次数
	 */
	private int winCount;

	/**
	 * 打输争霸赛的次数
	 */
	private int loseCount;

	/**
	 * 奖励的vip等级(不是充值来的)
	 */
	private int rewardVipLevel;

	/**
	 * 记录用户经历过的所有新手引导步骤
	 * 
	 * @return
	 */
	private String recordGuideStep;
	
	/**
	 * 记录用户选择的国家
	 * @return
	 */
	private int userNation;

	public String getRecordGuideStep() {
		return recordGuideStep;
	}

	public void setRecordGuideStep(String recordGuideStep) {
		this.recordGuideStep = recordGuideStep;
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

	public int getBuyCopperTimes() {
		return buyCopperTimes;
	}

	public void setBuyCopperTimes(int buyCopperTimes) {
		this.buyCopperTimes = buyCopperTimes;
	}

	public Date getLastBuyCopperTime() {
		return lastBuyCopperTime;
	}

	public void setLastBuyCopperTime(Date lastBuyCopperTime) {
		this.lastBuyCopperTime = lastBuyCopperTime;
	}

	public int getHeroMax() {
		return heroMax;
	}

	public void setHeroMax(int heroMax) {
		this.heroMax = heroMax;
	}

	public int getEquipMax() {
		return equipMax;
	}

	public void setEquipMax(int equipMax) {
		this.equipMax = equipMax;
	}

	public int getBuyPowerTimes() {
		return buyPowerTimes;
	}

	public void setBuyPowerTimes(int buyPowerTimes) {
		this.buyPowerTimes = buyPowerTimes;
	}

	public Date getLastBuyPowerTime() {
		return lastBuyPowerTime;
	}

	public void setLastBuyPowerTime(Date lastBuyPowerTime) {
		this.lastBuyPowerTime = lastBuyPowerTime;
	}
	public int getUserNation() {
		return userNation;
	}

	public void setUserNation(int userNation) {
		this.userNation = userNation;
	}


	public int getRewardVipLevel() {
		return rewardVipLevel;
	}

	public void setRewardVipLevel(int rewardVipLevel) {
		this.rewardVipLevel = rewardVipLevel;
	}
}
