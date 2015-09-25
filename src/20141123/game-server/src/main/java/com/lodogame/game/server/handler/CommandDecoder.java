package com.lodogame.game.server.handler;

import org.jboss.netty.buffer.ChannelBuffer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.lodogame.game.utils.Constant;

/**
 * 解析指令
 * 根据数据包的前4个字节，获得真正数据的长度，再从流中获取真实数据
 * @author CJ
 *
 */
public class CommandDecoder extends FrameDecoder {
	private final static int MESSAGE_LEN = 4;
	private int len = MESSAGE_LEN;
	private boolean isLen = true;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() < len) {
			System.out.println(len);
			return null;
		}
		ChannelBuffer bf = buffer.readBytes(len);
		if (isLen) {
			ChannelBuffer dbf = bf.duplicate();
			byte[] dst = new byte[len];
			dbf.readBytes(dst);
			
			len = Constant.bytesToInt(dst);
			for(int i = 0 ; i < dst.length ; i++){
				System.out.println(dst[i]);
			}
			isLen = false;
			return null;
		} else {
			len = MESSAGE_LEN;
			isLen = true;
		}
		return bf;
	}

}
