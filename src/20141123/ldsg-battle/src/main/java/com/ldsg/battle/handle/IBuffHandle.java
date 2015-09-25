package com.ldsg.battle.handle;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.role.Hero;

public interface IBuffHandle {

	/**
	 * 创建buff
	 * 
	 * @param effect
	 * @param hero
	 * @param target
	 * @param context
	 * @param critValue
	 * @return
	 */
	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue);

	/**
	 * 创建buff，用于拥有后续效果的效果使用
	 * 
	 * @param effect
	 * @param hero
	 * @param target
	 * @param context
	 * @param critValue
	 * @return
	 */
	public Buff create(Effect effect, Buff prevBuff, Hero hero, Hero target, Context context, int critValue);

	/**
	 * 处理buff
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	public void handle(Hero hero, Buff buff, Context context);

	/**
	 * 处理buff移除
	 * 
	 * @param hero
	 * @param buff
	 */
	public void handleRemove(Hero hero, Buff buff);

	/**
	 * 获取buff数值
	 * 
	 * @param buff
	 * @param attribute
	 * @return
	 */
	public double getBuffValue(Context context, Buff buff, String attribute);

	/**
	 * 获取buff提升值
	 * 
	 * @param context
	 * @param buff
	 * @param attribute
	 * @return
	 */
	public double getBuffAddRatio(Context context, Hero hero, Buff buff, String attribute);

	/**
	 * 获取属性
	 * 
	 * @return
	 */
	public String getAttribute();
}
