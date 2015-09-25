package com.lodogame.game.server.handler;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.lodogame.game.server.context.AttachmentContext;
import com.lodogame.game.server.context.ServerContext;
import com.lodogame.game.server.session.Session;

public class SessionHandler extends SimpleChannelHandler {
	private ServerContext handlerContext;

	public SessionHandler(ServerContext handlerContext) {
		this.handlerContext = handlerContext;
	}
	/**
	 * 客户端连接后的初始化工作，如：注册session
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel ch = e.getChannel();
		Session session = this.handlerContext.getSessionManager().registerSession(ch);
		AttachmentContext attachmentContext = new AttachmentContext();
		attachmentContext.setSession(session);
		ctx.setAttachment(attachmentContext);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.channelDisconnected(ctx, e);
	}

}
