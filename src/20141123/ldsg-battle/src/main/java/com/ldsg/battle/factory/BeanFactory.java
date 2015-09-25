package com.ldsg.battle.factory;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactory {

	private static Logger logger = Logger.getLogger(BeanFactory.class);

	private static BeanFactory instance;

	private ApplicationContext applicationContext;

	private BeanFactory() {

		logger.info("初始化游戏服务器上下文相关数据");
		String[] locations = { "applicationContext.xml" };
		applicationContext = new ClassPathXmlApplicationContext(locations);
	}

	public static BeanFactory getInstance() {

		if (instance == null) {
			instance = new BeanFactory();
		}
		return instance;
	}

	/**
	 * 获取一个dao
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 */
	public <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}

}
