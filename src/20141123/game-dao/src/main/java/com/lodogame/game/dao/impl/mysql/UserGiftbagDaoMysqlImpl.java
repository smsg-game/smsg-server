package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserGiftbagDao;
import com.lodogame.model.UserGiftbag;

public class UserGiftbagDaoMysqlImpl implements UserGiftbagDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public UserGiftbag getLast(String userId, int type) {

		String sql = "SELECT * FROM user_giftbag WHERE user_id = ? AND type = ? ORDER BY updated_time desc LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);

		return this.jdbc.get(sql, UserGiftbag.class, parameter);

	}

	@Override
	public boolean addOrUpdateUserGiftbag(String userId, int type, int giftBagId) {

		String sql = " INSERT INTO user_giftbag(user_id, type, giftbag_id, total_num, created_time, updated_time)";
		sql += " VALUES(?, ?, ?, 1, now(), now()) ON DUPLICATE KEY UPDATE ";
		sql += " total_num = total_num + VALUES(total_num), updated_time = VALUES(updated_time)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(type);
		parameter.setInt(giftBagId);

		return this.jdbc.update(sql, parameter) > 0;
	}
	
	@Override
	public int getCount(String userId, int type, int giftBagId) {
		String sql = "SELECT total_num FROM user_giftbag WHERE user_id = ? AND giftbag_id = ? AND type = ?";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(giftBagId);
		parameter.setInt(type);
		
		return this.jdbc.getInt(sql, parameter);
	}
	
	/**
	 * 查询用户下的所有记录 用于初始化缓存的
	 * @param userId
	 * @return
	 */
	public List<UserGiftbag> getAllUserGiftBagByUserId(String userId){
		String sql = "SELECT * FROM user_giftbag WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.getList(sql, UserGiftbag.class, parameter);
	}
}
