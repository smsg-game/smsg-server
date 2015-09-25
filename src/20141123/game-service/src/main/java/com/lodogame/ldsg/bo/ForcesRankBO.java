package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ForcesRankBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Mapper(name = "rk")
	private int rank;

	@Mapper(name = "pc")
	private int passCount;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Mapper(name = "un")
	private String username;

}
