package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemDeifyDefine;

public interface SystemDeifyDefineDao {

	public SystemDeifyDefine get(int deifyId);

	public List<SystemDeifyDefine> getByHeroId(int heroId);

	public SystemDeifyDefine getByHeroIdAndType(int heroId, int type);

}
