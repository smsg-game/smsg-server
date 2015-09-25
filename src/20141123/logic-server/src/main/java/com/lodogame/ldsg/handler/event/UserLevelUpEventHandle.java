package com.lodogame.ldsg.handler.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.UserInvitedDao;
import com.lodogame.game.utils.SqlUtil;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.UserLevelUpEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.model.UserInvited;

public class UserLevelUpEventHandle implements EventHandle {

	@Autowired
	private UserInvitedDao userInvitedDao;

	@Autowired
	private TaskService taskService;

	@Override
	public boolean handle(Event event) {

		if (!(event instanceof UserLevelUpEvent)) {
			return true;
		}

		// 查询这个用户是否使用了邀请码注册，如果不是，直接返回 false
		final UserInvited userInvited = userInvitedDao.getByInvitedUserId(event.getUserId());
		if (userInvited == null) {
			return true;
		}

		final List<Integer> finishTaskIds = new ArrayList<Integer>();
		if (StringUtils.isNotEmpty(userInvited.getFinishTaskIds())) {
			String[] datas = userInvited.getFinishTaskIds().split(",");
			for (String data : datas) {
				finishTaskIds.add(Integer.parseInt(data));
			}
		}

		// 更新邀请码所有者的 user_task 数据，
		final int userLevel = event.getInt("userLevel");

		this.taskService.updateTaskFinish(userInvited.getUserId(), 1, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.TASK_TYPE_INVITED_REGISTER) {

					int needLevel = NumberUtils.toInt(params.get("lv"));
					boolean finish = userLevel >= needLevel;

					if (finish) {

						if (finishTaskIds.contains(systemTaskId)) {
							return false;
						} else {
							finishTaskIds.add(systemTaskId);
							userInvitedDao.update(userInvited.getInvitedUserId(), SqlUtil.joinInteger(finishTaskIds));
						}

					}

					return finish;
				}

				return false;
			}

		});

		return true;
	}

	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}

}
