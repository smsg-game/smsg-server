package com.lodogame.ldsg.bo;


import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserLimOnlineRewardBO {

	/**
	 * 奖励领取状态
	 * 0表示没有奖励可以领取，1表示有奖励可领取
	 */
	@Mapper(name = "st")
	private int status;
	
	/**
	 * 下次领取奖励的时间
	 */
	@Mapper(name = "rt")
	private long recTime;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getRecTime() {
		return recTime;
	}

	public void setRecTime(long recTime) {
		this.recTime = recTime;
	}
}
