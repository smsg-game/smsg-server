package com.ldsg.battle;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ldsg.battle.factory.BeanFactory;
import com.ldsg.battle.util.Convert;
import com.ldsg.battle.util.ProcessUtils;

/**
 * app
 * 
 */
public class App {

	public static void main(String[] args) throws IOException {

		ProcessUtils.outputPid("/data/run/ldsg-battle.pid");
		BeanFactory.getInstance();

		Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("server.properties"));

		String servers = prop.getProperty("servers");

		String[] datas = servers.split(",");
		for (String data : datas) {

			int port = 6379;

			String[] ss = data.split(":");
			String host = ss[0];
			if (ss.length == 2) {
				port = Convert.toInt32(ss[1]);
			}

			Worker worker = BeanFactory.getInstance().getBean(WorkerImpl.class);

			worker.setPool(getJedisPool(host, port), port);
			worker.setName("server.host[" + host + "], port[" + port + "]");

			worker.start();
		}

	}

	private static JedisPool getJedisPool(String host, int port) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(256);
		config.setMaxIdle(8);
		config.setMinIdle(2);
		return new JedisPool(config, host, port);

	}
}
