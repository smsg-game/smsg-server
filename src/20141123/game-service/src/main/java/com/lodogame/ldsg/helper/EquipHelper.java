package com.lodogame.ldsg.helper;

import java.util.List;

import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.model.SystemSuitDetail;

public class EquipHelper {

	/**
	 * 获取元宝进阶需要的元宝数
	 * 
	 * @param color
	 * @return
	 */
	public static int getGoldMergeNeedMoney(int color) {

		switch (color) {
		case 0:
			return 50;
		case 1:
			return 100;
		case 2:
			return 350;
		case 3:
			return 800;
		case 4:
			return 2000;
		case 5:
			return 3000;
		default:
			return 0;

		}
	}

	public static boolean hasEquip(List<UserEquipBO> userEquipBOList, SystemSuitDetail systemSuitDetail) {

		for (UserEquipBO userEquipBO : userEquipBOList) {

			if (userEquipBO.getEquipId() == systemSuitDetail.getEquipId()) {
				return true;
			}

		}

		return false;

	}
}
