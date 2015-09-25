package com.lodogame.game.server.context;

/**
 * 包装ServerHandlerContext
 * 所有用户定制handler应该实现此接口
 * 
 * @author CJ
 *
 */
public interface ContextWrapper {
	/**
	 * 设置context
	 * @param handlerContext
	 */
	public void setContext(Context context);
	
	/**
	 * 获取context
	 * @return
	 */
	public Context getContext();
}
