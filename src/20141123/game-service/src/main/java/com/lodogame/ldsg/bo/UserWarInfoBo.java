package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserWarInfoBo {
	
	@Mapper(name = "uid")
	private String userId;
	
	@Mapper(name = "po")
	private Integer point;
	
	@Mapper(name = "cid")
	private int countryID;
	
	@Mapper(name = "mdn")
	private int myDefenseNum;
	
	@Mapper(name = "man")
	private int myAttackNum;
	
	@Mapper(name = "dcd")
	private long drawCD;
	
	@Mapper(name = "acd")
	private long actionCD;
	
	@Mapper(name = "icd")
	private long inspireCD;
	
	@Mapper(name = "rn")
	private int reputationNum;
	
	@Mapper(name = "inm")
	private int inspireNum;
	
	@Mapper(name = "lcd")
	private long liftCD;
	
	public long getLiftCD() {
		return liftCD;
	}
	public void setLiftCD(long liftCD) {
		this.liftCD = liftCD;
	}
	public int getInspireNum() {
		return inspireNum;
	}
	public void setInspireNum(int inspireNum) {
		this.inspireNum = inspireNum;
	}
	public int getReputationNum() {
		return reputationNum;
	}
	public void setReputationNum(int reputationNum) {
		this.reputationNum = reputationNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public int getCountryID() {
		return countryID;
	}
	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}
	public int getMyDefenseNum() {
		return myDefenseNum;
	}
	public void setMyDefenseNum(int myDefenseNum) {
		this.myDefenseNum = myDefenseNum;
	}
	public int getMyAttackNum() {
		return myAttackNum;
	}
	public void setMyAttackNum(int myAttackNum) {
		this.myAttackNum = myAttackNum;
	}
	public long getDrawCD() {
		return drawCD;
	}
	public void setDrawCD(long drawCD) {
		this.drawCD = drawCD;
	}
	public long getActionCD() {
		return actionCD;
	}
	public void setActionCD(long actionCD) {
		this.actionCD = actionCD;
	}
	public long getInspireCD() {
		return inspireCD;
	}
	public void setInspireCD(long inspireCD) {
		this.inspireCD = inspireCD;
	}
	
}
