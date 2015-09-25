package com.lodogame.ldsg.bo;

import java.io.Serializable;

import org.springframework.jmx.export.annotation.ManagedAttribute;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ContestPlayerPairBO implements Serializable{

	private static final long serialVersionUID = 1L;

	
	@Mapper(name = "id")
	private String playerPairId;
	
	/**
	 * 进攻方玩家 id 
	 */
	@Mapper(name = "atid")
	private String attUserId;
	
	@Mapper(name = "atname")
	private String attUserName;
	
	/**
	 * 防守方玩家 id
	 */
	@Mapper(name = "deid")
	private String defUserId;
	
	@Mapper(name = "dename")
	private String defUserName;
	
	/**
	 * 进攻方下注人数
	 */
	@Mapper(name = "abn")
	private int attBetNum;
	
	/**
	 * 防守方下注人数
	 */
	@Mapper(name = "dbn")
	private int defBetNum;

	/**
	 * 当前登录玩家是否已经下注，0表示没有下注1表是已经下注
	 */
	@Mapper(name = "isBet")
	private int isBet;
	
	/**
	 * 比赛结果
	 * <li>0表示这两个玩家还未战斗，1表示进攻方获胜，2表示防守方获胜
	 */
	@Mapper(name = "rt")
	private int result;
	
	/**
	 * 这个组玩家中是否包含当前登录玩家
	 * <p>0表示这个两个玩家都不是当前登录的玩家，1表示进攻方是，2表示防守方是
	 * @return
	 */
	@Mapper(name = "icu")
	private int isLoginUserIncluded;
	
	public int getIsLoginUserIncluded() {
		return isLoginUserIncluded;
	}

	/**
	 * <p>0表示这个两个玩家都不是当前登录的玩家，1表示进攻方是，2表示防守方是
	 * @param isLoginUserIncluded
	 */
	public void setIsLoginUserIncluded(int isLoginUserIncluded) {
		this.isLoginUserIncluded = isLoginUserIncluded;
	}


	public String getAttUserId() {
		return attUserId;
	}

	public void setAttUserId(String attUserId) {
		this.attUserId = attUserId;
	}

	public String getDefUserId() {
		return defUserId;
	}

	public void setDefUserId(String defUserId) {
		this.defUserId = defUserId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getAttUserName() {
		return attUserName;
	}

	public void setAttUserName(String attUsername) {
		this.attUserName = attUsername;
	}

	public String getDefUserName() {
		return defUserName;
	}

	public void setDefUserName(String defUsername) {
		this.defUserName = defUsername;
	}

	public int getAttBetNum() {
		return attBetNum;
	}

	public void setAttBetNum(int attBetNum) {
		this.attBetNum = attBetNum;
	}

	public int getDefBetNum() {
		return defBetNum;
	}

	public void setDefBetNum(int defBetNum) {
		this.defBetNum = defBetNum;
	}

	public int getIsBet() {
		return isBet;
	}

	public void setIsBet(int isBet) {
		this.isBet = isBet;
	}

	public String getPlayerPairId() {
		return playerPairId;
	}

	public void setPlayerPairId(String playerPairId) {
		this.playerPairId = playerPairId;
	}
}
