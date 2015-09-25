package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.lodogame.game.dao.ActivityDrawDao;
import com.lodogame.game.dao.impl.mysql.ActivityDrawDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.ActivityDrawTool;
import com.lodogame.model.SystemActivityDrawShow;
import com.lodogame.model.UserDrawTime;

/**
 * 活动抽奖缓存实现类
 * 
 * @author Candon
 * 
 */
public class ActivityDrawDaoCacheImpl implements ActivityDrawDao, ReloadAble {

	private ActivityDrawDaoMysqlImpl activityDrawDaoMysqlImpl;

	// 活动抽奖上限值Map
	private Map<Integer, Integer> toolUpper = new ConcurrentHashMap<Integer, Integer>();

	// 活动抽奖修正Map
	private Map<Integer, Integer> reviseMap = new ConcurrentHashMap<Integer, Integer>();

	// 抽奖概率Map
	private Map<Integer, List<ActivityDrawTool>> activityDrawToolMap = new ConcurrentHashMap<Integer, List<ActivityDrawTool>>();

	private List<ActivityDrawTool> thirtyActivityDrawToolList = new ArrayList<ActivityDrawTool>();

	private List<ActivityDrawTool> limitOverDropList = new ArrayList<ActivityDrawTool>();

	public void setActivityDrawDaoMysqlImpl(ActivityDrawDaoMysqlImpl activityDrawDaoMysqlImpl) {
		this.activityDrawDaoMysqlImpl = activityDrawDaoMysqlImpl;
	}

	@Override
	public int getUserDrawScore(String userId) {
		return this.activityDrawDaoMysqlImpl.getUserDrawScore(userId);
	}

	@Override
	public int getDrawToolUpper(int outID) {

		if (toolUpper.containsKey(outID)) {
			return toolUpper.get(outID);
		}
		int num = this.activityDrawDaoMysqlImpl.getDrawToolUpper(outID);
		toolUpper.put(outID, num);

		return num;
	}

	@Override
	public void updateUserDrawTime(String userId, String username, int time, int score) {
		this.activityDrawDaoMysqlImpl.updateUserDrawTime(userId, username, time, score);
	}

	@Override
	public UserDrawTime getActivityDrawUserData(String userId) {

		return this.activityDrawDaoMysqlImpl.getActivityDrawUserData(userId);
	}

	@Override
	public List<UserDrawTime> getActivityDrawScoreRank() {

		return this.activityDrawDaoMysqlImpl.getActivityDrawScoreRank();
	}

	@Override
	public boolean addActivityDrowToolNumRecorde(int num, int outId) {

		return this.activityDrawDaoMysqlImpl.addActivityDrowToolNumRecorde(num, outId);
	}

	@Override
	public int getTodayDrowToolNum(int outId) {

		return this.activityDrawDaoMysqlImpl.getTodayDrowToolNum(outId);
	}

	@Override
	public List<ActivityDrawTool> getNormalDropList(int type) {
		if (activityDrawToolMap.containsKey(type)) {
			return activityDrawToolMap.get(type);
		} else {
			List<ActivityDrawTool> activityDrawToolList = activityDrawDaoMysqlImpl.getNormalDropList(type);
			activityDrawToolMap.put(type, activityDrawToolList);
			return activityDrawToolList;
		}
	}

	@Override
	public List<ActivityDrawTool> getLimitOverDropList() {

		if (!limitOverDropList.isEmpty()) {
			return limitOverDropList;
		} else {
			limitOverDropList = this.activityDrawDaoMysqlImpl.getLimitOverDropList();
		}
		return limitOverDropList;
	}

	@Override
	public List<ActivityDrawTool> getThirtyDropList() {

		if (!thirtyActivityDrawToolList.isEmpty()) {
			return thirtyActivityDrawToolList;
		} else {
			thirtyActivityDrawToolList = this.activityDrawDaoMysqlImpl.getThirtyDropList();
		}
		return thirtyActivityDrawToolList;
	}

	@Override
	public void updateUserGainTotal(String userId, int outId, int num) {
		this.activityDrawDaoMysqlImpl.updateUserGainTotal(userId, outId, num);
	}

	@Override
	public int getUserGainTotal(String userId, int outId) {
		return this.activityDrawDaoMysqlImpl.getUserGainTotal(userId, outId);
	}

	@Override
	public int getAllUserGainTotal(int outId) {
		return this.activityDrawDaoMysqlImpl.getAllUserGainTotal(outId);
	}

	@Override
	public int getOutId(int time) {

		if (reviseMap.containsKey(time)) {
			return reviseMap.get(time);
		}
		int outId = this.activityDrawDaoMysqlImpl.getOutId(time);
		reviseMap.put(time, outId);

		return outId;
	}

	@Override
	public ActivityDrawTool getActivityDrawTool(int outId, int type) {
		if (activityDrawToolMap.containsKey(type)) {
			List<ActivityDrawTool> activityDrawToolList = this.activityDrawToolMap.get(type);
			for (ActivityDrawTool activityDrawTool : activityDrawToolList) {
				if (activityDrawTool.getOutId() == outId) {
					return activityDrawTool;
				}
			}
		} else {
			List<ActivityDrawTool> activityDrawToolList = this.activityDrawDaoMysqlImpl.getNormalDropList(type);
			activityDrawToolMap.put(type, activityDrawToolList);
			for (ActivityDrawTool activityDrawTool : activityDrawToolList) {
				if (activityDrawTool.getOutId() == outId) {
					return activityDrawTool;
				}
			}
		}
		return null;
	}

	@Override
	public void reload() {

		toolUpper.clear();
		reviseMap.clear();
		activityDrawToolMap.clear();
		thirtyActivityDrawToolList.clear();
		limitOverDropList.clear();

	}

	@Override
	public List<SystemActivityDrawShow> getActivityDrawShowList() {
		return this.activityDrawDaoMysqlImpl.getActivityDrawShowList();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
