package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ContestTeamBO implements Serializable{

	private static final long serialVersionUID = 1L; 
	
	@Mapper(name = "id")
	private int id;

	/**
	 * 如果这个队列是准备进入下一场战斗的队列，cd 值为下一场比赛开始时间。如果这个队列已经比赛完了，cd 值为0
	 */
	@Mapper(name = "cd")
	private long roundStartTime;
	
	@Mapper(name = "name")
	private String name;
	
	@Mapper(name = "pvl")
	private List<ContestPlayerPairBO> contestPairBOList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ContestPlayerPairBO> getContestPairBOList() {
		return contestPairBOList;
	}

	public void setContestPairBOList(List<ContestPlayerPairBO> contestPairBOList) {
		this.contestPairBOList = contestPairBOList;
	}

	public long getRoundStartTime() {
		return roundStartTime;
	}

	public void setRoundStartTime(long roundStartTime) {
		this.roundStartTime = roundStartTime;
	}

	
}
