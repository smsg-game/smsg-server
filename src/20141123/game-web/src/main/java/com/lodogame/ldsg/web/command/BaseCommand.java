package com.lodogame.ldsg.web.command;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

abstract public class BaseCommand {

	public static BaseCommand getBean(Class<?> cls) {

		String[] locations = { "applicationContext.xml" };

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(locations);

		return (BaseCommand) applicationContext.getBean(cls);

	}

	public void run() {

		try {
			this.work();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void exit() {
		System.exit(0);
	}

	abstract void work();

}
