package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTowerDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserTower;

public class UserTowerDaoMysqlImpl implements UserTowerDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<UserTower> getUserTower(String uid) {
		String table = TableUtils.getUserTowerTable(uid);
		String sql = "select * from " + table + " where user_id = ?";
		SqlParameter params = new SqlParameter();
		params.setString(uid);
		return jdbc.getList(sql, UserTower.class, params);
	}

	@Override
	public boolean resetTowerStage(String uid) {
		String table = TableUtils.getUserTowerTable(uid);
		String sql = "update " + table + " set times = 0, status = 1, floor = (stage - 1) * 5 + 1, created_time = now() where user_id = ?";
		SqlParameter params = new SqlParameter();
		params.setString(uid);
		jdbc.update(sql, params);
		return true;
	}
	
	@Override
	public UserTower newStage(String uid, int stage, int floor) {
		String table = TableUtils.getUserTowerTable(uid);
		String sql = "insert into " + table + " (user_id, stage, floor, status, times, created_time, updated_time) value (?,?,?,?,?,?,?) on duplicate key update updated_time = now();";
		UserTower userTower = new UserTower();
		Date now = new Date();
		userTower.setFloor(floor);
		userTower.setStage(stage);
		userTower.setStatus(0);
		userTower.setTimes(0);
		userTower.setUserId(uid);
		userTower.setCreatedTime(now);
		userTower.setUpdatedTime(now);

		SqlParameter params = new SqlParameter();
		params.setString(userTower.getUserId());
		params.setInt(userTower.getStage());
		params.setInt(userTower.getFloor());
		params.setInt(userTower.getStatus());
		params.setInt(userTower.getTimes());
		params.setObject(userTower.getCreatedTime());
		params.setObject(userTower.getUpdatedTime());
		jdbc.update(sql, params);
		return userTower;
	}

	@Override
	public boolean updateTowerStatus(String uid, int stage, int status, int times) {
		String table = TableUtils.getUserTowerTable(uid);
		String sql = "update " + table + " set times = times + ?, status = ? where user_id = ? and stage = ?";
		SqlParameter params = new SqlParameter();
		params.setInt(times);
		params.setInt(status);
		params.setString(uid);
		params.setInt(stage);
		return jdbc.update(sql, params) > 0;
	}

	@Override
	public boolean updateStageFloor(String uid, int curStage, int floor) {
		String table = TableUtils.getUserTowerTable(uid);
		String sql = "update " + table + " set floor = ? where user_id = ? and stage = ?";
		SqlParameter params = new SqlParameter();
		params.setInt(floor);
		params.setString(uid);
		params.setInt(curStage);
		return jdbc.update(sql, params) > 0;
	}

}
