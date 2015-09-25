package com.lodogame.game.server.session;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import com.lodogame.game.utils.EncryptUtil;

public class DefaultSessionIdGenerator implements SessionIdGenerator {
	private static final Logger LOG = Logger.getLogger(DefaultSessionIdGenerator.class);
	@Override
	public String genSid(Channel channel) {
		InetSocketAddress address = (InetSocketAddress)channel.getRemoteAddress();
		address.getHostName();
//		LOG.debug(address.getHostName());
		return EncryptUtil.getMD5(channel.getId().toString() + address.toString()).toLowerCase();
	}

}
