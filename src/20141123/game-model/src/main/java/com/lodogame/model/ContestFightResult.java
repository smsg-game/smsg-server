package com.lodogame.model;

public class ContestFightResult {

	
	private String playerPairId;
	
	/**
	 * 战斗结果
	 */
	private int result;
	
	/**
	 * 战斗类型
	 * 
	 * 擂台赛的战斗类型是7
	 */
	private int type = 7;
	
	/**
	 * 战斗背景id
	 */
	private int backgroundId = 1;
	
	/**
	 * 战报内容
	 */
	private String report;
	
	/**
	 * 进攻方用户名
	 */
	private String attUserName;
	
	/**
	 * 防守方用户名
	 */
	private String defUserName;


	public String getPlayerPairId() {
		return playerPairId;
	}

	public void setPlayerPairId(String playerPairId) {
		this.playerPairId = playerPairId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBackgroundId() {
		return backgroundId;
	}

	public void setBackgroundId(int backgroundId) {
		this.backgroundId = backgroundId;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getAttUserName() {
		return attUserName;
	}

	public void setAttUserName(String attUserName) {
		this.attUserName = attUserName;
	}

	public String getDefUserName() {
		return defUserName;
	}

	public void setDefUserName(String defUserName) {
		this.defUserName = defUserName;
	}
}
