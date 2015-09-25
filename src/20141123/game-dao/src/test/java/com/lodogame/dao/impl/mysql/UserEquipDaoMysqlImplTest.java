package com.lodogame.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.model.UserEquip;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import com.lodogame.game.dao.impl.mysql.UserEquipDaoMysqlImpl;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserEquipDaoMysqlImplTest extends AbstractTestNGSpringContextTests {

	String userId = "5349499dea96497db4a2e203ef064b15";

	String userHeroId = "58cd296df9084f60863d0db1db469146";

	@Autowired
	private UserEquipDao userEquipDaoMysqlImpl;

	@Test
	public void testAddUserEquip() {

		int[] equipIds = { 110000501, 110000502, 110101003, 210000501, 210000502, 210000503, 210000504, 210101005, 310000501, 310000502, 310101003, };

		for (int equipId : equipIds) {
			UserEquip userEquip = new UserEquip();
			userEquip.setEquipId(equipId);
			userEquip.setEquipLevel(55);
			userEquip.setUserEquipId(IDGenerator.getID());
			userEquip.setUserId(userId);
			userEquip.setCreatedTime(new Date());
			userEquip.setUpdatedTime(new Date());
			this.userEquipDaoMysqlImpl.add(userEquip);
		}
	}

	@Test
	public void testGetUserEquip() {

		List<UserEquip> userEquipList = this.userEquipDaoMysqlImpl.getUserEquipList(userId);
		System.out.println("size:" + userEquipList.size());
	}

	/**
	 * 
	 * @see com.lodogame.game.dao.impl.mysql.UserEquipDaoMysqlImpl#updateEquipLevel(String,String,int,int)
	 */
	@Test
	public void testUpdateEquipLevel() {
		boolean success = this.userEquipDaoMysqlImpl.updateEquipLevel("8a632db77b4a4c1f90d9335f9a998f68", "02cd9953408748c6886c4430d29a0084", 5, 22);
		Assert.assertTrue(success);
	}

}
