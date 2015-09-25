package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class AddDodgeHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.DODGE;
	}

}
