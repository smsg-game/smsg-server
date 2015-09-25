package com.lodogame.game.server.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class BeanContext {

	private static String[] filenames = { "applicationContext.xml"//
			, "applicationContext-action.xml" //
			, "applicationContext-gig-config.xml" //
			, "applicationContext-server-config.xml" //
	};

	private String configLocations = StringUtils.arrayToDelimitedString(filenames, ",");

	protected ApplicationContext context;

	public BeanContext() {
		context = new ClassPathXmlApplicationContext(StringUtils.tokenizeToStringArray(configLocations,
				ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
}
