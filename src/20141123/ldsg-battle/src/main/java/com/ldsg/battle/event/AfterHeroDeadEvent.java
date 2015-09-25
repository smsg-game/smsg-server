package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class AfterHeroDeadEvent extends BaseEvent implements IEvent {

	public AfterHeroDeadEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public AfterHeroDeadEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}