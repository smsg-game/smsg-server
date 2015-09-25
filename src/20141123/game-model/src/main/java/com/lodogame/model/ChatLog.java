package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天日志
 * 
 * @author zyz
 * 
 */
public class ChatLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2751923026627723215L;

	
	private String userId;
	
	private int channel;
	
	private String toUserName;
	
	private String content;
	
	private Date createdTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


}
