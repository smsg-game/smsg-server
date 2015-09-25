package com.lodogame.game.server.context;

import com.lodogame.game.server.session.Session;

/**
 * handler之间数据传递
 * 
 * @author CJ
 * 
 */
public class AttachmentContext {
	private Session session;
	private boolean isComment;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public boolean isComment() {
		return isComment;
	}

	public void setComment(boolean isComment) {
		this.isComment = isComment;
	}

}
