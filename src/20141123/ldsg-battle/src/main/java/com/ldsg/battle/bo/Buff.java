package com.ldsg.battle.bo;

import com.ldsg.battle.role.Hero;

/**
 * buff
 * 
 * @author jacky
 * 
 */
public class Buff {

	// <summary>
	// 控制buff
	// </summary>
	public static int BUFF_TYPE_COL = 3;

	// <summary>
	// 减益buff
	// </summary>
	public static int BUFF_TYPE_DEC = 2;

	// <summary>
	// 增益buff
	// </summary>
	public static int BUFF_TYPE_INC = 1;

	// <summary>
	// 特殊的轮数,表示一直跟随的buff
	// </summary>
	public static int ROUND_CONTINUED = -1;

	// <summary>
	// 特殊的轮数,表示那种立马生效那种buff
	// </summary>
	public static int ROUND_IMMEDIATELY = -2;

	private int type;

	private int round;

	private Effect effect;

	private long value;

	private long heroLife;

	/**
	 * 0数值
	 */
	private boolean isZeroVal = false;

	/**
	 * 前置Buff的数值
	 */
	private long prevValue;

	/**
	 * BUFF宿主
	 */
	private Hero target;

	/**
	 * 技能释放者
	 */
	private Hero source;

	private boolean report = true;

	/**
	 * 目标选择
	 */
	private int selectType;

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public boolean isReport() {
		return report;
	}

	public void setReport(boolean report) {
		this.report = report;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSelectType() {
		return selectType;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

	public boolean isIncBuff() {
		if (this.type == Buff.BUFF_TYPE_INC) {
			return true;
		}

		return false;
	}

	public Hero getTarget() {
		return target;
	}

	public void setTarget(Hero target) {
		this.target = target;
	}

	public Hero getSource() {
		return source;
	}

	public void setSource(Hero source) {
		this.source = source;
	}

	public long getPrevValue() {
		return prevValue;
	}

	public void setPrevValue(long prevValue) {
		this.prevValue = prevValue;
	}

	public long getHeroLife() {
		return heroLife;
	}

	public void setHeroLife(long heroLife) {
		this.heroLife = heroLife;
	}

	public boolean isZeroVal() {
		return isZeroVal;
	}

	public void setZeroVal(boolean isZeroVal) {
		this.isZeroVal = isZeroVal;
	}

}
