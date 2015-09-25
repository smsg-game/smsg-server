package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemGiftbagDao;
import com.lodogame.game.dao.impl.mysql.SystemGiftbagDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.GiftbagDropTool;
import com.lodogame.model.SystemGiftbag;

public class SystemGiftbagDaoCacheImpl implements SystemGiftbagDao, ReloadAble {

	private SystemGiftbagDaoMysqlImpl systemGiftbagDaoMysqlImpl;

	// 键值为 type list内为所有该type下的值
	private Map<Integer, List<SystemGiftbag>> giftCache = new ConcurrentHashMap<Integer, List<SystemGiftbag>>();

	// 键为giftbagId list为属于该id下的bean
	private Map<Integer, List<GiftbagDropTool>> giftBagDropCache = new ConcurrentHashMap<Integer, List<GiftbagDropTool>>();

	@Override
	public SystemGiftbag getFirstPayGiftbag() {

		List<SystemGiftbag> list = giftCache.get(2);
		if (list != null) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public SystemGiftbag getVipGiftbag(int vipLevel) {

		List<SystemGiftbag> list = giftCache.get(1);
		if (list != null) {
			for (SystemGiftbag systemGiftbag : list) {
				if (systemGiftbag.getVipLevel() == vipLevel) {
					return systemGiftbag;
				}
			}
		}
		return null;
	}

	@Override
	public SystemGiftbag getCodeGiftBag(int subType, int giftBagType) {

		List<SystemGiftbag> list = giftCache.get(giftBagType);
		if (list != null) {
			for (SystemGiftbag systemGiftbag : list) {
				if (systemGiftbag.getSubType() == subType) {
					return systemGiftbag;
				}
			}
		}
		return null;
	}

	@Override
	public List<GiftbagDropTool> getGiftbagDropToolList(int giftbagId) {
		if (giftBagDropCache.containsKey(giftbagId)) {
			return giftBagDropCache.get(giftbagId);
		}
		return new ArrayList<GiftbagDropTool>();
	}

	@Override
	public SystemGiftbag getOnlineGiftBag(int subType) {

		List<SystemGiftbag> list = giftCache.get(4);
		if (list != null) {
			for (SystemGiftbag systemGiftbag : list) {
				if (systemGiftbag.getSubType() == subType) {
					return systemGiftbag;
				}
			}
		}
		return null;
	}

	@Override
	public void reload() {
		initCache();
	}

	private void initCache() {
		giftCache.clear();
		List<SystemGiftbag> list = systemGiftbagDaoMysqlImpl.getAll();
		List<SystemGiftbag> temp = null;
		for (SystemGiftbag systemGiftbag : list) {
			if (giftCache.containsKey(systemGiftbag.getType())) {
				giftCache.get(systemGiftbag.getType()).add(systemGiftbag);
			} else {
				temp = new ArrayList<SystemGiftbag>();
				temp.add(systemGiftbag);
				giftCache.put(systemGiftbag.getType(), temp);
			}
		}
		giftBagDropCache.clear();
		List<GiftbagDropTool> listDropTool = systemGiftbagDaoMysqlImpl.getAllDropTool();
		List<GiftbagDropTool> tempDropTool = null;
		for (GiftbagDropTool giftbagDropTool : listDropTool) {
			if (giftBagDropCache.containsKey(giftbagDropTool.getGiftbagId())) {
				giftBagDropCache.get(giftbagDropTool.getGiftbagId()).add(giftbagDropTool);
			} else {
				tempDropTool = new ArrayList<GiftbagDropTool>();
				tempDropTool.add(giftbagDropTool);
				giftBagDropCache.put(giftbagDropTool.getGiftbagId(), tempDropTool);
			}
		}

	}

	public void setSystemGiftbagDaoMysqlImpl(SystemGiftbagDaoMysqlImpl systemGiftbagDaoMysqlImpl) {
		this.systemGiftbagDaoMysqlImpl = systemGiftbagDaoMysqlImpl;
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public SystemGiftbag getOncePayReward(int type, int subType) {
		List<SystemGiftbag> list = giftCache.get(type);
		if (list != null) {
			for (SystemGiftbag systemGiftbag : list) {
				if (systemGiftbag.getSubType() == subType) {
					return systemGiftbag;
				}
			}
		}
		return null;
	}

	@Override
	public SystemGiftbag getTotalPayReward(int type, int subType) {
		return systemGiftbagDaoMysqlImpl.getTotalPayReward(type, subType);
	}
}
