package com.lodogame.game.server.handler.amf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.utils.LogUtils;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;

/**
 * flash对象解码 从ChannelBuffer中获取流，然后使用Amf3Input进行对象转换
 * 
 * @author CJ
 * 
 */
public class AMF3Decoder extends FrameDecoder {
	private static Logger LOG = Logger.getLogger(AMF3Decoder.class);
	private Amf3Input amf3in;
	private final SerializationContext context = new SerializationContext();

	public AMF3Decoder() {
		amf3in = new Amf3Input(context);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() == 0) {
			return null;
		}
		byte[] b = new byte[buffer.readableBytes()];
		buffer.readBytes(b);
		InputStream memeryStream = null;
		Object message = null;
		try {
			memeryStream = new ByteArrayInputStream(b);
			amf3in.setInputStream(memeryStream);
			message = amf3in.readObject();
		} catch (Exception e) {
			LogUtils.error(LOG, e);
		} finally {
			IOUtils.closeQuietly(memeryStream);
		}
		if (message instanceof Message) {
			return message;
		}
		return null;
	}

}
