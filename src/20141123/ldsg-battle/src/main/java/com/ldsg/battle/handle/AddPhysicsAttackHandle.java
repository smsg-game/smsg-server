package com.ldsg.battle.handle;

import com.ldsg.battle.constant.AttrConstant;

public class AddPhysicsAttackHandle extends ModifyAttributeHandle implements IBuffHandle {

	@Override
	public String getAttribute() {
		return AttrConstant.PHYSICS_ATTACK;
	}

}
