package com.lodogame.model;

public class Partner  {

	//合作商ID
	private int pid;
	
	//合作商编号
	private String pNum;
	
	//合作商名称
	private String pName;
	
	//创建时间
	private String createTime;
	
	//编辑时间
	private String editTime;
	
	//删除时间
	private String removeTime;
	
	//删除状态
	private int removeStatus;
	
	//服务器数量
	private int serviceNum;

	public int getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(int serviceNum) {
		this.serviceNum = serviceNum;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getpNum() {
		return pNum;
	}

	public void setpNum(String pNum) {
		this.pNum = pNum;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

	public String getRemoveTime() {
		return removeTime;
	}

	public void setRemoveTime(String removeTime) {
		this.removeTime = removeTime;
	}

	public int getRemoveStatus() {
		return removeStatus;
	}

	public void setRemoveStatus(int removeStatus) {
		this.removeStatus = removeStatus;
	}

}
