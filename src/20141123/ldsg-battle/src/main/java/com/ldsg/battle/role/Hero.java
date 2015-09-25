package com.ldsg.battle.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.bo.MergeSkill;
import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.config.SkillConfig;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.TargetSelectType;
import com.ldsg.battle.handle.BuffHandleFactory;
import com.ldsg.battle.handle.IBuffHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.vo.BuffVO;

/**
 * 战斗武将
 * 
 * @author jacky
 * 
 */
public class Hero {

	private static Logger logger = Logger.getLogger(Hero.class);

	/**
	 * 属性列表
	 */
	private Map<String, Double> attr = new HashMap<String, Double>();

	private Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 当前目标
	 */
	private Hero target;

	/**
	 * 普通技能
	 */
	private int normalPlan;

	/**
	 * 主动技能
	 */
	private int plan;

	/**
	 * 攻击类型
	 */
	private int attackType;

	/**
	 * 中了不能加士气的buff
	 */
	private boolean canNotAddMorale = false;

	/**
	 * 身上有打人不加士气的buff
	 */
	private boolean notMorale = false;

	/**
	 * 中了混乱
	 */
	private boolean chaos = false;

	/**
	 * 死亡
	 */
	private boolean dead = false;

	/**
	 * 预死亡
	 */
	private boolean preDead = false;

	/**
	 * 站位
	 */
	private int pos;

	/**
	 * 兵符
	 */
	private int soliderToken;

	/**
	 * 兵符类型
	 */
	private int soliderTokenType;

	/**
	 * 是否中了眩晕
	 */
	private boolean dizzy = false;

	/**
	 * 武将ID
	 */
	private int heroId;

	/**
	 * 武将名称
	 */
	private String name;

	/**
	 * 头像ID
	 */
	private int imgId;

	/**
	 * 头像类型,是否放大
	 */
	private int imgType;

	/**
	 * 天赋技能列表
	 */
	private List<Integer> passiveSkillList = new ArrayList<Integer>();

	/**
	 * 是否免疫负面buff
	 */
	private boolean immuneDebuff = false;

	/**
	 * 开将身上的buff列表
	 */
	private List<Buff> buffList = new ArrayList<Buff>();

	/**
	 * 用户组合技列表
	 */
	private List<MergeSkill> mergeSkillList = new ArrayList<MergeSkill>();

	/**
	 * 武将等级
	 */
	private int level;

	/**
	 * 战斗位置
	 */
	private String position;

	/**
	 * 当前是否杀死目标
	 */
	private boolean killTarget = false;

	/**
	 * 是否进攻方
	 */
	private boolean attack = false;

	/**
	 * 头像
	 */
	private int portrait;

	public String getLogName() {
		return "[" + this.position + "]" + this.name;
	}

	public String getPrefix() {
		return this.position.substring(0, 1);
	}

	/**
	 * 职业
	 */
	private int career;

	/**
	 * 获取属性
	 * 
	 * @param attr
	 * @return
	 */
	public double getAttribute(String attr) {
		if (this.attr.containsKey(attr)) {
			return this.attr.get(attr);
		}
		return 0l;
	}

	/**
	 * 获取总的属性值
	 * 
	 * @param attr
	 * @return
	 */
	public double getTotalAttribute(Context context, String attr) {

		// 原来的值
		double value = this.getAttribute(attr);

		// 增加值
		double addValue = this.getAttribute(attr + "_add");

		// 临时增加值
		double tempAddValue = this.getAttribute(attr + "_temp_add");

		// 减少值
		double reduceValue = this.getAttribute(attr + "_reduce");

		// buf 数值
		double buffValue = BuffHelper.getBuffValue(context, this.buffList, attr);

		double v = value + addValue + tempAddValue + reduceValue + buffValue;
		if (v < 0) {
			v = 0;
		}

		double addRatio = BuffHelper.getBuffAddRatio(context, this, buffList, attr);
		if (addRatio > 0) {
			v = v * (1 + addRatio);
		}

		return v;
	}

	public void setAttribute(String attr, double value) {

		// 不能超过最大血量
		if (attr == AttrConstant.LIFE) {

			double maxLife = this.getAttribute(AttrConstant.MAX_LIFE);
			if (maxLife > 0 && value > maxLife) {
				value = maxLife;
			}
		}

		if (attr == AttrConstant.MORALE) {
			if (value > 100) {
				value = 100;
			}
		}

		this.attr.put(attr, value);
	}

