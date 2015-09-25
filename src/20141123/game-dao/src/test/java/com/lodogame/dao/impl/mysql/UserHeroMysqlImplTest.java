package com.lodogame.dao.impl.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.model.UserHero;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserHeroMysqlImplTest extends AbstractTestNGSpringContextTests {

	String userId = "5349499dea96497db4a2e203ef064b15";

	String userHeroId = "3aef50ec842b4b7ea5a7403b4e70a845";

	@Autowired
	private UserHeroDao userHeroDaoMysqlImpl;

	@Test
	public void testGetUseHeroList() {

		List<UserHero> userHeroList = this.userHeroDaoMysqlImpl.getUserHeroList(userId);
		for (UserHero hero : userHeroList) {
			System.out.println(hero.getUserHeroId());
			System.out.println(hero.getSystemHeroId());
			System.out.println(hero.getUserId());
			System.out.println(hero.getCreatedTime());
		}
	}

	@Test
	public void testAddUserHero() {

		for (int i = 0; i < 24; i++) {
			UserHero userHero = new UserHero();
			userHero.setUserHeroId(IDGenerator.getID());
			userHero.setSystemHeroId(1);
			userHero.setUpdatedTime(new Date());
			userHero.setUserId(userId);
			userHero.setCreatedTime(new Date());
			this.userHeroDaoMysqlImpl.addUserHero(userHero);
		}
	}

	@Test
	public void testGetUserHeroInfo() {
		UserHero userHero = this.userHeroDaoMysqlImpl.get(userId, userHeroId);
		if (userHero != null) {
			System.out.println(userHero.getUserHeroId());
			System.out.println(userHero.getSystemHeroId());
			System.out.println(userHero.getUserId());
			System.out.println(userHero.getCreatedTime());

		}
	}

	@Test
	public void testDelete() {
		List<String> userHeroIdList = new ArrayList<String>();
		userHeroIdList.add(userHeroId);
		userHeroIdList.add("3ef0e129b70c49c2b69fb9299819a6e8");
		this.userHeroDaoMysqlImpl.delete(userId, userHeroIdList);
	}

	@Test
	public void testGetCount() {

		int count = this.userHeroDaoMysqlImpl.getBattleHeroCount(userId);
		System.out.println(count);
	}
}
