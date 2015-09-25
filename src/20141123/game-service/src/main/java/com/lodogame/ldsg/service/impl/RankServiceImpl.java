package com.lodogame.ldsg.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.RankLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.AttackRankBO;
import com.lodogame.ldsg.bo.DefenceRankBO;
import com.lodogame.ldsg.bo.ForcesRankBO;
import com.lodogame.ldsg.bo.HeroColorRankBO;
import com.lodogame.ldsg.bo.HpRankBO;
import com.lodogame.ldsg.bo.PowerRankBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.WealthRankBO;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.RankService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.RankLog;
import com.lodogame.model.SystemHero;
import com.lodogame.model.User;
import com.lodogame.model.UserForcesCount;

public class RankServiceImpl implements RankService {

	private static final String DATE_FMT = "yyyy-MM-dd";
	private final static Logger LOG = Logger.getLogger(RankServiceImpl.class);
	@Autowired
	private UserService userService;
	@Autowired
	private HeroService heroService;
	@Autowired
	private ForcesService forcesService;
	@Autowired
	private RankLogDao rankLogDao;

	@Override
	public List<PowerRankBO> getPowerRanks() {
		RankLog rankLog = rankLogDao.getRankLog(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), RedisKey.getPowerRankKey());
		if (rankLog == null) {
			return null;
		}
		List<PowerRankBO> list = Json.toList(rankLog.getRankData(), PowerRankBO.class);
		if (list.size() > 10) {
			return list.subList(0, 10);
		}

