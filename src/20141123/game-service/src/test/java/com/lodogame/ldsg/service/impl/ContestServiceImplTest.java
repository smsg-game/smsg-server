package com.lodogame.ldsg.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.ContestTeamDao;
import com.lodogame.ldsg.bo.ContestFightResultBO;
import com.lodogame.ldsg.bo.ContestTeamBO;
import com.lodogame.ldsg.constants.ContestConstant;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ContestServiceImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ContestService contestService;

	@Autowired
	private ContestTeamDao contestTeamDao;

	@Autowired
	private UserService userService;

	@Test
	public void registerTest() {
		
		int userNum = 252;
		int count = 0;
//		int id = 2000000; // QA server
		int id = 1000000; // dev server
		while(count < userNum) {
			id = id + 1;
			String lodoId = String.valueOf(id); 
			User user = new User();
			try {
				user = userService.getByPlayerId(lodoId);
			} catch (Exception e) {
				System.out.println("user is null =================== lodoId[" + lodoId + "]");

			}
			if (user.getUserId() != null) {
				System.out.println("lodoid[" + user.getLodoId() + "]");
				contestService.register(user.getUserId());
				count++;
			}
		}
	}

	@Test
	public void enterRegTest() {
		Map<String, Object> enterReg = contestService.enterReg();
		Set<Entry<String, Object>> entrySet = enterReg.entrySet();
		Iterator<Entry<String, Object>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> next = iterator.next();
			System.out.println(next.getKey() + " " + next.getValue());
		}
	}

	@Test
	public void contestFightTest() {
		contestService.contestFight(ContestConstant.ROUND_128);
		System.out.println("===");
	}

	@Test
	public void getRoundResultTest() {
		List<ContestTeamBO> roundResult = contestService.getRoundResult("10939bdc90ec4dfaa6252f00cb0468e8", ContestConstant.ROUND_16);
		System.out.println(roundResult);
	}
	
	@Test
	public void getReportTest() {
		String contestId = "ae063dc2b6e146f6975bcf72128d0840";
		ContestFightResultBO report = contestService.getReport(contestId);
		System.out.println(report);
	}

}
