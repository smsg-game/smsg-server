package com.lodogame.model;

import java.io.Serializable;

public class ToolExchange implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private int exchangeId;
	private String preExchangeItems;
	private String postExchangeItems;
	private String description;
	private int times;
	public int getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getPreExchangeItems() {
		return preExchangeItems;
	}
	public void setPreExchangeItems(String preExchangeItems) {
		this.preExchangeItems = preExchangeItems;
	}
	public String getPostExchangeItems() {
		return postExchangeItems;
	}
	public void setPostExchangeItems(String postExchangeItems) {
		this.postExchangeItems = postExchangeItems;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	

}
