package com.lodogame.ldsg.constants;

public class LogTypes {
  //装备操作日志类型 ( 1.强化2.进阶.3.获取 4.出售)
	public static final int EQUIPMENT_STRENTH = 1;
	public static final int EQUIPMENT_STEPUP = 2;
	public static final int EQUIPMENT_GET = 3;
	public static final int EQUIPMENT_SALES = 4;
  //武将操作日志类型（1.获取 2.吞噬 3.被吞噬 4.进阶  5.出售 6被进阶（因为进阶也需要消耗武将）） 7兑换
	public static final int HERO_GET = 1;
	public static final int HERO_DEVOUR = 2;
	public static final int HERO_BE_DEVOUR = 3;
	public static final int HERO_STEP_UP = 4;
	public static final int HERO_SALES = 5;
	public static final int HERO_BE_STEP_UP = 6;
	public static final int HERO_EXCHANGE = 7;
	
 //战斗日志类型 type
	//关卡
	public static final int BATTLE_TYPE_SCENCE = 1;
	//PK争霸赛
	public static final int BATTLE_TYPE_PK = 2;
	//塔战
	public static final int BATTLE_TYPE_TOWER = 3;
	//百人斩
	public static final int BATTLE_TYPE_ARANE = 4;
	//BOSS战
	public static final int BATTLE_TYPE_BOSS = 5;
}
