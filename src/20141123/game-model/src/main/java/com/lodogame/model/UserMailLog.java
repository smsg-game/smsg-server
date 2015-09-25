package com.lodogame.model;

import java.util.Date;

public class UserMailLog {

	private Date lastReceiveTime;
	private int isZaned;

	public Date getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(Date lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public int getIsZaned() {
		return isZaned;
	}

	public void setIsZaned(int isZaned) {
		this.isZaned = isZaned;
	}

}
