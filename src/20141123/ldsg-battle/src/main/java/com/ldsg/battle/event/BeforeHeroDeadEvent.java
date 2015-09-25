package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class BeforeHeroDeadEvent extends BaseEvent implements IEvent {

	public BeforeHeroDeadEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public BeforeHeroDeadEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}