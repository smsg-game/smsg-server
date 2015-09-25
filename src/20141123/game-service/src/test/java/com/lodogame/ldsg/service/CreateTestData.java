package com.lodogame.ldsg.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.SystemLevelExpDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.constants.ToolUseType;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })

public class CreateTestData extends AbstractTestNGSpringContextTests{

	@Autowired
	private HeroService heroService;
	
	@Autowired
	private SystemLevelExpDao systemLevelExpDao;

	@Autowired
	private UserHeroDao userHeroDao;

	@Test
	public void addUserHeros() {
		String userId = "87bfeee8803547a1b2be534845896678";
		String userHeroId = IDGenerator.getID();
		int level = 10;
		
		heroService.addUserHero(userId, userHeroId, 206, 0, ToolUseType.ADD_ADMIN_ADD);

		int exp = systemLevelExpDao.getHeroExp(level).getExp();

		userHeroDao.updateExpLevel(userId, userHeroId, exp, level);
	}
}
