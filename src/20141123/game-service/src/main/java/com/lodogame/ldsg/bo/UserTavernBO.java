package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserTavernBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 类型,0:广交豪杰，1：大摆筵席，2：千金一掷
	 */
	@Mapper(name = "tp")
	private int type;
	/**
	 * 描述
	 */
	@Mapper(name = "ds")
	private String desc;

	@Mapper(name = "gd")
	private int needGold;
	
	
	@Mapper(name = "tips")
	private String tips;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCoolTime() {
		return coolTime;
	}

	public void setCoolTime(long coolTime) {
		this.coolTime = coolTime;
	}

	/**
	 * 冷却时间，unix时间戳
	 */
	@Mapper(name = "cd")
	private long coolTime;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getNeedGold() {
		return needGold;
	}

	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	
}
