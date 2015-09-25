package com.lodogame.ldsg.checker;

import java.util.Map;

public interface TaskChecker {

	/**
	 * 任务是否完成
	 * 
	 * @param taskTarget
	 * @param params
	 * @return
	 */
	public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params);

}
