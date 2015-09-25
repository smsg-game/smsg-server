/**
 * @author : langgui

 * Created : 06/08/2013

 */

package com.lodogame.game.dao.impl.mysql;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.model.UserPkInfo;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserPkInfoDaoMysqlImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private UserPkInfoDaoMysqlImpl userPkInfoDaoMysqlImpl;

	/**
	 * 
	 * @see com.lodogame.game.dao.impl.mysql.UserPkInfoDaoMysqlImpl#getByRank(int)
	 */
	@Test
	public void getByRank() {
		UserPkInfo userPkInfo = userPkInfoDaoMysqlImpl.getByRank(1);
		assertEquals(userPkInfo.getRank(), 1);
	}

	/**
	 * 
	 * @see com.lodogame.game.dao.impl.mysql.UserPkInfoDaoMysqlImpl#backUserPkInfo()
	 */
	@Test
	public void backUserPkInfo() {
		userPkInfoDaoMysqlImpl.backUserPkInfo();
	}

	/**
	 *
	 * @see com.lodogame.game.dao.impl.mysql.UserPkInfoDaoMysqlImpl#add(UserPkInfo)
	 */
	@Test
	public void add() {
		UserPkInfo userPkInfo = new UserPkInfo();
		userPkInfo.setUserId("test3");
		
		userPkInfo.setUpdatePkTime(new Date());
		userPkInfo.setPkTimes(25);
		boolean result = userPkInfoDaoMysqlImpl.add(userPkInfo);
		assertNotNull(result, "result cannot be null");
	}

}
