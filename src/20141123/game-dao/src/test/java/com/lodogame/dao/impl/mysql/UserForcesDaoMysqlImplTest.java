package com.lodogame.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.model.UserForces;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import com.lodogame.game.dao.impl.mysql.UserForcesDaoMysqlImpl;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserForcesDaoMysqlImplTest extends AbstractTestNGSpringContextTests {

	String userId = "5349499dea96497db4a2e203ef064b15";

	String userHeroId = "58cd296df9084f60863d0db1db469146";

	int sceneId = 1;

	@Autowired
	private UserForcesDao userForcesDaoMysqlImpl;

	@Test
	public void testGetUserForcesList() {

		List<UserForces> userForcesList = this.userForcesDaoMysqlImpl.getUserForcesList(userId, sceneId);
		for (UserForces userForces : userForcesList) {
			System.out.println(userForces.getUserId());
			System.out.println(userForces.getSceneId());
			System.out.println(userForces.getStatus());
		}

	}

	/**
	 * 
	 * @see com.lodogame.game.dao.impl.mysql.UserForcesDaoMysqlImpl#getUserCurrentForces(String,int)
	 */
	@Test
	public void getUserCurrentForces() {
		userForcesDaoMysqlImpl.getUserCurrentForces(userId, 1);
	}

}
