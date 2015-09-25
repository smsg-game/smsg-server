package com.lodogame.model;

/**
 * 修炼塔
 * @author chengevo
 *
 */
public class SystemDeifyTower {
	
	private int systemTowerId;
	private String towerName;
	
	/**
	 * 开放等级
	 */
	private int openLevel;
	
	/**
	 * 房间数量
	 */
	private int roomNum;
	
	/**
	 * 进入塔要花费的铜币
	 */
	private int copper;
	
	/**
	 * 产出
	 */
	private int output;
	
	/**
	 * 双倍收益的价格
	 */
	private int doubleOutputPrice;
	
	/**
	 * 保护时间价格
	 */
	private int protectTimePrice;

	/**
	 * 购买延长修炼费用
	 */
	private int buyDeifyTimePrice;
	
	public int getBuyDeifyTimePrice() {
		return buyDeifyTimePrice;
	}

	public void setBuyDeifyTimePrice(int buyDeifyTimePrice) {
		this.buyDeifyTimePrice = buyDeifyTimePrice;
	}

	public int getSystemTowerId() {
		return systemTowerId;
	}

	public void setSystemTowerId(int systemTowerId) {
		this.systemTowerId = systemTowerId;
	}

	public String getTowerName() {
		return towerName;
	}

	public void setTowerName(String towerName) {
		this.towerName = towerName;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}

	public int getDoubleOutputPrice() {
		return doubleOutputPrice;
	}

	public void setDoubleOutputPrice(int doubleOutputPrice) {
		this.doubleOutputPrice = doubleOutputPrice;
	}

	public int getProtectTimePrice() {
		return protectTimePrice;
	}

	public void setProtectTimePrice(int protectTimePrice) {
		this.protectTimePrice = protectTimePrice;
	}
}
