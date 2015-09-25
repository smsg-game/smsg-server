package com.lodogame.model;

import java.util.Date;
/**
 * 用户每日获取日志
 * @author foxwang
 *
 */
public class UserDailyGainLog {
    private String userId;
    private int type;
	private Date date;
	private int amount;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
  
}
