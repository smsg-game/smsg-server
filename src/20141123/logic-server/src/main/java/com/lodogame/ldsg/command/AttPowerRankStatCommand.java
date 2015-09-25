package com.lodogame.ldsg.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.bo.PowerRankBO;
import com.lodogame.ldsg.service.RankService;

/**
 * 前30名战斗力统计，临时需求
 * @author chenjian
 *
 */
public class AttPowerRankStatCommand extends BaseCommand {

	private final static Logger log = Logger.getLogger(AttPowerRankStatCommand.class);
	
	@Autowired
	private RankService rankService;
	
	@Override
	void work(){
		List<PowerRankBO> list = rankService.powerRankStat(System.currentTimeMillis(), 30);
		BufferedWriter writer = null;
		try {
			File f = new File("/data/log/power_rank");
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			for(PowerRankBO bo : list){
				String line = bo.getRank() + "," + bo.getPlayerId() + "," + bo.getUsername() + "," + bo.getPower();
				writer.write(line);
				writer.newLine();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally{
			IOUtils.closeQuietly(writer);
		}
	}
	
	public static void main(String[] args) {
		BaseCommand command = BaseCommand.getBean(AttPowerRankStatCommand.class);
		try {
			command.run();
		} finally {
			if (command != null) {
				command.exit();
			}
		}
	}

}
