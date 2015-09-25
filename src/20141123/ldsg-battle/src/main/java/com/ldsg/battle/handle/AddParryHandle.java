package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class AddParryHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.PARRY;
	}

}
