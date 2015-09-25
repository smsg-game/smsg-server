package com.lodogame.game.server.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 服务器关闭过滤器  直接关闭连接 不接受任何请求数据
 * @author foxwang
 *
 */
public class CloseServerHandler extends SimpleChannelHandler {
	private boolean isCloseServer = false;
@Override
public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception {
	// TODO Auto-generated method stub
	 	 if(isCloseServer){
	 		 return;
	 	 }else{
	 		super.messageReceived(ctx, e);
	 	 }
}

public boolean isCloseServer() {
	return isCloseServer;
}
public void setCloseServer(boolean isCloseServer) {
	this.isCloseServer = isCloseServer;
}
 
 
}
