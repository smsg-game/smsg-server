package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class ReducePhysicsDefenseHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.PHYSICS_DEFENSE;
	}

}
