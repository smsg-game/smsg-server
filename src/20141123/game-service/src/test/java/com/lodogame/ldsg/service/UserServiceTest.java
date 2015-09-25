package com.lodogame.ldsg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserTaskDao;
import com.lodogame.game.dao.impl.mysql.UserDaoMysqlImpl;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.constants.TaskStatus;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.UserLevelUpEvent;
import com.lodogame.model.User;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserTask;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class UserServiceTest extends AbstractTestNGSpringContextTests {

	private int systemHeroId = 1;

	@Autowired
	private UserService userSrevice;

	@Autowired
	private EquipService equipService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserDaoMysqlImpl userDaoMysqlImpl;
	
	@Autowired
	private UserTaskDao userTaskDao;
	
	@Autowired
	private EventServcie eventServcie;

	@Test
	public void testGet() {

		UserBO userBO = userSrevice.getUserBO("3c5b884e0609487b82919431b932b6e3");

		if (userBO != null) {
			System.out.println(userBO.getUsername());
		}
	}

	@Test
	public void testCreate() {
		
		Map<String, String> m = new HashMap<String, String>();
		
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < 30; i++) {
			String id = IDGenerator.getID();
			String tk = "tk" + String.valueOf(i);
			m.put(id, tk);
			
			list.add(id + ", " + tk);
		}

		System.out.println("printList");
		for (String str : list) {
			System.out.println(str);
		}
		
//		IDGenerator.getID()
		
//		StringBuilder sb = new StringBuilder();
//		for(int i = 0; i < 2000; i++) {
//			String uid = IDGenerator.getID();
//			String tk = "tk1";
//			
//			int diff = 8 - (i+"").length();
//			for (int j = 0; j < diff; j++) {
//				tk += "0";
//			}
//			tk += i;
//			
//			m.put(uid, tk);
//			sb.append("m.put(\"").append(uid).append("\", \"").append(tk).append("\");\n");
//		}
//		
//		System.out.println(m);
//		try {
//			FileUtils.writeStringToFile(new File("ab.ab"), sb.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		for (Map.Entry<String, String> entry : m.entrySet()) {

			String userId = entry.getKey();

			UserMapper userMapper = this.userMapperDao.get(userId);
			if (userMapper != null) {
				this.userMapperDao.delete(userId);
			} else {
				userMapper = new UserMapper();
			}
			userMapper.setUserId(userId);
			userMapper.setCreatedTime(new Date());
			userMapper.setUpdatedTime(new Date());
			userMapper.setPartnerId("1001");
			userMapper.setPartnerUserId(userId);
			userMapper.setServerId("d1");
			try {
				this.userMapperDao.save(userMapper);
			} catch (Exception e) {

			}

			 User user = this.userDao.get(userId);
			 if (user == null) {
			
				 String username = "用户[" + userId.substring(0, 6) + "]";
				
				 this.userSrevice.create(userId, systemHeroId, username, new
					 EventHandle() {
					
					 public boolean handle(Event event) {
						 return true;
						 }
					 });
			 }

		}
		/*
		 * int[] equipIds = { 110000501, 110000502, 110101003, 210000501,
		 * 210000502, 210000503, 210000504, 210101005, 310000501, 310000502,
		 * 310101003, };
		 * 
		 * for (int equipId : equipIds) { this.equipService.addUserEquip(userId,
		 * IDGenerator.getID(), equipId); }
		 * 
		 * for (int i = 1; i < 20; i++) { this.heroService.addUserHero(userId,
		 * IDGenerator.getID(), i, 0); }
		 */

	}

	/**
	 * 
	 * @see com.lodogame.ldsg.service.UserService#checkPowerAdd(User)
	 */
	@Test
	public void checkPowerAdd() {

		User user = this.userSrevice.get("000000000000000000000000000172");
		this.userSrevice.checkPowerAdd(user);
	}

	/**
	 * 
	 * @see com.lodogame.ldsg.service.UserService#updateVipLevel(String)
	 */
	@Test
	public void updateVipLevel() {
		this.userSrevice.updateVipLevel("6088e467c47e435a99a2361381eaee7e");
	}
	
	@Test
	public void addUserGoldTest() {
		
		String userId = "78449f8a35dc4f779dc2cd895ae158f3";
		int amount = 10;
		
		this.userSrevice.addGold(userId, amount, ToolUseType.ADD_ADMIN_ADD, 101);
	}
	
	@Test
	public void addExpTest() {
		this.userSrevice.addExp("4a9a318be9aa45219d142af0864b612d", 10, ToolUseType.ADD_ADMIN_ADD);
	}
	
	@Test
	public void getId() {
		System.out.println(IDGenerator.getID());
	}
	
	@Test
	public void test() {
		List<String> list = userDaoMysqlImpl.getAllUserIds();
		List<UserTask> taskList = new ArrayList<UserTask>();
		
		for (String id : list) {
			UserTask userTask = new UserTask();
			userTask.setCreatedTime(new Date());
			userTask.setFinishTimes(0);
			userTask.setNeedFinishTimes(1);
			userTask.setStatus(TaskStatus.TASK_STATUS_NEW);
			userTask.setSystemTaskId(5001);
			userTask.setTaskType(1);
			userTask.setUpdatedTime(new Date());
			userTask.setUserId(id);
			taskList.add(userTask);
		}
		
		userTaskDao.add(taskList);
	}
	
	@Test
	public void invitedRegisterTest() throws InterruptedException {
		this.userSrevice.addExp("1e427651b89446b6868021f9a0492056", 40000, ToolUseType.ADD_ADMIN_ADD);
		while(true){
			Thread.sleep(100);
		}

	}
	
	
}
