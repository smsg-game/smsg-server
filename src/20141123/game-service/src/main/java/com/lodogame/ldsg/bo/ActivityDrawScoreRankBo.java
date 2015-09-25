package com.lodogame.ldsg.bo;

import java.io.Serializable;
import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ActivityDrawScoreRankBo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Mapper(name = "rk")
	private int rank;
	
	@Mapper(name = "un")
	private String userName;

	@Mapper(name = "sc")
	private int score;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
	
}
