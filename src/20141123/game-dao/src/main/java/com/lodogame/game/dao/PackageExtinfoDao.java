package com.lodogame.game.dao;

import com.lodogame.model.PackageExtinfo;

public interface PackageExtinfoDao {
	public PackageExtinfo getByVersion(String version, String partnerId);
}
