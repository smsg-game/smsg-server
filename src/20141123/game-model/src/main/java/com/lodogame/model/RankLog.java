package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class RankLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rankKey;
	private String date;
	private String rankData;
	private Date createTime;

	public String getRankKey() {
		return rankKey;
	}

	public void setRankKey(String rankKey) {
		this.rankKey = rankKey;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRankData() {
		return rankData;
	}

	public void setRankData(String rankData) {
		this.rankData = rankData;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
