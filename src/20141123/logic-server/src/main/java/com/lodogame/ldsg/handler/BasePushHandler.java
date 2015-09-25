package com.lodogame.ldsg.handler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.render.ViewRender;

public class BasePushHandler {

	private static final Logger logger = Logger.getLogger(BasePushHandler.class);

	@Autowired
	private ViewRender render;

	/**
	 * 推数据
	 * 
	 * @param uid
	 * @param action
	 * @param params
	 */
	public void push(String action, Map<String, Object> params) {

		String uid = (String) params.get("uid");

		Session session = ServerConnectorMgr.getInstance().getServerSession("pushConn");

		if (session == null) {
			logger.error("推送数据出错.服务器连接未准备好");
			return;
		}

		try {
			Message message = new Message();
			message.setAct(action);

			message = this.render.render(message, null, params);

			session.send(message);

		} catch (Throwable t) {
			logger.error("推送数据出错.uid[" + uid + "], action[" + action + "]");
			logger.error(t.getMessage(), t);
		}

	}

}
