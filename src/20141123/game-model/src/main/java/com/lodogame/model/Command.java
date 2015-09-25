package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 命令
 * 
 * @author jacky
 * 
 */
public class Command implements Serializable {

	private static final long serialVersionUID = -5723050208777874441L;

	/**
	 * 优先级
	 * 
	 */
	private int priority = 5;

	/**
	 * 命令ID
	 */
	private int commandId;

	/**
	 * 命令
	 */
	private int command;

	/**
	 * 类型 (1 报有人 2 指定用户)
	 */
	private int type;

	/**
	 * 参数
	 */
	private Map<String, String> params;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
