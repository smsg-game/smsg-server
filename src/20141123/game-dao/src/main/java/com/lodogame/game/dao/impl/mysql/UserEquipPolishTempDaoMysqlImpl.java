package com.lodogame.game.dao.impl.mysql;


import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserEquipPolishTempDao;
import com.lodogame.model.UserEquipPolishTemp;

public class UserEquipPolishTempDaoMysqlImpl implements UserEquipPolishTempDao {

	private static final String table = "user_equip_polish_temp";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public UserEquipPolishTemp getUserEquipPolishTempById(String userEquipId) {
		String sql = "SELECT * FROM " + table +" WHERE user_equip_id = ? LIMIT 1";
		SqlParameter param = new SqlParameter();
		param.setString(userEquipId);
		return this.jdbc.get(sql, UserEquipPolishTemp.class, param);
	}

	@Override
	public boolean insertUserEquipPolishTemp(UserEquipPolishTemp userEquipPolishTemp) {
		return this.jdbc.insert(userEquipPolishTemp) > 0;
	}

	@Override
	public boolean updateUserEquipPollishTemp(String userId, String userEquipId,
			int attackRand, int lifeRand, int defenseRand) {
		
		String sql = "UPDATE " + table + " SET attack = ?, life = ?, defense = ? WHERE user_equip_id = ? AND  user_id = ?";
		SqlParameter param = new SqlParameter();
		
		param.setInt(attackRand);
		param.setInt(lifeRand);
		param.setInt(defenseRand);
		param.setString(userEquipId);
		param.setString(userId);
		
		return this.jdbc.update(sql, param) > 0;
	}

}
