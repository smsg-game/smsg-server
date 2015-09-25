package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemDeifyNode;


/**
 *  化神节点信息
 * 
 */
public interface SystemDeifyNodeDao {

	/**
	 *  化神指定节点信息
	 */
	public SystemDeifyNode getSystemDeifyNodeById(int systemHeroId, int systemDeifyNodeId);

	/**
	 *  获取所以化神信息
	 */
	public List<SystemDeifyNode> getSystemDeifyNode(int systemHeroId);

}
