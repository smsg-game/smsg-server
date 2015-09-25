package com.lodogame.game.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.action.RequestAction;
import com.lodogame.game.server.constant.CodeConstants;
import com.lodogame.game.server.context.ConfigContext;
import com.lodogame.game.server.context.Context;
import com.lodogame.game.server.context.ContextWrapper;
import com.lodogame.game.server.context.FilterContext;
import com.lodogame.game.server.exception.GSRuntimeExcepiton;
import com.lodogame.game.server.exception.HandlerNotFoundException;
import com.lodogame.game.server.exception.JsonException;
import com.lodogame.game.server.exception.RequestFilterException;
import com.lodogame.game.server.exception.handler.ExceptionHandler;
import com.lodogame.game.server.filter.RequestFilter;
import com.lodogame.game.server.request.DefaultRequest;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.DefaultResponse;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.ServerConnectorSessionMgr;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.json.Json;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class RequestHandler extends SimpleChannelHandler implements ContextWrapper {
	private static Logger LOG = Logger.getLogger(RequestHandler.class);

	private final SerializationContext scontext = new SerializationContext();
	private final Amf3Output amf3out;
	private Context context;

	public RequestHandler() {
		amf3out = new Amf3Output(scontext);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		LOG.debug("channelDisconnected: " + e.getChannel().getId());
		// 删除客户端的连接
		String sid = DefaultSessionManager.getInstance().getSid(e.getChannel());
		DefaultSessionManager.getInstance().closeSession(sid);
		// 删除服务端连接
		sid = ServerConnectorSessionMgr.getInstance().getSid(e.getChannel());
		ServerConnectorSessionMgr.getInstance().closeSession(sid);
		ServerConnectorMgr.getInstance().removeConnections(sid);
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// LOG.error("exceptionCaught: " + e.getChannel().getId());
		// LOG.error(e.getCause().getMessage(), e.getCause());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {

		Channel ch = event.getChannel();

		Object obj = event.getMessage();

		// LOG.debug("messageReceived:" + Json.toJson(obj));

		ConfigContext config = context.getConfigContext();
		Request request = createRequest(obj, config, ch);
		// 获取action
		String handlerName = request.getRequestHandlerName();
		String methodName = request.getMethodName();
		RequestAction requestAction = null;
		Response response = createResponse(request);
		if (StringUtils.isBlank(handlerName)) {
			LOG.info("处理器参数未设置，无需处理以及回调，request:" + request);
			return;
		} else if ("error".equals(handlerName)) {
			LOG.info("错误信息，只做输出，request:" + request);
			return;
		} else {
			if (context.getApplicationContext().containsBean(handlerName)) {
				LOG.debug("create request Action:" + handlerName);
				requestAction = (RequestAction) context.getApplicationContext().getBean(handlerName);
				LOG.debug("create request Action Success:" + handlerName);
			} else {
				Exception e = new HandlerNotFoundException(handlerName);
				LOG.error(e.getMessage(), e);
				if (!request.getMethodName().endsWith("Rq")) {
					Message msg = (Message) response.getMessage();
					msg.setAct("error");
					msg.setRc(CodeConstants.REQUEST_FAILED);
					response.setMessage(msg);
					flush(ch, response);
				}
				return;
			}
		}

		try {
			filter(context.getConfigContext().getFilters(), request, response);
			requestAction.init(request, response);
			Method m = null;
			try {
				m = requestAction.getClass().getMethod(methodName);
			} catch (NoSuchMethodException nme) {

			}
			// response = requestAction.action();
			try {
				if (m != null) {
					response = (Response) m.invoke(requestAction, null);
				} else {
					response = requestAction.handle();
				}
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				if (t instanceof GSRuntimeExcepiton) {
					throw (GSRuntimeExcepiton) t;
				}
				if (context.getApplicationContext().containsBean("exceptionHandler")) {
					ExceptionHandler exceptionHandler = (ExceptionHandler) context.getApplicationContext().getBean("exceptionHandler");
					exceptionHandler.handler(e.getTargetException(), request, response);
				}
			}

		} catch (RequestFilterException e) {
			LOG.info(e);
		} catch (GSRuntimeExcepiton e) {
			LOG.error(e.getMessage(), e);
			Message msg = (Message) response.getMessage();
			msg.setRc(e.getCode());
			response.setMessage(msg);
		} catch (Exception e) {
			// 未知错误不错任何处理，只输出错误
			LOG.error(e.getMessage(), e);
			// 请求方法以Rq结尾，则不做返回处理
			if (!request.getMethodName().endsWith("Rq")) {
				Message msg = (Message) response.getMessage();
				msg.setAct("error");
				msg.setRc(CodeConstants.REQUEST_FAILED);
				response.setMessage(msg);
			} else {
				response = null;
			}
		}

		// 获取response，并返回响应
		if (response != null) {
			LOG.debug("开始返回数据： " + Json.toJson(response.getMessage()));
			flush(ch, response);
		}
	}

	private void flush(final Channel ch, final Response response) throws Exception {
		ChannelBuffer out = ChannelBuffers.dynamicBuffer();
		Message msg = response.getMessage();
		byte[] bytes = jsonEncode(msg);
		out.writeBytes(Constant.intToBytes(bytes.length));
		out.writeBytes(bytes);
		ChannelFuture future = ch.write(out);
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// fail
				if (future.isDone() && future.getCause() != null) {
					LOG.error("请求失败，原因：", future.getCause());
				}
			}
		});
	}

	private void flush2(Channel ch, Response response) throws Exception {
		ChannelBuffer out = ChannelBuffers.dynamicBuffer();
		Message msg = response.getMessage();
		byte[] bytes = jsonEncode(msg);
		out.writeBytes(Constant.intToBytes(bytes.length));
		out.writeBytes(bytes);
		ChannelFuture future = ch.write(out);
	}

	/**
	 * 创建response
	 * 
	 * @param request
	 * @return
	 */
	private Response createResponse(Request request) {
		Response response = new DefaultResponse();
		Message message = new Message();
		if (!request.getMethodName().endsWith("Rq") && !request.getMethodName().startsWith("push")) {
			message.setAct(request.getRequestHandlerName() + "." + request.getMethodName() + "Rq");
		} else {
			message.setAct(request.getRequestHandlerName() + "." + request.getMethodName());
		}
		response.setMessage(message);
		return response;
	}

	/**
	 * 创建request
	 * 
	 * @param session
	 * @param obj
	 * @param encoding
	 * @return
	 */
	private Request createRequest(Object obj, ConfigContext config, Channel channel) {
		Message requestMessage = (Message) obj;
		return new DefaultRequest(requestMessage, config, channel);
	}

	/**
	 * 执行过滤器
	 * 
	 * @param filters
	 * @throws RequestFilterException
	 */
	private void filter(FilterContext filters, Request request, Response response) throws RequestFilterException {
		List<RequestFilter> list = filters.getFilters();
		if (list == null || list.isEmpty()) {
			return;
		}

		for (RequestFilter filter : list) {
			filter.filter(request, response);
		}
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

	private byte[] jsonEncode(Object msg) throws IOException {
		try {
			String json = Json.toJson(msg);
			return json.getBytes(Charset.forName("utf-8"));
		} catch (JsonException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Response response = new DefaultResponse();
		Message message = new Message();
		message.setAct("connect");
		message.setRc(0);
		message.setAttribate("rt", "ok");
		response.setMessage(message);
		flush(e.getChannel(), response);
		super.channelConnected(ctx, e);
	}
}
