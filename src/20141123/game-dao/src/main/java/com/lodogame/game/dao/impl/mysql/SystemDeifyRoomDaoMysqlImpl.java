package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.SystemDeifyRoomDao;
import com.lodogame.model.SystemDeifyRoom;

public class SystemDeifyRoomDaoMysqlImpl implements SystemDeifyRoomDao{
	
	private static final String table = "system_deify_room";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<SystemDeifyRoom> getAllRoom() {
		String sql = "SELECT * FROM " + table;
		return this.jdbc.getList(sql, SystemDeifyRoom.class);
	}

}
