package com.lodogame.game.server.context;

import java.util.List;

/**
 * 处理器列表，从配置文件按顺序加载
 * @author CJ
 *
 */
public class HandlerContext {
	private List<String> handlers;

	public List<String> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<String> handlers) {
		this.handlers = handlers;
	}

}
