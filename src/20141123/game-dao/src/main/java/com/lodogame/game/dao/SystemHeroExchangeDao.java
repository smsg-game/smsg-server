package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemHeroExchange;

public interface SystemHeroExchangeDao {

	public List<SystemHeroExchange> getList(int week);

	public SystemHeroExchange get(int exchangeHeroId);

}
