package com.lodogame.game.server.context;

import org.springframework.context.ApplicationContext;

import com.lodogame.game.server.session.SessionManager;

public interface Context {

	public ConfigContext getConfigContext();

	public void setConfigContext(ConfigContext configContext);

	public ApplicationContext getApplicationContext();

	public void setApplicationContext(ApplicationContext applicationContext);

	public SessionManager getSessionManager();

	public void setSessionManager(SessionManager sessionManager);
	
}
