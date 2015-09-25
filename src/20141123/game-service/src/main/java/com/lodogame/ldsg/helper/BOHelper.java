package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.easou.sso.sdk.example.GetUserExample;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.ContestPlayerPairBO;
import com.lodogame.ldsg.bo.ContestRankBO;
import com.lodogame.ldsg.bo.ContestTeamBO;
import com.lodogame.ldsg.bo.ContestTeamInfoBO;
import com.lodogame.ldsg.bo.CopyActivityBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.SystemActivityDrawShowBO;
import com.lodogame.ldsg.bo.SystemGoldSetBO;
import com.lodogame.ldsg.bo.SystemMallBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserTaskBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ContestPlayerPair;
import com.lodogame.model.ContestTeam;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemActivityDrawShow;
import com.lodogame.model.SystemGoldSet;
import com.lodogame.model.SystemMall;
import com.lodogame.model.SystemMonster;
import com.lodogame.model.SystemTask;
import com.lodogame.model.User;
import com.lodogame.model.UserContestRank;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserTask;
import com.lodogame.model.UserTool;

/**
 * bo帮助类
 * 
 * @author jacky
 * 
 */
public class BOHelper {
	
	/**
	 * 从怪物模型创建战斗英雄模型
	 * 
	 * @param systemMonster
	 * @return
	 */
	public static BattleHeroBO createBattleHeroBO(SystemMonster systemMonster) {
		BattleHeroBO battleHeroBO = new BattleHeroBO();
		BeanUtils.copyProperties(systemMonster, battleHeroBO);
		battleHeroBO.setSystemHeroId(systemMonster.getHeroModel());
		battleHeroBO.setName(systemMonster.getMonsterName());
		battleHeroBO.setLevel(systemMonster.getMonsterLevel());

		battleHeroBO.addSkill(systemMonster.getSkill1());
		battleHeroBO.addSkill(systemMonster.getSkill2());
		battleHeroBO.addSkill(systemMonster.getSkill3());
		battleHeroBO.addSkill(systemMonster.getSkill4());

		battleHeroBO.setMaxLife(systemMonster.getLife());

		return battleHeroBO;
	}

	/**
	 * 从用户武将模型创建战斗武将模型
	 * 
	 * @param userHeroBO
	 * @return
	 */
	public static BattleHeroBO createBattleHeroBO(UserHeroBO userHeroBO) {
		BattleHeroBO battleHeroBO = new BattleHeroBO();
		BeanUtils.copyProperties(userHeroBO, battleHeroBO);
		battleHeroBO.addSkill(userHeroBO.getSkill1());
		battleHeroBO.addSkill(userHeroBO.getSkill2());
		battleHeroBO.addSkill(userHeroBO.getSkill3());
		battleHeroBO.addSkill(userHeroBO.getSkill4());
		battleHeroBO.addSkill(userHeroBO.getSkill5());
		battleHeroBO.setMaxLife(battleHeroBO.getLife());
		return battleHeroBO;
	}

