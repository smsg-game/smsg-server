package com.ldsg.battle.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.TargetSelectType;
import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.role.Hero;

/**
 * 技能发放目标选择帮助类
 * 
 * @author jacky
 * 
 */
public class TargetSelectHelper {

	private static Logger logger = Logger.getLogger(TargetSelectHelper.class);

	/**
	 * 第一列时的目标选择优先级
	 */
	private static int[] FIRST_COLUMN_SELECT = { 1, 2, 3 };

	/**
	 * 第二列时的目标选择优先级
	 */
	private static int[] SECOND_COLUMN_SELECT = { 2, 1, 3 };

	/**
	 * 第三列时的目标选择优先级
	 */
	private static int[] THIRD_COLUMN_SELECT = { 3, 2, 1 };

	/**
	 * 当前被攻击者
	 * 
	 * @param context
	 * @param hero
	 * @return
	 */
	public static List<Hero> HeroCurrentBeAttack(Context context, Hero hero) {
		List<Hero> heroList = new ArrayList<Hero>();

		if (context.getCurrentBeAttackHero() != null) {
			heroList.add(context.getCurrentBeAttackHero());
		}
		return heroList;
	}

	/**
	 * 当前被攻击者周围
	 * 
	 * @param context
	 * @param hero
	 * @return
	 */
	public static List<Hero> HeroCurrentBeAttackRound(Context context, Hero hero) {
		List<Hero> heroList = new ArrayList<Hero>();

		if (context.getCurrentBeAttackHero() != null) {

			Hero beAttackHero = context.getCurrentBeAttackHero();

			Map<String, Hero> heroDict = GetHeroDict(context, beAttackHero, false);

			String position = beAttackHero.getPosition();

			int col = GetCol(position);
			int row = GetRow(position);

			String prefix = position.substring(0, 1);

			List<String> posList = new ArrayList<String>();

			// 左边
			if (col > 1) {
				String pos = GetPosition(prefix, col - 1, row);
				posList.add(pos);
			}

			// 右边
			if (col < 3) {
				String pos = GetPosition(prefix, col + 1, row);
				posList.add(pos);
			}

			// 前边
			if (row > 1) {
				String pos = GetPosition(prefix, col, row - 1);
				posList.add(pos);
			}

			// 后边
			if (row < 3) {
				String pos = GetPosition(prefix, col, row + 1);
				posList.add(pos);
			}

			for (String pos : posList) {
				if (heroDict.containsKey(pos)) {
					heroList.add(heroDict.get(pos));
				}
			}

		}

		return heroList;
	}

	/**
	 * 自身
	 * 
	 * @param context
	 * @param hero
	 * @return
	 */
	public static List<Hero> HeroSelf(Context context, Hero hero) {
		List<Hero> heroList = new ArrayList<Hero>();
		heroList.add(hero);
		return heroList;
	}

	/**
	 * 生命值最低
	 * 
	 * @param context
	 * @param hero
	 * @param enemy
	 * @return
	 */
	public static List<Hero> lowestLife(Context context, Hero hero, boolean enemy) {
		List<Hero> heroList = new ArrayList<Hero>();

		Map<String, Hero> heroDict = GetHeroDict(context, hero, enemy);

		Hero target = null;

		for (Hero h : heroDict.values()) {
			if (target == null) {
				target = h;
				continue;
			}

			double targetLifePencent = (double) target.getTotalAttribute(context, AttrConstant.LIFE) / target.getTotalAttribute(context, AttrConstant.MAX_LIFE);
			double heroLifePencent = (double) h.getTotalAttribute(context, AttrConstant.LIFE) / h.getTotalAttribute(context, AttrConstant.MAX_LIFE);
			if (targetLifePencent > heroLifePencent) {
				target = h;
			}
		}

		heroList.add(target);

		return heroList;
	}

	public static List<Hero> selectTarget(int selectType, Context context, Hero hero) {
		return selectTarget(selectType, context, hero, null);
	}

