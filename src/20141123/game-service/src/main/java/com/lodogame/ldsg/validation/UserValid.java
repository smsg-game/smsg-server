package com.lodogame.ldsg.validation;

import com.lodogame.game.utils.Constant;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.UserService;
import com.mysql.jdbc.StringUtils;

public class UserValid {

	/**
	 * 是不是合法的用户名
	 * 
	 * @param username
	 */
	public static void isValidUsername(String username) {

		if (StringUtils.isNullOrEmpty(username)) {
			throw new ServiceException(UserService.CREATE_USERNAME_INVAILD, "用户名不能为空");
		}

		if (Constant.getBytes(username) > 12) {
			throw new ServiceException(UserService.CREATE_USERNAME_INVAILD, "用户名不长度不对.username[" + username + "]");
		}
	}

	public static void isValidInitHeroId(int systemHeroId) {

		/**
		 * 测试服不做验证
		 */
		if (Config.ins().isDebug()) {
			return;
		}

		// 1，关羽更改为马超。ID为：354
		// 2，张郃。ID为：359
		// 3，周泰。ID为：406
		// 4，张角。ID为：447
		if (systemHeroId != 1 && systemHeroId != 2 && systemHeroId != 3 && systemHeroId != 4) {
			throw new ServiceException(3001, "初始武将选择异常.systemHeroId[" + systemHeroId + "]");
		}
	}
}
