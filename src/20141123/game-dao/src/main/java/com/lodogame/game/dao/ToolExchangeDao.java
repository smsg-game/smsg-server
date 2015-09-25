package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;

import com.lodogame.model.ToolExchange;
import com.lodogame.model.UserToolExchangeLog;

public interface ToolExchangeDao {

	public int getUserTimes(String userId, int toolExchangeId, Date startTime, Date endTime);

	/**
	 * 查询某一兑换最大可兑换次数
	 */
	public int getTimes(int toolExchangeId);

	public int getNum();

	public boolean updateExchangeCount(String userId, int toolExchangeId, int times);

	public boolean addExchangeCount(UserToolExchangeLog userToolExchange);

	ToolExchange getExchangeItems(int toolExchangeId);

	List<ToolExchange> getAll();

}
