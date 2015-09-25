package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.FactionDao;
import com.lodogame.model.Faction;



public class FactionDaoCacheImpl implements FactionDao {

	private FactionDao factionDaoMysqlImpl;

	private Map<Integer, Faction> factionMap = new HashMap<Integer, Faction>();
	
	private Map<String, Faction> factionStrMap = new HashMap<String, Faction>();
 	
	public void setFactionDaoMysqlImpl(FactionDao factionDaoMysqlImpl) {
		this.factionDaoMysqlImpl = factionDaoMysqlImpl;
	}

	@Override
	public boolean createFaction(Faction faction) {
		return this.factionDaoMysqlImpl.createFaction(faction);
	}

	@Override
	public Faction getFactionByName(String factionName) {
		if (!factionStrMap.containsKey(factionName)) {
			Faction faction = this.factionDaoMysqlImpl.getFactionByName(factionName);
			if (faction != null){
				factionStrMap.put(factionName, faction);
			}
		}
		return factionStrMap.get(factionName);
	}

	@Override
	public Faction getFactionByFid(int factionId) {
		if (!factionMap.containsKey(factionId)) {
			Faction faction = this.factionDaoMysqlImpl.getFactionByFid(factionId);
			if (faction != null) {
				factionMap.put(factionId, faction);
			}
		}
		return factionMap.get(factionId);
	}

	@Override
	public List<Faction> getFactionByPage(int start, int end) {
		return this.factionDaoMysqlImpl.getFactionByPage(start, end);
	}

	@Override
	public boolean saveFactionNotice(int factionId, String factionNotice) {
	    boolean succ = this.factionDaoMysqlImpl.saveFactionNotice(factionId, factionNotice);
	    if (succ) {
	    	if (factionMap.containsKey(factionId)) {
	    		factionMap.get(factionId).setFactionNotice(factionNotice);
	    	}
	    	Faction faction = getFactionByFid(factionId);
	    	
	    	if (factionStrMap.containsKey(faction.getFactionName())) {
	    		factionStrMap.get(faction.getFactionName()).setFactionNotice(factionNotice);
	    	}

	    }
	     
		return succ;
	}

	@Override
	public boolean updateFactionMemberNum(int factionId, int num) {
		boolean succ =this.factionDaoMysqlImpl.updateFactionMemberNum(factionId, num);
		if (succ) {
	    	if (factionMap.containsKey(factionId)) {
	    		factionMap.remove(factionId);
	    	}
	    	Faction faction = getFactionByFid(factionId);
	    	
	    	if (factionStrMap.containsKey(faction.getFactionName())) {
	    		factionMap.remove(factionId);
	    	}
		}
		return false;
	}

	@Override
	public boolean deleteFaction(int factionId,String factionName) {
		boolean succ = this.factionDaoMysqlImpl.deleteFaction(factionId,factionName);
		if(succ){
			if(factionStrMap.containsKey(factionName)){
				factionStrMap.remove(factionName);
			}
			if(factionMap.containsKey(factionId)){
				factionMap.remove(factionId);
			}
		}
		
		return succ;
	}

}
