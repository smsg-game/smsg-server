package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class AddPhysicsDefenseHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.PHYSICS_DEFENSE;
	}

}
