package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.bo.ContestTeamBO;
import com.lodogame.ldsg.bo.ContestTeamInfoBO;
import com.lodogame.ldsg.constants.ContestConstant;
import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.handler.ContestPushHandler;
import com.lodogame.ldsg.helper.ContestHelper;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.model.ContestRound;

public class ContestPushHandlerImpl extends BasePushHandler implements ContestPushHandler{

	private static final Logger LOG = Logger.getLogger(ContestPushHandlerImpl.class);

	@Autowired
	private ContestService contestService;
	
	@Override
	public void pushRoundFinished(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		ContestRound currentRound = contestService.getCurrentRound();
		int lastRound = currentRound.getRoundId() - 1;
		
		if (lastRound == ContestConstant.ROUND_64 || lastRound == ContestConstant.ROUND_16 || lastRound == ContestConstant.ROUND_1) {
			params.put("nextRound", lastRound);
			params.put("round", lastRound);
		} else {
			params.put("nextRound", currentRound.getRoundId());
			params.put("round", lastRound);
		}

		LOG.debug("擂台赛一场比赛结束 round[" + lastRound + "]");
		this.push("Contest.pushRoundFinished", params);
	}

	@Override
	public void pushStatus(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("st", contestService.getStatus(userId));

		LOG.debug("擂台赛报名时间结束，进入第一轮对战界面");
		this.push("Contest.pushStatus", params);
	}

	@Override
	public void pushBet(String userId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		ContestTeamBO teamBO = contestService.createPushBetTeamBO(userId);
		params.put("tl", teamBO);
		params.put("uid", userId);
		
		LOG.debug("擂台赛用户下注成功");
		this.push("Contest.pushBet", params);
	}

	@Override
	public void pushUserReg(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<ContestTeamInfoBO> teamInfoBOList = contestService.createTeamInfoBOList();

		params.put("tl", teamInfoBOList);
		params.put("uid", userId);
		LOG.debug("擂台赛用户注册成功");
		this.push("Contest.pushUserReg", params);
	}

}
