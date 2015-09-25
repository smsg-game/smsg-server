package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class WarAllCDBO {
	
	@Mapper(name = "acd")
	private long actionTime;
	
	@Mapper(name = "lcd")
	private long liftTime;
	
	@Mapper(name = "dcd")
	private long drawTime;
	
	@Mapper(name = "man")
	private int attackNum;
	
	@Mapper(name = "mdn")
	private int defenseNum;
	
	@Mapper(name = "icd")
	private long inspireTime;
	
	@Mapper(name = "inm")
	private int inspireNum;
	
	@Mapper(name = "po")
	private Integer point;
	
	@Mapper(name = "st")
	private long startTime;
	
	@Mapper(name = "et")
	private long endTime;

	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getInspireNum() {
		return inspireNum;
	}
	public void setInspireNum(int inspireNum) {
		this.inspireNum = inspireNum;
	}
	public long getActionTime() {
		return actionTime;
	}
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}
	public long getLiftTime() {
		return liftTime;
	}
	public void setLiftTime(long liftTime) {
		this.liftTime = liftTime;
	}
	public long getDrawTime() {
		return drawTime;
	}
	public void setDrawTime(long drawTime) {
		this.drawTime = drawTime;
	}
	public int getAttackNum() {
		return attackNum;
	}
	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}
	public int getDefenseNum() {
		return defenseNum;
	}
	public void setDefenseNum(int defenseNum) {
		this.defenseNum = defenseNum;
	}
	public long getInspireTime() {
		return inspireTime;
	}
	public void setInspireTime(long inspireTime) {
		this.inspireTime = inspireTime;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	
}
