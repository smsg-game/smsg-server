package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 用户签到信息bo
 * 
 * @author jacky
 * 
 */
@Compress
public class UserCheckInLogBO {

	@Mapper(name = "day")
	public int day;

	@Mapper(name = "ci")
	public int isCheckIn;

	@Mapper(name = "fd")
	public int finishDay;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getIsCheckIn() {
		return isCheckIn;
	}

	public void setIsCheckIn(int isCheckIn) {
		this.isCheckIn = isCheckIn;
	}

	public int getFinishDay() {
		return finishDay;
	}

	public void setFinishDay(int finishDay) {
		this.finishDay = finishDay;
	}

}
