package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemDeifyNodeDao;
import com.lodogame.model.SystemCheckInReward;
import com.lodogame.model.SystemDeifyNode;

public class SystemDeifyNodeDaoMysqlImpl implements SystemDeifyNodeDao{

	@Autowired
	private Jdbc jdbc;
	
	private String table = "system_deity_node";

	@Override
	public SystemDeifyNode getSystemDeifyNodeById(int heroId,
			int systemDeifyNodeId) {
		String sql = "SELECT * FROM " + table + " WHERE hero_id = ? AND deify_node_level = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(heroId);
		parameter.setInt(systemDeifyNodeId);

		return this.jdbc.get(sql, SystemDeifyNode.class, parameter);
	}

	@Override
	public List<SystemDeifyNode> getSystemDeifyNode(int systemHeroId) {
		String sql = "SELECT * FROM " + table + " WHERE hero_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);

		return this.jdbc.getList(sql, SystemDeifyNode.class, parameter);
	}
	


}
