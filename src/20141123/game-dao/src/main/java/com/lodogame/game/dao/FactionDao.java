package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.Faction;

/**
 * 帮派dao
 * 
 * @author zyz
 * 
 */
public interface FactionDao {


	/**
	 * 增加帮派
	 * 
	 * @param userId
	 * @param factionName
	 * @return
	 */
	public boolean createFaction(Faction faction);
	
	/**
	 * 根据名字去帮派信息
	 * 
	 * @param userId
	 * @param factionName
	 * @return
	 */
	public Faction getFactionByName(String factionName);
	
	/**
	 * 获取指定帮派的信息
	 * 
	 * @param factionId
	 * @return
	 */
	public Faction getFactionByFid(int factionId);
	
	/**
	 * 获取指定页帮派的信息
	 * 
	 * @param fid
	 * @return
	 */
	public List<Faction> getFactionByPage(int start, int end);
	
	/**
	 * 保存帮派公告
	 * @param factionId
	 * @param factionNotice
	 * @return
	 */
	public boolean saveFactionNotice(int factionId, String factionNotice);
	

	/**
	 * 增加减少帮派成员
	 * 
	 * @param userId
	 * @param factionName
	 * @return
	 */
	public boolean updateFactionMemberNum(int factionId, int num);
	
	/**
	 * 解散帮派
	 * @param factionId
	 * @return
	 */
	public boolean deleteFaction(int factionId,String factionName);
	
}