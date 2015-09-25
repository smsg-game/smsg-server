package com.lodogame.game.dao.clearcache;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CacheInit implements BeanPostProcessor{
	private static final Logger LOG = Logger.getLogger(CacheInit.class);
 
	/**
	 * 是在bean加载每个bean之前，都会调用该办法。
	 */
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof ClearCacheOnLoginOut) {
			ClearCacheOnLoginOut cache = (ClearCacheOnLoginOut) bean;
			ClearCacheManager.getInstance().addCache(cache);
			LOG.info("加载用户登出后清理缓存类"+cache.getClass().getSimpleName()+";");
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String arg1)
			throws BeansException {
		return bean;
	}
}
