package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserHeroSkillDao;
import com.lodogame.model.UserHeroSkill;

public class UserHeroSkillDaoMysqlImpl implements UserHeroSkillDao {

	private String table = "user_hero_skill";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<UserHeroSkill> getList(String userId, String userHeroId) {

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND user_hero_id = ? ORDER BY skill_group_id DESC";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.getList(sql, UserHeroSkill.class, parameter);

	}

	@Override
	public List<UserHeroSkill> getList(String userId) {

		String sql = "SELECT * FROM " + table + " WHERE user_id = ?  ORDER BY skill_group_id DESC";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserHeroSkill.class, parameter);
	}

	@Override
	public boolean update(String userId, int userHeroSkillId, int skillGroupId, int skillId) {

		String sql = "UPDATE " + table + " SET skill_group_id = ?, skill_id = ? WHERE user_id = ? AND user_hero_skill_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(skillGroupId);
		parameter.setInt(skillId);
		parameter.setString(userId);
		parameter.setInt(userHeroSkillId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean udpate(String userId, String userHeroId, int skillGroupId, int skillId) {

		String sql = "UPDATE " + table + " SET skill_id = ? WHERE user_id = ? AND user_hero_id = ? AND skill_group_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(skillId);
		parameter.setString(userId);
		parameter.setString(userHeroId);
		parameter.setInt(skillGroupId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean delete(String userId, String userHeroId, int userHeroSkillId) {

		String sql = "DELETE FROM " + table + " WHERE user_id = ?  AND user_hero_id = ? AND user_hero_skill_id = ? ";

		SqlParameter parameter = new SqlParameter();

		parameter.setString(userId);
		parameter.setString(userHeroId);
		parameter.setInt(userHeroSkillId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public UserHeroSkill get(String userId, int userHeroSkillId) {

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND user_hero_skill_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(userHeroSkillId);

		return this.jdbc.get(sql, UserHeroSkill.class, parameter);
	}

	@Override
	public boolean add(UserHeroSkill userHeroSkill) {
		return this.jdbc.insert(userHeroSkill) > 0;
	}

}
