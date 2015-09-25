package com.lodogame.game.dao;

import com.lodogame.model.RuntimeData;

public interface RuntimeDataDao {

	public boolean add(RuntimeData rumtimeData);

	public RuntimeData get(String key);

	public boolean inc(String key);

	public boolean set(String key, long value);
}
