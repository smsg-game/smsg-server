/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2013
 */

package com.lodogame.model;

public class SweepInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1;
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.String userId;
	private java.lang.Integer forcesId;
	private java.util.Date createdTime;
	private java.lang.Integer times;
	private java.lang.Integer power;
	/**
	 * 状态，0正在扫荡，1为完成，2为领取，-1为停止
	 */
	private Integer status;
	private java.util.Date endTime;
	private java.util.Date updatedTime;
	//columns END

	public SweepInfo(){
	}

	public SweepInfo(
		java.lang.String userId,
		java.lang.Integer forcesId,
		java.util.Date createdTime
	){
		this.userId = userId;
		this.forcesId = forcesId;
		this.createdTime = createdTime;
	}

	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setForcesId(java.lang.Integer value) {
		this.forcesId = value;
	}
	
	public java.lang.Integer getForcesId() {
		return this.forcesId;
	}
	public void setCreatedTime(java.util.Date value) {
		this.createdTime = value;
	}
	
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	public void setTimes(java.lang.Integer value) {
		this.times = value;
	}
	
	public java.lang.Integer getTimes() {
		return this.times;
	}
	public void setPower(java.lang.Integer value) {
		this.power = value;
	}
	
	public java.lang.Integer getPower() {
		return this.power;
	}
	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	public void setEndTime(java.util.Date value) {
		this.endTime = value;
	}
	
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	public void setUpdatedTime(java.util.Date value) {
		this.updatedTime = value;
	}
	
	public java.util.Date getUpdatedTime() {
		return this.updatedTime;
	}
}

