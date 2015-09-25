package com.lodogame.ldsg.command;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserPkInfoDao;

public class DailyUpdateCommand extends BaseCommand {

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	@Override
	void work() {

		userPkInfoDao.backUserPkInfo();

	}

	public static void main(String[] args) {

		BaseCommand command = BaseCommand.getBean(DailyUpdateCommand.class);
		try {
			command.run();
		} finally {
			if (command != null) {
				command.exit();
			}
		}
	}
}
