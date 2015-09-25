package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.EquipUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroUpdateEvent;
import com.lodogame.ldsg.event.ToolUpdateEvent;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.EquipHelper;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.UserEquip;

/**
 * 武将相关action
 * 
 * @author jacky
 * 
 */

public class EquipAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(EquipAction.class);

	@Autowired
	private EquipService equipService;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private HeroService heroService;

	public Response loadEquipments() {

		LOG.debug("获取用户装备列表.uid[" + getUid() + "]");

		List<UserEquipBO> userEquipList = this.equipService.getUserEquipList(getUid());

		set("eql", userEquipList);

		return this.render();

	}

	/**
	 * 装备穿戴
	 * 
	 * @return
	 */
	public Response dress() {

		Integer equipType = this.getInt("dp", 1);
		String userEquipId = this.getString("ueid", null);
		final String userHeroId = this.getString("uhid", null);

		LOG.debug("武将穿戴装备.uid[" + getUid() + "], userEquipId[" + userEquipId + "], userHeroId[" + userHeroId + "], equipType[" + equipType + "]");

		final List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

		this.equipService.updateEquipHero(getUid(), userEquipId, userHeroId, equipType, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				if (event instanceof EquipUpdateEvent) {
					UserEquip userEquip = (UserEquip) event.getObject("userEquip");
					if (userEquip.getUserHeroId() == null) {
						set("uueid", userEquip.getUserEquipId());
					}
				} else if (event instanceof HeroUpdateEvent) {
					UserHeroBO userHero = heroService.getUserHeroBO(getUid(), event.getString("userHeroId"));
					userHeroBOList.add(userHero);
				}

				return true;

			}
		});

		set("uhid", userHeroId);
		set("hls", userHeroBOList);
		set("ueid", userEquipId);

		return this.render();

	}

	/**
	 * 装备出售
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response sell() {

		final List<String> userEquipIds = (List<String>) this.getList("ueids");

		this.equipService.sell(getUid(), userEquipIds, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof CopperUpdateEvent) {
					pushHandler.pushUser(getUid());
				}
				return true;
			}
		});

		set("ueids", userEquipIds);

		return this.render();

	}

	/**
	 * 武将预览
	 * 
	 * @return
	 */
	public Response upgradePre() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		// 当前装备的再一次预览
		Map<String, Object> retVal = this.equipService.upgradePre(getUid(), userEquipId);
		set("eq", retVal.get("userEquipBO"));
		set("st", retVal.get("status"));
		set("co", retVal.get("needCopper"));

		return this.render();
	}

	/**
	 * 装备打造
	 * 
	 * @return
	 */
	public Response autoUpgrade() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		List<Integer> addLevelList = new ArrayList<Integer>();

		int stopResult = this.equipService.autoUpgrade(getUid(), userEquipId, addLevelList);

		UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), userEquipId);
		set("eq", userEquipBO);
		if (StringUtils.isNotEmpty(userEquipBO.getUserHeroId())) {
			UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userEquipBO.getUserHeroId());
			set("hero", userHeroBO);
		}

		set("rt", stopResult);
		set("al", addLevelList);

		pushHandler.pushUser(getUid());

		// 当前装备的再一次预览
		Map<String, Object> retVal = this.equipService.upgradePre(getUid(), userEquipId);
		set("eqPre", retVal.get("userEquipBO"));
		set("st", retVal.get("status"));
		set("co", retVal.get("needCopper"));

		return this.render();

	}

	/**
	 * 装备打造
	 * 
	 * @return
	 */
	public Response upgrade() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		this.equipService.upgrade(getUid(), userEquipId, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof HeroUpdateEvent) {
					UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), event.getString("userHeroId"));
					set("hero", userHeroBO);
				} else if (event instanceof EquipUpdateEvent) {
					// 返加装备BO对象
					UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), event.getString("userEquipId"));
					set("eq", userEquipBO);
				} else if (event instanceof CopperUpdateEvent) {
					// 推送用户信息
					pushHandler.pushUser(getUid());
				}
				return true;
			}
		});

		// 当前装备的再一次预览
		Map<String, Object> retVal = this.equipService.upgradePre(getUid(), userEquipId);
		set("eqPre", retVal.get("userEquipBO"));
		set("st", retVal.get("status"));
		set("co", retVal.get("needCopper"));

		return this.render();

	}

	public Response merge() {

		final String userEquipId = (String) this.request.getParameter("ueid");
		int type = this.getInt("tp", 0);

		this.equipService.mergeEquip(getUid(), userEquipId, type == 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				if (event instanceof HeroUpdateEvent) {
					UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), event.getString("userHeroId"));
					set("hero", userHeroBO);
				} else if (event instanceof EquipUpdateEvent) {
					// 返加装备BO对象
					UserEquipBO userEquipBO = equipService.getUserEquipBO(getUid(), event.getString("userEquipId"));
					set("eq", userEquipBO);
				} else if (event instanceof ToolUpdateEvent) {
					// pushHandler.pushToolList(getUid());
				}

				return true;
			}
		});

		if (type == 1) {// 金币刷新
			this.pushHandler.pushUser(getUid());
		}

		set("tp", type);

		UserEquipBO userEquipBO = this.equipService.mergeEquipPre(getUid(), userEquipId);
		set("eqPre", userEquipBO);
		set("st", 1);
		if (userEquipBO == null) {
			set("st", -3);
		}

		return this.render();
	}

	public Response mergePre() {

		final String userEquipId = (String) this.request.getParameter("ueid");

		UserEquipBO userEquipBO = this.equipService.mergeEquipPre(getUid(), userEquipId);

		UserEquip userEquip = this.equipService.getUserEquip(getUid(), userEquipId);
		SystemEquip systemEquip = this.equipService.getSysEquip(userEquip.getEquipId());

		set("ng", EquipHelper.getGoldMergeNeedMoney(systemEquip.getColor()));
		set("eq", userEquipBO);
		set("st", 1);
		if (userEquipBO == null) {
			set("st", -3);
		}

		return this.render();
	}
	
	public Response polish(){
		
		String userEquipId = (String) this.request.getParameter("ueid");
		int polishType = this.getInt("tp",1);
		Map<String, Object> map = this.equipService.polish(getUid(), userEquipId, polishType);
		set("at", map.get("attack"));
		set("de", map.get("defense"));
		set("li", map.get("life"));

		set("tp", polishType);
		
		
		 // 推送用户信息
		pushHandler.pushUser(getUid());
		return this.render();
	}
	
	public Response savePolish(){
		
		String userEquipId = (String) this.request.getParameter("ueid");
		String userId = getUid();
		Map<String, Object> map = this.equipService.savePolish(getUid(), userEquipId);
		set("hero", map.get("hero"));
		
		UserEquipBO userEquipBO = this.equipService.getUserEquipBO(userId, userEquipId);
		set("eq", userEquipBO);
		return this.render();
	}
	
}
