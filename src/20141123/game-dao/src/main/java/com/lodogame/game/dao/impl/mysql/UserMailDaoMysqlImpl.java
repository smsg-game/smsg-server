package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserMailDao;
import com.lodogame.game.utils.SqlUtil;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserMail;
import com.lodogame.model.UserMailLog;

public class UserMailDaoMysqlImpl implements UserMailDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean updateStatus(String userId, int userMailId, int status) {

		String table = TableUtils.getUserMailTable(userId);

		String sql = "UPDATE " + table + " SET status = ? WHERE user_id = ? AND user_mail_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setString(userId);
		parameter.setInt(userMailId);

		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateReceiveStatus(String userId, int userMailId, int status) {

		String table = TableUtils.getUserMailTable(userId);

		String sql = "UPDATE " + table + " SET receive_status = ? WHERE user_id = ? AND user_mail_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setString(userId);
		parameter.setInt(userMailId);

		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<UserMail> getList(String userId) {

		String table = TableUtils.getUserMailTable(userId);

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND status <> 2";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserMail.class, parameter);

	}

	@Override
	public boolean add(List<UserMail> userMailList) {
		if (userMailList.isEmpty()) {
			return false;
		}
		String table = TableUtils.getUserMailTable(userMailList.get(0).getUserId());
		this.jdbc.insert(table, userMailList);
		return true;
	}

	@Override
	public UserMail get(String userId, int userMailId) {

		String table = TableUtils.getUserMailTable(userId);

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND user_mail_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(userMailId);

		return this.jdbc.get(sql, UserMail.class, parameter);
	}

	@Override
	public UserMail getBySystemMailId(String userId, String systemMailId) {

		String table = TableUtils.getUserMailTable(userId);

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND system_mail_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(systemMailId);

		return this.jdbc.get(sql, UserMail.class, parameter);
	}

	@Override
	public Date getLastReceiveTime(String userId) {

		String sql = "SELECT * FROM user_mail_log WHERE user_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		UserMailLog userMailLog = this.jdbc.get(sql, UserMailLog.class, parameter);

		if (userMailLog != null) {
			return userMailLog.getLastReceiveTime();
		}

		return null;
	}

	@Override
	public boolean setLastReceiveTime(String userId, Date date) {

		String sql = "INSERT INTO user_mail_log(user_id, last_receive_time) VALUES(?, ?) ";
		sql += "ON DUPLICATE KEY UPDATE last_receive_time = VALUES(last_receive_time)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(date);

		return jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateStatus(String userId, List<Integer> userMailIdList, int status) {

		String table = TableUtils.getUserMailTable(userId);

		String sql = "UPDATE " + table + " SET status = ? WHERE user_id = ? AND user_mail_id in (" + SqlUtil.joinInteger(userMailIdList) + ") ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setString(userId);

		return jdbc.update(sql, parameter) > 0;
	}
	
	@Override
	public boolean isZaned(String userId){
		String sql = "SELECT * FROM user_mail_log WHERE user_id = ? LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		UserMailLog userMailLog = this.jdbc.get(sql, UserMailLog.class, parameter);
		if(userMailLog!=null){
			return userMailLog.getIsZaned()==1;
		}
		return false;
	}
	
	@Override
	public boolean setZaned(String userId){
		String sql = "INSERT INTO user_mail_log(user_id, last_receive_time, is_zaned) VALUES(?, ?, ?) ";
		sql += "ON DUPLICATE KEY UPDATE is_zaned = VALUES(is_zaned)";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setObject(new Date());
		parameter.setInt(1);
		return this.jdbc.update(sql, parameter) > 0;
	}

}
