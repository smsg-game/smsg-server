package com.ldsg.battle.model;

public class SkillEffectModel {

	private int skillEffectId;

	private int skillId;

	private int effectId;

	private int nextEffectId;

	/**
	 * 后续效果选择类型，只支持0|1,0：技能释放者，1：被释放者
	 */
	private int nextSelectType;

	private String params;

	private int round;

	private int triggerType;

	private int selectType;

	private int showText;

	private String textName;

	private String textId;

	private int showIcons;

	private String remark;

	private int immediately;

	private int removeAble;

	private int addType;

	private int removeType;

	/**
	 * 目标选择
	 */
	private int targetSelect;

	public int getSkillEffectId() {
		return skillEffectId;
	}

	public void setSkillEffectId(int skillEffectId) {
		this.skillEffectId = skillEffectId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
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

	public int getShowText() {
		return showText;
	}

	public void setShowText(int showText) {
		this.showText = showText;
	}

	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}

	public String getTextId() {
		return textId;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public int getShowIcons() {
		return showIcons;
	}

	public void setShowIcons(int showIcons) {
		this.showIcons = showIcons;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getImmediately() {
		return immediately;
	}

	public void setImmediately(int immediately) {
		this.immediately = immediately;
	}

	public int getRemoveAble() {
		return removeAble;
	}

	public void setRemoveAble(int removeAble) {
		this.removeAble = removeAble;
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