	/**
	 * 根据目标选择类型获取技能发放目标
	 * 
	 * @param selectType
	 * @param context
	 * @param hero
	 * @return
	 */
	public static List<Hero> selectTarget(int selectType, Context context, Hero hero, Skill skill) {
		List<Hero> heroList = null;

		switch (selectType) {
		case TargetSelectType.ENEMY_TEAM_RANDOM:// 敌方随机
			heroList = TeamRandom(context, hero, true);
			break;
		case TargetSelectType.SELF_TEAM_RANDOM:// 敌方随机单体
			heroList = TeamRandom(context, hero, false);
			break;
		case TargetSelectType.ENEMY_RANDOM_MORE:// 敌方随机多个
			int count = 1;
			if (skill != null) {
				count = (int) skill.getParam(6);
			}
			heroList = TeamRandomMore(context, hero, count, true);
			break;
		case TargetSelectType.ENEMY_SINGLE:// 敌方单体
			heroList = EnemySigle(context, hero, false);
			break;
		case TargetSelectType.ENEMY_BACK_SINGLE:// 敌方后方单体
			heroList = EnemySigle(context, hero, true);
			break;
		case TargetSelectType.ENEMY_ONE_COL:// 敌方一列
			heroList = EnemySigleColOrRow(context, hero, false);
			break;
		case TargetSelectType.ENEMY_ONE_ROW:// 敌方一排
			heroList = EnemySigleColOrRow(context, hero, true);
			break;
		case TargetSelectType.ENEMY_BACK_ROW:// 后方一排
			heroList = EnemyBackRow(context, hero);
			break;
		case TargetSelectType.ENEMY_TEAM_ALL:// 敌方全敌
			heroList = TeamAll(context, hero, true);
			break;
		case TargetSelectType.SELF_TEAM_ALL:// 本方全体
			heroList = TeamAll(context, hero, false);
			break;
		case TargetSelectType.ENEMY_TEAM_LIFE_LOWEST:
			heroList = lowestLife(context, hero, true);
			break;
		case TargetSelectType.SELF_TEAM_LIFE_LOWEST:
			heroList = lowestLife(context, hero, false);
			break;
		case TargetSelectType.SELF_ROUND:
			heroList = SelfRound(context, hero);
			break;
		case TargetSelectType.SELF:
			heroList = HeroSelf(context, hero);
			break;
		case TargetSelectType.CURRENT_BE_ATTACK:
			heroList = HeroCurrentBeAttack(context, hero);
			break;
		case TargetSelectType.CURRENT_BE_ATTACK_ROUND:
			heroList = HeroCurrentBeAttackRound(context, hero);
			break;
		default:
			throw new BattleRuntimeException("未知目标选择类型[" + selectType + "]");
		}

		String type = "";
		switch (selectType) {
		case TargetSelectType.ENEMY_TEAM_RANDOM:
			type = "敌方随机";
			break;
		case TargetSelectType.SELF_TEAM_RANDOM:
			type = "已方随机";
			break;
		case TargetSelectType.ENEMY_SINGLE:
			type = "敌方单体";
			break;
		case TargetSelectType.ENEMY_BACK_SINGLE:
			type = "敌方后方单体";
			break;
		case TargetSelectType.ENEMY_ONE_COL:
			type = "敌方一列";
			break;
		case TargetSelectType.ENEMY_ONE_ROW:
			type = "敌方一排";
			break;
		case TargetSelectType.ENEMY_TEAM_ALL:
			type = "敌方全敌";
			break;
		case TargetSelectType.SELF_TEAM_ALL:
			type = "己方全体";
			break;
		case TargetSelectType.ENEMY_TEAM_LIFE_LOWEST:
			type = "敌方生命值最低";
			break;
		case TargetSelectType.SELF_TEAM_LIFE_LOWEST:
			type = "己方生命值最低";
			break;
		case TargetSelectType.SELF_ROUND:
			type = "自身周围";
			break;
		case TargetSelectType.SELF:
			type = "自己";
			break;
		case TargetSelectType.CURRENT_BE_ATTACK:
			type = "当前被攻击者";
			break;
		default:
			break;

		}

		String heroNameList = "";
		for (Hero target : heroList) {
			heroNameList += target.getLogName();
			heroNameList += ",";
		}

		logger.debug("目标选择方式[" + type + "], 当前武将[" + hero.getLogName() + "], 目标列表[" + heroNameList + "]");

		return heroList;
	}

	// <summary>
	// 敌方单体
	//
	// 法则
	// 1.最近
	// 2 从前往后(back=true 则由后往前)
	// 3.从上往下
	//
	// </summary>
	// <param name="context"></param>
	// <param name="back">true则由后往前</param>
	// <returns></returns>
	private static List<Hero> EnemySigle(Context context, Hero hero, boolean back) {
		List<Hero> heroList = new ArrayList<Hero>();

		String position = hero.getPosition();

		// 获取自己站在第几列
		int col = GetCol(position);

		int[] cols;

		switch (col) {
		case 1:
			cols = FIRST_COLUMN_SELECT;
			break;
		case 2:
			cols = SECOND_COLUMN_SELECT;
			break;
		case 3:
			cols = THIRD_COLUMN_SELECT;
			break;
		default:
			throw new BattleRuntimeException("错误的站位信息.col[" + col + "], position[" + position + "]");
		}

		String prefix = GetEnemyPrefix(position);

		// 对方的武将站位列表
		Map<String, Hero> enemyHeroDict = context.getEnemyHeroDict(hero);

		int[] indexList;
		if (back) {
			indexList = new int[] { 3, 2, 1 };
		} else {
			indexList = new int[] { 1, 2, 3 };
		}

		for (int row : indexList) {
			for (int i : cols) {
				String pos = GetPosition(prefix, i, row);
				if (enemyHeroDict.containsKey(pos)) {
					heroList.add(enemyHeroDict.get(pos));
					return heroList;
				}
			}
		}

		return heroList;
	}

