package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class BeforeHurtCalculateEvent extends BaseEvent implements IEvent {

	public BeforeHurtCalculateEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public BeforeHurtCalculateEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}
