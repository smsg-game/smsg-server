package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserBossInfoBO {

	@Mapper(name = "fid")
	private int forcesId;
	
	@Mapper(name = "ts")
	private int times;
	
	@Mapper(name = "cd")
	private long cooldown;
	
	@Mapper(name = "bdt")
	private long bossDisappearTime;
	
	@Mapper(name="mt")
	private int maxChallengeTime;

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

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public long getBossDisappearTime() {
		return bossDisappearTime;
	}

	public void setBossDisappearTime(long bossDisappearTime) {
		this.bossDisappearTime = bossDisappearTime;
	}

	public int getMaxChallengeTime() {
		return maxChallengeTime;
	}

	public void setMaxChallengeTime(int maxChallengeTime) {
		this.maxChallengeTime = maxChallengeTime;
	}
}
