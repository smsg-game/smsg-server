package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.WarAward;

public interface WarAwardDao {
	public WarAward get(int awardId);
	public List<WarAward> getAll();
}
