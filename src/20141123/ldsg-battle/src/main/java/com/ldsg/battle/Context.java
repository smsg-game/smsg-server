package com.ldsg.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.event.ActionEndEvent;
import com.ldsg.battle.event.ActionStartEvent;
import com.ldsg.battle.event.BattleStartEvent;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.event.RoundStartEvent;
import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.helper.BattleHeroHelper;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.listener.IEventListener;
import com.ldsg.battle.role.Hero;
import com.ldsg.battle.vo.AttackVO;
import com.ldsg.battle.vo.BuffVO;
import com.ldsg.battle.vo.MergeSkillBO;

public class Context {

	private static Logger logger = Logger.getLogger(Context.class);

	private Stack<String> jumpQueueHero = new Stack<String>();

	/**
	 * 出过场的武将列表
	 */
	private Set<String> outPosList = new HashSet<String>();

	/**
	 * 当前时间点
	 */
	private int status;

	/**
	 * 比赛结果
	 */
	private int result;

	/**
	 * 当前技能
	 */
	private Skill currentSkill;

	/**
	 * 当前行动武将
	 */
	private Hero currentHero;

	/**
	 * 当前攻击的伤害量
	 */
	private long currentOriginalAttackHurt;

	/**
	 * 当前被攻击武将
	 */
	private Hero currentBeAttackHero;

	/**
	 * 当前是否反击
	 */
	private boolean currentIsCounter;

	/**
	 * 上一个出场武将阵营
	 */
	private String preHeroPrefix = null;

	/**
	 * 战报
	 */
	// private StringBuffer report = new StringBuffer();

	/**
	 * report map
	 */
	private Map<String, StringBuffer> reportMap = new HashMap<String, StringBuffer>();

	private Set<MergeSkillBO> attackMergeSkillSet = new HashSet<MergeSkillBO>();

	private Set<MergeSkillBO> defensesMergeSkillSet = new HashSet<MergeSkillBO>();

	/**
	 * 当前伤害
	 */
	private long currentAttackHurt;

	/**
	 * 进攻方剩余总血量
	 */
	private long attackTotalLeftLife;

	/**
	 * 防守方剩余总血量
	 */
	private long defenseTotalLeftLife;

	/**
	 * 进攻方总血量
	 */
	private long attackTotalLife;

	/**
	 * 防守方总血量
	 */
	private long defenseTotalLife;

	/**
	 * 进攻武将
	 */
	private Map<String, Hero> attackHeroDict;

	/**
	 * 防守武将
	 */
	private Map<String, Hero> defenseHeroDict;

	/**
	 * 进攻方等级
	 */
	private int attackLevel;

	/**
	 * 防守方等级
	 */
	private int defenseLevel;

	/**
	 * 当前回合
	 */
	private int round;

	/**
	 * 是否第一个行动
	 */
	private boolean isFirstAction = true;

	/**
	 * 事件监听器列表
	 */
	private List<IEventListener> eventListenerList = new ArrayList<IEventListener>();

	private boolean finish;

	/**
	 * 当前行动是不是插队的情况
	 */
	private boolean isAddAction = false;

	/**
	 * action buff vo list
	 */
	private List<BuffVO> actionBuffVOList = new ArrayList<BuffVO>();

	/**
	 * 组合技能 buff
	 */
	private List<BuffVO> mergeSkillBuffVOList = new ArrayList<BuffVO>();

	/**
	 * attack vo list
	 */
	private List<AttackVO> attackVOList = new ArrayList<AttackVO>();

	/**
	 * round buff vo list
	 */
	private List<BuffVO> roundBuffVOList = new ArrayList<BuffVO>();

	/**
	 * 出场顺序
	 */
	private List<String> startOrderList;

	/**
	 * 当前出场的索引
	 */
	private int startOrderIndex;

	/**
	 * 回合结束
	 */
	private boolean roundEnd;

	/**
	 * 获取当前被攻击的武将
	 * 
	 * @return
	 */
	public Hero getCurrentBeAttackHero() {
		return this.currentBeAttackHero;
	}

	/**
	 * 获取敌方英雄列表
	 * 
	 * @param hero
	 * @return
	 */
	public Map<String, Hero> getEnemyHeroDict(Hero hero) {

		String position = hero.getPosition();
		if (position.startsWith(BattleConstant.ATTACK_PREFIX)) {
			return this.defenseHeroDict;
		} else {
			return this.attackHeroDict;
		}
	}

