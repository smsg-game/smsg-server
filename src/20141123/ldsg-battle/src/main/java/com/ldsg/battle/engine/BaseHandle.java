package com.ldsg.battle.engine;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.handle.IBuffHandle;
import com.ldsg.battle.role.Hero;

public abstract class BaseHandle implements IBuffHandle {
	public Buff create(Effect effect, Buff prevBuff, Hero hero, Hero target, Context context, int critValue) {
		return null;
	}

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		return null;
	}

	@Override
	public String getAttribute() {
		return null;
	}

	public double getBuffValue(Context context, Buff buff, String attribute) {
		return 0;
	}

	@Override
	public double getBuffAddRatio(Context context, Hero hero, Buff buff, String attribute) {
		return 0;
	}

}
