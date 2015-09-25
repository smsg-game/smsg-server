package com.lodogame.game.server.handler.amf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import com.lodogame.game.utils.LogUtils;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;

public class AMF3Decoder2 extends LengthFieldBasedFrameDecoder{
	public static final Logger LOG = Logger.getLogger(AMF3Decoder2.class);

	/**
	 * 
	 * @param maxFrameLength
	 *            包的最大大小
	 * @param lengthFieldOffset
	 *            包头信息，长度的偏移位
	 * @param lengthFieldLength
	 *            包头信息，长度位数
	 */
	public AMF3Decoder2(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	}

	/**
	 * 
	 * @param maxFrameLength
	 */
	public AMF3Decoder2() {
		super(1024 * 1024, 0, 4, 0, 4);
	}

	/**
	*
	*/
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		ChannelBuffer frame = (ChannelBuffer) super.decode(ctx, channel, buffer);
		if (frame == null) {
			return null;
		}
		//
//		int magicNum = frame.readInt();
//		int dataLength = frame.readInt();
//		LOG.info("magic num={},data length={}", magicNum, dataLength);
		// 读AMF3字节流的内容
//		byte[] content = new byte[frame.readableBytes()];
//		frame.readBytes(content);
//		SerializationContext serializationContext = new SerializationContext();
//		Amf3Input amf3Input = new Amf3Input(serializationContext);
//		amf3Input.setInputStream(new ByteArrayInputStream(content));
//		
//		Object message = amf3Input.readObject();
		
		SerializationContext serializationContext = new SerializationContext();
		Amf3Input amf3in = new Amf3Input(serializationContext);
		byte[] b = new byte[frame.readableBytes()];
		frame.readBytes(b);
		InputStream memeryStream = null;
		Object message = null;
		try {
			memeryStream = new ByteArrayInputStream(b);
			amf3in.setInputStream(memeryStream);
			message = amf3in.readObject();
			System.out.println(message);
		} catch (Exception e) {
			LogUtils.error(LOG, e);
		} finally {
			IOUtils.closeQuietly(memeryStream);
		}
		
		return message;
	}
}
