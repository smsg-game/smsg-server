package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.UserEquipMentChangeBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.EquipUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroUpdateEvent;
import com.lodogame.ldsg.service.HeroService;

/**
 * 关卡相关action
 * 
 * @author jacky
 * 
 */

public class EmbattleAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(EmbattleAction.class);

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserForcesDao userForcesDao;

	public Response amend() {

		String userId = getUid();

		long ts = this.getLong("ts", 0L);

		long serverTs = this.userForcesDao.getAmendEmbattleTime(userId);
		if (serverTs > 0 && ts < serverTs) {// 如果请求比较晚，就忽略掉
			return this.render();
		} else {
			this.userForcesDao.setAmendEmbattleTime(userId, ts);
		}

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

		return this.render();

	}

	public Response change() {

		String userHeroId = (String) this.getString("uhid", null);
		int pos = this.getInt("pos", 1);

		LOG.debug("改变武将阵法.uid[" + getUid() + "], userHeroId[" + userHeroId + "], pos[" + pos + "]");

		final List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();
		final List<UserEquipMentChangeBO> userEquipIdList = new ArrayList<UserEquipMentChangeBO>();
		set("st",0);
		this.heroService.changePos(getUid(), userHeroId, pos, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof HeroUpdateEvent) {
					String userHeroId = event.getString("userHeroId");
					UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
					userHeroBOList.add(userHeroBO);
				} else if (event instanceof EquipUpdateEvent) {
					Object o = event.getObject("userHeroId");
					String userHeroId = ((o==null)?null:(String)o);
					userEquipIdList.add(new UserEquipMentChangeBO(event.getString("userEquipId"),userHeroId));
				}else{
					set("st",1);
				}
				return true;
			}
		});
		set("ueids", userEquipIdList);
		set("hl", userHeroBOList);
		return this.render();
	}
}
