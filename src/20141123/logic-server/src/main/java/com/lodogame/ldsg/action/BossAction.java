/**
 * BossAction.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.BossTeamBO;
import com.lodogame.ldsg.bo.BossTeamDetailBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserBossBO;
import com.lodogame.ldsg.bo.UserBossInfoBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.constants.PushType;
import com.lodogame.ldsg.event.BossEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.BossHelper;
import com.lodogame.ldsg.service.BossService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Boss;
import com.lodogame.model.BossTeam;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-26
 */
public class BossAction extends LogicRequestAction {

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private BossService bossService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	public Response getUserBoss() {

		// 读取用户封魔信息列表，在每个地图中都有一个 boss 可以攻打，这个列表中包含该用户攻打各个地图中的 boss 的情况
		List<UserBossBO> userBossList = bossService.getUserBossList(getUid());
		List<UserBossInfoBO> bossInfoList = new ArrayList<UserBossInfoBO>();

		int status = bossService.getStatus();

		for (UserBossBO userBoss : userBossList) {
			UserBossInfoBO ubi = new UserBossInfoBO();
			ubi.setBossDisappearTime(userBoss.getBossDisappearTime());
			ubi.setCooldown(userBoss.getCooldown());
			ubi.setForcesId(userBoss.getForcesId());
			ubi.setMaxChallengeTime(Boss.MAX_CHALLENGE_TIMES);
			ubi.setTimes(userBoss.getTimes());

			bossInfoList.add(ubi);
		}

		set("ubis", bossInfoList);
		set("ex", bossService.getStatus());

		return this.render();
	}

	@Deprecated
	public Response accessBossRoom() {
		List<BossTeamBO> teams = bossService.accessBossRoom(getUid());
		set("ts", teams);
		return this.render();
	}

