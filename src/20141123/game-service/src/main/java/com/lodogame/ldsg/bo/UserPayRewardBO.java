package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserPayRewardBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 奖励ID
	 */
	@Mapper(name = "rid")
	private int rewardId;

	/**
	 * 金额
	 */
	@Mapper(name = "pm")
	private int payMoney;

	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBoList;

	/**
	 * 可领取次数
	 */
	@Mapper(name = "tlm")
	private int timesLimit;

	/**
	 * 已领取次数
	 */
	@Mapper(name = "ts")
	private int times;

	/**
	 * 描述
	 */
	@Mapper(name = "desc")
	private String description;

	/**
	 * 状态，-1表示未达条件，不可领取，0表示可领取，1表示已领取
	 */
	@Mapper(name = "st")
	private int status;

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}

	public List<DropToolBO> getDropToolBoList() {
		return dropToolBoList;
	}

	public void setDropToolBoList(List<DropToolBO> dropToolBoList) {
		this.dropToolBoList = dropToolBoList;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(int timesLimit) {
		this.timesLimit = timesLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
