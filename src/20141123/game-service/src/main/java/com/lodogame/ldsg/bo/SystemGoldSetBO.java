package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 金币套餐
 * 
 * @author jacky
 * 
 */
@Compress
public class SystemGoldSetBO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Mapper(name = "id")
	private int systemGoldSetId;

	@Mapper(name = "mo")
	private BigDecimal money;

	@Mapper(name = "gd")
	private int gold;

	private int type;

	@Mapper(name = "ti")
	private String title;

	@Mapper(name = "des")
	private String description;

	private Date startTime;

	private Date endTime;

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getSystemGoldSetId() {
		return systemGoldSetId;
	}

	public void setSystemGoldSetId(int systemGoldSetId) {
		this.systemGoldSetId = systemGoldSetId;
	}

}
