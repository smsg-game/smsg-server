package com.ldsg.battle.constant;

public class BattleStatus {

	// <summary>
	// 战斗开始
	// </summary>

	public final static int BATTLE_START = 1;
	// <summary>
	// 回合开始
	// </summary>
	public final static int ROUND_START = 2;

	// <summary>
	// 行动开始
	// </summary>
	public final static int ACTION_START = 3;

	// <summary>
	// 技能发动前
	// </summary>
	public final static int BEFORE_RELEASE = 4;

	// <summary>
	// 行动后
	// </summary>
	public final static int AFTER_ACTION = 5;

	// <summary>
	// 闪避
	// </summary>
	public final static int PARRY = 6;

	// <summary>
	// 被闪避
	// </summary>
	public final static int BE_PARRY = 7;

	// <summary>
	// 爆击
	// </summary>
	public final static int CRIT = 8;

	// <summary>
	// 被爆击
	// </summary>
	public final static int BE_CRIT = 9;

	// <summary>
	// 挡格
	// </summary>
	public final static int DODGE = 10;

	// <summary>
	// 被挡格
	// </summary>
	public final static int BE_DODGE = 11;

	// <summary>
	// 死亡前
	// </summary>
	public final static int BEFORE_DEAD = 12;

	// <summary>
	// 死亡后
	// </summary>
	public final static int AFTER_DEAD = 13;

	// <summary>
	// 计算伤害前
	// </summary>
	public final static int CALCULATE_INJURY = 14;
}
