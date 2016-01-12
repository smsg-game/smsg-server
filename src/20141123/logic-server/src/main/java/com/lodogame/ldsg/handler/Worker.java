package com.lodogame.ldsg.handler;

import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.UnSynDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.service.RankService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.User;

public class Worker {

	private static final Logger LOG = Logger.getLogger(Worker.class);

	private boolean started = false;

	private Date lastPushMidnightTime = new Date();

	private Date lastCreatTableTime = new Date();

	private Date lastCreateRankTime = null;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private RankService rankService;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private UnSynDao unSynDao;

	private void work1() {

		try {
			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {

				User user = this.userService.get(userId);
				LOG.debug("体力恢复判断.userId[" + userId + "]");
				this.userService.checkPowerAdd(user);
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage());
		}

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException ie) {
			LOG.debug(ie);
		}
	}

	private void work2() {

		try {

			while (true) {
				final Command command = this.commandDao.get(Priority.HIGH, Priority.NORMAL, Priority.LOWER, Priority.MESSAGE);
				if (command != null) {
					LOG.info("command[" + Json.toJson(command) + "]");
					final CommandHandler commandHandler = CommandHandlerFactory.getInstance().getHandler(command.getCommand());
					Runnable task = new Runnable() {

						@Override
						public void run() {
							commandHandler.handle(command);
						}
					};

					taskExecutor.execute(task);

				} else {
					break;
				}
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage());
			try {
				Thread.sleep(1000 * 5);
			} catch (InterruptedException ie) {
				LOG.debug(ie);
			}
		}

	}

	/**
	 * 晚上12点推送
	 */
	private void work4() {

		try {

			Date now = new Date();
			if (!DateUtils.isSameDay(lastPushMidnightTime, now)) {

				LOG.debug("晚上12点消息推送");

				this.lastPushMidnightTime = now;

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_PUSH_MIDNIGHT);
				command.setType(CommandType.PUSH_ALL);
				commandDao.add(command);
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage());
		}
		try {
			// 睡5分钟
			Thread.sleep(1000 * 10);
		} catch (InterruptedException ie) {
			LOG.debug(ie);
		}

	}

	/**
	 * 5分钟一次的在线数据生成
	 */
	private void work3() {
		try {
			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			StringBuffer userIdStr = new StringBuffer();
			for (String userId : onlineUserIdList) {
				userIdStr.append(userId).append(",");
			}
			unSynLogService.userOnlineLog(new Date(), onlineUserIdList.size(), userIdStr.toString());
		} catch (Throwable t) {
			LOG.error(t.getMessage());
		}
		try {
			// 睡5分钟
			Thread.sleep(1000 * 5 * 60);
		} catch (InterruptedException ie) {
			LOG.debug(ie);
		}
	}

	/**
	 * 排行榜刷新
	 */
	private void work6() {

		try {

			Date now = new Date();
			if (lastCreateRankTime == null || !DateUtils.isSameDay(lastCreateRankTime, now)) {

				LOG.info("刷新排行榜");
				this.rankService.rankStat();
				this.lastCreateRankTime = now;
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage());
		}
		try {
			// 睡5分钟
			Thread.sleep(1000 * 10);
		} catch (InterruptedException ie) {
			LOG.debug(ie);
		}

	}

	public void init() {

		// stated = true;

		if (started) {
			return;
		}

		if (!Config.ins().isGameServer()) {
			return;
		}

		started = true;

		LOG.info("开始线程 ");

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					work1();
				}
			}

		}).start();

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					work2();
				}
			}

		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					work3();
				}
			}

		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					work4();
				}
			}

		}).start();

		// 排行榜
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					work6();
				}
			}

		}).start();

	}

}
