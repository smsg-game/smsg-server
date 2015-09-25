package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.ldsg.bo.SystemGoldSetBO;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.GoldSetService;
import com.lodogame.model.SystemGoldSet;

public class GoldSetServiceImpl implements GoldSetService {

	@Autowired
	private SystemGoldSetDao systemGoldSetDao;

	@Override
	public List<SystemGoldSetBO> getGoldSetList() {

		List<SystemGoldSet> systemGoldSetList = this.systemGoldSetDao.getList(2);

		List<SystemGoldSetBO> systemGoldSetBOList = new ArrayList<SystemGoldSetBO>();

		Date now = new Date();

		// 活动的充值信息
		for (SystemGoldSet systemGoldSet : systemGoldSetList) {

			if (now.before(systemGoldSet.getStartTime()) || now.after(systemGoldSet.getEndTime())) {// 不在时间段的不要
				continue;
			}
			systemGoldSetBOList.add(BOHelper.createSystemGoldSetBO(systemGoldSet));
		}

		if (systemGoldSetBOList.isEmpty()) {

			systemGoldSetList = this.systemGoldSetDao.getList(1);
			for (SystemGoldSet systemGoldSet : systemGoldSetList) {
				systemGoldSetBOList.add(BOHelper.createSystemGoldSetBO(systemGoldSet));
			}
		}

		return systemGoldSetBOList;
	}

	@Override
	public SystemGoldSet getByPayAmount(double amount) {

		// 活动套餐
		SystemGoldSet systemGoldSet = this.systemGoldSetDao.getByPayAmount(2, amount);
		if (systemGoldSet != null) {

			Date now = new Date();

			if (now.after(systemGoldSet.getStartTime()) && now.before(systemGoldSet.getEndTime())) {// 在有效时间内
				return systemGoldSet;
			}
		}

		// 普通套餐
		return this.systemGoldSetDao.getByPayAmount(1, amount);
	}

	@Override
	public SystemGoldSet getMaxGoldSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
