package com.lodogame.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.ContestTeamDao;
import com.lodogame.model.ContestPlayerPair;
import com.lodogame.model.ContestTeam;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ContestTeamDaoTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private ContestTeamDao contestTeamDao;
	
	@Test
	public void addTeamTest() {
		ContestTeam team  = new ContestTeam();
		team.setCapacity(4);
		team.setIsFighted(0);
		team.setPlayerNum(1);
		team.setTeamId(12804);
		team.setTeamName("128强比赛");
		
		
	
	}
	
	
}
