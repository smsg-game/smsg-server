package com.lodogame.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.model.UserScene;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserSceneDaoMysqlImplTest extends AbstractTestNGSpringContextTests {

	String userId = "5349499dea96497db4a2e203ef064b15";

	String userHeroId = "58cd296df9084f60863d0db1db469146";

	@Autowired
	private UserSceneDao userSceneDaoMysqlImpl;

	@Test
	public void testGetUserSceneList() {

		List<UserScene> userSceneList = this.userSceneDaoMysqlImpl.getUserSceneList(userId);
		for (UserScene userScene : userSceneList) {
			System.out.println(userScene.getUserId());
			System.out.println(userScene.getSceneId());
			System.out.println(userScene.getPassFlag());
		}

	}

}
