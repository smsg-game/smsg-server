package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class PkGroupFirstBo {
	//角色名
	@Mapper(name = "un")
	private String username;
	
	//称号
	private String title;
	
	//组ID
	@Mapper(name = "gro")
	private int group;
	
	//等级区间 
	private String range;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	
	
}
