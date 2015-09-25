package com.lodogame.game.server.session;

import org.jboss.netty.channel.Channel;

/**
 * Session ID生成器
 * @author CJ
 *
 */
public interface SessionIdGenerator {
	public String genSid(Channel channle);
}
