package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.NoticeDao;
import com.lodogame.model.Notice;

public class NoticeDaoMysqlImpl implements NoticeDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public Notice getNotice(String serverId,String partnerId) {
		String sql = "select * from notice where server_id = ? and partner_id = ?";
		SqlParameter params = new SqlParameter();
		params.setString(serverId);
		params.setString(partnerId);
		return this.jdbc.get(sql, Notice.class, params);
	}

	@Override
	public boolean updateNotice(Notice notice) {
		String sql = "insert into notice values (?, ?, ?, ?, ?, now(), now()) on duplicate key update title = ?, content = ?, updated_time = now(), is_enable = ?";
		SqlParameter params = new SqlParameter();
		params.setString(notice.getServerId());
		params.setString(notice.getPartnerId());
		params.setString(notice.getTitle());
		params.setString(notice.getContent());
		params.setInt(notice.getIsEnable());
		params.setString(notice.getTitle());
		params.setObject(notice.getContent());
		params.setInt(notice.getIsEnable());
		return this.jdbc.update(sql, params) > 0;
	}

}
