package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.DeifyRoomDao;
import com.lodogame.model.DeifyRoomInfo;

public class DeifyRoomDaoMysqlImpl implements DeifyRoomDao {

	private static final String table = "deify_room_info";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<DeifyRoomInfo> getRoomListByTowerId(int towerId) {
		throw new NotImplementedException();
	}

	@Override
	public List<DeifyRoomInfo> getAllRoomInfoList() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, DeifyRoomInfo.class);
	}

	@Override
	public boolean add(DeifyRoomInfo roomInfo) {
		return this.jdbc.insert(roomInfo) > 0;
	}

	@Override
	public DeifyRoomInfo getRoom(int towerId, int roomId) {
		throw new NotImplementedException();
	}

	@Override
	public DeifyRoomInfo getRoomByUid(String uid) {
		throw new NotImplementedException();
	}

	@Override
	public boolean cleanRoom() {

		String select = "select * from " + table + " where tower_id = 4 LIMIT 1";
		DeifyRoomInfo room = this.jdbc.get(select, DeifyRoomInfo.class, null);
		
		
		if (room == null) {
			return true;
		}
		String sql = "DELETE FROM " + table + " WHERE tower_id = 4 AND user_id is null AND id <> ? ";
		SqlParameter param = new SqlParameter();
		param.setString(room.getId());

		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public boolean update(DeifyRoomInfo room) {

		String sql = "UPDATE " + table + " SET deify_start_time = ?, deify_end_time = ?, protect_start_time = ?, protect_end_time = ?, user_id = ? , double_profit_start_time = ? ";
		sql += ", double_profit_end_time = ?  where id = ? ";
		SqlParameter param = new SqlParameter();
		param.setObject(room.getDeifyStartTime());
		param.setObject(room.getDeifyEndTime());
		param.setObject(room.getProtectStartTime());
		param.setObject(room.getProtectEndTime());
		param.setObject(room.getUserId());
		param.setObject(room.getDoubleProfitStartTime());
		param.setObject(room.getDoubleProfitEndTime());
		param.setString(room.getId());

		return this.jdbc.update(sql, param) > 0;
	}

}
