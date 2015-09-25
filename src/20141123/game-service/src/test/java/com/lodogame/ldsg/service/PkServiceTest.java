package com.lodogame.ldsg.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.lodogame.ldsg.bo.AwardBO;
import com.lodogame.ldsg.bo.PkInfoBO;
import com.lodogame.ldsg.bo.PkPlayerBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class PkServiceTest extends AbstractTestNGSpringContextTests {
	String userId = "711615315fbd495a84bebb3f6f0d01da";
	int awardId = 1;
	int num = 2;
	
	@Autowired
	private PkService pkService;
	
	@Test
	public void exchangeTest() {
		pkService.exchange(userId, awardId);
		
	}
	
	@Test
	public void batchExchangeTest() {
		pkService.batchExchange(userId, awardId, num);
	}
	
	@Test
	public void enterPkTest(){
		
		pkService.enterPk("2c50ee50fe04461eaed9f12e28806aa5",new EventHandle() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean handle(Event event) {
				
				return true;
			}
		},0);
		
	}
	@Test
	public void getGrankTen(){
		System.out.println(pkService.getGrankTen(10).size());
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	@Test
	public void getGrankFirst(){
		System.out.println(pkService.getGrankFirst().size());
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}
}
