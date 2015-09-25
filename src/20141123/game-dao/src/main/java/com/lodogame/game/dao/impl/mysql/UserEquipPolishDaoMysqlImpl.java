package com.lodogame.game.dao.impl.mysql;


import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserEquipPolishDao;
import com.lodogame.model.UserEquipPolish;

public class UserEquipPolishDaoMysqlImpl implements UserEquipPolishDao {

	private static final String table = "user_equip_polish";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public UserEquipPolish getUserEquipPolishById(String userEquipId) {
		String sql = "SELECT * FROM " + table +" WHERE user_equip_id = ? LIMIT 1";
		SqlParameter param = new SqlParameter();
		param.setString(userEquipId);
		return this.jdbc.get(sql, UserEquipPolish.class, param);
	}

	@Override
	public boolean insertUserEquipPolish(UserEquipPolish userEquipPolish) {
		return this.jdbc.insert(userEquipPolish) > 0;
	}

	@Override
	public boolean updateUserEquipPollish(String userId, String userEquipId,
			int attackRand, int lifeRand, int defenseRand) {
		
		String sql = "INSERT INTO " + table +"(user_id, user_equip_id, attack, life, defense) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE attack = attack + values(attack), life = life + values(life), defense = defense + values(defense) ";
	
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		param.setString(userEquipId);
		param.setInt(attackRand);
		param.setInt(lifeRand);
		param.setInt(defenseRand);
		
		return this.jdbc.update(sql, param) > 0;
	}

}
