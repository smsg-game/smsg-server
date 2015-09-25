package com.lodogame.game.server.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * 过滤Flash策备请求
 * 
 * @author CJ
 * 
 */
@Sharable
public class PolicyRequestFilterHandler extends OneToOneDecoder {

	private final static int POLICY_REQUEST_LENGTH = 22;
	private final static String POLICY_REQUEST_STRING = "<policy-file-request/>";

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if(!(msg instanceof ChannelBuffer)){
			return msg;
		}
		ChannelBuffer buf = (ChannelBuffer) msg;
		if (buf.readableBytes() < POLICY_REQUEST_LENGTH) {
			return msg;
		}

		ChannelBuffer tmp = buf.duplicate();
		byte[] dst = new byte[POLICY_REQUEST_LENGTH];
		tmp.readBytes(dst);
		if (POLICY_REQUEST_STRING.equals(new String(dst))) {
			buf.setIndex(POLICY_REQUEST_LENGTH + 1, POLICY_REQUEST_LENGTH + 1);
		}
		return msg;
	}

}
