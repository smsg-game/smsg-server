package com.lodogame.ldsg.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mysql.jdbc.StringUtils;

/**
 * 任务帮助类
 * 
 * @author jacky
 * 
 */
public class TaskHelper {

	private static final Logger logger = Logger.getLogger(TaskHelper.class);

	/**
	 * 解析任务参数
	 * 
	 * @param params
	 * @return
	 */
	public static Map<String, String> parse(String params) {

		Map<String, String> map = new HashMap<String, String>();

		if (StringUtils.isNullOrEmpty(params)) {
			return map;
		}

		try {
			String[] infos = params.split(",");
			for (String info : infos) {
				String[] datas = info.split(":");
				if (datas.length != 2) {
					// logger.error("错误的任务参数配置.params[" + params + "]");
					continue;
				}
				map.put(datas[0], datas[1]);

			}
		} catch (Throwable t) {
			logger.error("错误的任务参数配置.params[" + params + "]", t);
		}

		return map;

	}

	/**
	 * VIP奖励日志
	 * 
	 * @param taskId
	 * @return
	 */
	public static boolean isVIPTask(int taskId) {
		if (taskId == 90000) {
			return true;
		}
		return false;
	}

	public static int getVipTaskCopperAdd(int vipLevel) {

		if (vipLevel == 2) {
			return 30000;
		} else if (vipLevel == 3) {
			return 60000;
		}

		return 0;
	}

}
