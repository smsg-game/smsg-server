package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ArenaConfigBO {

	/**
	 * 开始时间
	 */
	@Mapper(name = "st")
	private long startTime;

	/**
	 * 结束时间
	 */
	@Mapper(name = "et")
	private long endTime;

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

}