	/**
	 * 创建用户任务BO
	 * 
	 * @param userTask
	 * @return
	 */
	public static UserTaskBO crateUserTaskBO(SystemTask systemTask, UserTask userTask) {
		UserTaskBO userTaskBO = new UserTaskBO();
		BeanUtils.copyProperties(userTask, userTaskBO);
		if (systemTask != null) {
			userTaskBO.setNeedFinishTimes(systemTask.getNeedFinishTimes());
			userTaskBO.setTaskName(systemTask.getTaskName());
			userTaskBO.setTaskDesc(systemTask.getTaskDesc());
			userTaskBO.setSort(systemTask.getSort());
			userTaskBO.setImgId(systemTask.getImgId());
			List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemTask.getToolIds());
			// 特殊处理下金币
			int goldNum = systemTask.getGoldNum();
			if (goldNum > 0) {
				DropToolBO dropToolBo = new DropToolBO(ToolType.GOLD, ToolId.TOOL_GLOD_ID, goldNum);
				dropToolBOList.add(dropToolBo);
			}

			userTaskBO.setDropToolBO(dropToolBOList);
		}
		return userTaskBO;
	}

	/**
	 * 创建充值套餐BO
	 * 
	 * @param userTask
	 * @return
	 */
	public static SystemGoldSetBO createSystemGoldSetBO(SystemGoldSet systemGoldSet) {
		SystemGoldSetBO systemGoldSetBO = new SystemGoldSetBO();
		BeanUtils.copyProperties(systemGoldSet, systemGoldSetBO);
		return systemGoldSetBO;
	}

	/**
	 * 创建用户BO
	 * 
	 * @param user
	 * @return
	 */
	public static UserBO craeteUserBO(User user, UserExtinfo userExtinfo) {
		UserBO userBO = new UserBO();
		userBO.setCopper(user.getCopper());
		userBO.setExp(user.getExp());
		userBO.setGoldNum(user.getGoldNum());
		userBO.setLevel(user.getLevel());
		userBO.setPlayerId(user.getLodoId());
		userBO.setOrderNum(user.getOrderNum());
		userBO.setPower(user.getPower());
		userBO.setUserId(user.getUserId());
		userBO.setUsername(user.getUsername());
		userBO.setReputation(user.getReputation());
		userBO.setPowerAddTime(user.getPowerAddTime().getTime() + InitDefine.POWER_ADD_INTERVAL);
		userBO.setPowerAddInterval(InitDefine.POWER_ADD_INTERVAL);
		userBO.setVipExpired(user.getVipExpiredTime() != null ? user.getVipExpiredTime().getTime() : System.currentTimeMillis());
		userBO.setVipLevel(user.getVipLevel());
        userBO.setMind(user.getMind());
		
		if (userExtinfo != null) {
			userBO.setHeroBagLimit(userExtinfo.getHeroMax());
			userBO.setEquipBagLimit(userExtinfo.getEquipMax());
			userBO.setGuideStep(userExtinfo.getGuideStep());
			userBO.setWinCount(userExtinfo.getWinCount());
			userBO.setLoseCount(userExtinfo.getLoseCount());
		} else {
			userBO.setHeroBagLimit(InitDefine.HERO_BAG_INIT);
			userBO.setEquipBagLimit(InitDefine.EQUIP_BAG_INIT);
			userBO.setGuideStep(InitDefine.INIT_GUIDE_STEP);
		}

		int buyCoppertimes = 1;

		if (userExtinfo != null && DateUtils.isSameDay(new Date(), userExtinfo.getLastBuyCopperTime())) {
			buyCoppertimes = userExtinfo.getBuyCopperTimes() + 1;
		}

		if (userExtinfo.getRewardVipLevel() > user.getVipLevel()) {
			userBO.setVipLevel(userExtinfo.getRewardVipLevel());
		}

		userBO.setBuyCopperInfo(CopperHelper.getCopperInfo(buyCoppertimes, user.getLevel()));

		return userBO;
	}

	/**
	 * 创建用户道具BO
	 * 
	 * @param userTool
	 * @return
	 */
	public static UserToolBO createUsetToolBO(UserTool userTool) {
		UserToolBO userToolBO = new UserToolBO();
		BeanUtils.copyProperties(userTool, userToolBO);
		return userToolBO;
	}

	/**
	 * 创建用户部队ID
	 * 
	 * @param userForces
	 * @return
	 */
	public static UserForcesBO createUserForcesBO(UserForces userForces) {
		UserForcesBO userForcesBO = new UserForcesBO();
		BeanUtils.copyProperties(userForces, userForcesBO);
		return userForcesBO;
	}

	/**
	 * 创建道具商城BO
	 * 
	 * @param systemMall
	 * @return
	 */
	public static SystemMallBO createSystemMallBO(SystemMall systemMall) {
		SystemMallBO systemMallBO = new SystemMallBO();
		BeanUtils.copyProperties(systemMall, systemMallBO);
		return systemMallBO;
	}

	/**
	 * 创建抽奖展示道具BO
	 * 
	 * @param systemActivityDrawShow
	 * @return
	 */
	public static SystemActivityDrawShowBO createSystemActivityDrawShowBO(SystemActivityDrawShow systemActivityDrawShow) {
		SystemActivityDrawShowBO systemActivityDrawShowBO = new SystemActivityDrawShowBO();
		BeanUtils.copyProperties(systemActivityDrawShow, systemActivityDrawShowBO);
		return systemActivityDrawShowBO;
	}

	/**
	 * 转成
	 * 
	 * @param systemActivity
	 * @return
	 */
	public static CopyActivityBO crateCopyActivityBO(SystemActivity systemActivity) {
		CopyActivityBO copyActivityBO = new CopyActivityBO();
		copyActivityBO.setActivityDesc(systemActivity.getActivityDesc());
		copyActivityBO.setActivityName(systemActivity.getActivityName());
		copyActivityBO.setActivityId(systemActivity.getActivityId());
		copyActivityBO.setStartTime(systemActivity.getStartTime().getTime());
		copyActivityBO.setEndTime(systemActivity.getEndTime().getTime());
		copyActivityBO.setSceneId(NumberUtils.toInt(systemActivity.getParam()));
		return copyActivityBO;
	}

	public static ContestTeamInfoBO createContestTeamInfoBO(ContestTeam team) {
		
		ContestTeamInfoBO teamBO = new ContestTeamInfoBO();
		
		teamBO.setCapacity(team.getCapacity());
		teamBO.setTeamId(team.getTeamId());
		teamBO.setPlayerNum(team.getPlayerNum());
		teamBO.setTeamName(team.getTeamName());
		
		return teamBO;
	}
}
