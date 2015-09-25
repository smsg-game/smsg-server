package com.lodogame.game.dao.impl.mysql;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.model.UserExtinfo;

public class UserExtinfoDaoMysqlImpl implements UserExtinfoDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "user_extinfo";

	public final static String columns = "*";

	@Override
	public boolean add(UserExtinfo userExtinfo) {
		Date now = new Date();
		if (userExtinfo.getLastBuyPowerTime() == null) {
			userExtinfo.setLastBuyPowerTime(now);
		}
		if (userExtinfo.getLastBuyCopperTime() == null) {
			userExtinfo.setLastBuyCopperTime(now);
		}
		return this.jdbc.insert(userExtinfo) > 0;
	}

	@Override
	public UserExtinfo get(String userId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, UserExtinfo.class, parameter);
	}

	@Override
	public boolean updateBuyCopperTimes(String userId, int times) {

		String sql = "UPDATE " + table + " SET buy_copper_times = ?, last_buy_copper_time = now() WHERE user_id = ?;";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateBuyPowerTimes(String userId, int times) {

		String sql = "UPDATE " + table + " SET buy_power_times = ?, last_buy_power_time = now() WHERE user_id = ?;";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateHeroMax(String userId, int heroMax) {

		String sql = "UPDATE " + table + " SET hero_max = hero_max + ? WHERE user_id = ?;";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroMax);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateEquipMax(String userId, int equipMax) {

		String sql = "UPDATE " + table + " SET equip_max = equip_max + ?  WHERE user_id = ?;";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(equipMax);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateGuideStep(String userId, int step) {
		String sql = "UPDATE " + table + " SET guide_step = ?  WHERE user_id = ?;";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(step);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean recordGuideStep(String userId, String newStep) {
		String sql = "UPDATE " + table + " SET record_guide_step = ? WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(newStep);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateFightRecode(String userId, boolean isWin) {
		String sql = "update " + table + " set win_count = win_count + 1 where user_id = ?;";
		if (!isWin) {
			sql = "update " + table + " set lose_count = lose_count + 1 where user_id = ?;";
		}
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateRewardVipLevel(String userId, int vipLevel) {

		String sql = "UPDATE " + table + " SET reward_vip_level = ? WHERE user_id = ?";
		SqlParameter parameter = new SqlParameter();

		parameter.setInt(vipLevel);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

}
