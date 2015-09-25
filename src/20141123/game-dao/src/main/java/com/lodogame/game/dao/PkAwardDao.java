package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.PkAward;

public interface PkAwardDao {

	public List<PkAward> getAll();

	public PkAward getById(int awardId);

	/**
	 * 添天奖励是否已经发放
	 * 
	 * @param date
	 * @return
	 */
	public boolean isAwardSended(String date);

	/**
	 * 添天奖励是否已经发放
	 * 
	 * @param date
	 * @return
	 */
	public boolean addAwardSendLog(String date);
}
