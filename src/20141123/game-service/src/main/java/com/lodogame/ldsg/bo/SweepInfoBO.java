package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SweepInfoBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Mapper(name = "st")
	private long startTime;
	@Mapper(name = "et")
	private long endTime;
	@Mapper(name = "ts")
	private int times;
	@Mapper(name = "fid")
	private int forcesId;
	@Mapper(name = "pw")
	private int power;
	@Mapper(name = "sta")
	private int status;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}
}
