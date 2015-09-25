package com.lodogame.ldsg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.UserEquip;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class EquipServiceTest extends AbstractTestNGSpringContextTests {

	String userId = "f9d72437a9d4475aa282c614a7c4644e";

	String userHeroId = "58cd296df9084f60863d0db1db469146";

	String userEquipId = "2ae30025464e404c95738afab206db7d";

	int equipType = 1;

	@Autowired
	private EquipService equipService;

	@Test
	public void testUpdateEquipHero() {

		this.equipService.updateEquipHero(userId, userEquipId, userHeroId, equipType, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return true;
			}
		});

	}

	@Test
	public void testAddEquip() {

		for (int i = 0; i < 2; i++) {

			String userEquipId = IDGenerator.getID();

			UserEquip userEquip = new UserEquip();
			userEquip.setEquipId(110001);
			userEquip.setEquipLevel(1);
			userEquip.setUserEquipId(userEquipId);
			userEquip.setUserId(userId);
			userEquip.setCreatedTime(new Date());
			userEquip.setUpdatedTime(new Date());

			// this.equipService.addUserEquip(userEquip);
		}
	}

	@Test
	public void testSellEquip() {

		String userEquipId = IDGenerator.getID();

		UserEquip userEquip = new UserEquip();
		userEquip.setEquipId(110000501);
		userEquip.setEquipLevel(55);
		userEquip.setUserEquipId(userEquipId);
		userEquip.setUserId(userId);
		userEquip.setCreatedTime(new Date());
		userEquip.setUpdatedTime(new Date());

		// this.equipService.addUserEquip(userEquip);

		List<String> userEquipIdList = new ArrayList<String>();
		userEquipIdList.add(userEquipId);

		this.equipService.sell(userId, userEquipIdList, new EventHandle() {

			public boolean handle(Event event) {
				return true;
			}
		});
	}

	@Test
	public void testUpgradeEquip() {

		this.equipService.upgrade(userId, userEquipId, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return true;
			}
		});

	}
}
