package com.lodogame.ldsg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.bo.UserBossBO;
import com.lodogame.ldsg.service.BossService;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class BossServiceImplTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private BossService bossService;
	
	@Test
	public void getUserBossListTest() {
		String uid = "1085d7bf3cd74d0db3c70d2e7a5052e0";
		List<UserBossBO> userBossList = bossService.getUserBossList(uid);
		System.out.println(userBossList);
	}
}
