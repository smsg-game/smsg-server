package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ArenaRegBO {

	/**
	 * 最大连胜次数
	 */
	@Mapper(name = "mwc")
	private int maxWinCount;

	/**
	 * 连胜次数
	 */
	@Mapper(name = "wc")
	private int winCount;

	/**
	 * 胜利次数
	 */
	@Mapper(name = "wt")
	private int winTimes;

	/**
	 * 鼓舞次数
	 */
	@Mapper(name = "et")
	private int encourageTimes;

	public int getMaxWinCount() {
		return maxWinCount;
	}

	public void setMaxWinCount(int maxWinCount) {
		this.maxWinCount = maxWinCount;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(int winTimes) {
		this.winTimes = winTimes;
	}

	public int getEncourageTimes() {
		return encourageTimes;
	}

	public void setEncourageTimes(int encourageTimes) {
		this.encourageTimes = encourageTimes;
	}

}
