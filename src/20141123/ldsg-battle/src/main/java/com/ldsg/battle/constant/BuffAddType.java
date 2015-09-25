package com.ldsg.battle.constant;

public class BuffAddType {

	/**
	 * 修改类型 直接改基础值(可以累加)
	 */
	public final static int ADD_BASE = 1;

	/**
	 * 改修正值，不可能累加
	 */
	public final static int ADD_ONE = 2;

	/**
	 * 改临时值(有条件的那些buff,比如如果a物攻高于b,则xxxx这种)
	 */
	public final static int ADD_TEMP = 3;

}
