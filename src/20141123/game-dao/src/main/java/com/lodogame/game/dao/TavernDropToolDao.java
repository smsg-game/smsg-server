package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;

public interface TavernDropToolDao {

	/**
	 * 获取酒馆掉落道具列表
	 * 
	 * @param type
	 *            抽奖类型
	 * @return
	 */
	public List<TavernDropTool> getTavernDropToolList(int type);

	/**
	 * 获取修改正掉落的武将
	 * 
	 * @param type
	 * @return
	 */
	public List<TavernAmendDropTool> getTavernAmendDropToolList(int type);

}
