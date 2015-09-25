package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class PowerRankBO implements Serializable, Comparable<PowerRankBO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Mapper(name = "rk")
	private int rank;

	@Mapper(name = "pr")
	private int power;

	@Mapper(name = "wp")
	private double winPercent;

	@Mapper(name = "un")
	private String username;

	private long playerId;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public double getWinPercent() {
		return winPercent;
	}

	public void setWinPercent(double winPercent) {
		this.winPercent = winPercent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public int compareTo(PowerRankBO o) {
		return this.power - o.getPower();
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