	/**
	 * 获取本方英雄列表
	 * 
	 * @param hero
	 * @return
	 */
	public Map<String, Hero> getSelfHeroDict(Hero hero) {

		String position = hero.getPosition();
		if (position.startsWith(BattleConstant.ATTACK_PREFIX)) {
			return this.attackHeroDict;
		} else {
			return this.defenseHeroDict;
		}
	}

	public Skill getCurrentSkill() {
		return currentSkill;
	}

	public void setCurrentSkill(Skill currentSkill) {
		this.currentSkill = currentSkill;
	}

	public void appendAttackVO(AttackVO attackVO) {
		this.attackVOList.add(attackVO);
	}

	/**
	 * 事件分发
	 * 
	 * @param event
	 */
	public void dispatchEvent(IEvent event) {

		for (IEventListener listener : eventListenerList) {
			listener.listen(event);
		}
	}

	public Hero getCurrentHero() {

		// /如果当前武将是空，则判断下当前要行动的武将是哪一个
		// /所以在每次行动结束时要把当前武将设为空
		if (currentHero == null) {

			String index;

			if (jumpQueueHero.size() > 0) {
				// /如果有插队的武将的话,获取插队的武将位置
				index = jumpQueueHero.pop();
				isAddAction = true;
			} else {
				// /否则就是按着顺序来

				if (startOrderIndex < 0) {
					logger.warn("出现场手索引小于0的情况.startOrderIndex[" + this.startOrderIndex + "]");
					startOrderIndex = 0;
				}

				index = this.startOrderList.get(startOrderIndex);
				isAddAction = false;
			}

			Hero hero = BattleHeroHelper.getHeroByIndex(this.attackHeroDict, this.defenseHeroDict, index);
			this.currentHero = hero;
		}

		return currentHero;

	}

	public void setCurrentHero(Hero currentHero) {
		this.currentHero = currentHero;
	}

	/**
	 * 状态切换
	 * 
	 * @param actionStart
	 */
	public void switchStatus(int status) {

		this.status = status;

		IEvent e = null;

		switch (status) {
		case BattleStatus.BATTLE_START:
			e = new BattleStartEvent(this, this.getHeroList());
			break;
		case BattleStatus.ROUND_START:
			e = new RoundStartEvent(this, this.getHeroList());
			break;
		case BattleStatus.ACTION_START:
			e = new ActionStartEvent(this, this.currentHero);
			break;
		case BattleStatus.AFTER_ACTION:
			e = new ActionEndEvent(this, this.currentHero);
			break;
		default:
			break;

		}

		if (e != null) {
			this.dispatchEvent(e);
		}
	}

	public void setCurrentBeAttackHero(Hero currentBeAttackHero) {
		this.currentBeAttackHero = currentBeAttackHero;
	}

	public boolean isFinish() {

		if (finish) {
			return true;
		}

		// 有一方的武将死光
		if (this.attackHeroDict.size() == 0 || this.defenseHeroDict.size() == 0) {
			finish = true;
			return true;
		}

		return false;
	}

