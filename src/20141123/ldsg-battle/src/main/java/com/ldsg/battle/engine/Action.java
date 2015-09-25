package com.ldsg.battle.engine;

import com.ldsg.battle.Context;

public interface Action {

	// <summary>
	// </summary>
	// <param name="context">战斗上下文</param>
	public void doAction(Context context);

	// <summary>
	// 获取下一个动作
	// </summary>
	// <returns></returns>
	public Action getNextAction();

	// <summary>
	// 是否有下一行动
	// </summary>
	// <returns></returns>
	public boolean hasNextAction();
}
