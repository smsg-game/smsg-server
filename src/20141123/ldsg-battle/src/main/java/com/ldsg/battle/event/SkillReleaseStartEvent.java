package com.ldsg.battle.event;

import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class SkillReleaseStartEvent extends BaseEvent implements IEvent {

	public SkillReleaseStartEvent(Context context, Hero hero) {
		super(context, hero);
	}

	public SkillReleaseStartEvent(Context context, List<Hero> heroList) {
		super(context, heroList);
	}

}