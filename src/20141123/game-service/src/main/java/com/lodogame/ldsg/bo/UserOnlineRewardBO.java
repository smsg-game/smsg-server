package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserOnlineRewardBO {

	/**
	 * 礼包类型
	 */
	@Mapper(name = "stp")
	private int subType;

	/**
	 * 领取时间
	 */
	@Mapper(name = "rt")
	private long time;

	/**
	 * 分钟
	 */
	@Mapper(name = "min")
	private int minute;

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

}
