package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class AddStrategyAttackHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.STRATEGY_ATTACK;
	}

}
