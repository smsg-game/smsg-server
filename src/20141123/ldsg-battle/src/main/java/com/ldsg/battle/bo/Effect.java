package com.ldsg.battle.bo;

import java.util.HashMap;
import java.util.Map;

import com.ldsg.battle.util.Convert;

public class Effect implements Cloneable {

	/**
	 * 减益效果
	 */
	public final static int EFFECT_TYPE_DEC = 2;

	/**
	 * 增益效果
	 */
	public final static int EFFECT_TYPE_INC = 1;

	/**
	 * 技能释放者
	 */
	public final static int NEXT_SELECT_TYPE_SOURCE = 0;

	/**
	 * 效果目标
	 */
	public final static int NEXT_SELECT_TYPE_TARGET = 1;

	private int effectUid;

	private int skillId;

	private int nextEffectId;

	private int effectId;

	private int round;

	private String funId;

	private int immediately;

	private String remark;

	private int showText;

	private int type;

	private int triggerType;

	private int selectType;

	private int addType;

	private int targetSelect;

	/**
	 * 后续效果选择类型，只支持0|1,0：技能释放者，1：被释放者
	 */
	private int nextSelectType;

	private int showIcons;

	private String name;

	private int removeAble;

	private int removeType;

	private Map<String, Double> params;

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}

	public String getParam(String key, String defaultValue) {
		if (this.params != null && this.params.containsKey(key)) {
			return Convert.toString(this.params.get(key));
		}
		return defaultValue;
	}

	public double getParam(String key, double defaultValue) {
		if (this.params != null && this.params.containsKey(key)) {
			return Convert.toDouble(this.params.get(key).toString());
		}
		return defaultValue;
	}

	public int getParam(String key, int defaultValue) {
		if (this.params != null && this.params.containsKey(key)) {
			return Convert.toInt32(this.params.get(key));
		}
		return defaultValue;

	}

	public int getEffectUid() {
		return effectUid;
	}

	public void setEffectUid(int effectUid) {
		this.effectUid = effectUid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getImmediately() {
		return immediately;
	}

	public void setImmediately(int immediately) {
		this.immediately = immediately;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getShowText() {
		return showText;
	}

	public void setShowText(int showText) {
		this.showText = showText;
	}

	@Override
	public Object clone() {
		Effect effect = new Effect();
		effect.setEffectId(effectId);
		effect.setFunId(funId);
		effect.setName(name);
		effect.setRemark(remark);
		effect.setType(type);
		return effect;
	}

	public int getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(int triggerType) {
		this.triggerType = triggerType;
	}

	public int getSelectType() {
		return selectType;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

	public int getShowIcons() {
		return showIcons;
	}

	public void setShowIcons(int showIcons) {
		this.showIcons = showIcons;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Double> getParams() {
		if (params == null) {
			return new HashMap<String, Double>();
		}
		return params;
	}

	public void setParams(Map<String, Double> params) {
		this.params = params;
	}

	public int getRemoveAble() {
		return removeAble;
	}

	public void setRemoveAble(int removeAble) {
		this.removeAble = removeAble;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getNextEffectId() {
		return nextEffectId;
	}

	public void setNextEffectId(int nextEffectId) {
		this.nextEffectId = nextEffectId;
	}

	public int getNextSelectType() {
		return nextSelectType;
	}

	public void setNextSelectType(int nextSelectType) {
		this.nextSelectType = nextSelectType;
	}

	public int getAddType() {
		return addType;
	}

	public void setAddType(int addType) {
		this.addType = addType;
	}

	public int getTargetSelect() {
		return targetSelect;
	}

	public void setTargetSelect(int targetSelect) {
		this.targetSelect = targetSelect;
	}

	public int getRemoveType() {
		return removeType;
	}

	public void setRemoveType(int removeType) {
		this.removeType = removeType;
	}

}
