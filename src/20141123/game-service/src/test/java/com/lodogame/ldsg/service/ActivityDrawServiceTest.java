package com.lodogame.ldsg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.ActivityDrawDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.CommonDropBO;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ActivityDrawServiceTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private ActivityDrawDao activityDrawDao;
	
	@Autowired 
	private EventServcie eventServcie;
	
	@Autowired
	private ActivityDrawService activityDrawService;
	
	@Test
	public void testDraw(){
		
		String json = Json.toJson(this.activityDrawService.draw("5b682917bba04a96a2231bd5d19846b3", 9));
		System.out.println(json);
	}
}
