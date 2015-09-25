package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.TavernDropToolDao;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;

public class TavernDropToolDaoMysqlImpl implements TavernDropToolDao {

	@Autowired
	private Jdbc jdbc;

	public List<TavernDropTool> getTavernDropToolList(int type) {

		String sql = "SELECT * FROM tavern_drop_tool WHERE type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);

		return this.jdbc.getList(sql, TavernDropTool.class, parameter);
	}

	@Override
	public List<TavernAmendDropTool> getTavernAmendDropToolList(int type) {

		String sql = "SELECT * FROM tavern_amend_drop_tool WHERE type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);

		return this.jdbc.getList(sql, TavernAmendDropTool.class, parameter);
	}

}
