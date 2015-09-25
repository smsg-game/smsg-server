package com.ldsg.battle;

import redis.clients.jedis.JedisPool;

public interface Worker {

	public void run();

	public void setPool(JedisPool pool, int port);

	public void setName(String name);

	public void start();
}
