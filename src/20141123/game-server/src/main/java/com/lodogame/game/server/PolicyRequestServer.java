package com.lodogame.game.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;

import com.lodogame.game.server.handler.PolicyRequestHandler;

public class PolicyRequestServer {
	private static Logger LOG = Logger.getLogger(PolicyRequestServer.class);

	public void start() {
		LOG.info("启动FLASH安全策略服务");
		// 创建ChannelFactory实例，管理Channel，指定相应的线程池
		ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		// 用于服务相关配置
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		ExecutionHandler executionHandler = new ExecutionHandler(new MemoryAwareThreadPoolExecutor(8, 1048576, 1048576));
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("handler", executionHandler);

		initHandler(pipeline);

		bootstrap.setPipeline(pipeline);
		// 禁用Nagle 算法，适用于交互性强的客户端
		bootstrap.setOption("child.tcpNoDelay", true);
		// 设置keepalive
		bootstrap.setOption("child.keepAlive", false);
		bootstrap.bind(new InetSocketAddress("192.168.1.14", 843));
		LOG.info("FLASH安全策略服务器启动成功，监听IP：192.168.1.14，端口：843");
	}

	/**
	 * 初始化handler列表
	 * 
	 * @param pipeline
	 */
	private void initHandler(ChannelPipeline pipeline) {
		LOG.info("初始化handler");
		pipeline.addLast("policyRequestHandler", new PolicyRequestHandler());
	}
	
	public static void main(String[] args) {
		new PolicyRequestServer().start();
	}
}
