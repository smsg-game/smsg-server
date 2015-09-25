package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemEquipAttrDao;
import com.lodogame.model.SystemEquipAttr;

public class SystemEquipAttrDaoMysqlImpl implements SystemEquipAttrDao {

	private String table = "system_equip_attr";

	private String columns = "*";

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemEquipAttr getEquipAttr(int systemHeroId, int heroLevel) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE equip_id = ? and equip_level = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(systemHeroId);
		parameter.setInt(heroLevel);
		return this.jdbc.get(sql, SystemEquipAttr.class, parameter);
	}
}
