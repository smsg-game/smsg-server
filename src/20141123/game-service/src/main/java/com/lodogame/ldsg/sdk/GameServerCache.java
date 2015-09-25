package com.lodogame.ldsg.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.sdk.AdminApiSdk;
import com.lodogame.model.GameServer;

public class GameServerCache {
	
	private static class GameServerCacheHolder{
		private static GameServerCache gameServerCache = new GameServerCache();
	}
	
	private  Map<String,List<GameServer>> gameServerMap;
	
	private GameServerCache(){
	
	}
	
	public static GameServerCache getInstance(){
		return GameServerCacheHolder.gameServerCache;
	}
	
	
	public List<GameServer> getGameServers(String partnerId){
		List<GameServer> serverList = null;
		if (gameServerMap != null && gameServerMap.size() > 0) {
			serverList = gameServerMap.get(partnerId);
		} else {
			gameServerMap = new HashMap<String, List<GameServer>>();
		}
		if (serverList == null || serverList.size() == 0) {
			serverList = AdminApiSdk.getInstance().loadServers(partnerId);
			if (serverList == null) {
				serverList = new ArrayList<GameServer>();
			}
			gameServerMap.put(partnerId, serverList);
		}
		return serverList;
	}
	
	public void reload(){
		if(gameServerMap != null){
			gameServerMap.clear();
		}
	}

}
