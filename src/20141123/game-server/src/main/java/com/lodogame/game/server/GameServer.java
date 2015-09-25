package com.lodogame.game.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.action.ServerActionConstants;
import com.lodogame.game.server.context.ClientConfigContext;
import com.lodogame.game.server.context.ClientContext;
import com.lodogame.game.server.context.ConfigContext;
import com.lodogame.game.server.context.Context;
import com.lodogame.game.server.context.ServerContext;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.ServerConnectorSessionMgr;
import com.lodogame.game.server.session.Session;

public class GameServer {
	private static Logger LOG = Logger.getLogger(GameServer.class);
	private Context serverContext;
	private ApplicationContext applicationContext;
	private ConfigContext configContext;
	private List<ClientConfigContext> connectorConfigs;
	private String configPath;

	public int getPort() {

		if (configContext != null) {
			return configContext.getPort();
		}
		return 0;
	}

	public GameServer(String configPath) {
		this.configPath = configPath;
	}

	public void start() {
		initContext();
		initNioSocketServer();
		initServerConnector();
	}

	/**
	 * 初始化上下文相关数据
	 */
	private void initContext() {
		LOG.info("初始化游戏服务器上下文相关数据");
		String[] locations = { "applicationContext.xml" };
		if (StringUtils.isBlank(configPath)) {
			applicationContext = new ClassPathXmlApplicationContext(locations);
		} else {
			applicationContext = new FileSystemXmlApplicationContext(configPath);
		}
		serverContext = new ServerContext();
		serverContext.setApplicationContext(applicationContext);
		serverContext.setSessionManager(DefaultSessionManager.getInstance());
		configContext = (ConfigContext) applicationContext.getBean("config", ConfigContext.class);
		serverContext.setConfigContext(configContext);
		initConnectorConfigs();
	}

	private void initConnectorConfigs() {
		String[] connectorNames = applicationContext.getBeanNamesForType(ClientConfigContext.class);
		if (connectorNames != null) {
			connectorConfigs = new ArrayList<ClientConfigContext>();
			for (int i = 0; i < connectorNames.length; i++) {
				connectorConfigs.add((ClientConfigContext) applicationContext.getBean(connectorNames[i], ConfigContext.class));
			}
		} else {
			LOG.info("没有配置服务连接");
		}
	}

	/**
	 * 初始化NIO服务器
	 */
	private void initNioSocketServer() {
		LOG.info("初始化NIO服务");
		// 创建ChannelFactory实例，管理Channel，指定相应的线程池
		ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		// 用于服务相关配置
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		ExecutionHandler executionHandler = new ExecutionHandler(new MemoryAwareThreadPoolExecutor(configContext.getCorePoolSize(), 0, 0));

		bootstrap.setPipelineFactory(new GameServerPipelineFactory(serverContext, executionHandler));

		// 禁用Nagle 算法，适用于交互性强的客户端
		bootstrap.setOption("child.tcpNoDelay", true);
		// 设置keepalive
		bootstrap.setOption("child.keepAlive", configContext.isKeepalive());
		bootstrap.bind(new InetSocketAddress(configContext.getIp(), configContext.getPort()));
		LOG.info("游戏服务器启动成功，监听IP：" + configContext.getIp() + "，端口：" + configContext.getPort());
	}

	/**
	 * 初始化服务连接器
	 */
	private void initServerConnector() {
		LOG.info("初始化服务连接器");
		if (connectorConfigs == null || connectorConfigs.size() == 0) {
			LOG.info("没有服务连接配置");
			return;
		}

		for (int i = 0; i < connectorConfigs.size(); i++) {
			for (int j = 0; j < connectorConfigs.get(i).getNums(); j++) {
				ClientConfigContext config = connectorConfigs.get(i);
				ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
				ClientContext clientContext = new ClientContext();
				clientContext.setApplicationContext(applicationContext);
				clientContext.setSessionManager(DefaultSessionManager.getInstance());
				clientContext.setConfigContext(config);
				ExecutionHandler executionHandler = new ExecutionHandler(new MemoryAwareThreadPoolExecutor(8, 0, 0));
				bootstrap.setPipelineFactory(new GameServerPipelineFactory(clientContext, executionHandler));
				bootstrap.setOption("tcpNoDelay", config.isTcpNoDelay());
				bootstrap.setOption("keepAlive", config.isKeepalive());
				ChannelFuture future = bootstrap.connect(new InetSocketAddress(config.getIp(), config.getPort()));

				Channel channel = future.awaitUninterruptibly().getChannel();
				if (!future.isSuccess()) {
					future.getCause().printStackTrace();
					bootstrap.releaseExternalResources();
					LOG.info("连接外部服务器失败，IP：" + configContext.getIp() + "，端口：" + configContext.getPort());
					return;
				} else {
					serverLogin(channel, config);
				}
			}
		}
	}

	/**
	 * 服务器登陆注册其他服务器
	 * 
	 * @param channel
	 */
	private void serverLogin(Channel channel, ClientConfigContext config) {
		Session session = ServerConnectorSessionMgr.getInstance().registerSession(channel);
		ServerConnectorMgr.getInstance().putSessionId(config.getServerType(), config.getConnectorId(), session.getSid());
		Message message = new Message();
		message.setAct(ServerActionConstants.SERVER_CONNECT_ACTION);
		message.setAttribate("serverType", config.getServerType());
		message.setAttribate("connectorId", config.getConnectorId());
		try {
			session.send(message);
		} catch (IOException e) {
			LOG.info("连接外部服务器失败，IP：" + config.getIp() + "，端口：" + config.getPort());
			LOG.error(e.getMessage(), e);
		}
	}
}
