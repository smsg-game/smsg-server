package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserSceneDao;
import com.lodogame.model.UserScene;

public class UserSceneDaoMysqlImpl implements UserSceneDao {

	public final static String table = "user_scene";

	public final static String columns = "user_id, scene_id, pass_flag, created_time, updated_time";

	@Autowired
	private Jdbc jdbc;

	public List<UserScene> getUserSceneList(String userId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.getList(sql, UserScene.class, parameter);
	}

	public int add(UserScene userScene) {

		return this.jdbc.insert(userScene);

	}

	@Override
	public UserScene getLastUserScene(String userId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE user_id = ? order by scene_id";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, UserScene.class, parameter);
	}

	@Override
	public boolean updateScenePassed(String userId, int sceneId) {
		Date date = new Date();
		String sql = "update " + table + " set pass_flag = 1, updated_time = ? where user_id = ? and scene_id = ?";
		SqlParameter params = new SqlParameter();
		params.setObject(date);
		params.setString(userId);
		params.setInt(sceneId);
		return this.jdbc.update(sql, params) == 1;
	}

}
