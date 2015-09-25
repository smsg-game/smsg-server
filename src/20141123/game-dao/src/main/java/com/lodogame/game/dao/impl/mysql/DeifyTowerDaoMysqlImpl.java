package com.lodogame.game.dao.impl.mysql;

import com.lodogame.game.dao.DeifyTowerDao;

public class DeifyTowerDaoMysqlImpl implements DeifyTowerDao {

	// private final static String table = "deify_tower_info";
	//
	// @Autowired
	// private Jdbc jdbc;

	// @Override
	// public List<DeifyTowerInfo> getAllTowerInfoList() {
	// throw new NotImplementedException();
	// // String sql = "SELECT * FROM " + table;
	// // return this.jdbc.getList(sql, DeifyTowerInfo.class);
	// }
	//
	// @Override
	// public boolean add(DeifyTowerInfo towerInfo) {
	// return this.jdbc.insert(towerInfo) > 0;
	// }
	//
	// @Override
	// public boolean addOccupiedRoomNum(int towerId) {
	// String sql = "UPDATE " + table +
	// " SET occupied_room_num = occupied_room_num + 1 WHERE tower_id = ?";
	// SqlParameter param = new SqlParameter();
	// param.setInt(towerId);
	// return this.jdbc.update(sql, param) > 0;
	//
	// }
	//
	// @Override
	// public boolean reduceOccupiedRoomNum(int towerId) {
	// String sql = "UPDATE " + table +
	// " SET occupied_room_num = occupied_room_num - 1 WHERE tower_id = ? AND occupied_room_num >= 1";
	// SqlParameter param = new SqlParameter();
	// param.setInt(towerId);
	// return this.jdbc.update(sql, param) > 0;
	//
	// }
	//
	// @Override
	// public DeifyTowerInfo getTower(int towerId) {
	// String sql = "SELECT * FROM " + table + " WHERE tower_id = ?";
	// SqlParameter param = new SqlParameter();
	// param.setInt(towerId);
	// return this.jdbc.get(sql, DeifyTowerInfo.class, param);
	// }

}
