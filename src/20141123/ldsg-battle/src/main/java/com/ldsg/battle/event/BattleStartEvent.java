package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class BattleStartEvent extends BaseEvent implements IEvent {

	public BattleStartEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public BattleStartEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}