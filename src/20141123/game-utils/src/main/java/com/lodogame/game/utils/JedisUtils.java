package com.lodogame.game.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtils {

	private static Logger LOG = Logger.getLogger(JedisUtils.class);

	private static List<JedisPool> pools = new ArrayList<JedisPool>();

	private static final String DEFAULT_HOST = "redis.ldsg.lodogame.com";

	private static final int DEFAULT_PORT = 6379;

	private static String host;

	private static int port;

	private static int poolCount = 1;

	private static Properties prop;

	static {
		JedisPoolConfig config = null;
		try {
			config = initConfig();
			for (int i = 0; i < poolCount; i++) {
				JedisPool pool = new JedisPool(config, host, port);
				pools.add(pool);
			}
		} catch (IOException e) {
			config = new JedisPoolConfig();
			config.setMaxActive(128);
			JedisPool pool = new JedisPool(config, DEFAULT_HOST, DEFAULT_PORT);
			pools.add(pool);
		}
		// JedisPoolConfig config = new JedisPoolConfig();
		// config.setMaxActive(1024);
	}

	private static JedisPool getPool() {
		return pools.get(RandomUtils.nextInt(poolCount));
	}

	private static JedisPoolConfig initConfig() throws IOException {
		prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("jedis.properties"));
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(Integer.parseInt(prop.getProperty("maxActive", "256")));
		config.setMaxIdle(Integer.parseInt(prop.getProperty("maxIdle", "8")));
		config.setMinIdle(Integer.parseInt(prop.getProperty("minIdle", "2")));
		poolCount = Integer.parseInt(prop.getProperty("poolCount", "1"));
		host = prop.getProperty("host", DEFAULT_HOST);
		port = Integer.parseInt(prop.getProperty("port", DEFAULT_PORT + ""));
		return config;
	}

	/**
	 * 根据key获取一个String
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {

		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			String val = jedis.get(key);
			return val;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			return null;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static String flushAll() {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.flushAll();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			return null;
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 保存value到redis中
	 * 
	 * @param key
	 * @param value
	 */
	public static void setString(String key, String value) {

		if (value == null) {
			return;
		}

		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 保存一个map到redis中
	 * 
	 * @param key
	 * @param map
	 */
	public static void setObject(String key, Map<String, String> map) {

		if (map == null || map.isEmpty()) {
			return;
		}

		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.hmset(key, map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取一个map的元素数量
	 * 
	 * @param key
	 * @return
	 */
	public static long hlen(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.hlen(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}

		return 0;
	}

	/**
	 * 获取一个对象中的多个field的值
	 * 
	 * @param key
	 *            指定一个对象
	 * @param fields
	 *            指定map中的多个key
	 * @return
	 */
	public static List<String> getFieldsFromObject(String key, String... fields) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			return null;
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 删除一个key
	 * 
	 * @param key
	 */
	public static void delete(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.del(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 判断一个key存不存在
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.exists(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}

		return false;
	}

	/**
	 * 将value保存到一个对象的字段中
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void setFieldToObject(String key, String field, String value) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.hset(key, field, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static void setFieldsToObject(String key, Map<String, String> hash) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.hmset(key, hash);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 从一个对象中删除某个字段
	 * 
	 * @param key
	 * @param field
	 */
	public static void delFieldFromObject(String key, String field) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.hdel(key, field);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取一个对象的某个字段的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static String getFieldFromObject(String key, String field) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.hget(key, field);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			return null;
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取整个Object
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, String> getMap(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.hgetAll(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			return null;
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 内存中的值+x
	 * 
	 * @param key
	 * @param incrVal
	 * @return
	 */
	public static long incrBy(String key, long incrVal) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.incrBy(key, incrVal);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
		return 0;
	}

	/**
	 * 向一个队列插入一条消息
	 * 
	 * @param key
	 * @param val
	 */
	public static void pushMsg(String key, String val) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.rpush(key, val);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 从一个队列消费一条消息
	 * 
	 * @param key
	 * @return
	 */
	public static String popMsg(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.lpop(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			return null;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static String blockPopMsg(int timeOut, String... keys) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			List<String> ret = jedis.blpop(timeOut, keys);
			if (ret != null) {
				return ret.get(1);
			}
			return null;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取一个
	 * 
	 * @param key
	 * @return
	 */
	public static List<String> getMapValues(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.hvals(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取一个
	 * 
	 * @param key
	 * @return
	 */
	public static void sadd(String key, String member) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.sadd(key, member);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取一个
	 * 
	 * @param key
	 * @return
	 */
	public static void srem(String key, String member) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.srem(key, member);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取一个
	 * 
	 * @param key
	 * @return
	 */
	public static boolean sIsMember(String key, String member) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.sismember(key, member);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 获取一个
	 * 
	 * @param key
	 * @return
	 */
	public static Set<String> smembers(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.smembers(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 求一个集合的元素个数
	 * 
	 * @param key
	 * @return
	 */
	public static long llen(String key) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.llen(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}

	}

	public static void zadd(String key, double score, String member) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.zadd(key, score, member);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static Set<String> zmember(String key, double start, double end) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.zrangeByScore(key, start, end);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static Double zscore(String key, String member) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			return jedis.zscore(key, member);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			pool.returnBrokenResource(jedis);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static void main(String[] args) {
		test3();
	}

	private static void test3() {

		JedisUtils.pushMsg("test", "msg1");
		JedisUtils.pushMsg("test", "msg2");
		JedisUtils.pushMsg("test", "msg3");
		JedisUtils.pushMsg("test", "msg4");
		JedisUtils.pushMsg("test", "msg5");
		JedisUtils.pushMsg("test", "msg6");

		System.out.println(JedisUtils.llen("test"));

		System.out.println(JedisUtils.blockPopMsg(0, "test"));
		System.out.println(JedisUtils.blockPopMsg(0, "test"));
		System.out.println(JedisUtils.blockPopMsg(0, "test"));
		System.out.println(JedisUtils.blockPopMsg(0, "test"));
		System.out.println(JedisUtils.blockPopMsg(0, "test"));
		System.out.println(JedisUtils.blockPopMsg(0, "test"));

		System.out.println(JedisUtils.llen("test99"));

	}

	private static void test1() {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			jedis.set("foo", "fooval");
		} finally {
			pool.returnResource(jedis);
		}
	}

	private static void test2() {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		try {
			String value = jedis.get("foo");
			System.out.println(value);
		} finally {
			pool.returnResource(jedis);
		}
	}

}
