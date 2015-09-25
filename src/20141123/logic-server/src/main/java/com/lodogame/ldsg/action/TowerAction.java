package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.UserTowerBO;
import com.lodogame.ldsg.bo.UserTowerMapDataBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.TowerService;

public class TowerAction extends LogicRequestAction {

	private static final Logger logger = Logger.getLogger(TowerAction.class);

	@Autowired
	private TowerService towerService;
	@Autowired
	private HeroService heroService;

	@Autowired
	private PushHandler pushHandlerImpl;

	/**
	 * 进入转生塔
	 * 
	 * @return
	 */
	public Response enter() {

		List<UserTowerBO> floors = this.towerService.enter(this.getUid());

		set("fls", floors);

		return this.render();
	}

	public Response enterFloor() {

		int floor = (Integer) request.getParameter("fl");
		List<UserTowerMapDataBO> mds = this.towerService.enterFloor(this.getUid(), floor);

		set("md", mds);

		return this.render();
	}

	public Response useTool() {
		this.towerService.useTool(this.getUid());
		return null;
	}

	public Response openBox() {

		int tid = (Integer) request.getParameter("tid");
		int floor = (Integer) request.getParameter("fl");
		int pos = (Integer) request.getParameter("pos");
		this.towerService.pickUpBox(getUid(), floor, pos, tid);

		set("tid", tid);

		return this.render();
	}

	public Response reset() {

		List<UserTowerBO> floors = this.towerService.reset(this.getUid(), true);

		set("fls", floors);

		pushHandlerImpl.pushUser(getUid());

		return this.render();
	}

	/**
	 * 转生塔战斗
	 * 
	 * @param type
	 * @param param
	 */
	public Response fight() {
		final int floor = (Integer) request.getParameter("fl");
		final int pos = (Integer) request.getParameter("pos");
		final int forcesId = (Integer) request.getParameter("fid");
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

		towerService.towerFight(getUid(), floor, pos, forcesId, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				String report = event.getString("report");
				int flag = event.getInt("flag");
				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", 3);
				set("dr", event.getObject("forcesDropBO"));
				set("bgid", event.getInt("bgid"));
				set("fid", forcesId);
				pushHandlerImpl.pushUser(getUid());
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

		return null;
	}
}
