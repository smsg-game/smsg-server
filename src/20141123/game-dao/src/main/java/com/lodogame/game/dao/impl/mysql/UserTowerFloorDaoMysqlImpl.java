package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserTowerFloorDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserTowerFloor;

public class UserTowerFloorDaoMysqlImpl implements UserTowerFloorDao {

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<UserTowerFloor> getUserTowerFloor(String uid, int floor) {
		String table = TableUtils.getUserTowerFloorTable(uid);
		String sql = "select * from " + table + " where user_id = ? and floor = ?";
		SqlParameter params = new SqlParameter();
		params.setString(uid);
		params.setInt(floor);
		return jdbc.getList(sql, UserTowerFloor.class, params);
	}

	@Override
	public boolean clearTowerFloorData(String uid) {
		String table = TableUtils.getUserTowerFloorTable(uid);
		String sql = "delete from " + table + " where user_id = ?";
		SqlParameter params = new SqlParameter();
		params.setString(uid);
		jdbc.update(sql, params);
		return true;
	}

	@Override
	public UserTowerFloor getUserTowerFloor(String uid, int floor, int pos, int tid) {
		String table = TableUtils.getUserTowerFloorTable(uid);
		String sql = "select * from " + table + " where user_id = ? and floor = ? and pos = ? and objId = ?";
		SqlParameter params = new SqlParameter();
		params.setString(uid);
		params.setInt(floor);
		params.setInt(pos);
		params.setInt(tid);
		return jdbc.get(sql, UserTowerFloor.class, params);
	}

	@Override
	public boolean saveUserTowerFloor(UserTowerFloor userTowerFloor) {
		String table = TableUtils.getUserTowerFloorTable(userTowerFloor.getUserId());
		String sql = "insert into " + table + " (user_id, map_id, floor, pos, obj_type, obj_id, passed, created_time, updated_time) value (?,?,?,?,?,?,?,?,?)";
		SqlParameter params = new SqlParameter();
		params.setString(userTowerFloor.getUserId());
		params.setInt(userTowerFloor.getMapId());
		params.setInt(userTowerFloor.getFloor());
		params.setInt(userTowerFloor.getPos());
		params.setInt(userTowerFloor.getObjType());
		params.setInt(userTowerFloor.getObjId());
		params.setInt(userTowerFloor.getPassed());
		params.setObject(userTowerFloor.getCreatedTime());
		params.setObject(userTowerFloor.getUpdatedTime());
		return jdbc.update(sql, params) > 0;
	}

	@Override
	public boolean updateObjStatus(String uid, int floor, int pos, int isPassed) {
		String table = TableUtils.getUserTowerFloorTable(uid);
		String sql = "update " + table + " set passed = ? where user_id = ? and floor = ? and pos = ?";
		SqlParameter params = new SqlParameter();
		params.setInt(isPassed);
		params.setString(uid);
		params.setInt(floor);
		params.setInt(pos);
		return jdbc.update(sql, params) > 0;
	}

}
