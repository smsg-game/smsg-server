package com.lodogame.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserHeroDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.model.User;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserDaoMysqlImplTest extends AbstractTestNGSpringContextTests {

	String userId = "074cce71b5704d45b54cb978d6b1ca73";

	@Autowired
	private UserDao userDaoMysqlImpl;

	@Autowired
	private UserHeroDao userHeroDaoMysqlImpl;

	@Test
	public void testAdd() {
		User user = new User();
		user.setUsername("乐斗三国");
		user.setUserId(IDGenerator.getID());
		user.setRegTime(new Date());
		user.setOrderAddTime(new Date());
		user.setUpdatedTime(new Date());
		this.userDaoMysqlImpl.add(user);
	}

	@SuppressWarnings("unused")
	@Test
	public void testGet() {
		User user = this.userDaoMysqlImpl.get(userId);
		userHeroDaoMysqlImpl.getUserHeroList(userId);
	}

	/**
	 * 
	 * @see com.lodogame.game.dao.impl.mysql.UserDaoMysqlImpl#addPower(String,int,Date)
	 */
	@Test
	public void addPower() {
		userDaoMysqlImpl.addPower(userId, 68, 100, null);
	}
}
