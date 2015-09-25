package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

import flex.messaging.io.ArrayList;

/**
 * 国战入口对象
 * @author Candon
 *
 */
@Compress
public class WarEnterBo {
	
	//屏蔽国家数组
	@SuppressWarnings("unchecked")
	@Mapper(name = "hl")
	private List<Integer> hideList = new ArrayList();
	
	//本次国战用户信息
	@Mapper(name = "uws")
	private UserWarInfoBo currBo;
	
	//上次国战用户信息
	@Mapper(name = "pus")
	private UserWarInfoBo preBo;
	
	//本次国战城池状态
	@SuppressWarnings("unchecked")
	@Mapper(name = "cl")
	private List<CityBo> currCity = new ArrayList();
	
	//上次国战城池状态
	@SuppressWarnings("unchecked")
	@Mapper(name = "pcl")
	private List<CityBo> preCity = new ArrayList();
	
	//国战开始时间
	@Mapper(name = "stm")
	private long startTime;
	
	//国战结束时间
	@Mapper(name = "etm")
	private long endTime;
	
	public List<Integer> getHideList() {
		return hideList;
	}
	public void setHideList(List<Integer> hideList) {
		this.hideList = hideList;
	}
	public UserWarInfoBo getCurrBo() {
		return currBo;
	}
	public void setCurrBo(UserWarInfoBo currBo) {
		this.currBo = currBo;
	}
	public UserWarInfoBo getPreBo() {
		return preBo;
	}
	public void setPreBo(UserWarInfoBo preBo) {
		this.preBo = preBo;
	}
	
	public List<CityBo> getCurrCity() {
		return currCity;
	}
	public List<CityBo> getPreCity() {
		return preCity;
	}
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
	
	
	
	
}
