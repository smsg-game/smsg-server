package com.lodogame.game.server.session;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.jboss.netty.channel.Channel;

import com.lodogame.game.server.exception.DuplicateSessionIdException;

public class AbstructSessionManager implements SessionManager {

	protected Hashtable<String, Session> sessions = new Hashtable<String, Session>();
	protected SessionIdGenerator sidGenerator;
	protected SessionCloseHandler sessionCloseHandler;
	
	// TODO 在此添加分布式session操作，注册session时，向数据库/分布式缓存注册SID
		@Override
		public Session registerSession(Channel channel) {
			String sid = sidGenerator.genSid(channel);
			if (sessions.get(sid) != null) {
				throw new DuplicateSessionIdException(sid);
			}
			
			Session session = new DefaultSession(this, sid, channel);
			session.setAttribute("heartbeatTime", System.currentTimeMillis());
			sessions.put(session.getSid(), session);
			return session;
		}

		@Override
		public String getSid(Channel channel) {
			return sidGenerator.genSid(channel);
		}
		
		@Override
		public void closeSession(String sid) {
			Session s = sessions.get(sid);
			if(sessionCloseHandler != null){
				sessionCloseHandler.handler(s);
			}
			sessions.remove(sid);
			if(s != null){
				s.close();
			}
		}

		@Override
		public void closeSession(Session session) {
			closeSession(session.getSid());
		}

		@Override
		public Collection<Session> getAllSession() {
			return sessions.values();
		}
		
		@Override
		public Iterator<Session> getAllSessionKey(){
			return sessions.values().iterator();
		}

		@Override
		public Session getSession(String sid) {
			if(sid == null){
				return null;
			}
			return sessions.get(sid);
		}

		@Override
		public boolean hasSession(String sid) {
			return sessions.containsKey(sid);
		}

		@Override
		public Session registerSession(String sid, Session session) {
			return sessions.put(sid, session);
		}

		@Override
		public Session removeSession(String sid) {
			return sessions.remove(sid);
		}
		
		@Override
		public void setSessionCloseHandler(SessionCloseHandler handler){
			this.sessionCloseHandler = handler;
		}
}
