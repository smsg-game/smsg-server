package com.lodogame.ldsg.handler.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UnSynDao;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.handler.CommandHandler;
import com.lodogame.ldsg.handler.CommandHandlerFactory;
import com.lodogame.ldsg.service.TowerService;
import com.lodogame.model.Command;

public class SysCommandHandlerImpl implements CommandHandler {

	private static final Logger logger = Logger.getLogger(SysCommandHandlerImpl.class);

	@Autowired
	private TowerService towerService;

	@Autowired
	private UnSynDao unSynDao;

	@Override
	public void handle(final Command command) {

		logger.debug("handle sys command");

		int comm = command.getCommand();
		int type = command.getType();
		Map<String, String> params = command.getParams();

		switch (comm) {

		case CommandType.COMMAND_RELOAD_SYSTEM_DATA:
			handleReloadData(type, params);
			break;
		case CommandType.COMMAND_SAVE_LOG:
			handleSaveLog(type, params);
			break;
		case CommandType.COMMAND_SET_LOOGER_LEVEL:
			handleChangeLoggerLevel(type, params);
			break;
		default:
			break;
		}

	}

	private void handleReloadData(int type, Map<String, String> params) {

		String className = params.get("className");
		ReloadManager.getInstance().reload(className);
		if (StringUtils.equalsIgnoreCase(className, "ALL") || StringUtils.equalsIgnoreCase(className, "towerService")) {
			towerService.loadSystemTowerData();
		}
	}

	private void handleChangeLoggerLevel(int type, Map<String, String> params) {

		Logger log = null;
		String level = params.get("level");

		String className = params.get("className");
		String name = params.get("name");

		if (className != null) {
			try {
				Class<?> cls = Class.forName(className);
				log = Logger.getLogger(cls);
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage(), e);
			}
		} else if (name != null) {
			log = Logger.getLogger(name);
		} else {
			log = Logger.getRootLogger();
		}

		log.setLevel(Level.toLevel(level, Level.INFO));

	}

	/**
	 * 异步执行sql
	 * 
	 * @param type
	 * @param params
	 */
	private void handleSaveLog(int type, Map<String, String> params) {

		String sql = params.get("sql");
		if (sql != null && !sql.equals("")) {
			unSynDao.executSql(sql);
		}
	}

	@Override
	public void init() {
		CommandHandlerFactory.getInstance().register(2, this);
	}

}
