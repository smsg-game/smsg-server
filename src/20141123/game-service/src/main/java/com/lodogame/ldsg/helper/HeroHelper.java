package com.lodogame.ldsg.helper;

import com.lodogame.model.SystemHero;

/**
 * 武将帮助类
 * 
 * @author jacky
 * 
 */
public class HeroHelper {

	/**
	 * 获取属性值
	 * 
	 * @param initAttr
	 * @param attrGrowth
	 * @return
	 */
	public static int getAttribte(int initAttr, int attrGrowth, int level) {
		return initAttr + (attrGrowth * (level - 1));
	}

	/**
	 * 获取用户可以学习的技能数量
	 * 
	 * @param heroLevel
	 * @return
	 */
	public static int getStudyHeroCount(int heroLevel) {
		if (heroLevel >= 30) {
			return 6;
		}
		return 0;
	}
	
	/**
	 * 武将传承 - 获取元宝传承时，要消耗的元宝数量
	 * @param 传承武将的品质
	 * @return
	 */
	public static int getGoldNum(int gColor) {
		int goldenNum = 0;
		
		switch(gColor) {
			case 0:
				goldenNum = 10;
				break;
			case 1:
				goldenNum = 50;
				break;
			case 2:
				goldenNum = 100;
				break;
			case 3:
				goldenNum = 450;
				break;
			case 4:
				goldenNum = 1000;
				break;
			case 5:
				goldenNum = 2000;
				break;
			default:
				break;
		}
			
		return goldenNum;
	}
}
