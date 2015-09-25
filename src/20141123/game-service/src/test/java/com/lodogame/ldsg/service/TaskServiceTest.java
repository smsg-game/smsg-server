package com.lodogame.ldsg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class TaskServiceTest extends AbstractTestNGSpringContextTests {

	String userId = "5349499dea96497db4a2e203ef064b15";

	@Autowired
	private TaskService taskSrevice;

	@Test
	public void testGetList() {

		this.taskSrevice.getUserTaskList(userId, 0);
		this.taskSrevice.getUserTaskList(userId, 1);
		this.taskSrevice.getUserTaskList(userId, 2);
		this.taskSrevice.getUserTaskList(userId, 100);
	}

	@Test
	public void testReceive() {

		this.taskSrevice.receive(userId, 20, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}

		});

	}
}
