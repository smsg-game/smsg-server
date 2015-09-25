package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.BloodSacrificeBO;
import com.lodogame.ldsg.bo.SystemBloodSacrificeLooseBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserHeroSkillBO;
import com.lodogame.ldsg.bo.UserHeroSkillTrainBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.ToolUpdateEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;

/**
 * 武将相关action
 * 
 * @author jacky
 * 
 */

public class HeroAction extends LogicRequestAction {
	public static final int NEED_THREE_HEROD = 2007;

	private static final Logger LOG = Logger.getLogger(HeroAction.class);

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private PushHandler pushHandler;

	public Response loadHeros() {

		int type = this.getInt("ft", 0);

		LOG.debug("获取武将列表.uid[" + getUid() + "], type[" + type + "]");

		List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(getUid(), type);

		set("hls", userHeroList);
		return this.render();

	}

	/**
	 * 武将传承
	 */
	public Response inherit() {
		String uid = this.getUid();
		String givingHeroId = this.getString("uhidGiving", null);
		String inheritHeroId = this.getString("uhidInherit", null);
		int type = this.getInt("type", 0);

		Map<String, Object> retVal = this.heroService.inherit(uid, givingHeroId, inheritHeroId, type);
		UserHeroBO givingHero = heroService.getUserHeroBO(uid, givingHeroId);
		UserHeroBO inheritHero = heroService.getUserHeroBO(uid, inheritHeroId);

		set("givingHero", givingHero);
		set("inheritHero", inheritHero);
		set("contract", retVal.get("contract"));
		set("tp", type);
		pushHandler.pushUser(uid);
		return this.render();
	}

	/**
	 * 武将传承预览接口
	 */
	public Response inheritPre() {
		String uid = this.getUid();
		String givingHeroId = this.getString("uhidGiving", null);
		String inheritHeroId = this.getString("uhidInherit", null);
		int type = this.getInt("type", 0);

		Map<String, Object> retVal = this.heroService.inheritPre(uid, givingHeroId, inheritHeroId, type);

		set("givingHero", retVal.get("givingHero"));
		set("inheritHero", retVal.get("inheritHero"));
		set("gold", retVal.get("gold"));
		set("contract", retVal.get("contract"));
		set("tp", type);
		return this.render();
	}

	/**
	 * 进阶预览
	 * 
	 * @return
	 */
	public Response upgradePre() {

		String userHeroId = this.getString("uhid", null);

		UserHeroBO userHeroBO = this.heroService.upgradePre(getUid(), userHeroId);
		set("hero", userHeroBO);

		return this.render();
	}

	/**
	 * 出售
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response sell() {

		final List<String> userHeroIdList = (List<String>) getList("uhids");

		this.heroService.sell(getUid(), userHeroIdList, new EventHandle() {

			public boolean handle(Event event) {
				UserBO userBO = userService.getUserBO(getUid());
				set("co", userBO.getCopper());
				return true;
			}
		});

		set("uhids", userHeroIdList);

		return this.render();
	}

	/**
	 * 吞噬
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response devour() {

		final String uid = getUid();
		final String userHeroId = this.getString("uhid", null);
		final List<String> userHeroIdList = (List<String>) this.getList("uhids");

		this.heroService.devour(uid, userHeroId, userHeroIdList, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof CopperUpdateEvent) {
					pushHandler.pushUser(uid);
				}
				return true;
			}
		});

		set("uhids", userHeroIdList);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
		set("hero", userHeroBO);

		return this.render();
	}

	/**
	 * 吞噬预览
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response devourPre() {

		String userHeroId = this.getString("uhid", null);
		List<String> userHeroIdList = (List<String>) this.getList("uhids");

		Map<String, Object> retVal = heroService.devourPre(getUid(), userHeroId, userHeroIdList);
		set("hero", retVal.get("userHeroBO"));
		set("co", retVal.get("copper"));

		return this.render();
	}

	@SuppressWarnings("unchecked")
	public Response upgrade() {

		final String userHeroId = this.getString("uhid", null);
		final List<String> userHeroIdList = (List<String>) this.getList("uhids");
		int force = this.getInt("fce", 0);

		set("uhids", userHeroIdList);

		int rc = 1000;
		try {

			this.heroService.upgrade(getUid(), userHeroId, userHeroIdList, force == 1, new EventHandle() {

				public boolean handle(Event event) {
					UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
					set("hero", userHeroBO);
					return true;
				}
			});

		} catch (ServiceException se) {
			rc = se.getCode();
			if (rc != HeroService.UPGRADE_HERO_RATE_FAILURE) {// 不是概率上的失败直接丢出去
				throw se;
			}
		}

		return this.render(rc);

	}

	/**
	 * 技能学习
	 * 
	 * @return
	 */
	public Response studySkill() {

		int toolId = this.getInt("tid", 0);
		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);

		this.heroService.studySkill(userId, userHeroId, toolId);

		List<UserHeroSkillBO> userHeroSkillBOList = this.heroService.getUserHeroSkillBOList(userId, userHeroId);

		set("hskls", userHeroSkillBOList);

