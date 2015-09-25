package com.lodogame.game.server;

import java.util.List;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.lodogame.game.server.context.ConfigContext;
import com.lodogame.game.server.context.Context;
import com.lodogame.game.server.context.ContextWrapper;
import com.lodogame.game.server.context.HandlerContext;
import com.lodogame.game.server.exception.HandlerListConfigIsEmptyException;

public class GameServerPipelineFactory implements ChannelPipelineFactory {
	@SuppressWarnings("unused")
	private boolean isInit = false;
	private final ExecutionHandler executionHandler;
	private Context context;

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		initHandler(pipeline);
		return pipeline;
	}

	public GameServerPipelineFactory(Context context, ExecutionHandler executionHandler) {
		this.executionHandler = executionHandler;
		this.context = context;
	}

	/**
	 * 初始化handler列表
	 * 
	 * @param pipeline
	 */
	private void initHandler(ChannelPipeline pipeline) {
		// pipeline.addLast("handler", executionHandler);
		Timer timer = new HashedWheelTimer();
		ConfigContext configContext = context.getConfigContext();
		// pipeline.addLast("timeout", new ReadTimeoutHandler(timer,
		// context.getConfigContext().getTimeout()));
		HandlerContext handlerContext = configContext.getHandlers();
		List<String> handlers = handlerContext.getHandlers();
		if (handlers == null || handlers.isEmpty()) {
			throw new HandlerListConfigIsEmptyException();
		}
		for (String h : handlers) {
			ChannelHandler handler = (ChannelHandler) context.getApplicationContext().getBean(h);
			if (handler instanceof ContextWrapper) {
				((ContextWrapper) handler).setContext(context);
			}
			pipeline.addLast(h, handler);
			//在lengthFieldDecoder后加入多线程处理业务，释放worker
			if(h.equals("lengthFieldDecoder")){
				pipeline.addLast("handler", executionHandler);
			}
		}

		isInit = true;
	}
}
