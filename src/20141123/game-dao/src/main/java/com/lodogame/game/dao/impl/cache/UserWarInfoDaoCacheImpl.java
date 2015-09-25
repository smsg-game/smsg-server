package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserWarInfoDao;
import com.lodogame.game.dao.impl.mysql.UserWarInfoDaoMysqlImpl;
import com.lodogame.model.UserWarInfo;
import com.lodogame.model.WarAttackRank;
import com.lodogame.model.WarAttackRankReward;
import com.lodogame.model.WarCity;

public class UserWarInfoDaoCacheImpl implements UserWarInfoDao {

	private Map<String, UserWarInfo> userWarInfoCache = new ConcurrentHashMap<String, UserWarInfo>();

	private UserWarInfoDaoMysqlImpl userWarInfoDaoMysqlImpl;

	public void setUserWarInfoDaoMysqlImpl(UserWarInfoDaoMysqlImpl userWarInfoDaoMysqlImpl) {
		this.userWarInfoDaoMysqlImpl = userWarInfoDaoMysqlImpl;
	}

	@Override
	public UserWarInfo getUserWarInfo(String userId) {
		if (userWarInfoCache.containsKey(userId)) {
			return userWarInfoCache.get(userId);
		}
		UserWarInfo userWarInfo = this.userWarInfoDaoMysqlImpl.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfoCache.put(userId, userWarInfo);
			return userWarInfo;
		} else {
			return null;
		}

	}

	@Override
	public Collection<String> getWarUserIdList() {
		return this.userWarInfoCache.keySet();
	}

	@Override
	public List<WarCity> getCityStatus() {

		Map<Integer, Integer> countryIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> pointMap = new HashMap<Integer, Integer>();

		for (Entry<String, UserWarInfo> entry : this.userWarInfoCache.entrySet()) {
			UserWarInfo userWarInfo = entry.getValue();
			int point = userWarInfo.getPoint();
			int countryId = userWarInfo.getCountryId();
			int count = 0;
			if (pointMap.containsKey(point)) {
				count = pointMap.get(point);
			}

			count += 1;
			pointMap.put(point, count);
			if (!countryIdMap.containsKey(point)) {
				countryIdMap.put(point, countryId);
			}
		}

		List<WarCity> cityList = new ArrayList<WarCity>();
		for (Entry<Integer, Integer> entry : pointMap.entrySet()) {
			WarCity warCity = new WarCity();
			warCity.setNum(entry.getValue());
			warCity.setPoint(String.valueOf(entry.getKey()));
			warCity.setCountryId(countryIdMap.get(entry.getKey()));
			cityList.add(warCity);
		}

		return cityList;
	}

	@Override
	public List<WarCity> getCountryPeopleNum() {

		Map<Integer, Integer> countryIdMap = new HashMap<Integer, Integer>();
		countryIdMap.put(0, 0);
		countryIdMap.put(1, 0);
		countryIdMap.put(2, 0);
		countryIdMap.put(3, 0);

		for (Entry<String, UserWarInfo> entry : this.userWarInfoCache.entrySet()) {
			UserWarInfo userWarInfo = entry.getValue();
			int countryId = userWarInfo.getCountryId();
			int count = 0;
			if (countryIdMap.containsKey(countryId)) {
				count = countryIdMap.get(countryId);
			}

			count += 1;
			countryIdMap.put(countryId, count);

		}

		List<WarCity> cityList = new ArrayList<WarCity>();
		for (Entry<Integer, Integer> entry : countryIdMap.entrySet()) {
			WarCity warCity = new WarCity();
			warCity.setNum(entry.getValue());
			warCity.setCountryId(entry.getKey());
			cityList.add(warCity);
		}

		return cityList;
	}

	@Override
	public void setDrawTime(String userId, Date time) {
		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setDrawTime(time);
		}

	}

	@Override
	public int getCityPeopleNum(Integer point) {
		int peopleNum = 0;
		for (Entry<String, UserWarInfo> entry : this.userWarInfoCache.entrySet()) {
			if (entry.getValue().getPoint() == point) {
				peopleNum += 1;
			}
		}
		return peopleNum;
	}

	@Override
	public void clearActionCD(String userId, Date time) {
		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setActionTime(time);
		}
	}

	@Override
	public void clearLiftCD(String userId, Date time) {
		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setLiftTime(time);
		}

	}

	@Override
	public void inspire(String userId, Date time) {

		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setInspireNum(userWarInfo.getInspireNum() + 1);
			userWarInfo.setInspireTime(time);
		}

	}

	@Override
	public void add(UserWarInfo userWarInfo) {
		if (!this.userWarInfoCache.containsKey(userWarInfo.getUserId())) {
			this.userWarInfoCache.put(userWarInfo.getUserId(), userWarInfo);
		}
	}

	@Override
	public void setUserCity(String userId, Integer point) {

		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setPoint(point);
		}

	}

	@Override
	public List<UserWarInfo> getAllUserByCityId(Integer cityId) {
		List<UserWarInfo> userWarInfoList = new ArrayList<UserWarInfo>();
		for (Entry<String, UserWarInfo> entry : this.userWarInfoCache.entrySet()) {
			if (entry.getValue().getPoint() == cityId) {
				userWarInfoList.add(entry.getValue());
			}
		}
		return userWarInfoList;
	}

	@Override
	public void setLifeTime(String userId, Date date) {

		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setLiftTime(date);
		}

	}

	@Override
	public void setActionTime(String userId, Date date) {

		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setActionTime(date);
		}

	}

	@Override
	public void setDefenseNum(String userId) {

		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setDefenseNum(userWarInfo.getDefenseNum() + 1);
		}

	}

	@Override
	public void setAttackNum(String userId) {

		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setAttackNum(userWarInfo.getAttackNum() + 1);
			userWarInfo.setTotalAttackNum(userWarInfo.getTotalAttackNum() + 1);
		}

	}

	@Override
	public void addWarLog(String date) {
		this.userWarInfoDaoMysqlImpl.addWarLog(date);

	}

	@Override
	public void backUpRankData() {
		this.userWarInfoDaoMysqlImpl.backUpRankData();
	}

	@Override
	public void saveRankData() {

		for (Entry<String, UserWarInfo> entry : this.userWarInfoCache.entrySet()) {

			WarAttackRank warAttackRank = new WarAttackRank();
			UserWarInfo userWarInfo = entry.getValue();
			warAttackRank.setUserId(userWarInfo.getUserId());
			warAttackRank.setUsername(userWarInfo.getUserName());
			warAttackRank.setSystemHeroId(userWarInfo.getSystemHeroId());
			warAttackRank.setAttackNum(userWarInfo.getTotalAttackNum());
			warAttackRank.setVipLevel(userWarInfo.getVipLevel());
			warAttackRank.setLevel(userWarInfo.getLevel());

			this.userWarInfoDaoMysqlImpl.addAttackRank(warAttackRank);
		}
	}

	@Override
	public void addAttackRank(WarAttackRank warAttackRank) {
		this.userWarInfoDaoMysqlImpl.addAttackRank(warAttackRank);
	}

	@Override
	public List<WarAttackRank> getWarAttackRankList() {
		return this.userWarInfoDaoMysqlImpl.getWarAttackRankList();
	}

	@Override
	public boolean isWarLog(String date) {

		return this.userWarInfoDaoMysqlImpl.isWarLog(date);
	}

	@Override
	public void cleanData() {
		this.userWarInfoCache.clear();
		this.userWarInfoDaoMysqlImpl.cleanData();
	}

	@Override
	public void clearDefenseNum(String userId) {
		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setDefenseNum(0);
		}

	}

	@Override
	public void setCopperClearTime(String userId, int num) {

		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setClearActionCopper(num);
		}

	}

	@Override
	public boolean updateWarRankAttackStatus(String userId, int status) {
		return this.userWarInfoDaoMysqlImpl.updateWarRankAttackStatus(userId, status);
	}

	@Override
	public WarAttackRankReward getAttackRankReward(int rank) {
		return this.userWarInfoDaoMysqlImpl.getAttackRankReward(rank);
	}

	@Override
	public boolean updateWarRankAttackStatus() {
		return this.userWarInfoDaoMysqlImpl.updateWarRankAttackStatus();
	}

	@Override
	public void backupData() {

		for (Entry<String, UserWarInfo> entry : this.userWarInfoCache.entrySet()) {
			this.userWarInfoDaoMysqlImpl.add(entry.getValue());
		}
	}

	@Override
	public void setPower(String userId, int power) {
		UserWarInfo userWarInfo = this.getUserWarInfo(userId);
		if (userWarInfo != null) {
			userWarInfo.setAbility(power);
		}

	}

}
