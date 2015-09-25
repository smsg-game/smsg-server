/**
 * BossPushHandlerImpl.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserBossDao;
import com.lodogame.ldsg.bo.BossTeamBO;
import com.lodogame.ldsg.bo.UserBossBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.handler.BossPushHandler;
import com.lodogame.ldsg.service.BossService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.BossTeam;
import com.lodogame.model.User;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-29
 */
public class BossPushHandlerImpl extends BasePushHandler implements BossPushHandler {

	private static final Logger LOG = Logger.getLogger(BossPushHandlerImpl.class);

	@Autowired
	private UserBossDao userBossDao;

	@Autowired
	private BossService bossService;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Override
	public void pushBossAppear(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("ex", bossService.getStatus());
		this.push("Boss.pushAppear", params);
	}

	@Override
	public void pushBossDisappear(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("ex", bossService.getStatus());
		this.push("Boss.pushDisappear", params);
	}

	@Override
	public void pushBossTeamUserLogout(long exitedPlayerId, String otherMemberUserId, long captainId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", otherMemberUserId);
		params.put("pid", exitedPlayerId);
		params.put("cid", captainId);
		this.push("Boss.pushExitTeam", params);
	}

	@Override
	public void pushResultOfChallengeBoss(String userId, String reportsId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("uid", userId);
		params.put("rps", bossService.getReports(reportsId));
		this.push("Boss.pushFightResult", params);
	}

	@Override
	public void pushUserBossInfo(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);

		List<UserBossBO> userBossList = bossService.getUserBossList(userId);
		for (UserBossBO ub : userBossList) {
			Map<String, Object> info = new HashMap<String, Object>(); // 该玩家在每幅地图中的封魔信息
			info.put("ts", ub.getTimes());
			info.put("cd", ub.getCooldown());

			int forceId = ub.getForcesId();

			params.put(String.valueOf(forceId), info);
		}

		this.push("Boss.pushUserBoss", params);
	}

	@Override
	public void pushDismissTeam(String teamId, int cause) {
		BossTeam team = bossService.getBossTeam(teamId);
		if (team != null) {
			for (String memId : team.getMembers()) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("uid", memId);
				params.put("ca", cause);

				this.push("Boss.pushDismissTeam", params);
			}

			bossService.dismissTeam(team.getId(), true);
		} else {
			LOG.error("推送小队解散信息出错，队伍已不存在.[TeamId]" + teamId + ".[Cause]" + cause);
		}
	}

	@Override
	public void pushUpdatedTeamInfo(String teamId, int forcesId, int status) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("st", status);
		params.put("tid", teamId);

		if (status == 3) {

		} else {

			BossTeam t = bossService.getBossTeam(teamId);

			if (t == null) {
				return;
			}

			User user = userService.get(t.getCaptainId());
			List<UserHeroBO> userHeroBOs = heroService.getUserHeroList(user.getUserId(), 1);
			BossTeamBO bossTeamBo = new BossTeamBO(t.getId(), user.getLodoId(), user.getLevel(), user.getUsername(), userService.getUserPower(userHeroBOs), t.getMembers().size(),
					BossTeam.MAX_MEBMER_NUMBER, userHeroBOs);

			params.put("bt", bossTeamBo);

		}

		List<String> userIds = userBossDao.getUsers(forcesId);

		for (String userId : userIds) {
			params.put("uid", userId);
			this.push("Boss.pushUpdatedTeamInfo", params);
		}

	}
}
