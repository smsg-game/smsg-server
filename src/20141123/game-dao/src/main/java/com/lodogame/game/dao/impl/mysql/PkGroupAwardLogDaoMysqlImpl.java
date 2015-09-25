package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.PkGroupAwardLogDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.PkGroupAwardLog;

public class PkGroupAwardLogDaoMysqlImpl implements PkGroupAwardLogDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public void add(PkGroupAwardLog pkGroupAwardLog) {
		this.jdbc.insert(pkGroupAwardLog);

	}

	@Override
	public PkGroupAwardLog get(String userId) {

		String sql = "select * from pk_group_award_log where user_id = ? and is_del = 0 and  is_hostory = 0 limit 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, PkGroupAwardLog.class, parameter);
	}

	@Override
	public List<PkGroupAwardLog> getList(int groupId) {
		String sql = "select * from pk_group_award_log where group_id = ? and is_hostory = 1 and is_del = 0 order by grank asc limit 10";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(groupId);
		return this.jdbc.getList(sql, PkGroupAwardLog.class, parameter);
	}

	@Override
	public boolean updateGet(String userId, Date date) {

		String sql = "update pk_group_award_log set is_get = 1 where user_id = ? and is_hostory = 0";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return jdbc.update(sql, parameter) > 0;

	}

	@Override
	public void deleteRecord() {
		String sql = "update pk_group_award_log set is_del = 1 where is_hostory = 1";

		SqlParameter parameter = new SqlParameter();

		jdbc.update(sql, parameter);
	}

	@Override
	public void updateHostory() {
		String sql = "update pk_group_award_log set is_hostory = 1 ";

		SqlParameter parameter = new SqlParameter();

		jdbc.update(sql, parameter);

	}

}
