package com.lodogame.ldsg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.TavernDropToolDao;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class TavernServiceTest extends AbstractTestNGSpringContextTests {

	String userId = "98261fe3550247ce8356ffaf7a7c5996";

	int type = 1;

	@Autowired
	private TavernService tavernService;

	@Autowired
	private TavernDropToolDao tavernDropToolDao;

	@Test
	public void testDraw() {

		tavernService.draw(userId, type, 9, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				return false;
			}
		});

	}

	/**
	 * 
	 * @see com.lodogame.ldsg.service.TavernService#draw(String,int,int,EventHandle)
	 */
	@Test
	public void draw() {

	}

}
