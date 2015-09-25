package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.FactionDao;
import com.lodogame.model.Faction;

public class FactionDaoMysqlImpl implements FactionDao {

	@Autowired
	private Jdbc jdbc;

	private String table = "faction";
	
	@Override
	public boolean createFaction(Faction faction) {
		return this.jdbc.insert(faction) > 0;
	}

	@Override
	public Faction getFactionByName(String factionName) {
		String sql = "SELECT * FROM " + table + " WHERE faction_name = ? LIMIT 1 ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(factionName);
		return this.jdbc.get(sql, Faction.class, parameter);
	}

	@Override
	public Faction getFactionByFid(int factionId) {
		String sql = "SELECT * FROM " + table + " WHERE faction_id = ? LIMIT 1 ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(factionId);
		return this.jdbc.get(sql, Faction.class, parameter);
	}

	@Override
	public List<Faction> getFactionByPage(int start, int end) {
		String sql = "SELECT * FROM " + table + " ORDER BY member_num DESC, faction_id ASC LIMIT ?,? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(start);
		parameter.setInt(end);
		return this.jdbc.getList(sql, Faction.class, parameter);
	}

	@Override
	public boolean saveFactionNotice(int factionId, String factionNotice) {
		String sql = "UPDATE " + table + " SET faction_notice  = ? WHERE faction_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(factionNotice);
		parameter.setInt(factionId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateFactionMemberNum(int factionId, int num) {
		String sql = "UPDATE " + table + " SET member_num = member_num + ? WHERE faction_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(num);
		parameter.setInt(factionId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean deleteFaction(int factionId,String factionName) {
		String sql = "delete from faction where faction_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(factionId);
		return jdbc.update(sql, parameter) > 0;
	}



}
