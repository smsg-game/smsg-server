package com.lodogame.game.server.constant;

/**
 * 响应代码定义
 * @author CJ
 *
 */
public class CodeConstants {
	/**
	 * 正常成功响应，无多余信息
	 */
	public final static int SUCCESS = 1000;
	/**
	 * 未知原因失败
	 */
	public final static int REQUEST_FAILED = 3000;
	/**
	 * 找不到处理类
	 */
	public final static int NOT_FOUND_ACTION = 4001;
	/**
	 * 用户未登录
	 */
	public final static int IS_NOT_LOGIN = 3002;
	/**
	 * 被踢下线
	 */
	public final static int KICKOUT = 3003;
}
