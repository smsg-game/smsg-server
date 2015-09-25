package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
/**
 * 
 * @author Candon
 *
 */

@Compress
public class ForcesBo {
	@Mapper(name = "fid")
	private int forcesId;
	@Mapper(name = "ts")
	private int times;
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
	
	
}
