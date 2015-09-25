package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserMonthlyCardBO {
	
	@Mapper(name = "isb")
	private int isBought = 0;
	
	@Mapper(name = "ld")
	private int leftDays = -1;

	public int getIsBought() {
		return isBought;
	}

	public void setIsBought(int isBought) {
		this.isBought = isBought;
	}

	public int getLeftDays() {
		return leftDays;
	}

	public void setLeftDays(int leftDays) {
		this.leftDays = leftDays;
	}
	
	
}
