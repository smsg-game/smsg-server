package com.lodogame.game.server.exception;

/**
 * 处理器没有配置，请在application-config.xml中配置
 * 如：
 * 	<bean id="handlers" class="com.lodogame.game.server.context.HandlerContext">
 * 		<property name="handlers">
 *			<list>
 *				<bean id="commandHandler" class="com.lodogame.game.server.handler.CommandDecoder"></bean>
 *				<bean id="serverHandler" class="com.lodogame.game.server.GiGServerHandler"></bean>
 *			</list>
 *		</property>
 *	</bean>
 * 
 * 并在config中引用handlers
 * @author CJ
 *
 */
public class HandlerListConfigIsEmptyException extends ServerRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HandlerListConfigIsEmptyException() {
		super("Handler列表没有配置");
	}

	public HandlerListConfigIsEmptyException(String message) {
		super(message);
	}

	public HandlerListConfigIsEmptyException(Throwable t) {
		super(t);
	}

}
