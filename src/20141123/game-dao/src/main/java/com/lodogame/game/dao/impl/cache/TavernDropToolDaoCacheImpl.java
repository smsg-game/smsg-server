package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.TavernDropToolDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;

public class TavernDropToolDaoCacheImpl implements TavernDropToolDao, ReloadAble {

	private Map<Integer, List<TavernDropTool>> tavernDropToolMap = new ConcurrentHashMap<Integer, List<TavernDropTool>>();

	private Map<Integer, List<TavernAmendDropTool>> tavernAmendDropToolMap = new ConcurrentHashMap<Integer, List<TavernAmendDropTool>>();

	private TavernDropToolDao tavernDropToolDaoMysqlImpl;

	public void setTavernDropToolDaoMysqlImpl(TavernDropToolDao tavernDropToolDaoMysqlImpl) {
		this.tavernDropToolDaoMysqlImpl = tavernDropToolDaoMysqlImpl;
	}

	@Override
	public List<TavernDropTool> getTavernDropToolList(int type) {

		if (this.tavernDropToolMap.containsKey(type)) {
			return this.tavernDropToolMap.get(type);
		} else {
			List<TavernDropTool> tavernDropToolList = this.tavernDropToolDaoMysqlImpl.getTavernDropToolList(type);
			tavernDropToolMap.put(type, tavernDropToolList);
			return tavernDropToolList;
		}

	}

	@Override
	public List<TavernAmendDropTool> getTavernAmendDropToolList(int type) {

		if (this.tavernAmendDropToolMap.containsKey(type)) {
			return this.tavernAmendDropToolMap.get(type);
		} else {
			List<TavernAmendDropTool> tavernAmendDropToolList = this.tavernDropToolDaoMysqlImpl.getTavernAmendDropToolList(type);
			tavernAmendDropToolMap.put(type, tavernAmendDropToolList);
			return tavernAmendDropToolList;
		}

	}

	@Override
	public void reload() {
		tavernDropToolMap.clear();
		tavernAmendDropToolMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
