package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class DeifyRoomInfoBO {
	
	@Mapper(name = "rid")
	private int roomId;
	
	@Mapper(name = "name")
	private String name;
	
	@Mapper(name = "uid")
	private String userId;
	
	/**
	 * 用户等级
	 */
	@Mapper(name = "level")
	private int level;
	
	/**
	 * 用户vip
	 */
	@Mapper(name = "vip")
	private int vip;
	
	/**
	 * 玩家上阵武将系统id列表
	 */
	@Mapper(name = "sidl")
	private List<Integer> systemHeroIdList;
	
	/**
	 * 修炼开始时间
	 */
	@Mapper(name = "dstm")
	private long deifyStartTime;
	
	/**
	 * 修炼结束时间
	 */
	@Mapper(name = "dtm")
	private long deifyEndTime;
	
	/**
	 * 修炼保护结束时间
	 */
	@Mapper(name = "ptm")
	private long protectEndTime;
	
	/**
	 * 双倍收益结束时间，毫秒值
	 */
	@Mapper(name = "dptm")
	private long doubleProfitEndTime;
	
	/**
	 * 1表示是当前登录的玩家，2表示不是当前登录玩家
	 */
	@Mapper(name = "ilu")
	private int roomStatus;

	/**
	 * 1表示在修炼保护中，2表示不再保护中
	 */
	@Mapper(name = "isp")
	private int protectStatus;

	/**
	 * 1表示开启双倍，2表示没有开启
	 */
	@Mapper(name = "isd")
	private int doubleProfitStatus;
	
	

	public int getProtectStatus() {
		return protectStatus;
	}

	public void setProtectStatus(int protectStatus) {
		this.protectStatus = protectStatus;
	}

	public int getDoubleProfitStatus() {
		return doubleProfitStatus;
	}

	public void setDoubleProfitStatus(int doubleProfitStatus) {
		this.doubleProfitStatus = doubleProfitStatus;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public List<Integer> getSystemHeroIdList() {
		return systemHeroIdList;
	}

	public void setSystemHeroIdList(List<Integer> systemHeroIdList) {
		this.systemHeroIdList = systemHeroIdList;
	}

	public long getDeifyEndTime() {
		return deifyEndTime;
	}

	public void setDeifyEndTime(long deifyEndTime) {
		this.deifyEndTime = deifyEndTime;
	}

	public long getProtectEndTime() {
		return protectEndTime;
	}

	public void setProtectEndTime(long protectEndTime) {
		this.protectEndTime = protectEndTime;
	}

	public long getDoubleProfitEndTime() {
		return doubleProfitEndTime;
	}

	public void setDoubleProfitEndTime(long doubleProfitEndTime) {
		this.doubleProfitEndTime = doubleProfitEndTime;
	}
	
	/**
	 * 1表示是当前登录的玩家，2表示不是当前登录玩家, 3表示是空修炼室
	 */
	public int getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(int roomStatus) {
		this.roomStatus = roomStatus;
	}

	public long getDeifyStartTime() {
		return deifyStartTime;
	}

	public void setDeifyStartTime(long deifyStartTime) {
		this.deifyStartTime = deifyStartTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
