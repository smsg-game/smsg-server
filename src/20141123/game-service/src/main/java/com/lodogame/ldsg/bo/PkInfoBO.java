package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class PkInfoBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 玩家ID
	@Mapper(name = "pid")
	private long playerId;
	// 排名
	@Mapper(name = "rk")
	private int rank;
	// 积分
	@Mapper(name = "sc")
	private int score;
	// 挑战剩余次数
	@Mapper(name = "pkt")
	private int pkTimes;
	//组排名
	@Mapper(name = "grk")
	private int gRank;
	@Mapper(name = "gid")
	private int gid;
	/**
	 * 购买挑战次数
	 */
	@Mapper(name = "bpkt")
	private int buyPkTimes;

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public int getGRank() {
		return gRank;
	}

	public void setGRank(int gRank) {
		this.gRank = gRank;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

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

	public int getPkTimes() {
		return pkTimes;
	}

	public void setPkTimes(int pkTimes) {
		this.pkTimes = pkTimes;
	}

	public int getBuyPkTimes() {
		return buyPkTimes;
	}

	public void setBuyPkTimes(int buyPkTimes) {
		this.buyPkTimes = buyPkTimes;
	}

}
