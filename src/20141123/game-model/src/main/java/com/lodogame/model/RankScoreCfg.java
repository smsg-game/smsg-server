package com.lodogame.model;

import java.io.Serializable;

public class RankScoreCfg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int rank;
	private int score;
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
