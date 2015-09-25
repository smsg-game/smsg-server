package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class WealthRankBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Mapper(name = "rk")
	private int rank;

	@Mapper(name = "cp")
	private double copper;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getCopper() {
		return copper;
	}

	public void setCopper(double copper) {
		this.copper = copper;
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
