package com.lodogame.game.dao.impl.mysql;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserHeroRegenerateDao;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class RegenerateDaoMysqlImplTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private UserHeroRegenerateDao regenerateDao;
	
	@Test
	public void getPostHeroId() {
		regenerateDao.getPostHeroId(733);
	}
	
	@Test
	public void getPillNum() {
		System.out.println(regenerateDao.getPillNum(733));
	}
	
	@Test
	public void getContractNum() {
		System.out.println(regenerateDao.getContractNum(715));
	}
	
	@Test
	public void getHeroNum() {
		System.out.println(regenerateDao.getHeroNum(739));
	}
}
