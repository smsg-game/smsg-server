package com.ldsg.battle.handle;

import com.ldsg.battle.Context;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.role.Hero;

public class AddMaxLifeHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public void afterModify(Context context, Hero hero, double value) {
		hero.addAttribute(context, AttrConstant.LIFE, value);
	}

	@Override
	public String getAttribute() {
		return AttrConstant.MAX_LIFE;
	}

}
