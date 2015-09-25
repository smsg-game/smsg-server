package com.lodogame.ldsg.action;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.ForcesStatus;
import com.lodogame.ldsg.constants.ForcesType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.TowerService;

/**
 * 关卡相关action
 * 
 * @author jacky
 * 
 */

public class BattleAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(BattleAction.class);

	@Autowired
	private SceneService sceneService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private PkService pkService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private PushHandler pushHandler;

	public Response fight() throws IOException {

		final int type = this.getInt("tp", 1);
		final long param = this.getInt("pa", 0);
		long ts = this.getLong("ts", 0L);

		this.userForcesDao.setAmendEmbattleTime(getUid(), ts);

		String pos1UserHeroId = this.getString("p1", null);
		String pos2UserHeroId = this.getString("p2", null);
		String pos3UserHeroId = this.getString("p3", null);
		String pos4UserHeroId = this.getString("p4", null);
		String pos5UserHeroId = this.getString("p5", null);
		String pos6UserHeroId = this.getString("p6", null);

		Map<Integer, String> posMap = new HashMap<Integer, String>();
		if (pos1UserHeroId != null) {
			posMap.put(1, pos1UserHeroId);
		}
		if (pos2UserHeroId != null) {
			posMap.put(2, pos2UserHeroId);
		}
		if (pos3UserHeroId != null) {
			posMap.put(3, pos3UserHeroId);
		}
		if (pos4UserHeroId != null) {
			posMap.put(4, pos4UserHeroId);
		}
		if (pos5UserHeroId != null) {
			posMap.put(5, pos5UserHeroId);
		}
		if (pos6UserHeroId != null) {
			posMap.put(6, pos6UserHeroId);
		}
		this.heroService.amendEmbattle(getUid(), posMap);

		LOG.debug("战斗请求uid[" + getUid() + "]");
		switch (type) {
		case 1:
			monsterFight(type, (int) param);
			break;
		case 2:
			pkFight(type, param);
			break;
		}

		return null;
	}

	/**
	 * 争霸赛挑战
	 * 
	 * @param type
	 * @param param
	 */
	private void pkFight(final int type, final long targetPid) {
		pkService.fight(getUid(), targetPid, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				String report = event.getString("report");
				int flag = event.getInt("flag");
				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", type);
				set("dr", event.getObject("forcesDropBO"));
				set("bgid", event.getInt("bgid"));
				set("dun", event.getString("dun"));
				set("pkt", event.getInt("pkt"));
				
				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});
	}

	/**
	 * 怪物战斗
	 * 
	 * @param type
	 * @param forcesId
	 */
	private void monsterFight(final int type, final int forcesId) {
		sceneService.attack(getUid(), forcesId, new EventHandle() {

			public boolean handle(Event event) {

				String report = event.getString("report");
				int flag = event.getInt("flag");

				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", type);
				set("dr", event.getObject("forcesDropBO"));
				//set("cp", event.getInt("canPass"));
				set("bgid", event.getInt("bgid"));
				set("fid", forcesId);

				// 普通怪最后一个
				UserForcesBO userForcesBO = forcesService.getUserCurrentForces(getUid(), ForcesType.FORCES_TYPE_NORMAL);
				set("mfid", userForcesBO != null ? userForcesBO.getForcesId() : 0);

				if (userForcesBO != null && userForcesBO.getStatus() == ForcesStatus.STATUS_PASS) {
					set("adf", 1);
				} else {
					set("adf", 0);
				}

				// 精英怪最后一个
				UserForcesBO userEliteForcesBO = forcesService.getUserCurrentForces(getUid(), ForcesType.FORCES_TYPE_ELITE);
				set("mefid", userEliteForcesBO != null ? userEliteForcesBO.getForcesId() : 0);
				if (userEliteForcesBO != null && userEliteForcesBO.getStatus() == ForcesStatus.STATUS_PASS) {
					set("eadf", 1);
				} else {
					set("eadf", 0);
				}

				pushHandler.pushUser(getUid());

				if (flag == 1) {

					// 打怪任务
					taskService.updateTaskFinish(getUid(), 1, new EventHandle() {

						@Override
						public boolean handle(Event event) {
							return false;
						}
					}, new TaskChecker() {

						@Override
						public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

							int needForcesId = NumberUtils.toInt(params.get("forces_id"));

							if (taskTarget == TaskTargetType.TASK_TYPE_PASS_FORCES && forcesId == needForcesId) {
								return true;
							}

							return false;
						}
					});
				}

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}

		});
	}

	public Response pass() throws IOException {

		String userId = this.getUid();

		LOG.debug("跳过战斗.userId[" + userId + "]");

		this.forcesService.passForcesBattle(getUid());

		return this.render();
	}
}
