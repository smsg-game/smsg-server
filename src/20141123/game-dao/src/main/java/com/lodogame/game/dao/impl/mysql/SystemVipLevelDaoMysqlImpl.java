package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.model.SystemVipLevel;

public class SystemVipLevelDaoMysqlImpl implements SystemVipLevelDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemVipLevel get(int vipLevel) {

		String sql = "SELECT * FROM system_vip_level WHERE vip_level = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(vipLevel);

		return this.jdbc.get(sql, SystemVipLevel.class, parameter);
	}

	@Override
	public SystemVipLevel getBuyMoney(int money) {

		String sql = "SELECT * FROM system_vip_level WHERE need_money <= ? ORDER BY vip_level DESC LIMIT 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(money);

		return this.jdbc.get(sql, SystemVipLevel.class, parameter);
	}

   public List<SystemVipLevel> getAll(){
		String sql = "SELECT * FROM system_vip_level";
		return this.jdbc.getList(sql, SystemVipLevel.class);
   }
}
