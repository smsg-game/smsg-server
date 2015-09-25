package com.lodogame.game.server.session;

/**
 * Session清理器，用于给上层应用定义清除Session的内容
 * @author Administrator
 *
 */
public interface SessionCloseHandler {
	public void handler(Session s);
}