	// <summary>
	// 敌方后排
	// </summary>
	// <param name="context"></param>
	// <param name="hero"></param>
	// <returns></returns>
	private static List<Hero> EnemyBackRow(Context context, Hero hero) {
		List<Hero> heroList = new ArrayList<Hero>();

		List<Hero> list = EnemySigle(context, hero, false);

		if (list.size() != 1) {
			throw new BattleRuntimeException("敌方单体时返回的数据出错,武将数不为1");
		}

		Hero targetHero = list.get(0);

		String position = targetHero.getPosition();

		Map<String, Hero> enemyHeroDict = context.getEnemyHeroDict(hero);

		String prefix = position.substring(0, 1);

		int row = 2;
		while (row >= 1 && heroList.size() == 0) {
			for (int col = 1; col <= 3; col++) {
				String pos = GetPosition(prefix, col, row);
				if (enemyHeroDict.containsKey(pos)) {
					heroList.add(enemyHeroDict.get(pos));
				}
			}
			row--;
		}

		return SortHeroList(context, hero, heroList);
	}

	// <summary>
	// 敌方一列或者一排
	// </summary>
	// <param name="context"></param>
	// <param name="getRow">true则是一排</param>
	// <returns></returns>
	private static List<Hero> EnemySigleColOrRow(Context context, Hero hero, boolean getRow) {
		List<Hero> heroList = new ArrayList<Hero>();

		List<Hero> list = EnemySigle(context, hero, false);

		if (list.size() != 1) {
			throw new BattleRuntimeException("敌方单体时返回的数据出错,武将数不为1");
		}

		Hero targetHero = list.get(0);

		String position = targetHero.getPosition();

		Map<String, Hero> enemyHeroDict = context.getEnemyHeroDict(hero);

		String prefix = position.substring(0, 1);

		if (getRow) {
			int row = GetRow(position);
			for (int col = 1; col <= 3; col++) {
				String pos = GetPosition(prefix, col, row);
				if (enemyHeroDict.containsKey(pos)) {
					heroList.add(enemyHeroDict.get(pos));
				}
			}
		} else {
			int col = GetCol(position);
			for (int row = 1; row <= 3; row++) {
				String pos = GetPosition(prefix, col, row);
				if (enemyHeroDict.containsKey(pos)) {
					heroList.add(enemyHeroDict.get(pos));
				}
			}

		}

		return SortHeroList(context, hero, heroList);
	}

	// <summary>
	// 根据位置字符串获取列数
	// </summary>
	// <param name="position"></param>
	// <returns></returns>
	private static int GetCol(String position) {
		int index = Integer.parseInt(position.substring(1));
		int col = index % 3;
		if (col == 0) {
			col = 3;
		}
		return col;
	}

	/**
	 * 获取敌对方的占位信息前缀
	 * 
	 * @param position
	 * @return
	 */
	private static String GetEnemyPrefix(String position) {
		String selfPefix = position.substring(0, 1);
		if (selfPefix.equals(BattleConstant.ATTACK_PREFIX)) {
			return BattleConstant.DEFENSE_PREFIX;
		} else {
			return BattleConstant.ATTACK_PREFIX;
		}
	}

	/**
	 * 获取目标武将集合字典
	 * 
	 * @param context
	 * @param hero
	 * @param enemy
	 * @return
	 */
	private static Map<String, Hero> GetHeroDict(Context context, Hero hero, boolean enemy) {

		Map<String, Hero> heroDict;

		if (enemy) {
			heroDict = context.getEnemyHeroDict(hero);
		} else {
			heroDict = context.getSelfHeroDict(hero);
		}

		return heroDict;
	}

	/**
	 * 获取站位字符串
	 * 
	 * @param prefix
	 * @param col
	 * @param row
	 * @return
	 */
	private static String GetPosition(String prefix, int col, int row) {
		return prefix + ((row - 1) * 3 + col);
	}

	/**
	 * 根据位置字符串获取排数
	 * 
	 * @param position
	 * @return
	 */
	private static int GetRow(String position) {
		int index = Integer.parseInt(position.substring(1));
		return (index - 1) / 3 + 1;
	}

