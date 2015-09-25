package com.lodogame.game.server.session;


public class ServerConnectorSessionMgr extends AbstructSessionManager {

	private static SessionManager mgr;

	// TODO 可以根据配置决定使用哪个ID生成器
	private ServerConnectorSessionMgr() {
		sidGenerator = new DefaultSessionIdGenerator();
	}

	public static SessionManager getInstance() {
		synchronized (ServerConnectorSessionMgr.class) {
			if (mgr == null) {
				mgr = new ServerConnectorSessionMgr();
			}
		}
		return mgr;
	}

}
