package com.lodogame.game.server;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.Session;

public class PushMessage {
	private static final Logger LOG = Logger.getLogger(PushMessage.class);

	public static void sendAllSessionMessage(final Session session, final Message message) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Iterator<Session> sessions = DefaultSessionManager.getInstance().getAllSessionKey();
				while (sessions.hasNext()) {
					Session s =	sessions.next();
					if (session != null && session.getSid() != s.getSid()) {
						try {
							session.send(s.getSid(), message);
						} catch (IOException e) {
							LOG.info("发送消息失败", e);
						}
					}
				}
			}
		});

		t.run();
	}
}
