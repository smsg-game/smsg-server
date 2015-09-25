package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserRecivePowerBO {

	@Mapper(name = "sq")
	private int type;

	@Mapper(name = "st")
	private long startTime;

	@Mapper(name = "et")
	private long endTime;

	@Mapper(name = "ts")
	private int times;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
