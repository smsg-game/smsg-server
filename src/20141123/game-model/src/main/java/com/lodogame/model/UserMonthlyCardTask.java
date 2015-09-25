package com.lodogame.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserMonthlyCardTask {

	private String userId;
	private String startDate;
	
	/**
	 * 结束日期，但是不包含这一天
	 */
	private String endDate;
	

	
	public UserMonthlyCardTask(String userId, String startDate, String endDate) {
		this.userId = userId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public UserMonthlyCardTask() {
		
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isTaskExpired() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate;
		Date endDate;
		try {
			startDate = sdf.parse(this.startDate + " 00:00:00");
			endDate = sdf.parse(this.endDate + " 00:00:00");
			
			Date now = new Date();
			
			if (now.after(startDate) && now.before(endDate)) {
				return false;
			} 
			return true;

		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
 	}
}
