package com.lodogame.game.server.context;

public class ClientConfigContext extends ConfigContext {
	protected String connectorId;
	protected int nums;

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}
	
}