	/**
	 * 自方周围
	 * 
	 * @param context
	 * @param hero
	 * @return
	 */
	private static List<Hero> SelfRound(Context context, Hero hero) {
		List<Hero> heroList = new ArrayList<Hero>();

		Map<String, Hero> heroDict = GetHeroDict(context, hero, false);

		String position = hero.getPosition();

		int col = GetCol(position);
		int row = GetRow(position);

		String prefix = position.substring(0, 1);

		// 自身
		heroList.add(hero);

		List<String> posList = new ArrayList<String>();

		// 左边
		if (col > 1) {
			String pos = GetPosition(prefix, col - 1, row);
			posList.add(pos);
		}

		// 右边
		if (col < 3) {
			String pos = GetPosition(prefix, col + 1, row);
			posList.add(pos);
		}

		// 前边
		if (row > 1) {
			String pos = GetPosition(prefix, col, row - 1);
			posList.add(pos);
		}

		// 后边
		if (row < 3) {
			String pos = GetPosition(prefix, col, row + 1);
			posList.add(pos);
		}

		for (String pos : posList) {
			if (heroDict.containsKey(pos)) {
				heroList.add(heroDict.get(pos));
			}
		}

		return heroList;
	}

	// <summary>
	// 对武将进行排序,把根据单体规则出来的武将放在第一位
	// </summary>
	// <param name="heroList"></param>
	// <returns></returns>
	private static List<Hero> SortHeroList(Context context, Hero hero, List<Hero> heroList) {
		List<Hero> list = EnemySigle(context, hero, false);
		if (list.size() != 1) {
			throw new BattleRuntimeException("敌方单体时返回的数据出错,武将数不为1");
		}
		Hero target = list.get(0);
		String position = target.getPosition();
		for (int i = 0; i < heroList.size(); i++) {
			Hero currentHero = heroList.get(i);
			if (StringUtils.equals(currentHero.getPosition(), position)) {
				if (i > 0) {
					heroList.set(i, heroList.get(0));
					heroList.set(0, currentHero);
				}
				break;
			}
		}

		return heroList;
	}

	// <summary>
	// 全体
	// </summary>
	// <param name="context"></param>
	// <returns></returns>
	private static List<Hero> TeamAll(Context context, Hero hero, boolean enemy) {
		Map<String, Hero> heroDict = GetHeroDict(context, hero, enemy);

		List<Hero> heroList = new ArrayList<Hero>(heroDict.values());

		if (enemy)// 如果是敌方全体,把根据敌方单体得到的武将放在第一位
		{
			heroList = SortHeroList(context, hero, heroList);
		}

		return heroList;
	}

	// <summary>
	// 随机
	// </summary>
	// <param name="context"></param>
	// <param name="enemy">敌方还是本方</param>
	// <returns></returns>
	private static List<Hero> TeamRandom(Context context, Hero hero, boolean enemy) {
		List<Hero> heroList = new ArrayList<Hero>();

		Map<String, Hero> heroDict = GetHeroDict(context, hero, enemy);

		Set<String> keys = heroDict.keySet();
		List<String> keyss = new ArrayList<String>();
		for (String key : keys) {
			if (StringUtils.equalsIgnoreCase(hero.getPosition(), key)) {
				continue;
			}

			keyss.add(key);
		}

		if (keyss.size() == 0) {
			return heroList;
		}

		int rand = RandomUtils.nextInt(keyss.size());

		// 随机出来的key
		String randKey = keyss.get(rand);

		// 随机获取的武将
		Hero targetHero = heroDict.get(randKey);

		heroList.add(targetHero);

		return heroList;
	}

	/**
	 * 随机多个
	 * 
	 * @param context
	 * @param hero
	 * @param enemy
	 * @return
	 */
	private static List<Hero> TeamRandomMore(Context context, Hero hero, int count, boolean enemy) {

		if (count < 1) {
			count = 1;
		}

		List<Hero> heroList = new ArrayList<Hero>();

		Map<String, Hero> heroDict = GetHeroDict(context, hero, enemy);

		Set<String> keys = heroDict.keySet();
		List<String> keyss = new ArrayList<String>();
		for (String key : keys) {
			if (StringUtils.equalsIgnoreCase(hero.getPosition(), key)) {
				continue;
			}

			keyss.add(key);
		}

		if (keyss.size() == 0) {
			return heroList;
		}

		Set<String> posSet = new HashSet<String>();

		boolean go = true;

		while (go) {

			for (String randKey : keyss) {
				int rand = RandomUtils.nextInt(100);
				if (rand > 50 && !posSet.contains(randKey)) {
					// 随机获取的武将
					Hero targetHero = heroDict.get(randKey);
					heroList.add(targetHero);
					posSet.add(randKey);
				}

				if (heroList.size() >= count || heroList.size() >= keyss.size()) {
					go = false;
					break;
				}
			}

		}

		return heroList;
	}
}
