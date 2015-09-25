package com.lodogame.game.dao;

import com.lodogame.model.UserMonthlyCardTask;

/**
 * 30天月卡任务
 * @author chengevo
 *
 */

public interface UserMonthlyCardTaskDao {
	
	public UserMonthlyCardTask getByUserId(String userId);

	public boolean add(UserMonthlyCardTask task);

	public void update(UserMonthlyCardTask task);
	
}
