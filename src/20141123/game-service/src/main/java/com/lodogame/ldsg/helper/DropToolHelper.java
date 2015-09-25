package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.lodogame.ldsg.bo.DropToolBO;

public class DropToolHelper {

	/**
	 * 解析旧落字符串
	 * 
	 * @param toolIds
	 * @return
	 */
	public static List<DropToolBO> parseDropTool(String toolIds) {

		List<DropToolBO> dropToolBOList = new ArrayList<DropToolBO>();

		if (StringUtils.isEmpty(toolIds)) {
			return dropToolBOList;
		}

		String[] strs = toolIds.split("\\|");
		for (String str : strs) {
			String[] infos = str.split(",");
			if (infos.length != 3) {
				continue;
			}

			int toolType = NumberUtils.toInt(infos[0]);
			int toolId = NumberUtils.toInt(infos[1]);
			int toolNum = NumberUtils.toInt(infos[2]);

			DropToolBO dropToolBO = new DropToolBO(toolType, toolId, toolNum);
			dropToolBOList.add(dropToolBO);
		}

		return dropToolBOList;
	}

	/**
	 * 是否掉落
	 * 
	 * @param lowerNum
	 * @param upperNum
	 * @return
	 */
	public static boolean isDrop(int lowerNum, int upperNum, int rand) {

		return rand >= lowerNum && rand <= upperNum;

	}

}
