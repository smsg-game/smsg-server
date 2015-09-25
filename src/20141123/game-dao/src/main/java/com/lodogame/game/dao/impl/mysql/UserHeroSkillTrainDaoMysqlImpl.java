package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserHeroSkillTrainDao;
import com.lodogame.model.UserHeroSkillTrain;

public class UserHeroSkillTrainDaoMysqlImpl implements UserHeroSkillTrainDao {

	private String table = "user_hero_skill_train";

	@Autowired
	private Jdbc jdbc;

	@Override
	public List<UserHeroSkillTrain> getList(String userId, String userHeroId) {

		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND user_hero_id = ? ORDER BY skill_group_id DESC ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.getList(sql, UserHeroSkillTrain.class, parameter);

	}

	@Override
	public boolean delete(String userId, String userHeroId) {

		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND user_hero_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);

		return this.jdbc.update(sql, parameter) > 0;

	}

	@Override
	public boolean delete(String userId, String userHeroId, int skillGroupId) {

		String sql = "DELETE FROM " + table + " WHERE user_id = ? AND user_hero_id = ? AND skill_group_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setString(userHeroId);
		parameter.setInt(skillGroupId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean add(List<UserHeroSkillTrain> userHeroSkillTrainList) {
		this.jdbc.insert(userHeroSkillTrainList);
		return true;
	}
}
