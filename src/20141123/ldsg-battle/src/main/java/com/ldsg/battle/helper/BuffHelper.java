package com.ldsg.battle.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.handle.BuffHandleFactory;
import com.ldsg.battle.handle.IBuffHandle;
import com.ldsg.battle.role.Hero;
import com.ldsg.battle.util.Json;
import com.ldsg.battle.vo.BuffVO;

public class BuffHelper {

	/**
	 * 从效果创建buff
	 * 
	 * @param effect
	 * @return
	 */
	public static Buff Effect2Buff(Effect effect) {
		Buff buff = new Buff();
		buff.setEffect(effect);
		buff.setRound(effect.getRound());
		return buff;
	}

	/**
	 * buff排序f
	 * 
	 * @param buffVOList
	 */
	public static void sortBuff(List<BuffVO> buffVOList) {

		// 排序
		Collections.sort(buffVOList, new Comparator<BuffVO>() {

			public int compare(BuffVO o1, BuffVO o2) {

				if (o1.getTriggerType() > o2.getTriggerType()) {
					return 1;
				} else if (o1.getTriggerType() < o2.getTriggerType()) {
					return -1;
				} else {
					return 0;
				}
			}

		});

		// 排序
		Collections.sort(buffVOList, new Comparator<BuffVO>() {

			public int compare(BuffVO o1, BuffVO o2) {

				if (o1.isAddLife() && o2.isAddLife()) {// 两个都是加血效果，血量大的在后面
					if (o1.getLife() > o2.getLife()) {
						return 1;
					} else {
						return -1;
					}
				} else if (o1.isReduceLife() && o2.isReduceLife()) {// 两个都是掉血，血少的在后面
					if (o1.getLife() < o2.getLife()) {
						return 1;
					} else {
						return -1;
					}
				} else {
					return 0;
				}
			}

		});

	}

	/**
	 * 获取buff数值
	 * 
	 * @param buffList
	 * @return
	 */
	public static double getBuffValue(Context context, List<Buff> buffList, String attribute) {

		double value = 0;

		for (Buff buff : buffList) {
			IBuffHandle handle = BuffHandleFactory.getBuffHandler(buff.getEffect().getFunId());
			double addVal = handle.getBuffValue(context, buff, attribute);
			if (addVal == 0) {
				continue;
			} else {
				value += addVal;
			}

		}

		return value;
	}

	/**
	 * 属性提升百分比
	 * 
	 * @param context
	 * @param buffList
	 * @param attribute
	 * @return
	 */
	public static double getBuffAddRatio(Context context, Hero hero, List<Buff> buffList, String attribute) {

		double addRaion = 0;

		for (Buff buff : buffList) {
			IBuffHandle handle = BuffHandleFactory.getBuffHandler(buff.getEffect().getFunId());
			double addVal = handle.getBuffAddRatio(context, hero, buff, attribute);
			if (addVal == 0) {
				continue;
			} else {
				addRaion += addVal;
			}

		}

		return addRaion;
	}

	public static void main(String[] args) {

		// [u'a2', 276, 9007, -2, 5652], [u'a5', 44, 9007, -2, 968], [u'a2',
		// 442, 184, -2, 5376]]

		List<BuffVO> buffVOList = new ArrayList<BuffVO>();
		BuffVO buffVO1 = new BuffVO();
		buffVO1.setAddLife(true);
		buffVO1.setLife(5652);
		buffVO1.setTriggerType(5);
		buffVOList.add(buffVO1);

		BuffVO buffVO2 = new BuffVO();
		buffVO2.setAddLife(false);
		buffVO2.setLife(968);
		buffVO2.setTriggerType(3);
		buffVOList.add(buffVO2);

		BuffVO buffVO3 = new BuffVO();
		buffVO3.setAddLife(true);
		buffVO3.setLife(5376);
		buffVO3.setTriggerType(5);
		buffVOList.add(buffVO3);

		BuffHelper.sortBuff(buffVOList);

		System.out.println(Json.toJson(buffVOList));

	}
}
