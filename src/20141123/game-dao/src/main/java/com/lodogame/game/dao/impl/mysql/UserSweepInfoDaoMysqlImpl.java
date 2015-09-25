/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2013
 */

package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserSweepInfoDao;
import com.lodogame.model.UserSweepInfo;

public class UserSweepInfoDaoMysqlImpl implements UserSweepInfoDao {
	@Autowired
	private Jdbc jdbc;
	private final static String TABLE = "user_sweep_info";

	@Override
	public boolean add(UserSweepInfo sweepInfo) {
		return jdbc.insert(sweepInfo) == 1;
	}

	@Override
	public UserSweepInfo getCurrentSweep(String userId) {
		String sql = "select * from " + TABLE + " where user_id = ? and status in (0, 1)";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		return jdbc.get(sql, UserSweepInfo.class, params);
	}

	@Override
	public boolean updateSweepComplete(String userId) {
		String sql = "update " + TABLE + " set status = 1 where user_id = ? and status = 0";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		return jdbc.update(sql, params) >= 1;
	}

	@Override
	public boolean stopSweep(String userId) {
		String sql = "update " + TABLE + " set status = -1 where user_id = ? and status = 0";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		return jdbc.update(sql, params) >= 1;
	}

	@Override
	public boolean updateSweepReceived(String userId) {
		String sql = "update " + TABLE + " set status = 2 where user_id = ? and status = 1";
		SqlParameter params = new SqlParameter();
		params.setString(userId);
		return jdbc.update(sql, params) >= 1;
	}
}
