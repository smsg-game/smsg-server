package com.lodogame.ldsg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.service.MallService;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class MallServiceImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private MallService mallService;

	@Test
	public void getMallListTest() {
		String userId = "b5d3b0daae9d43b3bce7dffe7a758233";
		mallService.getMallList(userId, 1);

	}
}
