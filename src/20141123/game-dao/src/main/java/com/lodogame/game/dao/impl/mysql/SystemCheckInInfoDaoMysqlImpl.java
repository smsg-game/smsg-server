package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemCheckInInfoDao;
import com.lodogame.model.SystemCheckInInfo;

public class SystemCheckInInfoDaoMysqlImpl implements SystemCheckInInfoDao {

	private String table = "system_checkin_info";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemCheckInInfo getSystemCheckInInfo() {

		String sql = "SELECT * FROM " + table + " LIMIT 1";

		SqlParameter parameter = new SqlParameter();

		return this.jdbc.get(sql, SystemCheckInInfo.class, parameter);
	}

	@Override
	public boolean setSystemCheckInInfo(int groupId, Date finishTime) {

		String sql = " INSERT INTO " + table + "(system_checkin_info_id, group_id, finish_time) VALUES(1, ?, ?) ";
		sql += "ON DUPLICATE KEY UPDATE group_id = VALUES(group_id), finish_time = VALUES(finish_time)";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(groupId);
		parameter.setObject(finishTime);

		return this.jdbc.update(sql, parameter) > 0;

	}
}
