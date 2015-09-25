package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserTotalDayPayRewardBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 奖励ID
	 */
	@Mapper(name = "rid")
	private int rewardId;


	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBoList;

	/**
	 * 领取条件的天数
	 */
	@Mapper(name = "day")
	private int day;

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

	public List<DropToolBO> getDropToolBoList() {
		return dropToolBoList;
	}

	public void setDropToolBoList(List<DropToolBO> dropToolBoList) {
		this.dropToolBoList = dropToolBoList;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
