package com.lodogame.game.dao.reload;

public abstract class ReloadAbleBase implements ReloadAble {

	@Override
	public void init() {
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
