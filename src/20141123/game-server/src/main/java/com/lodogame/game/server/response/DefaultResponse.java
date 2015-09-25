package com.lodogame.game.server.response;

import com.lodogame.game.server.request.Message;

public class DefaultResponse implements Response {

	private Message message;

	// TODO 需要支持写flash对象
	@Override
	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public Message getMessage() {
		return message;
	}

}
