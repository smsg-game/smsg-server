package com.ldsg.battle.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.handle.BuffHandleFactory;
import com.ldsg.battle.handle.IBuffHandle;
import com.ldsg.battle.role.Hero;

/**
 * 状态更新监听器的基类
 * 
 * @author jacky
 * 
 */
public class BaseListener {

	private static Logger logger = Logger.getLogger(BaseListener.class);

	/**
	 * buf处理
	 * 
	 * @param context
	 * @param heroList
	 * @param status
	 */
	protected void handle(Context context, List<Hero> heroList, int status) {

		if (context.isFinish()) {
			logger.debug("战斗已经结束,不再处理buff");
			return;
		}

		for (Hero hero : heroList) {

			// buff
			List<Buff> buffList = clonBuffList(hero.getBuffList());

			// 排序
			Collections.sort(buffList, new Comparator<Buff>() {

				public int compare(Buff o1, Buff o2) {
					return o1.getType() - o2.getType();
				}

			});

			for (Buff buff : buffList) {
				if (!hero.getBuffList().contains(buff)) {
					continue;
				}
				this._handle(hero, buff, context, status);
			}

		}
	}

	// <summary>
	// 拷贝一份buff列表(到时移走)
	// </summary>
	// <param name="buffList"></param>
	// <returns></returns>
	private List<Buff> clonBuffList(List<Buff> buffList) {
		List<Buff> clonBuffList = new ArrayList<Buff>();
		for (Buff buff : buffList) {
			clonBuffList.add(buff);
		}

		return clonBuffList;
	}

	/**
	 * buff处理
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 * @param status
	 */
	private void _handle(Hero hero, Buff buff, Context context, int status) {

		if (buff.getEffect().getImmediately() == 1) {
			// 立即处理的效果不需要重新处理，这种效果的添加只是为了移除时减掉加成而已
			return;
		}

		Effect effect = buff.getEffect();

		String funID = effect.getFunId();

		// buff的触发时机
		int triggerType = effect.getTriggerType();

		// 如果触发时机等于当前的时机,则buff触发
		if (triggerType == status) {
			IBuffHandle handler = BuffHandleFactory.getBuffHandler(funID);
			handler.handle(hero, buff, context);
		}
	}

}
