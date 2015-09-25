package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class ActionStartEvent extends BaseEvent implements IEvent {

	public ActionStartEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public ActionStartEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}
