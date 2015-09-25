package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class ActionEndEvent extends BaseEvent implements IEvent {

	public ActionEndEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public ActionEndEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}
