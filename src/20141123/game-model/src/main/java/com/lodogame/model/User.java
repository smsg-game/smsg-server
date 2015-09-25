package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户模型
 * 
 * @author jacky
 * 
 */

public class User implements Serializable {

	private static final long serialVersionUID = -8711904146612278418L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 等级
	 */
	private int level;

	/**
	 * 金币
	 */
	private long goldNum;

	/**
	 * 银币
	 */
	private long copper;

	/**
	 * 经验
	 */
	private long exp;

	/**
	 * 军令
	 */
	private int orderNum;

	/**
	 * 军增加时间
	 */
	private Date orderAddTime;

	/**
	 * 体力加时间
	 */
	private Date powerAddTime;

	/**
	 * 注册时间
	 */
	private Date regTime;

	/**
	 * 更新时间
	 */
	private Date updatedTime;

	/**
	 * 体力
	 */
	private int power;

	/**
	 * lodo id
	 */
	private long lodoId;

	/**
	 * vip等级
	 */
	private int vipLevel;

	/**
	 * vip过期时间
	 */
	private Date vipExpiredTime;
	
	/**
	 * 0：没有被封号，1：被封号
	 * @return
	 */
	private int isBanned;
	
	/**
	 * 封号截止日期，过了这个日期以后，自动解除封号
	 * @return
	 */
	private Date dueTime;
	
	/**
	 * 国战声望
	 */
	private int reputation;
	
	/**
	 * 神魄
	 */
	private int mind;
	
	/**
	 * 禁言时间
	 * 
	 * @return
	 */
	private Date bannedChatTime;

	/**
	 * 帮派ID
	 */
	private int factionId;

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public int getIsBanned() {
		return isBanned;
	}

	public void setIsBanned(int isBanned) {
		this.isBanned = isBanned;
	}

	public Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

	public long getLodoId() {
		return lodoId;
	}

	public void setLodoId(long lodoId) {
		this.lodoId = lodoId;
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

	public Date getPowerAddTime() {
		return powerAddTime;
	}

	public void setPowerAddTime(Date powerAddTime) {
		this.powerAddTime = powerAddTime;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Date getVipExpiredTime() {
		return vipExpiredTime;
	}

	public void setVipExpiredTime(Date vipExpiredTime) {
		this.vipExpiredTime = vipExpiredTime;
	}

	public int getMind() {
		return mind;
	}

	public void setMind(int mind) {
		this.mind = mind;
	}

	public Date getBannedChatTime() {
		return bannedChatTime;
	}

	public void setBannedChatTime(Date bannedChatTime) {
		this.bannedChatTime = bannedChatTime;
	}

	public int getFactionId() {
		return factionId;
	}

	public void setFactionId(int factionId) {
		this.factionId = factionId;
	}
}
