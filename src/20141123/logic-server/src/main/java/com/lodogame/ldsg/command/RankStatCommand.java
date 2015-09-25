package com.lodogame.ldsg.command;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.service.RankService;

/**
 * 计算排名信息
 * 
 * @author chenjian
 * 
 */
public class RankStatCommand extends BaseCommand {

	@Autowired
	private RankService rankService;

	@Override
	void work() {
		rankService.rankStat();
	}

	public static void main(String[] args) {

		BaseCommand command = BaseCommand.getBean(RankStatCommand.class);
		try {
			command.run();
		} finally {
			if (command != null) {
				command.exit();
			}
		}
	}
}
