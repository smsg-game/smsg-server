package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.bo.ArenaRankBO;
import com.lodogame.ldsg.bo.ArenaRecordBO;
import com.lodogame.ldsg.bo.ArenaRegBO;
import com.lodogame.ldsg.bo.ArenaRewardBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.handler.ArenaPusHandler;
import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.service.ArenaService;

public class ArenaPushHandlerImpl extends BasePushHandler implements ArenaPusHandler {

	private static final Logger logger = Logger.getLogger(ArenaPushHandlerImpl.class);

	@Autowired
	private ArenaService arenaService;

	@Override
	public void pushRank(String userId) {

		logger.debug("推送百人斩排行榜.userId[" + userId + "]");

		List<ArenaRankBO> arenaRankBOList = this.arenaService.getRankList();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("rkl", arenaRankBOList);

		this.push("Arena.pushRank", params);

	}

	@Override
	public void pushUserInfo(String userId) {

		logger.debug("推送百人斩用户信息.userId[" + userId + "]");

		ArenaRegBO arenaRegBO = this.arenaService.getRegBO(userId);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("uinfo", arenaRegBO);

		this.push("Arena.pushUserInfo", params);

	}

	@Override
	public void pushBattle(String userId, Map<String, String> params) {

		logger.debug("推送百人斩战斗.userId[" + userId + "], params[" + params + "]");

		CommonDropBO commonDropBO = new CommonDropBO();
		commonDropBO.setCopper(Integer.parseInt(params.get("copper")));

		boolean isAttack = StringUtils.equalsIgnoreCase(userId, params.get("attackUserId"));
		boolean isAttackWin = Integer.parseInt(params.get("flag")) == 1;

		int rf = 0;
		if (isAttack && isAttackWin) {
			rf = 1;
		} else if (!isAttack && !isAttackWin) {
			rf = 1;
		}

		Map<String, Object> pushParam = new HashMap<String, Object>();
		pushParam.put("uid", userId);
		// pushParam.put("auid", params.get("attackUserId"));
		pushParam.put("aun", params.get("attackUsername"));
		// pushParam.put("duid", params.get("defenseUserId"));
		pushParam.put("dun", params.get("defenseUsername"));
		pushParam.put("dr", commonDropBO);
		pushParam.put("rf", rf);
		pushParam.put("rp", params.get("report"));
		pushParam.put("at", isAttack ? 1 : 0);
		pushParam.put("tp", 5);

		this.push("Arena.pushBattle", pushParam);
	}

	@Override
	public void pushBattleRecord(String userId, Map<String, String> params) {

		logger.debug("推送百人斩战斗记录消息.userId[" + userId + "], params[" + params + "]");

		int flag = 0;
		if (StringUtils.equalsIgnoreCase(userId, params.get("attackUserId")) || StringUtils.equalsIgnoreCase(userId, params.get("defenseUserId"))) {
			flag = 1;
		}

		ArenaRecordBO arenaRecordBO = new ArenaRecordBO();
		arenaRecordBO.setAttackUsername(params.get("attackUsername"));
		arenaRecordBO.setDefenseUsername(params.get("defenseUsername"));
		arenaRecordBO.setType(Integer.parseInt(params.get("type")));
		arenaRecordBO.setWinCount(Integer.parseInt(params.get("winCount")));
		arenaRecordBO.setFlag(flag);

		Map<String, Object> pushParam = new HashMap<String, Object>();
		pushParam.put("uid", userId);
		pushParam.put("rcd", arenaRecordBO);

		this.push("Arena.pushBattleRecord", pushParam);

	}

	@Override
	public void pushReward(String userId) {

		logger.debug("推送百人斩活动奖厉消息.userId[" + userId + "]");

		ArenaRewardBO arenaRewardBO = this.arenaService.giveReward(userId);
		if (arenaRewardBO == null) {
			return;
		}

		Map<String, Object> pushParam = new HashMap<String, Object>();
		pushParam.put("uid", userId);
		pushParam.put("wt", arenaRewardBO.getWinTimes());
		pushParam.put("ft", arenaRewardBO.getLoseTiems());
		pushParam.put("mwc", arenaRewardBO.getMaxWinCount());
		pushParam.put("co", arenaRewardBO.getCopperNum());
		pushParam.put("dr", arenaRewardBO.getDropTools());

		this.push("Arena.pushReward", pushParam);
	}

}
