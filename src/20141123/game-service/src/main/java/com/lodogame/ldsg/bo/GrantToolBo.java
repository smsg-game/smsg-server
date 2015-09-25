package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.model.User;

/**
 * 发放道具BO
 * @author Candon
 *
 */
public class GrantToolBo {
	private List<User> list;
	private String failPlayId;
	public List<User> getList() {
		return list;
	}
	public void setList(List<User> list) {
		this.list = list;
	}
	public String getFailPlayId() {
		return failPlayId;
	}
	public void setFailPlayId(String failPlayId) {
		this.failPlayId = failPlayId;
	}
	
}
