package com.lodogame.game.server.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.Session;
import com.lodogame.game.utils.Constant;

/**
 * 心跳包检测
 * @author CJ
 *
 */
public class HeartbeatHandler extends OneToOneDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer)msg;
		if (buffer.readableBytes() == 0) {
			String sid = DefaultSessionManager.getInstance().getSid(channel);
			Session session = DefaultSessionManager.getInstance().getSession(sid);
			if(session != null){
				session.setAttribute("heartbeat", System.currentTimeMillis());
			}
			ChannelBuffer out = ChannelBuffers.dynamicBuffer();
			out.writeBytes(Constant.intToBytes(0));
			channel.write(out);
			return null;
		}
		
		return buffer;
	}
}
