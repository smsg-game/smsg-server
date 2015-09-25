package com.lodogame.game.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class AMF3Utils {
	/**
	 * amf 格式编码
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public static byte[] amf3Encode(Object msg) throws IOException {
		SerializationContext context = new SerializationContext();
		Amf3Output amf3out = new Amf3Output(context);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		amf3out.setOutputStream(stream);
		amf3out.writeObject(msg);
		amf3out.flush();
		byte bytes[] = stream.toByteArray();
		return bytes;
	}
}