		return this.render();
	}

	/**
	 * 技能训练
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response trainSkill() {

		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);
		List<Integer> lockHeroSkillIdList = (List<Integer>) this.getList("lhskids");

		this.heroService.trainSkill(userId, userHeroId, lockHeroSkillIdList);

		List<UserHeroSkillTrainBO> heroSkillTrainBOList = this.heroService.getUserHeroSkillTrainBOList(userId, userHeroId);

		this.pushHandler.pushUser(userId);

		set("hsktls", heroSkillTrainBOList);

		return this.render();
	}

	/**
	 * 获取上次训练结果
	 * 
	 * @return
	 */
	public Response getTrainList() {

		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);

		List<UserHeroSkillTrainBO> heroSkillTrainBOList = this.heroService.getUserHeroSkillTrainBOList(userId, userHeroId);

		set("hsktls", heroSkillTrainBOList);

		return this.render();
	}

	/**
	 * 放弃训练结果
	 * 
	 * @return
	 */
	public Response trainSkillCancel() {

		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);

		this.heroService.trainSkillCancel(userId, userHeroId);

		return this.render();
	}

	/**
	 * 保存训练结果
	 * 
	 * @return
	 */
	public Response trainSkillSave() {

		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);

		this.heroService.trainSkillSave(userId, userHeroId);

		List<UserHeroSkillBO> userHeroSkillBOList = this.heroService.getUserHeroSkillBOList(userId, userHeroId);

		set("hskls", userHeroSkillBOList);

		return this.render();
	}

	/**
	 * 遗忘技能
	 * 
	 * @return
	 */
	public Response forgetSkill() {

		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);
		int userHeroSkillId = this.getInt("hskid", 0);

		this.heroService.forgetSkill(userId, userHeroId, userHeroSkillId);

		List<UserHeroSkillBO> userHeroSkillBOList = this.heroService.getUserHeroSkillBOList(userId, userHeroId);

		set("hskls", userHeroSkillBOList);

		return this.render();
	}

	/**
	 * 升级技能
	 * 
	 * @return
	 */
	public Response upgradeSkill() {

		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);
		int userHeroSkillId = this.getInt("hskid", 0);

		this.heroService.upgradeSkill(userId, userHeroId, userHeroSkillId);

		List<UserHeroSkillBO> userHeroSkillBOList = this.heroService.getUserHeroSkillBOList(userId, userHeroId);

		set("hskls", userHeroSkillBOList);
		set("rskid", userHeroSkillId);
		return this.render();

	}

	/**
	 * 武将兑换碎片
	 * 
	 * @return
	 */
	public Response exchangeShard() {

		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);

		final List<UserToolBO> userToolBOList = new ArrayList<UserToolBO>();

		this.heroService.exchangeShard(userId, userHeroId, new EventHandle() {

			@Override
			public boolean handle(Event event) {

				if (event instanceof ToolUpdateEvent) {
					UserToolBO userToolBO = (UserToolBO) event.getObject("tool");
					if (userToolBO != null) {
						userToolBOList.add(userToolBO);
					}
				}

				return true;
			}
		});

		if (!userToolBOList.isEmpty()) {
			this.pushHandler.pushToolList(userId, userToolBOList);
		}

		return this.render();
	}

	/**
	 * 鬼之契约转生
	 * 
	 * @return
	 */
	public Response contractTransform() {
		String userId = this.getUid();
		String userHeroId = this.getString("uhid", null);

		this.heroService.contractTransform(userId, userHeroId);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(userId, userHeroId);
		set("hero", userHeroBO);

		return this.render();
	}

	/**
	 * 相同武将转生
	 * 
	 * @return
	 */
	public Response heroTransform() {
		String userId = getUid();
		String userHeroId = this.getString("uhid", null);
		List<String> userHeroIdList = (List<String>) this.getList("uhids");

		this.heroService.heroTransform(userId, userHeroId, userHeroIdList);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(userId, userHeroId);
		set("hero", userHeroBO);
		set("uhids", userHeroIdList);

		return this.render();
	}

	public Response transformPre() {
		String userId = getUid();
		String userHeroId = this.getString("uhid", null);
		UserHeroBO userHeroBo = this.heroService.transformPre(userId, userHeroId);
		set("hero", userHeroBo);
		return this.render();
	}

	/**
	 * 血祭预览
	 * 
	 * @return
	 */
	public Response bloodSacrificePre() {

		String userHeroId = this.getString("uhid", null);

		BloodSacrificeBO bloodSacrificeBO = heroService.bloodSacrificePre(getUid(), userHeroId);
		set("bs", bloodSacrificeBO);

		return this.render();
	}

	/**
	 * 血祭
	 * 
	 * @return
	 */
	public Response bloodSacrifice() {

		final String uid = getUid();
		final String userHeroId = this.getString("uhid", null);
		final String useUserHeroId = this.getString("hids", null);

		SystemBloodSacrificeLooseBO systemBloodSacrificeLooseBO = this.heroService.bloodSacrifice(uid, userHeroId, useUserHeroId, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof CopperUpdateEvent) {
					pushHandler.pushUser(uid);
				}
				return true;
			}
		});

		set("uuhid", useUserHeroId);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
		set("hero", userHeroBO);
		set("tel", systemBloodSacrificeLooseBO);

		return this.render();
	}

	/**
	 * 锁定英雄
	 * 
	 * @return
	 */
	public Response lockHero() {

		String userHeroId = this.getString("uhid", null);

		this.heroService.lockHero(getUid(), userHeroId);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
		set("hero", userHeroBO);

		return this.render();
	}

	/**
	 * 解除锁定英雄
	 * 
	 * @retur
	 */
	public Response unlockHero() {

		String userHeroId = this.getString("uhid", null);

		this.heroService.unlockHero(getUid(), userHeroId);
		UserHeroBO userHeroBO = heroService.getUserHeroBO(getUid(), userHeroId);
		set("hero", userHeroBO);

		return this.render();
	}

}
