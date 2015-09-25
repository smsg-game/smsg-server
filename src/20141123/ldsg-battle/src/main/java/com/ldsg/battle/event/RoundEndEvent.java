package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class RoundEndEvent extends BaseEvent implements IEvent {

	public RoundEndEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public RoundEndEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}
