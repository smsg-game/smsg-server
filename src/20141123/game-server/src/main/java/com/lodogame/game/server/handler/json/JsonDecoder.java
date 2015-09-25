package com.lodogame.game.server.handler.json;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.utils.json.Json;

public class JsonDecoder extends FrameDecoder {
	private static final Logger LOG = Logger.getLogger(JsonDecoder.class); 
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		LOG.debug("json decode start");
		if (buffer.readableBytes() == 0) {
			return null;
		}
		
		byte[] b = new byte[buffer.readableBytes()]; 
		buffer.readBytes(b);
		String data = new String(b, Charset.forName("utf8"));
//		LOG.debug("json data:" + data);
		Object obj = Json.toObject(data, Message.class);
		return obj;
	}
	
	public static String getEncoding(byte b[]) {  
        String encode = "GB2312";  
        try {  
            if (new String(b, Charset.forName(encode)).equals(new String(b, encode))) {  
                String s = encode;  
                return s;  
            }  
        } catch (Exception exception) {  
        }  
        encode = "ISO-8859-1";  
        try {  
        	if (new String(b, Charset.forName(encode)).equals(new String(b, encode))) {  
                String s1 = encode;  
                return s1;  
            }  
        } catch (Exception exception1) {  
        }  
        encode = "UTF-8";  
        try {  
        	if (new String(b, Charset.forName(encode)).equals(new String(b, encode))) {  
                String s2 = encode;  
                return s2;  
            }  
        } catch (Exception exception2) {  
        }  
        encode = "GBK";  
        try {  
        	if (new String(b, Charset.forName(encode)).equals(new String(b, encode))) {  
                String s3 = encode;  
                return s3;  
            }  
        } catch (Exception exception3) {  
        }  
        return "";  
    }  
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String st = "你好";
		byte[] b = st.getBytes();
		System.out.println(getEncoding(b));
		System.out.println(new String(new String(b).getBytes(getEncoding(b)), Charset.forName(getEncoding(b))));
//		System.out.println(new String(b, getEncoding(new String(b))));
	}

}
