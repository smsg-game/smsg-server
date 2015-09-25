package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserDao;
import com.lodogame.model.User;
import com.lodogame.model.UserFaction;
import com.lodogame.model.UserShareLog;

public class UserDaoMysqlImpl implements UserDao {

	/**
	 * 表名
	 */
	public final static String table = "user";

	/**
	 * 字段列表
	 */
	public final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public boolean add(User user) {
		return this.jdbc.insert(user) > 0;
	}

	public User get(String userId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, User.class, parameter);
	}

	public User getByName(String username) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE username = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(username);

		return this.jdbc.get(sql, User.class, parameter);
	}

	public boolean addCopper(String userId, int copper) {
		if (copper <= 0) {
			return false;
		}

		String sql = "UPDATE " + table + " SET copper = copper + ? WHERE user_id = ? LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(copper);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	public boolean reduceCopper(String userId, int copper) {
		if (copper <= 0) {
			return false;
		}

		String sql = "UPDATE " + table + " SET copper = copper - ? WHERE user_id = ? AND copper >= ? LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(copper);
		parameter.setString(userId);
		parameter.setInt(copper);

		return this.jdbc.update(sql, parameter) > 0;

	}

	public boolean addGold(String userId, int gold) {

		String sql = "UPDATE " + table + " SET gold_num = gold_num + ? WHERE user_id = ? LIMIT 1 ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(gold);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	public boolean reduceGold(String userId, int gold) {
		if (gold <= 0) {
			return false;
		}

		String sql = "UPDATE " + table + " SET gold_num = gold_num - ? WHERE user_id = ? AND gold_num >= ? LIMIT 1 ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(gold);
		parameter.setString(userId);
		parameter.setInt(gold);

		return this.jdbc.update(sql, parameter) > 0;

	}

	public boolean addExp(String userId, int exp, int level, int power) {

		String sql = "UPDATE " + table + " SET exp = exp + ?, level = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(exp);
		parameter.setInt(level);

		if (power > 0) {
			sql += ", power = ? ";
			parameter.setInt(power);
		}

		sql += " WHERE user_id = ? LIMIT 1 ";
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean resetPowerAddTime(String userId, Date powerAddTime) {

		SqlParameter parameter = new SqlParameter();

		String sql = "UPDATE " + table + " SET power_add_time = ? WHERE user_id = ? LIMIT 1 ";

		parameter.setObject(powerAddTime);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addPower(String userId, int power, int maxPower, Date powerAddTime) {

		SqlParameter parameter = new SqlParameter();

		parameter.setInt(power);
		parameter.setInt(maxPower);
		parameter.setInt(maxPower);
		parameter.setInt(power);

		String sql = "UPDATE " + table + " SET power = CASE WHEN power + ? > ? THEN ? ELSE power + ? END ";

		// String sql = "UPDATE " + table + " SET power = power + ? ";

		if (powerAddTime != null) {
			sql += ", power_add_time = ? ";
			parameter.setObject(powerAddTime);
		}

		sql += " WHERE user_id = ? LIMIT 1 ";

		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean reducePowre(String userId, int power) {

		if (power <= 0) {
			return true;
		}

		String sql = "UPDATE " + table + " SET power = power - ? WHERE user_id = ? AND power >= ? LIMIT 1 ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(power);
		parameter.setString(userId);
		parameter.setInt(power);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean cleanCacheData(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public Set<String> getOnlineUserIdList() {
		throw new NotImplementedException();
	}

	@Override
	public boolean setOnline(String userId, boolean online) {
		throw new NotImplementedException();
	}

	@Override
	public boolean updateVIPLevel(String userId, int VIPLevel, Date vipExpiredTime) {

		String sql = "UPDATE " + table + " SET vip_level = ? , vip_expired_time = ? WHERE user_id = ?;";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(VIPLevel);
		parameter.setObject(vipExpiredTime);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public User getByPlayerId(String playerId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE lodo_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(playerId);

		return this.jdbc.get(sql, User.class, parameter);
	}

	@Override
	public List<User> listOrderByLevelDesc(int offset, int size) {
		String sql = "SELECT " + columns + " FROM " + table + " order by level desc limit ?, ? ";
		SqlParameter param = new SqlParameter();
		param.setInt(offset);
		param.setInt(size);
		return jdbc.getList(sql, User.class, param);
	}

	@Override
	public List<User> listOrderByCopperDesc(int offset, int size) {
		String sql = "SELECT " + columns + " FROM " + table + " order by copper desc limit ?, ? ";
		SqlParameter param = new SqlParameter();
		param.setInt(offset);
		param.setInt(size);
		return jdbc.getList(sql, User.class, param);
	}

	@Override
	public boolean isOnline(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public List<String> getAllUserIds() {
		String sql = "SELECT user_id FROM " + table;
		return jdbc.getList(sql, String.class);
	}

	@Override
	public boolean reduceExp(String userId, int amount) {
		if (amount <= 0) {
			return false;
		}

		String sql = "UPDATE " + table + " SET exp = exp - ? WHERE user_id = ? AND copper >= ? LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(amount);
		parameter.setString(userId);
		parameter.setInt(amount);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean banUser(String userId, Date dueTime) {
		String sql = "UPDATE " + table + " SET is_banned = 1, due_time = ? WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();

		parameter.setObject(dueTime);
		parameter.setString(userId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {

		String sql = "UPDATE " + table + " SET level = ?, exp = ? WHERE user_id = ? LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(level);
		parameter.setInt(exp);
		parameter.setString(userId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean reduceReputation(String userId, int reputation) {

		String sql = "UPDATE " + table + " SET reputation = reputation - ? WHERE user_id = ? AND reputation >= ? LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(reputation);
		parameter.setString(userId);
		parameter.setInt(reputation);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addReputation(String userId, int reputation) {

		String sql = "UPDATE " + table + " SET reputation = reputation + ? WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(reputation);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public UserShareLog getUserLastShareLog(String userId) {

		String sql = "SELECT " + columns + " FROM  user_share_log  WHERE user_id = ? ORDER BY created_time DESC LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, UserShareLog.class, parameter);
	}

	@Override
	public boolean addUserShareLog(UserShareLog userShareLog) {
		return this.jdbc.insert(userShareLog) > 0;
	}

	@Override
	public boolean reduceMind(String userId, int mind) {
		String sql = "UPDATE " + table + " SET mind = mind - ? WHERE user_id = ? AND mind >= ? LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(mind);
		parameter.setString(userId);
		parameter.setInt(mind);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addMind(String userId, int mind) {
		String sql = "UPDATE " + table + " SET mind = mind + ? WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(mind);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean bannedToPost(String userId) {
		String sql = "UPDATE " + table + " SET banned_chart_time = now()  WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public boolean updateUserFactionId(String userId, int factionId) {
		String sql = "UPDATE " + table + " SET faction_id = ?  WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(factionId);
		param.setString(userId);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public boolean addPkScore(String userId, int pkScore) {
		String sql = "UPDATE " + table + " SET pk_score = pk_score + ?  WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(pkScore);
		param.setString(userId);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public boolean reducePkScore(String userId, int pkScore) {
		String sql = "UPDATE " + table + " SET pk_score = pk_score - ?  WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(pkScore);
		param.setString(userId);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public boolean updateAllFactionByFactionId(int factionId,List<UserFaction> list) {
		String sql = "UPDATE " + table + " SET faction_id = 0 WHERE faction_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(factionId);

		return this.jdbc.update(sql, parameter) > 0;
	}

}
