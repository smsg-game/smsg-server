package com.lodogame.game.connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.ServerConnectorSessionMgr;
import com.lodogame.game.server.session.Session;

/**
 * 服务连接器管理，用于管理其他与其他服务器的连接
 * 通过serverType获取其他服务器的session，同一个serverType可有多个session，
 * 管理器将会自动轮训使用同一个serverType的不同session
 * @author CJ
 *
 */
public class ServerConnectorMgr {
	
	private static long loopCnt = 0;
	
	private final Logger LOG = Logger.getLogger(ServerConnectorMgr.class);
	
	private Map<String, Map<String, List<String>>> connectors;
	
	private static ServerConnectorMgr ins;
	
	private Set<String> nullSids;

	private ServerConnectorMgr() {
		connectors = new HashMap<String, Map<String, List<String>>>();
		nullSids = new HashSet<String>();
	}

	public static ServerConnectorMgr getInstance() {
		synchronized (ServerConnectorMgr.class) {
			if (ins == null) {
				ins = new ServerConnectorMgr();
			}
		}

		return ins;
	}
	
	/**
	 * 注册服务器连接，并从普通客户端连接缓存中删除，用于被动连接时注册server
	 * @param serverType
	 * @param sessionId
	 */
	public void registerServer(String serverType, String connectorId, String sessionId){
		Map<String, List<String>>connector = connectors.get(serverType);
		if(connector == null){
			connector = new HashMap<String, List<String>>();
		}
		
		List<String> connections = connector.get(connectorId);
		if(connections == null){
			connections = new ArrayList<String>();
		}
		
		connections.add(sessionId);
		
		connector.put(connectorId, connections);
		connectors.put(serverType, connector);
		
		ServerConnectorSessionMgr.getInstance().registerSession(sessionId, DefaultSessionManager.getInstance().removeSession(sessionId));
	}
	
	/**
	 * 注册服务器连接，用于主动连接时注册server
	 * @param serverType
	 * @param sessionId
	 */
	public void putSessionId(String serverType, String connectorId, String sessionId){
		Map<String, List<String>>connector = connectors.get(serverType);
		if(connector == null){
			connector = new HashMap<String, List<String>>();
		}
		
		List<String> connections = connector.get(connectorId);
		if(connections == null){
			connections = new ArrayList<String>();
		}
		
		connections.add(sessionId);
		
		connector.put(connectorId, connections);
		connectors.put(serverType, connector);
	}
	
	public void removeConnections(String sid){
		Collection<Map<String, List<String>>> connectorMaps = connectors.values();
		Iterator<Map<String, List<String>>> it = connectorMaps.iterator();
		while(it.hasNext()){
			Map<String, List<String>> connector = it.next();
			Collection<List<String>> connectionsList = connector.values();
			Iterator<List<String>> connsIt = connectionsList.iterator();
			while(connsIt.hasNext()){
				List<String> conns = connsIt.next();
				conns.remove(sid);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Session getServerSession(String serverType){
		Session session = null;
		if(connectors.containsKey(serverType)){
			Map<String, List<String>> connector = connectors.get(serverType);
			Collection<List<String>> cl = connector.values();
			Object[] cls = new Object[cl.size()];
			cl.toArray(cls);
			List<String> conns = (List<String>)cls[RandomUtils.nextInt(cl.size())];
			if(conns != null && conns.size() > 0){
				int count = 0;
				while(true){
					if(count > 10){
						LOG.error("获取服务器连接失败，serverType:" + serverType);
						break;
					}
					long idx = loopCnt++ % conns.size();
					if(loopCnt > 10000000){
						loopCnt = 0;
					}
					if(idx < 0){
						idx = 0;
					}
					String sid = conns.get((int)idx);
					session = ServerConnectorSessionMgr.getInstance().getSession(sid);
					if(session != null && session.getChannel().isConnected()){
						return session;
					}else{
						nullSids.add(sid);
						LOG.info("新增空sid:" + sid);
						LOG.info("空SID数量：" + nullSids.size());
					}
					count++;
				}
			}
		}
		return session;
	}
	
	/**
	 * 返回服务器连接结构
	 * {
	 * 	serverType1: [
	 * 			connectorid1, connectorid2......
	 *     ],
	 *  serverType2:[......]
	 * }
	 * @return
	 */
	public Map<String, List<String>> getServerStruts(){
		Map<String, List<String>> servers = new HashMap<String, List<String>>();
		Set<String> serverTypes = connectors.keySet();
		Iterator<String> it = serverTypes.iterator();
		while(it.hasNext()){
			String serverType = it.next();
			Map<String, List<String>>connector = connectors.get(serverType);
			Set<String> connectorKeys = connector.keySet();
			Iterator<String> cit = connectorKeys.iterator();
			List<String> connectorids = new ArrayList<String>();
			while(cit.hasNext()){
				String connectorId = cit.next();
				connectorids.add(connectorId);
			}
			servers.put(serverType, connectorids);
		}
		return servers;
	}
	
	public void closeConnector(String serverType, String connectorId){
		Map<String, List<String>> connector = connectors.get(serverType);
		if(connector != null){
			List<String> conns = connector.get(connectorId);
			if(conns != null){
				for(int i = 0; i < conns.size(); i++){
					Session session = ServerConnectorSessionMgr.getInstance().getSession(conns.get(i));
					session.close();
				}
			}
		}
	}
}
