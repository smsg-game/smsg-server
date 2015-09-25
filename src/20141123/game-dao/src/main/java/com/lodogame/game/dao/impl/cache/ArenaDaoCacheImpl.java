package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.ArenaDao;
import com.lodogame.model.ArenaHero;
import com.lodogame.model.ArenaRank;
import com.lodogame.model.ArenaReg;

public class ArenaDaoCacheImpl implements ArenaDao {

	/**
	 * 组
	 */
	private List<Integer> winCountList = new ArrayList<Integer>();

	/**
	 * 用户连胜次数映射
	 */
	private Map<String, ArenaReg> regMap = new ConcurrentHashMap<String, ArenaReg>();

	/**
	 * 排行榜
	 */
	private Map<String, ArenaRank> rankMap = new ConcurrentHashMap<String, ArenaRank>();

	/**
	 * 用户武将信息
	 */
	private Map<String, Map<String, ArenaHero>> arenaHeroMap = new ConcurrentHashMap<String, Map<String, ArenaHero>>();

	private Object winCountLock = new Object();

	@Override
	public List<Integer> getWinCountList() {

		List<Integer> list = new ArrayList<Integer>();

		synchronized (winCountLock) {

			list.addAll(winCountList);
			return list;

		}
	}

	@Override
	public boolean add(ArenaReg arenaReg) {

		String userId = arenaReg.getUserId();

		int winCount = arenaReg.getWinCount();

		synchronized (winCountLock) {

			if (!winCountList.contains(winCount)) {
				winCountList.add(winCount);
			}
		}

		int maxWinCount = arenaReg.getMaxWinCount();

		regMap.put(userId, arenaReg);

		return this.handleRank(userId, maxWinCount);

	}

	/**
	 * 处理排名
	 */
	private boolean handleRank(String userId, int maxWinCount) {

		boolean update = false;

		if (rankMap.size() < 5 || rankMap.containsKey(userId)) {// 当前排行榜小于3个人，或者用户本身就在排行榜中

			ArenaRank arenaRank;

			if (rankMap.containsKey(userId)) {
				arenaRank = rankMap.get(userId);
				if (arenaRank.getMaxWinCount() < maxWinCount) {
					arenaRank.setMaxWinCount(maxWinCount);
					arenaRank.setTimestamp(System.currentTimeMillis());
				}
			} else {
				arenaRank = new ArenaRank();
				arenaRank.setMaxWinCount(maxWinCount);
				arenaRank.setUserId(userId);
				arenaRank.setTimestamp(System.currentTimeMillis());
			}

			rankMap.put(userId, arenaRank);
			update = true;

		} else {

			int lowerWinCount = Integer.MAX_VALUE;
			String targetUserId = null;
			for (Entry<String, ArenaRank> entry : rankMap.entrySet()) {
				ArenaRank arenaRank = entry.getValue();
				int wc = arenaRank.getMaxWinCount();
				if (lowerWinCount > wc) {
					lowerWinCount = wc;
					targetUserId = entry.getKey();
				}
			}

			if (maxWinCount > lowerWinCount) {
				rankMap.remove(targetUserId);
				ArenaRank arenaRank = new ArenaRank();
				arenaRank.setMaxWinCount(maxWinCount);
				arenaRank.setTimestamp(System.currentTimeMillis());
				arenaRank.setUserId(userId);
				rankMap.put(userId, arenaRank);
				update = true;
			}
		}

		return update;
	}

	@Override
	public List<ArenaReg> getByWinCount(int winCount, int status) {

		int winCountGroupSize = 0;

		List<ArenaReg> arenaRegList = new ArrayList<ArenaReg>();
		for (Entry<String, ArenaReg> entry : regMap.entrySet()) {
			ArenaReg arenaReg = entry.getValue();
			if (arenaReg.getWinCount() == winCount) {
				if (arenaReg.getStatus() == status) {
					arenaRegList.add(arenaReg);
				}
				winCountGroupSize += 1;
			}
		}

		synchronized (winCountLock) {
			if (winCountGroupSize == 0 && this.winCountList.contains(winCount)) {
				this.winCountList.remove((Object) winCount);
			}
		}

		return arenaRegList;
	}

	@Override
	public ArenaReg getByUserId(String userId) {

		if (regMap.containsKey(userId)) {
			return regMap.get(userId);
		}

		return null;
	}

	@Override
	public List<ArenaReg> getRankList() {

		List<ArenaReg> arenaRankList = new ArrayList<ArenaReg>();

		List<ArenaRank> rankList = new ArrayList<ArenaRank>();
		rankList.addAll(this.rankMap.values());

		Collections.sort(rankList, new Comparator<ArenaRank>() {

			@Override
			public int compare(ArenaRank o1, ArenaRank o2) {

				if (o1.getMaxWinCount() > o2.getMaxWinCount()) {
					return -1;
				} else if (o1.getMaxWinCount() < o2.getMaxWinCount()) {
					return 1;
				} else {
					if (o1.getTimestamp() < o2.getTimestamp()) {
						return -1;
					} else {
						return 1;
					}
				}

			}

		});

		for (ArenaRank arenaRank : rankList) {

			ArenaReg arenaReg = this.getByUserId(arenaRank.getUserId());
			if (arenaReg != null) {
				arenaRankList.add(arenaReg);
			}

			if (arenaRankList.size() >= 3) {
				break;
			}
		}

		return arenaRankList;
	}

	@Override
	public void cleanData() {
		this.winCountList.clear();
		this.regMap.clear();
		this.rankMap.clear();
		this.arenaHeroMap.clear();
	}

	@Override
	public ArenaHero getArenaHero(String userId, String userHeroId) {

		Map<String, ArenaHero> m = this.getUserArenaHero(userId);

		if (m != null && m.containsKey(userHeroId)) {
			return m.get(userHeroId);
		}

		return null;
	}

	@Override
	public boolean addArenaHero(String userId, String userHeroId, ArenaHero arenaHero) {

		Map<String, ArenaHero> m = this.getUserArenaHero(userId);

		if (m == null) {
			m = new HashMap<String, ArenaHero>();
		}

		m.put(userHeroId, arenaHero);

		this.arenaHeroMap.put(userId, m);

		return true;

	}

	@Override
	public int getRank(String userId) {

		if (!this.rankMap.containsKey(userId)) {
			return -1;
		} else {
			int rank = 1;

			ArenaRank arenaRank = this.rankMap.get(userId);
			int maxWinCount = arenaRank.getMaxWinCount();
			long timestamp = arenaRank.getTimestamp();

			for (Entry<String, ArenaRank> entry : this.rankMap.entrySet()) {

				String uid = entry.getKey();
				ArenaRank arenaRank2 = entry.getValue();
				if (userId.equals(uid)) {
					continue;
				}

				if (arenaRank2.getMaxWinCount() > maxWinCount) {
					rank += 1;
				} else if (arenaRank2.getMaxWinCount() == maxWinCount && arenaRank2.getTimestamp() < timestamp) {
					rank += 1;
				}

			}

			return rank;
		}
	}

	@Override
	public Collection<ArenaReg> getArenaRegList() {
		return this.regMap.values();
	}

	@Override
	public Map<String, ArenaHero> getUserArenaHero(String userId) {

		if (arenaHeroMap.containsKey(userId)) {
			return arenaHeroMap.get(userId);
		}

		return null;
	}

	@Override
	public boolean resetUserHero(String userId) {
		this.arenaHeroMap.remove(userId);
		return true;
	}

	@Override
	public int getRegCount() {
		return this.arenaHeroMap.size();
	}

}
