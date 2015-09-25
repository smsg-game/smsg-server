package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class AddSpeedHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.SPEED;
	}

}
