package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemScene;

public class SystemSceneDaoMysqlImpl implements SystemSceneDao, ExportDataDao {

	private final static String table = "system_scene";

	private final static String columns = "*";

	@Autowired
	private Jdbc jdbc;

	public List<SystemScene> getSystemSceneList() {

		String sql = "SELECT " + columns + " FROM " + table;

		return this.jdbc.getList(sql, SystemScene.class);
	}

	@Override
	public SystemScene get(int sceneId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE scene_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(sceneId);

		return this.jdbc.get(sql, SystemScene.class, parameter);
	}

	public String toJson() {
		List<SystemScene> list = getSystemSceneList();
		return Json.toJson(list);
	}

}