		return list;
	}

	@Override
	public List<WealthRankBO> getWealthRanks() {
		RankLog rankLog = rankLogDao.getRankLog(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), RedisKey.getWealthRankKey());
		if (rankLog == null) {
			return null;
		}
		return Json.toList(rankLog.getRankData(), WealthRankBO.class);
	}

	@Override
	public List<ForcesRankBO> getForcesRanks() {
		RankLog rankLog = rankLogDao.getRankLog(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), RedisKey.getForcesRankKey());
		if (rankLog == null) {
			return null;
		}
		return Json.toList(rankLog.getRankData(), ForcesRankBO.class);
	}

	@Override
	public List<AttackRankBO> getAttackRanks() {
		RankLog rankLog = rankLogDao.getRankLog(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), RedisKey.getAttackRankKey());
		if (rankLog == null) {
			return null;
		}
		return Json.toList(rankLog.getRankData(), AttackRankBO.class);
	}

	@Override
	public List<DefenceRankBO> getDefenceRanks() {
		RankLog rankLog = rankLogDao.getRankLog(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), RedisKey.getDefenceRankKey());
		if (rankLog == null) {
			return null;
		}
		return Json.toList(rankLog.getRankData(), DefenceRankBO.class);
	}

	@Override
	public List<HpRankBO> getHpRanks() {
		RankLog rankLog = rankLogDao.getRankLog(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), RedisKey.getHpRankKey());
		if (rankLog == null) {
			return null;
		}
		return Json.toList(rankLog.getRankData(), HpRankBO.class);
	}

	@Override
	public List<HeroColorRankBO> getHeroColorRanks() {
		RankLog rankLog = rankLogDao.getRankLog(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), RedisKey.getHeroColorRankKey());
		if (rankLog == null) {
			return null;
		}
		return Json.toList(rankLog.getRankData(), HeroColorRankBO.class);
	}

	@Override
	public long getRankStatTime() {
		String timeStr = JedisUtils.get(RedisKey.getRankStatTimeKey());
		long timestamp = System.currentTimeMillis();
		if (StringUtils.isBlank(timeStr)) {
			timestamp = DateUtils.getYesterday(new Date()).getTime();
		} else {
			try {
				timestamp = Long.parseLong(timeStr);
			} catch (Exception e) {
				timestamp = DateUtils.getYesterday(new Date()).getTime();
			}
		}
		return timestamp;
	}

	@Override
	public void rankStat() {
		// List<PowerRankBO> list = getPowerRanks();
		// if (list != null && list.size() > 0) {
		// return;
		// }
		long statTime = rankStatTime();

		String date = DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT);

		RankLog powerRank = this.rankLogDao.getRankLog(date, RedisKey.getPowerRankKey());
		if (powerRank == null || !StringUtils.equalsIgnoreCase(powerRank.getDate(), date)) {
			powerRankStat(statTime);
		}

		RankLog wealthRank = this.rankLogDao.getRankLog(date, RedisKey.getWealthRankKey());
		if (wealthRank == null || !StringUtils.equalsIgnoreCase(wealthRank.getDate(), date)) {
			wealthRankStat(statTime);
		}

		RankLog forcesRank = this.rankLogDao.getRankLog(date, RedisKey.getForcesRankKey());
		if (forcesRank == null || !StringUtils.equalsIgnoreCase(forcesRank.getDate(), date)) {
			forcesRankStat(statTime);
		}

		RankLog heroRank = this.rankLogDao.getRankLog(date, RedisKey.getHpRankKey());
		if (heroRank == null || !StringUtils.equalsIgnoreCase(heroRank.getDate(), date)) {
			heroRankStat(statTime);
		}

		RankLog qualityRank = this.rankLogDao.getRankLog(date, RedisKey.getHeroColorRankKey());
		if (qualityRank == null || !StringUtils.equalsIgnoreCase(qualityRank.getDate(), date)) {
			qualityRankStat(statTime);
		}

	}

	@Override
	public void wealthRankStat() {
		long statTime = rankStatTime();
		wealthRankStat(statTime);
	}

	private void qualityRankStat(long statTime) {
		List<UserHeroBO> userHeroBOList = heroService.listByHeroLevelDesc(0, 10);
		List<HeroColorRankBO> ranks = new ArrayList<HeroColorRankBO>();
		for (int i = 0; i < userHeroBOList.size(); i++) {
			UserHeroBO userHeroBO = userHeroBOList.get(i);
			HeroColorRankBO bo = new HeroColorRankBO();
			User user = userService.get(userHeroBO.getUserId());
			bo.setRank(i + 1);
			bo.setHeroName(userHeroBO.getName());
			bo.setHeroLevel(userHeroBO.getLevel());
			bo.setSystemHeroId(userHeroBO.getSystemHeroId());
			bo.setImgId(userHeroBO.getImgId());
			bo.setUsername(user.getUsername());
			bo.setAttackValue(userHeroBO.getPhysicalAttack());
			bo.setHp(userHeroBO.getLife());
			bo.setDefenceValue(userHeroBO.getPhysicalDefense());
			ranks.add(bo);
		}
		storeRank(RedisKey.getHeroColorRankKey(), Json.toJson(ranks), statTime);
	}

	/**
	 * 记录统计时间
	 */
	private long rankStatTime() {
		long timestamp = DateUtils.getYesterday(new Date()).getTime();
		JedisUtils.setString(RedisKey.getRankStatTimeKey(), Long.toString(timestamp));
		return timestamp;
	}

	/**
	 * 统计武将相关排名
	 */
	private void heroRankStat(long statTime) {
		List<UserHeroBO> userHeroBOList = heroService.listByHeroLevelDesc(0, 1000);

		List<AttackRankBO> attRanks = new ArrayList<AttackRankBO>();
		List<DefenceRankBO> defRanks = new ArrayList<DefenceRankBO>();
		List<HpRankBO> hpRanks = new ArrayList<HpRankBO>();
		List<AttackRankBO> attRankTmp = new ArrayList<AttackRankBO>();
		List<DefenceRankBO> defRankTmp = new ArrayList<DefenceRankBO>();
		List<HpRankBO> hpRankTmp = new ArrayList<HpRankBO>();

		for (UserHeroBO userHeroBO : userHeroBOList) {
			User user = userService.get(userHeroBO.getUserId());
			SystemHero sysHero = heroService.getSysHero(userHeroBO.getSystemHeroId());

			AttackRankBO attRankBO = packAttackRankBO(user, sysHero, userHeroBO);
			attRankTmp.add(attRankBO);
			DefenceRankBO defRankBO = packDefenceRankBO(user, sysHero, userHeroBO);
			defRankTmp.add(defRankBO);
			HpRankBO hpRankBO = packHpRankBO(user, sysHero, userHeroBO);
			hpRankTmp.add(hpRankBO);
		}
		Collections.sort(attRankTmp);
		Collections.sort(defRankTmp);
		Collections.sort(hpRankTmp);

		int len = attRankTmp.size();

		for (int i = len - 1; i >= len - 10; i--) {
			AttackRankBO bo = attRankTmp.get(i);
			bo.setRank(len - i);
			attRanks.add(bo);
		}

		len = defRankTmp.size();

		for (int i = len - 1; i >= len - 10; i--) {
			DefenceRankBO bo = defRankTmp.get(i);
			bo.setRank(len - i);
			defRanks.add(bo);
		}

		len = hpRankTmp.size();

		for (int i = len - 1; i >= len - 9; i--) {
			HpRankBO bo = hpRankTmp.get(i);
			bo.setRank(len - i);
			hpRanks.add(bo);
		}

		storeRank(RedisKey.getAttackRankKey(), Json.toJson(attRanks), statTime);
		storeRank(RedisKey.getDefenceRankKey(), Json.toJson(defRanks), statTime);
		storeRank(RedisKey.getHpRankKey(), Json.toJson(hpRanks), statTime);
	}

	private HpRankBO packHpRankBO(User user, SystemHero sysHero, UserHeroBO userHeroBO) {
		HpRankBO hpRankBO = new HpRankBO();
		hpRankBO.setHeroName(sysHero.getHeroName());
		hpRankBO.setImgId(sysHero.getImgId());
		hpRankBO.setUsername(user.getUsername());
		hpRankBO.setHeroLevel(userHeroBO.getLevel());
		hpRankBO.setSystemHeroId(userHeroBO.getSystemHeroId());
		hpRankBO.setAttackValue(userHeroBO.getPhysicalAttack());
		hpRankBO.setHp(userHeroBO.getLife());
		hpRankBO.setDefenceValue(userHeroBO.getPhysicalDefense());
		return hpRankBO;
	}

	private DefenceRankBO packDefenceRankBO(User user, SystemHero sysHero, UserHeroBO userHeroBO) {
		DefenceRankBO defRankBO = new DefenceRankBO();
		defRankBO.setHeroName(sysHero.getHeroName());
		defRankBO.setImgId(sysHero.getImgId());
		defRankBO.setUsername(user.getUsername());
		defRankBO.setHeroLevel(userHeroBO.getLevel());
		defRankBO.setSystemHeroId(userHeroBO.getSystemHeroId());
		defRankBO.setAttackValue(userHeroBO.getPhysicalAttack());
		defRankBO.setHp(userHeroBO.getLife());
		defRankBO.setDefenceValue(userHeroBO.getPhysicalDefense());
		return defRankBO;
	}

	private AttackRankBO packAttackRankBO(User user, SystemHero sysHero, UserHeroBO userHeroBO) {
		AttackRankBO attRankBO = new AttackRankBO();
		attRankBO.setHeroName(sysHero.getHeroName());
		attRankBO.setImgId(sysHero.getImgId());
		attRankBO.setUsername(user.getUsername());
		attRankBO.setHeroLevel(userHeroBO.getLevel());
		attRankBO.setSystemHeroId(userHeroBO.getSystemHeroId());
		attRankBO.setAttackValue(userHeroBO.getPhysicalAttack());
		attRankBO.setHp(userHeroBO.getLife());
		attRankBO.setDefenceValue(userHeroBO.getPhysicalDefense());
		return attRankBO;
	}

	/**
	 * 统计通关排名
	 */
	private void forcesRankStat(long statTime) {
		List<UserForcesCount> userForcesCounts = forcesService.listOrderByForceCntDesc(0, 10);
		List<ForcesRankBO> ranks = new ArrayList<ForcesRankBO>();
		for (int i = 0; i < userForcesCounts.size(); i++) {
			ForcesRankBO bo = new ForcesRankBO();
			UserForcesCount cnt = userForcesCounts.get(i);
			User user = userService.get(cnt.getUserId());
			bo.setUsername(user.getUsername());
			bo.setPassCount(cnt.getForcesCount());
			bo.setRank(i + 1);
			ranks.add(bo);
		}

		storeRank(RedisKey.getForcesRankKey(), Json.toJson(ranks), statTime);
	}

	/**
	 * 财富排行统计
	 */
	private void wealthRankStat(long statTime) {
		List<User> users = userService.listOrderByCopperDesc(0, 10);
		List<WealthRankBO> ranks = new ArrayList<WealthRankBO>();
		for (int i = 0; i < users.size(); i++) {
			WealthRankBO bo = new WealthRankBO();
			User user = users.get(i);
			bo.setRank(i + 1);
			bo.setCopper(user.getCopper());
			bo.setUsername(user.getUsername());
			ranks.add(bo);
		}
		storeRank(RedisKey.getWealthRankKey(), Json.toJson(ranks), statTime);
	}

	/**
	 * 统计战斗力
	 */
	private void powerRankStat(long statTime) {
		// 默认统计等级前1000名的用户
		List<UserBO> users = userService.listOrderByLevelDesc(0, 1000);
		List<PowerRankBO> ranks = new ArrayList<PowerRankBO>();
		List<PowerRankBO> tmpRanks = new ArrayList<PowerRankBO>();
		for (UserBO userBO : users) {
			List<UserHeroBO> userHeros = heroService.getUserHeroList(userBO.getUserId(), 1);
			PowerRankBO powerRankBO = new PowerRankBO();
			powerRankBO.setUsername(userBO.getUsername());
			int power = 0;
			for (UserHeroBO hero : userHeros) {
				power += hero.getPhysicalAttack() + hero.getPhysicalDefense() + hero.getLife();
			}
			powerRankBO.setPower(power);

			int fightCount = userBO.getWinCount() + userBO.getLoseCount();
			BigDecimal winPercent = new BigDecimal(0);
			if (fightCount != 0) {
				try {
					winPercent = new BigDecimal(userBO.getWinCount()).divide(new BigDecimal(fightCount), 2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100));
				} catch (Exception e) {
					LOG.info(e.getMessage(), e);
				}
			}
			DecimalFormat df = new java.text.DecimalFormat("0.00");
			powerRankBO.setWinPercent(Double.parseDouble(df.format(winPercent)));
			tmpRanks.add(powerRankBO);
		}
		Collections.sort(tmpRanks);

		int len = tmpRanks.size() > 100 ? 100 : tmpRanks.size();

		for (int i = tmpRanks.size() - 1, j = 1; i >= tmpRanks.size() - len; i--, j++) {
			PowerRankBO bo = tmpRanks.get(i);
			bo.setRank(j);
			ranks.add(bo);
		}

		String rankStr = Json.toJson(ranks);
		storeRank(RedisKey.getPowerRankKey(), rankStr, statTime);
	}

	private void storeRank(String rankKey, String rankStr, long statTime) {

		this.rankLogDao.delete(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT), rankKey);

		RankLog rankLog = new RankLog();
		rankLog.setDate(DateUtils.getDateStr(System.currentTimeMillis(), DATE_FMT));
		rankLog.setRankData(rankStr);
		rankLog.setRankKey(rankKey);
		rankLog.setCreateTime(new Date(statTime));
		rankLogDao.add(rankLog);
	}

	@Override
	public List<PowerRankBO> powerRankStat(long statTime, int limit) {
		// 默认统计等级前1000名的用户
		List<UserBO> users = userService.listOrderByLevelDesc(0, 1000);
		List<PowerRankBO> ranks = new ArrayList<PowerRankBO>();
		List<PowerRankBO> tmpRanks = new ArrayList<PowerRankBO>();
		for (UserBO userBO : users) {
			List<UserHeroBO> userHeros = heroService.getUserHeroList(userBO.getUserId(), 1);
			PowerRankBO powerRankBO = new PowerRankBO();
			powerRankBO.setUsername(userBO.getUsername());
			powerRankBO.setPlayerId(userBO.getPlayerId());
			int power = 0;
			for (UserHeroBO hero : userHeros) {
				power += hero.getPhysicalAttack() + hero.getPhysicalDefense() + hero.getLife();
			}
			powerRankBO.setPower(power);

			int fightCount = userBO.getWinCount() + userBO.getLoseCount();
			BigDecimal winPercent = new BigDecimal(0);
			if (fightCount != 0) {
				try {
					winPercent = new BigDecimal(userBO.getWinCount()).divide(new BigDecimal(fightCount), 2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100));
				} catch (Exception e) {
					LOG.info(e.getMessage(), e);
				}
			}
			DecimalFormat df = new java.text.DecimalFormat("0.00");
			powerRankBO.setWinPercent(Double.parseDouble(df.format(winPercent)));
			tmpRanks.add(powerRankBO);
		}
		Collections.sort(tmpRanks);

		// int len = tmpRanks.size() > 10 ? 9 : tmpRanks.size() - 1;
		//
		// for(int i = len; i >= 0; i--){
		// PowerRankBO bo = tmpRanks.get(i);
		// bo.setRank(i + 1);
		// ranks.add(bo);
		// }
		int len = tmpRanks.size() > limit ? limit : tmpRanks.size();

		for (int i = tmpRanks.size() - 1, j = 1; i >= tmpRanks.size() - len; i--, j++) {
			PowerRankBO bo = tmpRanks.get(i);
			bo.setRank(j);
			ranks.add(bo);
		}
		// int len = tmpRanks.size() > 10 ? 10 : tmpRanks.size();
		// for (int i = 0; i < len; i++) {
		// PowerRankBO bo = tmpRanks.get(i);
		// bo.setRank(i + 1);
		// ranks.add(bo);
		// }
		return ranks;
	}

	public static void main(String[] args) {

		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			list.add(i);
		}

		System.out.println(list.subList(0, 9).size());

	}

}
