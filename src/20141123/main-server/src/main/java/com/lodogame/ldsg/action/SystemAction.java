package com.lodogame.ldsg.action;

import java.io.IOException;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.response.Response;

public class SystemAction extends ProxyBaseAction {

	public Response getTime() throws IOException {
		Message msg = new Message();
		msg.setAct("System.getTimeRq");
		msg.setRc(1000);
		msg.setAttribate("st", System.currentTimeMillis());
		response.setMessage(msg);
		return response;
	}

}
