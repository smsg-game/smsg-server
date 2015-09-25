package com.lodogame.ldsg.constants;

import java.io.Serializable;

/**
 * 任务目录类型
 * 
 * @author jacky
 * 
 */
public class TaskTargetType implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 击败怪物部队
	 */
	public final static int TASK_TYPE_PASS_FORCES = 1;

	/**
	 * 每日登录
	 */
	public final static int TASK_TYPE_DAILY_LOGIN = 2;

	/**
	 * 获得武将
	 */
	public final static int TASK_TYPE_GET_HERO = 3;

	/**
	 * 获得装备
	 */
	public final static int TASK_TYPE_GET_EQUIP = 4;

	/**
	 * 酒馆招将
	 */
	public final static int TASK_TYPE_TAVERN_DRAW = 5;

	/**
	 * 单次充值
	 */
	public final static int TASK_TYPE_SINGLE_PAYMENT = 7;

	/**
	 * 累计充值
	 */
	public final static int TASK_TYPE_TOTAL_PAYMENT = 8;

	/**
	 * VIP任务
	 */
	public final static int TASK_TYPE_TO_BE_VIIP = 9;

	/**
	 * 达到某指定的等级
	 */
	public final static int TASK_TYPE_TO_SPC_LEVEL = 10;

	/**
	 * vip登录
	 */
	public final static int TASK_TYPE_VIP_LOGIN = 11;
	
	/**
	 * 邀请奖励
	 */
	public final static int TASK_TYPE_INVITED_REGISTER = 12;
	
	/**
	 * facebook点赞
	 */
	public final static int FACEBOOK_COMMENT = 13;
}