	/**
	 * 设置临时变量
	 * 
	 * @param attr
	 * @param value
	 */
	public void setTempAddAttribute(String attr, double value) {
		this.attr.put(attr + "_temp_add", value);
	}

	/**
	 * 增加属性
	 * 
	 * @param attr
	 * @param value
	 */
	public void addAttribute(Context context, String attr, double value) {

		double oldValue = this.getAttribute(attr);
		oldValue += value;

		this.setAttribute(attr, oldValue);
	}

	/**
	 * 减少属性
	 * 
	 * @param attr
	 * @param value
	 */
	public void reduceAttribute(String attr, double value) {

		double oldValue = this.getAttribute(attr);
		oldValue -= value;

		if (oldValue < 0) {
			oldValue = 0;
		}

		this.setAttribute(attr, oldValue);
	}

	/**
	 * 获取点位
	 * 
	 * @return
	 */
	public String getPosition() {
		return this.position;
	}

	public List<Buff> getBuffList() {
		return this.buffList;
	}

	/**
	 * 获取武将技能
	 * 
	 * @return
	 */
	public Skill getSkill() {
		return SkillConfig.getSkill(this.plan);
	}

	public Hero getTarget() {
		return target;
	}

	public void setTarget(Hero target) {
		this.target = target;
	}

