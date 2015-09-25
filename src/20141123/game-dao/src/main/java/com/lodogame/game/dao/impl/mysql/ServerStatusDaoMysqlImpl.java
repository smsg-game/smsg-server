package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ServerStatusDao;
import com.lodogame.game.utils.PartnerUtil;
import com.lodogame.model.ServerStatus;

public class ServerStatusDaoMysqlImpl implements ServerStatusDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public ServerStatus getServerStatus(String partnerId) {

		int id = PartnerUtil.getPartnerIdPre(partnerId);

		String sql = "SELECT * FROM server_status WHERE id = ? limit 1";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(id);

		return this.jdbc.get(sql, ServerStatus.class, parameter);

	}

	@Override
	public void setServerStatus(int id, int status, String whiteList) {

		String sql = "REPLACE INTO server_status(id, status, white_list) VALUES(?, ?, ?)";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(id);
		parameter.setInt(status);
		parameter.setString(whiteList);

		this.jdbc.update(sql, parameter);
	}

	@Override
	public boolean isWhiteIp(String partnerId, String ip) {
		return false;
	}

}
