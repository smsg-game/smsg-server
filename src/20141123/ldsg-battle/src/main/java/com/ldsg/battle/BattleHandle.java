package com.ldsg.battle;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ldsg.battle.bo.BattleInfo;
import com.ldsg.battle.engine.Battle;
import com.ldsg.battle.engine.base.DefaultBattle;
import com.ldsg.battle.helper.BattleHeroHelper;

/**
 * 战斗处理器
 * 
 * @author jacky
 * 
 */
public class BattleHandle {

	private static Logger logger = Logger.getLogger(WorkerImpl.class);

	public Map<String, Object> handle(BattleInfo attackInfo, BattleInfo defenseInfo, int type) {

		if (logger.isDebugEnabled()) {

			logger.debug("*********************进攻方武将列表*********************");
			for (ASObject obj : attackInfo.getHeroList()) {
				logger.debug(BattleHeroHelper.getHeroDebugInfo(obj));
			}

			logger.debug("*********************防守方武将列表*********************");
			for (ASObject obj : defenseInfo.getHeroList()) {
				logger.debug(BattleHeroHelper.getHeroDebugInfo(obj));
			}

		}

		Map<String, Object> result = null;
		try {
			result = this.getBattleEngine(type).execute(attackInfo, defenseInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new HashMap<String, Object>();
			result.put("msg", e.getMessage());
		}

		return result;
	}

	private Battle getBattleEngine(int type) {
		return new DefaultBattle();
	}
}
