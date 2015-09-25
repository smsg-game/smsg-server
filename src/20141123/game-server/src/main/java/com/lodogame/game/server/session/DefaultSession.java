package com.lodogame.game.server.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.lodogame.game.server.exception.JsonException;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.json.Json;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

/**
 * Session实现类
 * @author CJ
 *
 */
public class DefaultSession implements Session {
	public static Logger LOG = Logger.getLogger(DefaultSession.class);
	private SessionManager mgr;
	private Map<String, Object> attributes;
	private String sid;
	private Channel channel;
	
	private final SerializationContext scontext = new SerializationContext();
	private final Amf3Output amf3out;
	
	public DefaultSession(SessionManager mgr, String sid, Channel channel){
		this.mgr = mgr;
		this.sid = sid;
		this.channel = channel;
		attributes = new HashMap<String, Object>();
		amf3out = new Amf3Output(scontext);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
	}

	@Override
	public void close() {
		channel.disconnect();
	}

	@Override
	public void out(String message) {
		channel.write(message);
	}

	//TODO 支持分布式session需要从缓存获取SID，并向远程socket发送消息
	@Override
	public void send(String sid, Message message) throws IOException {
		Session otherSesssion = DefaultSessionManager.getInstance().getSession(sid);
		this.flush(otherSesssion.getChannel(), message);
	}

	
	//TODO 需要支持分布式session，向不同服务器的session发送消息
	@Override
	public void sendAll(Message message) throws IOException {
		Collection<Session> sessions = mgr.getAllSession();
		for(Session s : sessions){
			if(!s.getSid().equals(sid)){
				Channel ch = s.getChannel();
				if(ch.isConnected()){
					this.flush(ch, message);
				}
			}
		}
	}

	@Override
	public String getSid() {
		return sid;
	}

	@Override
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	private ChannelFuture flush(Channel ch, Message message) throws IOException {
		ChannelBuffer out = ChannelBuffers.dynamicBuffer();

		byte[] bytes = jsonEncode(message);
		out.writeBytes(Constant.intToBytes(bytes.length));
		out.writeBytes(bytes);
		ChannelFuture future = ch.write(out);
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				//fail
				if(future.isDone() && future.getCause() != null){
					LOG.error("请求失败，原因：", future.getCause());
				}
			}
		});
		return future;
	}
	
	private ChannelFuture flush2(Channel ch, Message message) throws Exception {
		ChannelBuffer out = ChannelBuffers.dynamicBuffer();
		byte[] bytes = jsonEncode(message);
		out.writeBytes(Constant.intToBytes(bytes.length));
		out.writeBytes(bytes);
		ChannelFuture future = ch.write(out);
		return future;
	}
	
	/**
	 * amf 格式编码
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private byte[] amf3Encode(Object msg) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		amf3out.setOutputStream(stream);
		amf3out.writeObject(msg);
		amf3out.flush();
		byte bytes[] = stream.toByteArray();
		return bytes;
	}
	
	/**
	 * Json编码
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	private byte[] jsonEncode(Object msg) throws IOException {
		try {
			String json = Json.toJson(msg);
			return json.getBytes(Charset.forName("utf-8"));
		} catch (JsonException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public Channel getChannel() {
		return this.channel;
	}

	@Override
	public ChannelFuture send(Message message) throws IOException {
		LOG.debug("推送数据：" + Json.toJson(message));
		return this.flush(channel, message);
	}
}
