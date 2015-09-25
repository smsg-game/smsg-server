package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserEquipDao;
import com.lodogame.game.utils.SqlUtil;
import com.lodogame.model.UserEquip;

public class UserEquipDaoMysqlImpl implements UserEquipDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "user_equip";

	public final static String columns = "*";

	public boolean add(UserEquip userEquip) {
		return this.jdbc.insert(userEquip) > 0;
	}

	public List<UserEquip> getUserEquipList(String userId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ORDER BY equip_level DESC LIMIT 800;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserEquip.class, parameter);

	}

	public List<UserEquip> getHeroEquipList(String userId, String userHeroId) {
		throw new NotImplementedException();
	}

	public UserEquip get(String userId, String userEquipId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND user_equip_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userEquipId);

		return this.jdbc.get(sql, UserEquip.class, parameter);
	}

	@Override
	public boolean updateEquipLevel(String userId, String userEquipId, int addLevel, int maxLevel) {

		String sql = "UPDATE " + table + " SET equip_level = CASE WHEN equip_level + ? > ? THEN ? ELSE equip_level + ? END WHERE user_id = ? AND user_equip_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(addLevel);
		parameter.setInt(maxLevel);
		parameter.setInt(maxLevel);
		parameter.setInt(addLevel);
		parameter.setString(userId);
		parameter.setString(userEquipId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	public boolean delete(String userId, String userEquipId) {

		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND user_equip_id = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userEquipId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean delete(String userId, List<String> userEquipId) {

		String ids = SqlUtil.join(userEquipId);

		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND user_equip_id in (" + ids + ") ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId) {

		String sql = "UPDATE " + table + " SET user_hero_id = ? WHERE user_equip_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userHeroId);
		parameter.setString(userEquipId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateEquipId(String userId, String userEquipId, int equipId) {

		String sql = "UPDATE " + table + " SET equip_id = ? WHERE user_equip_id = ? AND user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(equipId);
		parameter.setString(userEquipId);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public void setUserEquipList(String userId, List<UserEquip> userEquipList) {
		throw new NotImplementedException();
	}

	@Override
	public int getUserEquipCount(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addEquips(List<UserEquip> userEquipList) {
		this.jdbc.insert(userEquipList);
		return true;
	}

	@Override
	public List<UserEquip> getUserEquipList(String userId, int equipId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? AND equip_id = ? ;";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(equipId);

		return this.jdbc.getList(sql, UserEquip.class, parameter);
	}

	@Override
	public List<UserEquip> listUserEquipByLevelAsc(String userId, int equipId) {
		String sql = "SELECT" + columns + " FROM " + table + " WHERE user_id = ? AND equip_id = ? ORDER BY equip_level ASC";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(equipId);

		return this.jdbc.getList(sql, UserEquip.class, parameter);
	}

	@Override
	public boolean updateUserDeify(String userId, int equipId, int equipLevel, String userEquipId) {
		String sql = "UPDATE " + table + " SET equip_id = ?,equip_level = ? WHERE user_equip_id = ? AND user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(equipId);
		parameter.setInt(equipLevel);
		parameter.setString(userEquipId);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

}
