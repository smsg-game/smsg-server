package com.lodogame.ldsg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import com.lodogame.ldsg.service.ForcesService;
import java.util.List;
import com.lodogame.ldsg.bo.BattleHeroBO;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ForcesServiceTest extends AbstractTestNGSpringContextTests {

	String userId = "5349499dea96497db4a2e203ef064b15";

	int forcesId = 107;

	@Autowired
	private ForcesService forcesService;

	@Test
	public void testFight() {

		// this.forcesService.

	}

	/**
	 * 
	 * @see com.lodogame.ldsg.service.ForcesService#getForcesHeroBOList(int)
	 */
	@Test
	public void getForcesHeroBOList() {
		List<BattleHeroBO> list = forcesService.getForcesHeroBOList(forcesId);
		System.out.println(list.size());
	}
}