	/**
	 * 回合是否已经结束
	 * 
	 * @return
	 */
	public boolean isRoundEnd() {

		// /所有武将已经出场，并且没有插队的武将了，则认为本回合结束
		if (roundEnd && jumpQueueHero.size() == 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isCurrentIsCounter() {
		return currentIsCounter;
	}

	public void setCurrentIsCounter(boolean currentIsCounter) {
		this.currentIsCounter = currentIsCounter;
	}

	public void appendReport(String block, String s) {
		StringBuffer report = null;
		if (this.reportMap.containsKey(block)) {
			report = this.reportMap.get(block);
		} else {
			report = new StringBuffer();
			this.reportMap.put(block, report);
		}

		report.append(s);
	}

	/**
	 * 获取所有武将列表
	 * 
	 * @return
	 */
	public List<Hero> getHeroList() {

		List<Hero> heroList = new ArrayList<Hero>();
		for (Hero hero : this.attackHeroDict.values()) {
			heroList.add(hero);
		}
		for (Hero hero : this.defenseHeroDict.values()) {
			heroList.add(hero);
		}

		return heroList;
	}

	public Map<String, Hero> getAttackHeroDict() {
		return attackHeroDict;
	}

	public void setAttackHeroDict(Map<String, Hero> attackHeroDict) {
		this.attackHeroDict = attackHeroDict;
	}

	public Map<String, Hero> getDefenseHeroDict() {
		return defenseHeroDict;
	}

	public void setDefenseHeroDict(Map<String, Hero> defenseHeroDict) {
		this.defenseHeroDict = defenseHeroDict;
	}

	public long getAttackTotalLeftLife() {
		return attackTotalLeftLife;
	}

	public void setAttackTotalLeftLife(long attackTotalLeftLife) {
		this.attackTotalLeftLife = attackTotalLeftLife;
	}

	public long getDefenseTotalLeftLife() {
		return defenseTotalLeftLife;
	}

	public void setDefenseTotalLeftLife(long defenseTotalLeftLife) {
		this.defenseTotalLeftLife = defenseTotalLeftLife;
	}

	public long getAttackTotalLife() {
		return attackTotalLife;
	}

	public void setAttackTotalLife(long attackTotalLife) {
		this.attackTotalLife = attackTotalLife;
	}

	public long getDefenseTotalLife() {
		return defenseTotalLife;
	}

	public void setDefenseTotalLife(long defenseTotalLife) {
		this.defenseTotalLife = defenseTotalLife;
	}

	public int getAttackLevel() {
		return attackLevel;
	}

	public void setAttackLevel(int attackLevel) {
		this.attackLevel = attackLevel;
	}

	public int getDefenseLevel() {
		return defenseLevel;
	}

	public void setDefenseLevel(int defenseLevel) {
		this.defenseLevel = defenseLevel;
	}

	/**
	 * 获取比赛结果
	 * 
	 * @return
	 */
	public int getResult() {

		if (result > 0) {
			return result;
		}

		if (!this.isFinish()) {
			throw new BattleRuntimeException("战斗还未结束,不能读取战斗结果");
		}

		if (this.getAttackHeroDict().size() > 0 && this.getDefenseHeroDict().size() > 0)// /打平
		{
			logger.info("本次战斗结果,打平");
			this.result = BattleConstant.BATTLE_RESLUT_DRAW;
		} else {
			if (this.getAttackHeroDict().size() > 0)// /进攻方赢
			{

				logger.info("本次战斗结果,进攻方赢");
				this.result = BattleConstant.BATTLE_RESLUT_ATTACK_WIN;
			} else if (this.getDefenseHeroDict().size() > 0)// /防守方赢
			{

				logger.info("本次战斗结果,防守方赢");
				this.result = BattleConstant.BATTLE_RESLUT_DEFENSE_WIN;
			}

		}

		return this.result;

	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getReport() {

		StringBuffer sb = new StringBuffer();

		boolean first = true;

		sb.append("{");
		for (Entry<String, StringBuffer> entry : this.reportMap.entrySet()) {
			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}
			sb.append("\"" + entry.getKey() + "\"");
			sb.append(":");
			sb.append(entry.getValue().toString());
		}
		sb.append("}");

		return sb.toString();
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public List<BuffVO> getActionBuffVOList() {

		List<BuffVO> buffVOList = new ArrayList<BuffVO>();
		for (BuffVO vo : actionBuffVOList) {
			// 只有武将活着，或者当前buff是掉血的buff,才传回给客户端
			if (this.attackHeroDict.containsKey(vo.getPosition()) || this.defenseHeroDict.containsKey(vo.getPosition()) || vo.isReduceLife()) {
				buffVOList.add(vo);
			}
		}

		BuffHelper.sortBuff(buffVOList);

		return buffVOList;
	}

	public void setActionBuffVOList(List<BuffVO> actionBuffVOList) {
		this.actionBuffVOList = actionBuffVOList;
	}

	public List<AttackVO> getAttackVOList() {
		return attackVOList;
	}

	public void setAttackVOList(List<AttackVO> attackVOList) {
		this.attackVOList = attackVOList;
	}

	public void startOrderIndexInc() {

		if (this.startOrderIndex < startOrderList.size() - 1) {
			if (!isAddAction || this.startOrderIndex < 0)// /只有不是那种增加行动的情况，才将出场索引+1
			{
				this.startOrderIndex++;
			}
		} else {
			roundEnd = true;
			logger.debug("第[" + this.round + "]回合结束");
		}

	}

	/**
	 * 清除进攻vo list
	 */
	public void cleanAttackVOList() {
		this.attackVOList.clear();
	}

	/**
	 * 清除
	 */
	public void cleanActionBuffVOList() {
		this.actionBuffVOList.clear();
	}

	/**
	 * 清除当前武将列表
	 */
	public void cleanCurrentHero() {
		this.currentHero = null;
	}

	/**
	 * 清除回合buff vo列表
	 */
	public void cleanRoundBuffVOList() {
		this.roundBuffVOList.clear();
	}

	/**
	 * 清除组合技能的buff vo列表
	 */
	public void cleanMergeSkillBuffVOList() {
		this.mergeSkillBuffVOList.clear();
	}

	public List<String> getStartOrderList() {
		return startOrderList;
	}

	public void setStartOrderList(List<String> startOrderList) {
		this.startOrderList = startOrderList;
	}

	public int getStartOrderIndex() {
		return startOrderIndex;
	}

	public void setStartOrderIndex(int startOrderIndex) {
		this.startOrderIndex = startOrderIndex;
		if (startOrderIndex == 0) {
			this.roundEnd = false;
		}
	}

	public List<BuffVO> getRoundBuffVOList() {
		return roundBuffVOList;
	}

	public List<BuffVO> getMergeSkillBuffVOList() {
		return mergeSkillBuffVOList;
	}

	public void setRoundBuffVOList(List<BuffVO> roundBuffVOList) {
		this.roundBuffVOList = roundBuffVOList;
	}

	/**
	 * 追加回合buff vo
	 * 
	 * @param buffVO
	 */
	public void appendRoundBuffVO(BuffVO buffVO) {
		this.roundBuffVOList.add(buffVO);
	}

	/**
	 * 追加组合技能buff vo
	 * 
	 * @param buffVO
	 */
	public void appendMergeSkillBuffVO(BuffVO buffVO) {
		this.mergeSkillBuffVOList.add(buffVO);
	}

	public void appendActionBuffVO(BuffVO buffVO) {
		this.actionBuffVOList.add(buffVO);
	}

	public void addEventListener(IEventListener listener) {
		this.eventListenerList.add(listener);
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void addJumpQueueHero(String position) {
		this.jumpQueueHero.push(position);
	}

	public long getCurrentOriginalAttackHurt() {
		return currentOriginalAttackHurt;
	}

	public void setCurrentOriginalAttackHurt(long currentOriginalAttackHurt) {
		this.currentOriginalAttackHurt = currentOriginalAttackHurt;
	}

	public long getCurrentAttackHurt() {
		return currentAttackHurt;
	}

	public void setCurrentAttackHurt(long currentAttackHurt) {
		this.currentAttackHurt = currentAttackHurt;
	}

	public void setRoundEnd(boolean roundEnd) {
		this.roundEnd = roundEnd;
	}

	public boolean isFirstAction() {
		return isFirstAction;
	}

	public void setFirstAction(boolean isFirstAction) {
		this.isFirstAction = isFirstAction;
	}

	public Set<MergeSkillBO> getAttackMergeSkillSet() {
		return attackMergeSkillSet;
	}

	public void setAttackMergeSkillSet(Set<MergeSkillBO> attackMergeSkillSet) {
		this.attackMergeSkillSet = attackMergeSkillSet;
	}

	public Set<MergeSkillBO> getDefensesMergeSkillSet() {
		return defensesMergeSkillSet;
	}

	public void setDefensesMergeSkillSet(Set<MergeSkillBO> defensesMergeSkillSet) {
		this.defensesMergeSkillSet = defensesMergeSkillSet;
	}

	public String getPreHeroPrefix() {
		return preHeroPrefix;
	}

	public void setPreHeroPrefix(String preHeroPrefix) {
		this.preHeroPrefix = preHeroPrefix;
	}

	public Set<String> getOutPosList() {
		return outPosList;
	}

	public void setOutPosList(Set<String> outPosList) {
		this.outPosList = outPosList;
	}

}
