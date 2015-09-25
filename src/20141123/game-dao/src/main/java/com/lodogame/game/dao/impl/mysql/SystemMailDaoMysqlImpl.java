package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.SystemMail;

public class SystemMailDaoMysqlImpl implements SystemMailDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<SystemMail> getSystemMailByTime(Date date) {

		String sql = "SELECT * FROM system_mail WHERE created_time > ? AND target = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setObject(date);

		return this.jdbc.getList(sql, SystemMail.class, parameter);

	}

	@Override
	public List<SystemMail> getSystemList() {

		Date startTime = DateUtils.addDays(new Date(), -15);

		String sql = "SELECT * FROM system_mail WHERE created_time >= ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setObject(startTime);

		return this.jdbc.getList(sql, SystemMail.class, parameter);

	}

	@Override
	public SystemMail get(String systemMailId) {

		String sql = "SELECT * FROM system_mail WHERE system_mail_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(systemMailId);

		return this.jdbc.get(sql, SystemMail.class, parameter);

	}

	@Override
	public boolean add(SystemMail systemMail) {
		return this.jdbc.insert(systemMail) > 0;
	}

	@Override
	public int getMaxSourceId() {
		String sql = "SELECT max(source_id) FROM system_mail where 1 = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(1);
		return this.jdbc.getInt(sql, parameter);
	}

}
