package com.lodogame.ldsg.action.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.action.UserAction;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserActionTest extends AbstractTestNGSpringContextTests {

	@Autowired
	public UserAction User;

	@Test
	public void testLogin() {

		System.out.println("------------");
		this.User.create();

	}

}
