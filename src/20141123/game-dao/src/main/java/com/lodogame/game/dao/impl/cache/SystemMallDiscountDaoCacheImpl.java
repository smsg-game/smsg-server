package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.SystemMallDiscountDao;
import com.lodogame.game.dao.impl.mysql.SystemMallDiscountDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemMallDiscountActivity;
import com.lodogame.model.SystemMallDiscountItems;

public class SystemMallDiscountDaoCacheImpl implements SystemMallDiscountDao, ReloadAble {

	private SystemMallDiscountDaoMysqlImpl systemMallDiscountDaoMysqlImpl;

	private List<SystemMallDiscountItems> cache = new ArrayList<SystemMallDiscountItems>();

	@Override
	public List<SystemMallDiscountItems> getDiscountItems(String activityId) {
		List<SystemMallDiscountItems> itemList = new ArrayList<SystemMallDiscountItems>();

		for (SystemMallDiscountItems discount : cache) {
			if (discount.getActivityId().equals(activityId)) {
				itemList.add(discount);
			}
		}
		return itemList;
	}

	@Override
	public List<SystemMallDiscountItems> getAllDiscountItems() {
		return cache;
	}

	private void initCache() {
		cache = systemMallDiscountDaoMysqlImpl.getAllDiscountItems();
	}

	@Override
	public void reload() {
		initCache();
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public List<SystemMallDiscountActivity> getAllActivity() {
		return systemMallDiscountDaoMysqlImpl.getAllActivity();
	}

	public void setSystemMallDiscountDaoMysqlImpl(SystemMallDiscountDaoMysqlImpl systemMallDiscountDaoMysqlImpl) {
		this.systemMallDiscountDaoMysqlImpl = systemMallDiscountDaoMysqlImpl;
	}

	@Override
	public boolean addDiscountActivity(SystemMallDiscountActivity activity) {
		return systemMallDiscountDaoMysqlImpl.addDiscountActivity(activity);
	}

	@Override
	public boolean addDiscountItem(SystemMallDiscountItems item) {
		boolean success = systemMallDiscountDaoMysqlImpl.addDiscountItem(item);
		if (success) {
			this.cache.add(item);
		}
		return success;
	}

	@Override
	public boolean delActivity(String activityId) {
		return systemMallDiscountDaoMysqlImpl.delActivity(activityId);
	}

	@Override
	public boolean delItems(String activityId) {
		boolean flag = false;
		if (systemMallDiscountDaoMysqlImpl.delItems(activityId)) {

			for (SystemMallDiscountItems item : cache) {
				if (item.getActivityId().equals(activityId)) {
					cache.remove(item);
				}
			}
		}

		return flag;
	}

	@Override
	public SystemMallDiscountItems getDiscountItem(String activityId, int mallId) {
		for(SystemMallDiscountItems item: cache) {
			if (item.getActivityId().equals(activityId) && item.getMallId() == mallId) {
				return item;
			}
		}
		return null;
	}

}
