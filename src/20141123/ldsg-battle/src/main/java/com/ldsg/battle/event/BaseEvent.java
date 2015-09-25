package com.ldsg.battle.event;

import java.util.ArrayList;
import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.role.Hero;

public class BaseEvent implements IEvent {

	private Context context;
	private List<Hero> heroList;

	public BaseEvent(Context context, List<Hero> heroList) {
		this.context = context;
		this.heroList = heroList;
	}

	public BaseEvent(Context context, Hero hero) {
		this.context = context;
		this.heroList = new ArrayList<Hero>();
		heroList.add(hero);
	}

	public Context getContext() {
		return this.context;
	}

	public List<Hero> getHeroList() {
		return this.heroList;
	}
}
