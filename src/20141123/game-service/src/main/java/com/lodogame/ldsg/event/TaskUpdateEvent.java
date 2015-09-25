package com.lodogame.ldsg.event;

public class TaskUpdateEvent extends BaseEvent {

	public TaskUpdateEvent(String userId, int systemTaskId, int flag) {
		this.userId = userId;
		this.data.put("systemTaskId", systemTaskId);
		this.data.put("flag", flag);
	}
}
