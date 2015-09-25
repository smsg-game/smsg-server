package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;


@Compress
public class ContestTeamInfoBO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 小组名称
	 */
	@Mapper(name = "name")
	private String teamName;
	
	/**
	 * 小组 id
	 */
	@Mapper(name = "id")
	private int teamId;
	
	/**
	 * 可以容纳的玩家数量
	 */
	@Mapper(name = "cp")
	private int capacity;
	
	/**
	 * 已经进入该组的玩家数量
	 */
	@Mapper(name = "mc")
	private int playerNum;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}



	
}
