package com.lodogame.ldsg.helper;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.UserNationType;
import com.lodogame.model.UserExtinfo;

/**
 * 道具帮助类型
 * 
 * @author jacky
 * 
 */
public class ToolHelper {

	/**
	 * 是否普通道具(放在 user tool表中的道具 )
	 * 
	 * @param toolType
	 * @return
	 */
	public static boolean isNormalTool(int toolType) {
		return toolType == ToolType.MATERIAL;
	}

	/**
	 * 更具用户注册时选择的国家，为用户指定的蛋出指定英雄
	 * 
	 * @param toolType
	 * @return
	 */
	public static int direcEggGetRand(UserExtinfo userExtinfo) {

		int rand = 0;
		// 曹操获贾诩；刘备获魏延；孙权获周泰；董卓获颜良
		int userNation = userExtinfo.getUserNation();
		if (userNation == UserNationType.USER_NATION_CAOCAO) {
			rand = 1;
		} else if (userNation == UserNationType.USER_NATION_LIUBEI) {
			rand = 2501;
		} else if (userNation == UserNationType.USER_NATION_SUNQUAN) {
			rand = 5001;
		} else if (userNation == UserNationType.USER_NATION_DONGZHUO) {
			rand = 7501;
		} else {
			rand = RandomUtils.nextInt(10000) + 1;
		}
		return rand;
	}

}
