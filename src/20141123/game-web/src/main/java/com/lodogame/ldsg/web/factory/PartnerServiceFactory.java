package com.lodogame.ldsg.web.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.lodogame.ldsg.config.PartnerConfig;
import com.lodogame.ldsg.partner.service.PartnerService;

public class PartnerServiceFactory implements BeanFactoryAware{
	
	private BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public  PartnerService getBean(String beanName) {
        return beanFactory.getBean(PartnerConfig.ins().getPartnerName(beanName), PartnerService.class);  
   }  
}
