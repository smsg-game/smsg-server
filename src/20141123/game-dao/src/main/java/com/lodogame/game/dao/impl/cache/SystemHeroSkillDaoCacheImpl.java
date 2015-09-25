package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.SystemHeroSkillDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemHeroSkill;

public class SystemHeroSkillDaoCacheImpl implements SystemHeroSkillDao, ReloadAble {

	private SystemHeroSkillDao systemHeroSkillDaoMysqlImpl;

	private Map<Integer, List<SystemHeroSkill>> heroSkillMap = new ConcurrentHashMap<Integer, List<SystemHeroSkill>>();

	public void setSystemHeroSkillDaoMysqlImpl(SystemHeroSkillDao systemHeroSkillDaoMysqlImpl) {
		this.systemHeroSkillDaoMysqlImpl = systemHeroSkillDaoMysqlImpl;
	}

	@Override
	public List<SystemHeroSkill> getHeroSkillList(int heroId) {

		if (heroSkillMap.containsKey(heroId)) {
			return heroSkillMap.get(heroId);
		}
		List<SystemHeroSkill> systemHeroSkillList;

		synchronized (SystemHeroSkillDaoCacheImpl.class) {

			systemHeroSkillList = this.systemHeroSkillDaoMysqlImpl.getHeroSkillList(heroId);
			if (systemHeroSkillList != null) {
				heroSkillMap.put(heroId, systemHeroSkillList);
			}
		}

		return systemHeroSkillList;
	}

	@Override
	public void reload() {
		heroSkillMap.clear();
	}

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