	public Map<String, Double> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, Double> attr) {
		this.attr = attr;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getAttackType() {
		return attackType;
	}

	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}

	public boolean isCanNotAddMorale() {
		return canNotAddMorale;
	}

	public void setCanNotAddMorale(boolean canNotAddMorale) {
		this.canNotAddMorale = canNotAddMorale;
	}

	public boolean isNotMorale() {
		return notMorale;
	}

	public void setNotMorale(boolean notMorale) {
		this.notMorale = notMorale;
	}

	public boolean isChaos() {
		return chaos;
	}

	public void setChaos(boolean chaos) {
		this.chaos = chaos;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getSoliderToken() {
		return soliderToken;
	}

	public void setSoliderToken(int soliderToken) {
		this.soliderToken = soliderToken;
	}

	public int getSoliderTokenType() {
		return soliderTokenType;
	}

	public void setSoliderTokenType(int soliderTokenType) {
		this.soliderTokenType = soliderTokenType;
	}

	public boolean isDizzy() {
		return dizzy;
	}

	public void setDizzy(boolean dizzy) {
		this.dizzy = dizzy;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public boolean isImmuneDebuff() {
		return immuneDebuff;
	}

	public void setImmuneDebuff(boolean immuneDebuff) {
		this.immuneDebuff = immuneDebuff;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPortrait() {
		return portrait;
	}

	public void setPortrait(int portrait) {
		this.portrait = portrait;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isPreDead() {
		return preDead;
	}

	public void setPreDead(boolean preDead) {
		this.preDead = preDead;
	}

	public void addBuff(Context context, Buff buff) {

		if (buff == null) {
			logger.debug("添加的buff为空");
			return;
		}

		if (this.immuneDebuff && !buff.isIncBuff())// /如果当前武将对debuff免疫的话
		{
			logger.debug("当前武将对debuff免疫.name[" + this.getLogName() + "]");
			return;
		}

		logger.debug("添加buff.hero.Name[" + this.getLogName() + "], effect.Remark[" + buff.getEffect().getRemark() + "], effect.SelectType[" + buff.getEffect().getSelectType() + "], effect.TriggerType["
				+ buff.getEffect().getTriggerType() + "]");

		for (Buff b : this.buffList) {
			if (b.getEffect().getEffectUid() == buff.getEffect().getEffectUid()) {

				logger.debug("重置buff[" + b.getEffect().getRemark() + "]有效轮次为.buff.round[" + buff.getRound() + "]");
				b.setRound(buff.getRound());
				return;
			}
		}

		this.buffList.add(buff);

		// /处理战报VO
		if (buff.getEffect().getShowIcons() > 0 || (buff.getEffect().getTriggerType() == 0 && buff.getEffect().getShowText() > 0)) {
			BuffVO buffVO = new BuffVO();
			buffVO.setPosition(this.getPosition());
			buffVO.setReduceLife(buff.getEffect().getEffectId() == EffectConfig.REDUCE_LIFE_EFFECT_ID);

			boolean isAddLife = buff.getEffect().getEffectId() == EffectConfig.ADD_LIFE_EFFECT_ID_1 || buff.getEffect().getEffectId() == EffectConfig.ADD_LIFE_EFFECT_ID_2;

			buffVO.setAddLife(isAddLife);
			buffVO.setEffectUID(buff.getEffect().getEffectUid());

			if (buff.getEffect().getTriggerType() == 0) {
				buffVO.setRound(buff.getRound());
				buffVO.setValue(buff.getValue());
			} else {
				buffVO.setRound(100);
			}

			buffVO.setLife((long) this.getTotalAttribute(context, AttrConstant.LIFE));
			context.appendActionBuffVO(buffVO);
		}

	}

	/**
	 * 添加组合技
	 * 
	 * @param mergeSkill
	 */
	public void addMergeSkill(MergeSkill mergeSkill) {
		this.mergeSkillList.add(mergeSkill);
	}

	public List<MergeSkill> getMergeSkillList() {
		return mergeSkillList;
	}

	public void reset() {

		this.target = null;
		this.setTempAddAttribute(AttrConstant.CRIT, 0);
		this.setTempAddAttribute(AttrConstant.HIT, 0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getImgType() {
		return imgType;
	}

	public void setImgType(int imgType) {
		this.imgType = imgType;
	}

	public List<Skill> getPassiveSkillList() {

		List<Skill> list = new ArrayList<Skill>();

		for (int id : passiveSkillList) {
			Skill skill = SkillConfig.getSkill(id);
			if (skill != null) {
				list.add(skill);
			}
		}

		return list;
	}

	public void addDeleteBuff(Buff buff) {
		deleteBuffList.add(buff);
	}

	/**
	 * 清除buff
	 * 
	 * @param context
	 */
	public void cleanBuff(Context context) {

		for (Buff buff : this.deleteBuffList) {
			logger.debug("开始清除过期buff.hero.Name[" + this.getLogName() + "],buff.effect.Remark[" + buff.getEffect().getRemark() + "], round[" + buff.getRound() + "]");
			this.removeBuff(context, buff, true);
		}

		this.deleteBuffList.clear();
	}

	private List<Buff> deleteBuffList = new ArrayList<Buff>();

	public void removeBuff(Context context, Buff buff) {
		this.removeBuff(context, buff, false);
	}

	/**
	 * 移除buff
	 * 
	 * @param context
	 * @param buff
	 * @param expire
	 */
	public void removeBuff(Context context, Buff buff, boolean expire) {

		this.buffList.remove(buff);
		Effect effect = buff.getEffect();
		String funID = effect.getFunId();

		if (!expire || buff.getRound() == 0 && effect.getSelectType() == TargetSelectType.SELF) {// 有持续回合数的buff,清除时要把对应的加成去掉(同时需要满足的条件给自身加效果的buff)
			IBuffHandle handle = BuffHandleFactory.getBuffHandler(funID);
			handle.handleRemove(this, buff);
		}

		// 处理战报VO
		if (effect.getShowIcons() > 0) {
			BuffVO buffVO = new BuffVO();
			buffVO.setPosition(this.position);
			buffVO.setReduceLife(effect.getEffectId() == EffectConfig.REDUCE_LIFE_EFFECT_ID);

			boolean isAddLife = buff.getEffect().getEffectId() == EffectConfig.ADD_LIFE_EFFECT_ID_1 || buff.getEffect().getEffectId() == EffectConfig.ADD_LIFE_EFFECT_ID_2;

			buffVO.setAddLife(isAddLife);
			buffVO.setEffectUID(buff.getEffect().getEffectUid());
			buffVO.setRound(0); // 表示删除
			buffVO.setLife((long) this.getAttribute(AttrConstant.LIFE));

			if (expire) {// 如果是过期清除的,放在回合的buff vo列表中
				context.appendRoundBuffVO(buffVO);
			} else {// 其它的放到行动的buff vo列表中
				context.appendActionBuffVO(buffVO);
			}
		}
	}

	public boolean isKillTarget() {
		return killTarget;
	}

	public void setKillTarget(boolean killTarget) {
		this.killTarget = killTarget;
	}

	/**
	 * 是否进攻方武将
	 * 
	 * @return
	 */
	public boolean isAttackHero() {
		return this.position.startsWith(BattleConstant.ATTACK_PREFIX);
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean addSkill(int skillId) {

		Skill skill = SkillConfig.getSkill(skillId);
		if (skill == null) {
			return false;
		}

		this.passiveSkillList.add(skillId);

		return true;
	}

	public int getNormalPlan() {
		return normalPlan;
	}

	public void setNormalPlan(int normalPlan) {
		this.normalPlan = normalPlan;
	}

	public boolean isAttack() {
		return attack;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

}
