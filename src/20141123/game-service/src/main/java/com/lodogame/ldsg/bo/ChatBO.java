package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ChatBO {

	@Mapper(name = "channel")
	private int channel;
	
	/**
	 * 发言人名字
	 */
	@Mapper(name = "un")
	private String username;

	/**
	 * 发言人等级
	 */
	@Mapper(name = "lv")
	private int level;

	/**
	 * 发言人VIP等级
	 */
	@Mapper(name = "vl")
	private int vipLevel;

	/**
	 * 发言内容
	 */
	@Mapper(name = "ct")
	private String content;

	/**
	 * 发言Id
	 */
	@Mapper(name = "uid")
	private String userId;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}