	private void pushLeaveTeam(Event event) {
		BossTeam team = (BossTeam) event.getObject(BossEvent.KEY_OF_BOSS_TEAM);

		if (team.getTeamMemberCount() == 0)
			return;

		UserBO exitedUser = userService.getUserBO(getUid());
		UserBO captain = userService.getUserBO(team.getCaptainId());

		for (String memId : team.getMembers()) {
			if (!event.getUserId().equals(memId)) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("uid", memId);
				params.put("pid", exitedUser.getPlayerId());
				params.put("cid", captain.getPlayerId());
				params.put("tp", PushType.PUSH_TYPE_UPDATE);

				pushHandler.push("Boss.pushExitTeam", params);
			}
		}
	}

	private void pushKickoutTeam(Event event, long outedPlayerId) {
		BossTeam team = (BossTeam) event.getObject(BossEvent.KEY_OF_BOSS_TEAM);

		for (String memId : team.getMembers()) {
			if (!event.getUserId().equals(memId)) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("uid", memId);
				params.put("pid", outedPlayerId);
				params.put("tp", PushType.PUSH_TYPE_UPDATE);

				pushHandler.push("Boss.pushKickoutTM", params);
			}
		}

	}

	public Response exitTeam() {
		bossService.exitTeam(getUid(), new EventHandle() {
			@Override
			public boolean handle(Event event) {
				pushLeaveTeam(event);
				return true;
			}
		});

		return this.render();
	}

	private void pushBossTeamDetailToOtherMembers(Event event, String pushName) {
		BossTeam team = (BossTeam) event.getObject(BossEvent.KEY_OF_BOSS_TEAM);
		List<UserHeroBO> userHeros = heroService.getUserHeroList(getUid(), 1);
		UserBO user = userService.getUserBO(getUid());

		BossTeamDetailBO detail = new BossTeamDetailBO(userHeros, getUid() == team.getCaptainId(), user.getPlayerId(), user.getUsername(), user.getLevel(), userService.getUserPower(userHeros));

		for (String memId : team.getMembers()) {
			if (!event.getUserId().equals(memId)) {
				Map<String, Object> params = new HashMap<String, Object>();

				params.put("uid", memId);
				params.put("pid", detail.getPlayerId());
				params.put("ic", detail.getCaptain());
				params.put("ul", detail.getUserLevel());
				params.put("po", detail.getPower());
				params.put("un", detail.getUserName());
				params.put("hls", detail.getUserHeroBOList());
				params.put("tp", PushType.PUSH_TYPE_UPDATE);

				pushHandler.push(pushName, params);
			}
		}
	}

	public Response joinTeam() {

		List<BossTeamDetailBO> bossTeamDetailBOs = bossService.joinTeam(getUid(), getString("tid", ""), new EventHandle() {
			@Override
			public boolean handle(Event event) {
				pushBossTeamDetailToOtherMembers(event, "Boss.pushJoinTeam");
				return true;
			}
		});

		set("bs", bossTeamDetailBOs);
		return this.render();
	}

	public Response quickStart() {

		int mid = getInt("mid", 0);
		int forcesId = BossHelper.getForcesIdByMapId(mid);

		List<BossTeamDetailBO> bossTeamDetailBOs = bossService.quickStart(getUid(), forcesId, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				pushBossTeamDetailToOtherMembers(event, "Boss.pushJoinTeam");
				return true;
			}
		});

		set("bs", bossTeamDetailBOs);
		return this.render();
	}

	public Response quickSwapping() {

		int mid = getInt("mid", 0);
		int forcesId = BossHelper.getForcesIdByMapId(mid);

		List<BossTeamDetailBO> bossTeamDetailBOs = bossService.quickSwapping(getUid(), forcesId, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				pushBossTeamDetailToOtherMembers(event, "Boss.pushJoinTeam");
				return true;
			}
		});

		set("bs", bossTeamDetailBOs);
		return this.render();
	}

	public Response kickoutTeamMember() {
		final long pupilId = getLong("pid", 0);

		bossService.kickoutTeamMember(getUid(), userService.getByPlayerId(String.valueOf(pupilId)).getUserId(), new EventHandle() {
			@Override
			public boolean handle(Event event) {
				pushKickoutTeam(event, pupilId);
				return true;
			}
		});

		set("pid", pupilId);

		return render();
	}

	public Response createTeam() {

		int mid = getInt("mid", 0);
		int forcesId = BossHelper.getForcesIdByMapId(mid);

		List<BossTeamDetailBO> team = bossService.createTeam(getUid(), forcesId);

		set("bs", team);
		return this.render();
	}

	public Response resetCooldown() {

		int mid = getInt("mid", 0);
		int forcesId = BossHelper.getForcesIdByMapId(mid);

		bossService.resetCooldown(getUid(), forcesId);
		pushHandler.pushUser(getUid());
		return this.render();
	}

	public Response dismissTeam() {
		bossService.dismissTeamByCaptain(getUid());
		return null;
	}

	public Response challengeBoss() {
		bossService.challengeBoss(getUid(), new EventHandle() {
			@Override
			public boolean handle(Event event) {
				BossTeam team = (BossTeam) event.getObject(BossEvent.KEY_OF_BOSS_TEAM);

				for (String memId : team.getMembers()) {
					// if (!event.getUserId().equals(memId)) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("uid", memId);
					params.put("tp", PushType.PUSH_TYPE_UPDATE);

					pushHandler.push("Boss.pushPrepareToFight", params);
					// }
				}

				return true;
			}
		});
		return this.render();
	}

	public Response ackPrepareToFight() {

		int mid = getInt("mid", 0);
		int forcesId = BossHelper.getForcesIdByMapId(mid);

		bossService.ackPrepareChallengeBoss(getUid(), forcesId);

		return this.render();
	}

	/**
	 * 读取已经创建的队伍
	 */
	public Response getTeams() {
		int mid = getInt("mid", 0); // 读取地图编号

		int forcesId = BossHelper.getForcesIdByMapId(mid);

		List<BossTeamBO> bts = bossService.getBossTeamInfoList(forcesId, getUid());
		set("bts", bts);

		return this.render();
	}
}
