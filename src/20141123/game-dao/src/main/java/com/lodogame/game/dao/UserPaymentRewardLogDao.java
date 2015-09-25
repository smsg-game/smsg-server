package com.lodogame.game.dao;

import com.lodogame.model.UserPaymentRewardLog;

public interface UserPaymentRewardLogDao {

	public void add(UserPaymentRewardLog userPaymentRewardLog);

	public int getUserPaymentRewardLogCount(String userId, int amountF);

}
