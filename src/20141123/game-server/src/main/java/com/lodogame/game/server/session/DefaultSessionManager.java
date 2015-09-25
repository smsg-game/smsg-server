package com.lodogame.game.server.session;


public class DefaultSessionManager extends AbstructSessionManager {
	
	private static SessionManager mgr;

	// TODO 可以根据配置决定使用哪个ID生成器
	private DefaultSessionManager() {
		sidGenerator = new DefaultSessionIdGenerator();
	}

	public static SessionManager getInstance() {
		synchronized (DefaultSessionManager.class) {
			if (mgr == null) {
				mgr = new DefaultSessionManager();
			}
		}
		return mgr;
	}
}
