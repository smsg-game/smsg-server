package com.lodogame.game.dao.impl.mysql;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserWarInfoDao;
import com.lodogame.model.UserWarInfo;
import com.lodogame.model.WarAttackRank;
import com.lodogame.model.WarAttackRankReward;
import com.lodogame.model.WarCity;

public class UserWarInfoDaoMysqlImpl implements UserWarInfoDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public UserWarInfo getUserWarInfo(String userId) {
		String sql = "select * from user_war_info where user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return jdbc.get(sql, UserWarInfo.class, parameter);
	}

	@Override
	public List<WarCity> getCityStatus() {
		throw new NotImplementedException();
	}

	@Override
	public List<WarCity> getCountryPeopleNum() {
		throw new NotImplementedException();
	}

	@Override
	public void setDrawTime(String userId, Date time) {
		throw new NotImplementedException();
	}

	@Override
	public int getCityPeopleNum(Integer point) {
		throw new NotImplementedException();
	}

	@Override
	public void clearActionCD(String userId, Date time) {
		throw new NotImplementedException();
	}

	@Override
	public void clearLiftCD(String userId, Date time) {
		throw new NotImplementedException();
	}

	@Override
	public void inspire(String userId, Date time) {
		throw new NotImplementedException();
	}

	@Override
	public void add(UserWarInfo userWarInfo) {

		String sql = "REPLACE INTO user_war_info(user_id, user_name, point, country_id, attack_num, defense_num, clear_action_copper, ";
		sql += "inspire_num, lift_time, inspire_time, draw_time, action_time, created_time, ability)";
		sql += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userWarInfo.getUserId());
		parameter.setString(userWarInfo.getUserName());
		parameter.setInt(userWarInfo.getPoint());
		parameter.setInt(userWarInfo.getCountryId());
		parameter.setInt(userWarInfo.getAttackNum());
		parameter.setInt(userWarInfo.getDefenseNum());
		parameter.setInt(userWarInfo.getClearActionCopper());
		parameter.setInt(userWarInfo.getInspireNum());
		parameter.setObject(userWarInfo.getLiftTime());
		parameter.setObject(userWarInfo.getInspireTime());
		parameter.setObject(userWarInfo.getDrawTime());
		parameter.setObject(userWarInfo.getActionTime());
		parameter.setObject(new Date());
		parameter.setInt(userWarInfo.getAbility());

		jdbc.update(sql, parameter);
	}

	@Override
	public void setUserCity(String userId, Integer point) {
		throw new NotImplementedException();
	}

	@Override
	public List<UserWarInfo> getAllUserByCityId(Integer cityId) {
		throw new NotImplementedException();
	}

	@Override
	public void setLifeTime(String userId, Date date) {
		throw new NotImplementedException();
	}

	@Override
	public void setActionTime(String userId, Date date) {
		throw new NotImplementedException();
	}

	@Override
	public void setDefenseNum(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void setAttackNum(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void addWarLog(String date) {

		String sql = "INSERT INTO war_log(date, created_time) VALUES(?, ?)";
		SqlParameter param = new SqlParameter();
		param.setString(date);
		param.setObject(new Date());

		this.jdbc.update(sql, param);

	}

	@Override
	public boolean isWarLog(String date) {

		String sql = "SELECT id FROM war_log WHERE date = ? ";
		SqlParameter param = new SqlParameter();
		param.setString(date);

		return this.jdbc.getInt(sql, param) > 0;
	}

	@Override
	public void cleanData() {

		// 备份
		String sql = "insert into user_war_info_history(user_id, user_name, point, country_id, attack_num, defense_num, clear_action_copper, ";
		sql += "inspire_num, lift_time, inspire_time, draw_time, action_time, created_time, ability) select user_id, user_name, point, country_id, attack_num, defense_num, clear_action_copper, ";
		sql += "inspire_num, lift_time, inspire_time, draw_time, action_time, created_time, ability from user_war_info";

		SqlParameter parameter = new SqlParameter();
		jdbc.update(sql, parameter);

		String cleanSql = "truncate table user_war_info";
		jdbc.update(cleanSql, parameter);

	}

	@Override
	public boolean updateWarRankAttackStatus(String userId, int status) {

		String sql = "UPDATE war_attack_rank SET status = ? where user_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateWarRankAttackStatus() {

		String sql = "UPDATE war_attack_rank SET status = 1 ";
		SqlParameter parameter = new SqlParameter();

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public WarAttackRankReward getAttackRankReward(int rank) {

		String sql = "SELECT * FROM war_attack_rank_reward where rank = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(rank);

		return this.jdbc.get(sql, WarAttackRankReward.class, parameter);
	}

	@Override
	public List<WarAttackRank> getWarAttackRankList() {

		String sql = "SELECT * FROM war_attack_rank order by attack_num DESC LIMIT 20";

		return this.jdbc.getList(sql, WarAttackRank.class);
	}

	@Override
	public void backUpRankData() {

		// 备份
		String sql = "insert into war_attack_rank_history(user_id, username, system_hero_id, vip_level, level, attack_num, created_time)";
		sql += "select user_id, username, system_hero_id, vip_level, level, attack_num, created_time from war_attack_rank";

		SqlParameter parameter = new SqlParameter();
		jdbc.update(sql, parameter);

		String cleanSql = "truncate table war_attack_rank";
		jdbc.update(cleanSql, parameter);
	}

	@Override
	public void saveRankData() {
		throw new NotImplementedException();
	}

	@Override
	public void addAttackRank(WarAttackRank warAttackRank) {

		String sql = "insert into war_attack_rank(user_id, username, system_hero_id, vip_level, level, attack_num, created_time)";
		sql += " VALUES(?, ?, ?, ?, ?, ?, now()) ON DUPLICATE KEY UPDATE attack_num  = attack_num + VALUES(attack_num)";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(warAttackRank.getUserId());
		parameter.setString(warAttackRank.getUsername());
		parameter.setInt(warAttackRank.getSystemHeroId());
		parameter.setInt(warAttackRank.getVipLevel());
		parameter.setInt(warAttackRank.getLevel());
		parameter.setInt(warAttackRank.getAttackNum());

		jdbc.update(sql, parameter);

	}

	@Override
	public void clearDefenseNum(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void setCopperClearTime(String userId, int num) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<String> getWarUserIdList() {
		throw new NotImplementedException();
	}

	@Override
	public void backupData() {
		throw new NotImplementedException();
	}

	@Override
	public void setPower(String userId, int power) {
		throw new NotImplementedException();
	}

}
