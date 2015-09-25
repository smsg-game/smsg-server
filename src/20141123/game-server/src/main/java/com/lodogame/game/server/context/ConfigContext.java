package com.lodogame.game.server.context;

/**
 * 服务器配置上下文
 * 
 * @author CJ
 * 
 */
public class ConfigContext {
	/**
	 *  监控IP
	 */
	private String ip = "127.0.0.1";

	/**
	 *  监控端口
	 */
	private int port = 8888;
	
	/**
	 *  是否支持分布式session
	 */
	private boolean supportDistributedSession = false;

	/**
	 *  数据编码
	 */
	private String encoding = "utf-8";

	/**
	 *  支持flash，主要用于flash连接成功后返回cross-domain-policy
	 */
	private boolean supportFlash = true;

	/**
	 *  禁用Nagle 算法，适用于交互性强的客户端
	 */
	private boolean tcpNoDelay = true;

	/**
	 * 是否keepalive
	 */
	private boolean keepalive = true;

	/**
	 *  处理器列表
	 */
	private HandlerContext handlers;

	/**
	 * 过滤器
	 */
	private FilterContext filters;
	
	/**
	 * 服务器类型，如logic, combat, arena...
	 */
	private String serverType;
	
	/**
	 * 请求处理线程池
	 */
	private int corePoolSize = 32;
	
	/**
	 * 超时时间，单位是秒(s)
	 */
	private int timeout = 10;
	
	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	/**
	 * 服务连接器配置
	 */
	private String connectorPath;

	public String getConnectorPath() {
		return connectorPath;
	}

	public void setConnectorPath(String connectorPath) {
		this.connectorPath = connectorPath;
	}

	public FilterContext getFilters() {
		return filters;
	}

	public void setFilters(FilterContext filters) {
		this.filters = filters;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public boolean isKeepalive() {
		return keepalive;
	}

	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}

	public HandlerContext getHandlers() {
		return handlers;
	}

	public void setHandlers(HandlerContext handlers) {
		this.handlers = handlers;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSupportDistributedSession() {
		return supportDistributedSession;
	}

	public void setSupportDistributedSession(boolean supportDistributedSession) {
		this.supportDistributedSession = supportDistributedSession;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isSupportFlash() {
		return supportFlash;
	}

	public void setSupportFlash(boolean supportFlash) {
		this.supportFlash = supportFlash;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
