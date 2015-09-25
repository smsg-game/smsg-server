package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class ReduceStrategyDefenseHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.STRATEGY_DEFENSE;
	}

}
