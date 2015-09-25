/**
 * @author : langgui

 * Created : 05/15/2013

 */

package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.model.UserMapper;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserMapperDaoMysqlImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private UserMapperDao userMapperDaoMysqlImpl;

	@Test
	public void getByPartnerUserId() {
		UserMapper userMapper = userMapperDaoMysqlImpl.getByPartnerUserId("1002","1", "1");
		AssertJUnit.assertNull(userMapper);
		UserMapper userMapper2 = userMapperDaoMysqlImpl.getByPartnerUserId("1002","", "1");
		AssertJUnit.assertNotNull(userMapper2);
	}

	public UserMapperDao getUserMapperDaoMysqlImpl() {
		return userMapperDaoMysqlImpl;
	}

	public void setUserMapperDaoMysqlImpl(UserMapperDao userMapperDaoMysqlImpl) {
		this.userMapperDaoMysqlImpl = userMapperDaoMysqlImpl;
	}

}
