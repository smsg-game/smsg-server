package com.ldsg.battle.engine;

import com.ldsg.battle.Context;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.event.RoundEndEvent;

public abstract class AbstractRound implements Round {

	protected Context context;

	/**
	 * 执行一个回合的战斗
	 * 
	 * @param context
	 */
	public void execute(Context context) {
		this.context = context;

		// 切换到回合开始前
		this.context.switchStatus(BattleStatus.ROUND_START);

		Boolean go = true;

		// 回合开始前可能有人已经挂掉了
		if (context.isFinish()) {
			go = false;
		}

		Action action = this.Action();

		while (go) {

			action.doAction(context);

			// 如果下一个行动存在,则切到下一个行动
			if (action.hasNextAction()) {
				action = action.getNextAction();
			} else {
				go = false;
			}

		}

		// 广播回合结束事件
		IEvent et = new RoundEndEvent(context, context.getHeroList());
		this.context.dispatchEvent(et);
	}

	public abstract Round getNextRound();

	public boolean hashNextRound() {
		if (this.context.isFinish()) {
			return false;
		}

		if (context.getRound() >= BattleConstant.MAX_ROUND) {
			this.context.setFinish(true);
			return false;
		}

		return true;
	}

	/**
	 * 返回当前动作
	 * 
	 * @return
	 */
	protected abstract Action Action();

}
