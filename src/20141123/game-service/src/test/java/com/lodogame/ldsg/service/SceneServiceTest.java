package com.lodogame.ldsg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class SceneServiceTest extends AbstractTestNGSpringContextTests {

	String userId = "49ff94dbe9ac4b3f953a1870718cb32b";

	int forcesId = 101;

	@Autowired
	private SceneService sceneService;

	@Test
	public void testFight() {

		sceneService.attack(userId, forcesId, new EventHandle() {

			public boolean handle(Event event) {
				return true;
			}
		});

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
