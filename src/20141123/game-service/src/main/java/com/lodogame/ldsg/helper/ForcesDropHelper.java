package com.lodogame.ldsg.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.lodogame.model.ForcesDropTool;

/**
 * 
 * @author chenjian
 * 
 */
public class ForcesDropHelper {
	/**
	 * 处理打怪批量随机掉落，用于扫荡
	 * 
	 * @param list
	 *            掉落物品列表
	 * @param times
	 *            次数
	 * @return
	 */
	public static Collection<ForcesDropTool> dropTools(List<ForcesDropTool> list, int times) {

		Map<String, ForcesDropTool> dropToolMap = new HashMap<String, ForcesDropTool>();

		for (int i = 0; i < times; i++) {
			int randVal = new Random().nextInt(10000);
			for (ForcesDropTool t : list) {
				if (t.getLowerNum() <= randVal && randVal <= t.getUpperNum()) {
					String key = t.getToolId() + "" + t.getToolType();
					ForcesDropTool tmp = dropToolMap.get(key);
					if (tmp != null) {
						tmp.setToolNum(tmp.getToolNum() + t.getToolNum());
						dropToolMap.put(key, tmp);
					} else {
						ForcesDropTool fdt = new ForcesDropTool();
						fdt.setForesId(t.getForesId());
						fdt.setLowerNum(t.getLowerNum());
						fdt.setToolId(t.getToolId());
						fdt.setToolName(t.getToolName());
						fdt.setToolNum(t.getToolNum());
						fdt.setToolType(t.getToolType());
						fdt.setUpperNum(t.getUpperNum());
						dropToolMap.put(key, fdt);
					}
				}
			}
		}
		return dropToolMap.values();
	}
}
