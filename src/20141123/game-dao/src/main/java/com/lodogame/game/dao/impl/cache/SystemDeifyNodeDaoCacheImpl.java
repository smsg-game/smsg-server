package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.SystemDeifyNodeDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemDeifyNode;

public class SystemDeifyNodeDaoCacheImpl implements SystemDeifyNodeDao, ReloadAble {


	private Map<String, SystemDeifyNode> systemDeifyNodeMap = new HashMap<String, SystemDeifyNode>();
	
	private Map<Integer, List<SystemDeifyNode>> systemDeifyNodeListMap = new HashMap<Integer, List<SystemDeifyNode>>();
	
	private  SystemDeifyNodeDao systemDeifyNodeDaoMysqlImpl;
	
	public void setSystemDeifyNodeDaoMysqlImpl(
			SystemDeifyNodeDao systemDeifyNodeDaoMysqlImpl) {
		this.systemDeifyNodeDaoMysqlImpl = systemDeifyNodeDaoMysqlImpl;
	}

	@Override
	public SystemDeifyNode getSystemDeifyNodeById(int heroId,
			int systemDeifyNodeId) {
		String key = heroId + "_" + systemDeifyNodeId;
		if (!systemDeifyNodeMap.containsKey(key)) {
			SystemDeifyNode systemDeifyNode = this.systemDeifyNodeDaoMysqlImpl.getSystemDeifyNodeById(heroId, systemDeifyNodeId);
			this.systemDeifyNodeMap.put(key, systemDeifyNode);
		}
		return systemDeifyNodeMap.get(key);
	}

	@Override
	public List<SystemDeifyNode> getSystemDeifyNode(int systemHeroId) {
		if (!systemDeifyNodeListMap.containsKey(systemHeroId)) {
			List<SystemDeifyNode> systemDeifyNodeList = this.systemDeifyNodeDaoMysqlImpl.getSystemDeifyNode(systemHeroId);
			this.systemDeifyNodeListMap.put(systemHeroId, systemDeifyNodeList);
		}
		return systemDeifyNodeListMap.get(systemHeroId);
	}
	
	@Override
	public void reload() {
		systemDeifyNodeMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
