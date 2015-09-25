package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.utils.SqlUtil;
import com.lodogame.model.UserPkInfo;

public class UserPkInfoDaoMysqlImpl implements UserPkInfoDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "user_pk_info";

	public final static String columns = "*";

	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	public static String getColumns() {
		return columns;
	}

	@Override
	public boolean add(UserPkInfo userPkInfo) {
		String sql = "insert into " + table + " (user_id, rank, score, pk_times, update_pk_time,user_level,times,username) select ?, rank + 1, ?, ?, ?, ?, ?, ? from " + table + " order by rank desc limit 1";

		SqlParameter params = new SqlParameter();
		params.setString(userPkInfo.getUserId());
		params.setInt(userPkInfo.getScore());
		params.setInt(userPkInfo.getPkTimes());
		params.setObject(userPkInfo.getUpdatePkTime());
		params.setInt(userPkInfo.getUser_level());
		params.setInt(userPkInfo.getTimes());
		params.setString(userPkInfo.getUsername());

		return this.jdbc.update(sql, params) == 1;
	}

	@Override
	public boolean addFirst(UserPkInfo userPkInfo) {
		return this.jdbc.insert(userPkInfo) > 0;
	}

	@Override
	public UserPkInfo getByRank(int rank) {
		String sql = "select " + columns + " from " + table + " where rank = ? ";
		SqlParameter param = new SqlParameter();
		param.setInt(rank);
		return jdbc.get(sql, UserPkInfo.class, param);
	}

	@Override
	public UserPkInfo getByUserId(String userId) {
		String sql = "select " + columns + " from " + table + " where user_id = ? ";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		return jdbc.get(sql, UserPkInfo.class, param);
	}

	@Override
	public boolean update(UserPkInfo userPkInfo) {
		String sql = "update " + table + " set rank = ?, score = ?, pk_times = ?, update_pk_time = ?, see_type = ? where user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(userPkInfo.getRank());
		param.setInt(userPkInfo.getScore());
		param.setInt(userPkInfo.getPkTimes());
		param.setObject(userPkInfo.getUpdatePkTime());
		param.setInt(userPkInfo.getSeeType());
		param.setString(userPkInfo.getUserId());
		return this.jdbc.update(sql, param) == 1;
	}

	@Override
	public boolean changeRank(String userId, String targetUserId) {
		String sql = "update user_pk_info a, user_pk_info b set a.update_pk_time = now(), a.rank = b.rank, b.rank = a.rank where a.user_id = ? and b.user_id = ? and a.rank > b.rank";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		param.setString(targetUserId);
		return this.jdbc.update(sql, param) == 2;
	}

	@Override
	public List<UserPkInfo> getByRankRange(int start, int end) {
		String sql = "select " + columns + " from " + table + " where rank >= ? and rank <= ? order by rank";
		SqlParameter param = new SqlParameter();
		param.setInt(start);
		param.setInt(end);
		return jdbc.getList(sql, UserPkInfo.class, param);
	}

	@Override
	public List<UserPkInfo> getRanks(List<Integer> rankList) {

		String sql = "SELECT * FROM " + table + " WHERE rank in (" + SqlUtil.joinInteger(rankList) + ")  ORDER BY rank ASC ";

		SqlParameter param = new SqlParameter();

		return jdbc.getList(sql, UserPkInfo.class, param);
	}

	@Override
	public UserPkInfo getLastUserPkInfo() {
		String sql = "select " + columns + " from " + table + " order by rank desc limit 1";
		return jdbc.get(sql, UserPkInfo.class, null);
	}

	public List<UserPkInfo> getAllUserPkInfo() {
		String sql = "select " + columns + " from " + table;
		return jdbc.getList(sql, UserPkInfo.class);
	}

	@Override
	public void backUserPkInfo() {

		String sql = "INSERT INTO user_pk_info_log(rank, user_id, score, created_time) SELECT rank, user_id, score, now() FROM user_pk_info";

		SqlParameter parameter = new SqlParameter();

		this.jdbc.update(sql, parameter);
	}

	@Override
	public boolean addScore(String userId, int score) {

		String sql = "UPDATE " + table + " SET score = score + ? WHERE user_id = ? ";
		SqlParameter parameter = new SqlParameter();

		parameter.setInt(score);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean buyPkTimes(String userId, int buyPkTimes) {

		String sql = "UPDATE " + table + " SET pk_times = pk_times - 1, buy_pk_times = ?, last_buy_time = now() WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();

		parameter.setInt(buyPkTimes);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addPkTimes(String userId, int pkTimes) {

		String sql = "update " + table + " set pk_times = pk_times + ? where user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(pkTimes);
		param.setString(userId);
		return this.jdbc.update(sql, param) == 1;
	}

	@Override
	public List<UserPkInfo> getGroupRanks(int beginLevel, int endLevel) {

		String sql = "SELECT * FROM " + table + " WHERE user_level >= ? and user_level <= ? ORDER BY rank ASC ";

		SqlParameter param = new SqlParameter();
		param.setInt(beginLevel);
		param.setInt(endLevel);

		return jdbc.getList(sql, UserPkInfo.class, param);
	}

	@Override
	public void updateTimes(String userId) {
		String sql = "update " + table + " set times = times + 1 where user_id = ?";

		SqlParameter param = new SqlParameter();
		param.setString(userId);

		this.jdbc.update(sql, param);

	}

	@Override
	public void clearTimes(String userId) {
		String sql = "update " + table + " set times = 0 where user_id = ?";

		SqlParameter param = new SqlParameter();
		param.setString(userId);

		this.jdbc.update(sql, param);

	}

	@Override
	public List<UserPkInfo> getGrankTen(int beginLevel, int endLevel) {
		String sql = "SELECT * FROM " + table + " WHERE  user_level >= ? and user_level <= ? ORDER BY rank ASC limit 10 ";

		SqlParameter param = new SqlParameter();
		param.setInt(beginLevel);
		param.setInt(endLevel);

		return jdbc.getList(sql, UserPkInfo.class, param);
	}

	@Override
	public List<UserPkInfo> getTotalTen() {
		String sql = "SELECT * FROM " + table + " ORDER BY rank ASC limit 10 ";

		return jdbc.getList(sql, UserPkInfo.class);
	}

	@Override
	public void updateUserLevel(String userId, int level) {
		String sql = "update " + table + " set user_level = ? where user_id = ?";

		SqlParameter param = new SqlParameter();
		param.setInt(level);
		param.setString(userId);

		this.jdbc.update(sql, param);
		
	}

	@Override
	public UserPkInfo getGroupFirstRanks(int beginLevel, int endLevel) {
		String sql = "SELECT * FROM " + table + " WHERE user_level >= ? and user_level <= ? ORDER BY rank ASC limit 1 ";

		SqlParameter param = new SqlParameter();
		param.setInt(beginLevel);
		param.setInt(endLevel);

		return jdbc.get(sql, UserPkInfo.class, param);
	}

}
