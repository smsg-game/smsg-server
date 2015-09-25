package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class AddCounterRateHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.COUNTER_RATE;
	}

}
