package com.ldsg.battle.engine;

import com.ldsg.battle.Context;

public interface Round {

	public void execute(Context context);

	public Round getNextRound();

	public boolean hashNextRound();
}
