package com.lodogame.model;

public class GiftCode {

	private String code;

	private int flag;

	private int type;

	private int giftBagType;

	private int timesLimit;

	private String serverIds;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGiftBagType() {
		return giftBagType;
	}

	public void setGiftBagType(int giftBagType) {
		this.giftBagType = giftBagType;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(int timesLimit) {
		this.timesLimit = timesLimit;
	}

	public String getServerIds() {
		return serverIds;
	}

	public void setServerIds(String serverIds) {
		this.serverIds = serverIds;
	}

}
