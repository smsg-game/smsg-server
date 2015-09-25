/**
 * UserBoss.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public class UserBoss {

	private String userId;
	private int forcesId;

	private int times;
	private Date lastime;

	public UserBoss(String userId, int forcesId) {
		this.userId = userId;
		this.forcesId = forcesId;
		this.times = 0;
	}

	public void resetCooldown() {
		this.lastime = null;
	}

	private static Date getAfterTime(Date date, int second) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.SECOND, second);
		return cl.getTime();
	}

	public void addTimes() {
		this.times++;
	}

	public void refreshLastime() {
		this.lastime = new Date();
	}

	public boolean isCooldownCompleted() {
		return lastime == null || getAfterTime(lastime, Boss.COOLDOWN_SECOND).before(new Date());
	}

	public int getCooldown(TimeUnit unit) {
		if (isCooldownCompleted())
			return 0;

		int interval = (int) ((new Date().getTime() - lastime.getTime()) / 1000);

		switch (unit) {
		case MINUTES:
			return (interval % 60 > 0) ? (interval / 60) + 1 : interval / 60;
		case MILLISECONDS:
			return interval * 1000;
		case SECONDS:
			return interval;
		default:
			throw new IllegalArgumentException("The TimeUnit unsupported: " + unit);
		}

	}

	public long getCooldownCompleteTimepoint() {
		if (isCooldownCompleted()) {
			return 0;
		}
		return lastime.getTime() + (Boss.COOLDOWN_SECOND * 1000);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public Date getLastime() {
		return lastime;
	}

	public void setLastime(Date lastime) {
		this.lastime = lastime;
	}

}
