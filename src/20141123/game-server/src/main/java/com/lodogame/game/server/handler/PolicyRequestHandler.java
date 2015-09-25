package com.lodogame.game.server.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 过滤Flash策备请求
 * 
 * @author CJ
 * 
 */
@Sharable
public class PolicyRequestHandler extends SimpleChannelHandler {

//	private final static int POLICY_REQUEST_LENGTH = 22;
//	private final static String POLICY_REQUEST_STRING = "<policy-file-request/>";

	/**
	 * 客户端连接后的初始化工作，如：注册session
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel ch = e.getChannel();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		byte[] sbf = "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>\0".getBytes();
		buf.writeBytes(sbf);
		ch.write(buf);
		super.channelConnected(ctx, e);
	}
	
//
//	@Override
//	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
//		super.writeComplete(ctx, e);
//		e.getChannel().close();
//		System.out.println("关闭连接");
//	}
}
