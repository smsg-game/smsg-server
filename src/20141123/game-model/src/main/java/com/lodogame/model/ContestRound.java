package com.lodogame.model;

import java.io.Serializable;

/**
 * 表示擂台赛每轮比赛的信息
 * @author chengevo
 *
 */

public class ContestRound implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ContestRound(int roundId, long roundStartTime) {
		super();
		this.roundId = roundId;
		this.roundStartTime = roundStartTime;
	}

	private int roundId;
	
	private long roundStartTime;

	public int getRoundId() {
		return roundId;
	}

	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}

	public long getRoundStartTime() {
		return roundStartTime;
	}

	public void setRoundStartTime(long roundStartTime) {
		this.roundStartTime = roundStartTime;
	}
}
