package com.lodogame.game.server.context;

import org.springframework.context.ApplicationContext;

import com.lodogame.game.server.session.SessionManager;

/**
 * 处理器上下文容器，保存服务器相关上下文资源，以及配置相关信息
 * @author CJ
 *
 */
public class ServerContext implements Context{
	private ApplicationContext applicationContext;
	private SessionManager sessionManager;
	private ConfigContext configContext;
	
	public ServerContext(){
		
	}
	
	@Override
	public ConfigContext getConfigContext() {
		return configContext;
	}
	
	@Override
	public void setConfigContext(ConfigContext configContext) {
		this.configContext = configContext;
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	@Override
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	
}
