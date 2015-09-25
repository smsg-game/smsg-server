package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExportDataDao;
import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.SystemEquip;

public class SystemEquipDaoMysqlImpl implements SystemEquipDao, ExportDataDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "system_equip";

	public final static String columns = "*";

	public List<SystemEquip> getSystemEquipList() {

		String sql = "SELECT " + columns + " FROM " + table;

		SqlParameter parameter = new SqlParameter();

		return this.jdbc.getList(sql, SystemEquip.class, parameter);

	}

	public SystemEquip get(int equipId) {

		String sql = "SELECT " + columns + " FROM " + table + " WHERE equip_id = ? ;";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(equipId);

		return this.jdbc.get(sql, SystemEquip.class, parameter);
	}

	@Override
	public boolean add(SystemEquip systemEquip) {
		throw new NotImplementedException();
	}

	public String toJson() {
		List<SystemEquip> list = getSystemEquipList();
		return Json.toJson(list);
	}

}
