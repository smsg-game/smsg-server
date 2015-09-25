package com.lodogame.ldsg.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.bo.BattleStartBO;
import com.lodogame.ldsg.bo.DeifyRoomInfoBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.DeifyService;
import com.lodogame.model.DeifyTowerInfo;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class DeifyServiceImplTest extends AbstractTestNGSpringContextTests {

	private static final String userId = "43be66888ff04acca0aaf05a74de3a96";
	
	@Autowired
	private DeifyService deifyService;
	
	@Test
	public void getTowerListTest() {
		Map<String, Object> map = deifyService.getTowerList(userId);
		List<DeifyTowerInfo> list = (List<DeifyTowerInfo>) map.get("twls");
		System.out.println(map);
	}
	
	@Test
	public void getRoomListTest() {
		List<DeifyRoomInfoBO> roomList = deifyService.getRoomList(userId, 1);
		System.out.println(roomList);
	}
	
	@Test
	public void doubleProfitTest() {
		 DeifyRoomInfoBO infoBO = deifyService.doubleProfit(userId, 1);
		System.out.println(infoBO);
	}
	
	@Test
	public void protectTest() {
		DeifyRoomInfoBO infoBO = deifyService.protect(userId, 1);
		System.out.println(infoBO);
	}
	
	@Test
	public void getDeifyStatus() {
		Map<String, Object> map = deifyService.getDeifyStatus(userId);
		System.out.println(map);
	}
	
	@Test
	public void occupyTest() {
		deifyService.occupy(userId, 1, 1, new EventHandle() {
			
			@Override
			public boolean handle(Event event) {
				return true;
			}
			
		});
		System.out.println("-----");
	}
	
	@Test
	public void checkLevelTest() {
		String uid = "bc268cfae4ca46509b79ff849478c2fa";
		deifyService.checkLevel(uid, 1);
	}
	
	@Test
	public void checkStatusTest() {
		String uid = "bc268cfae4ca46509b79ff849478c2fa";
		BattleStartBO checkStatus = deifyService.checkStatus(uid);
		System.out.println(checkStatus);
	}
}
