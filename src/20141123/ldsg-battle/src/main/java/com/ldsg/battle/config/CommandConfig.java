package com.ldsg.battle.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.helper.BattleHeroHelper;

public class CommandConfig {

	private static Logger logger = Logger.getLogger(BattleHeroHelper.class);

	/**
	 * 系统默认兵符ID
	 */
	public final static int DEFAULT_COMMAND_TYPE_ID = 65;

	/**
	 * 兵符-技能映射
	 */
	private static Map<Integer, List<Integer>> COMMAND_SKILL_MAP = null;

	/**
	 * 兵符-类型映射
	 */
	private static Map<Integer, Integer> COMMAND_TYPE_MAP = null;

	/**
	 * 根据兵符ID获取兵符的技能ID列表
	 * 
	 * @param commandID
	 * @return
	 */
	public static List<Integer> getCommandSkillIDList(int commandID) {
		Init();

		if (!COMMAND_SKILL_MAP.containsKey(commandID)) {
			throw new BattleRuntimeException("该系统兵符不存在.commandID[" + commandID + "]");
		}

		return COMMAND_SKILL_MAP.get(commandID);
	}

	/**
	 * 根据兵符ID获取兵符的类型
	 * 
	 * @param commandID
	 * @return
	 */
	public static int getCommandType(int commandID) {

		Init();

		if (!COMMAND_TYPE_MAP.containsKey(commandID)) {
			throw new BattleRuntimeException("该系统兵符不存在.commandID[" + commandID + "]");
		}

		return COMMAND_TYPE_MAP.get(commandID);
	}

	// <summary>
	// 重新加载数据
	// </summary>
	public static void Reload() {
		synchronized (CommandConfig.class) {
			if (COMMAND_SKILL_MAP != null) {
				COMMAND_SKILL_MAP.clear();
				COMMAND_SKILL_MAP = null;
			}
		}
	}

	private static void Init() {
		if (COMMAND_SKILL_MAP != null && COMMAND_TYPE_MAP != null) {
			return;
		}

		synchronized (CommandConfig.class) {
			logger.debug("开始初始化兵符技能配置信息");
			try {
				InitFromDb();
			} catch (Exception ae) {
				logger.error("初始化兵符技能配置数据失败:" + ae.getMessage());
				throw new BattleRuntimeException("初始化兵符技能配置数据失败.", ae);
			}

		}
	}

	/**
	 * 从数据库初始化兵符的技能配置信息
	 */
	private static void InitFromDb() {
		if (COMMAND_SKILL_MAP != null && COMMAND_TYPE_MAP != null) {
			return;
		}

		// SoldierTokenDao soldierTokenDao =
		// DaoFactory.getInstance().getDao(SoldierTokenDao.class);

		COMMAND_SKILL_MAP = new HashMap<Integer, List<Integer>>();
		COMMAND_TYPE_MAP = new HashMap<Integer, Integer>();
	}
}
