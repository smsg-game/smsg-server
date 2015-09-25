package com.lodogame.model;

import java.io.Serializable;

/**
 * VIP
 * 
 * @author jacky
 * 
 */
public class SystemVIP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7715157674011222231L;

	private int vipId;

	private int vipLevel;

	private int vipGold;

	private String vipDesc;

	private String vipName;

	public int getVipId() {
		return vipId;
	}

	public void setVipId(int vipId) {
		this.vipId = vipId;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getVipGold() {
		return vipGold;
	}

	public void setVipGold(int vipGold) {
		this.vipGold = vipGold;
	}

	public String getVipDesc() {
		return vipDesc;
	}

	public void setVipDesc(String vipDesc) {
		this.vipDesc = vipDesc;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

}
