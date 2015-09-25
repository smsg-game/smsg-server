/**
 * Boss.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.model;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public class Boss {

	public static final int BOSS_EXISTS_MINUTES = 30;

	public static final int MAX_CHALLENGE_TIMES = 4;
	public static final int MAX_EXISTING_SECOND_TIME = 30 * 60;

	public static final int COOLDOWN_SECOND = 10 * 60;

	public Boss(int forcesId) {
		this.forcesId = forcesId;
	}

	private int forcesId;

	public int getForcesId() {
		return forcesId;
	}

	public void setForcesId(int forcesId) {
		this.forcesId = forcesId;
	}

}
