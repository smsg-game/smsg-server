package com.lodogame.ldsg.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.helper.CompressHelper;

public class DefaultViewRender implements ViewRender {
	private final static Logger logger = Logger.getLogger(DefaultViewRender.class);
	public Message render(Message message,  Request request, Map<String, Object> params) {
		return this.render(message, request, params, 1000);
	}

	public Message render(Message message, Request request, Map<String, Object> params, int rc) {
		if(request != null){
			message.setReqId(request.getReqId());
			message.setRt(request.getRt());
		}
		message.setRc(rc);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value != null) {
				message.setAttribate(key, CompressHelper.compress(value));
			} else {
				message.setAttribate(key, value);
			}
		}
		logger.debug("logic response：" + Json.toJson(message));
		return message;
	}

	public Response render(Response res,  Request request, Map<String, Object> params) {
		return this.render(res, request, params, 1000);
	}

	public Response render(Response res,  Request request, Map<String, Object> params, int rc) {

		Message message = res.getMessage();
		this.render(message, request, params, rc);
		return res;
	}

}
