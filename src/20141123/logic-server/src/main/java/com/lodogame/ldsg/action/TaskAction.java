package com.lodogame.ldsg.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserTaskBO;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.TaskUpdateEvent;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.model.UserTask;

/**
 * 任务相关action
 * 
 * @author jacky
 * 
 */

public class TaskAction extends LogicRequestAction {

	private static final Logger logger = Logger.getLogger(TaskAction.class);

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private TaskService taskService;

	/**
	 * 创建角色
	 * 
	 * @return
	 */
	public Response loadTasks() {

		Integer status = (Integer) this.request.getParameter("st");

		logger.debug("获取用户任务列表.uid[" + getUid() + "]");

		List<UserTaskBO> userTaskBOList = this.taskService.getUserTaskList(getUid(), status);
		set("tl", userTaskBOList);

		return this.render();
	}

	public Response receive() {

		logger.debug("任务提交.uid[" + getUid() + "]");

		Integer taskId = (Integer) this.request.getParameter("tid");

		UserTaskBO userTaskBO = taskService.get(getUid(), taskId);

		CommonDropBO commonDropBO = this.taskService.receive(getUid(), taskId, new EventHandle() {

			public boolean handle(Event event) {

				if (event instanceof TaskUpdateEvent) {

					int systemTaskId = event.getInt("systemTaskId");
					int flag = event.getInt("flag");
					pushHandler.pushTask(getUid(), systemTaskId, flag);
				}

				return true;
			}
		});

		set("hls", commonDropBO.getUserHeroBOList());
		set("eqs", commonDropBO.getUserEquipBOList());
		set("tls", commonDropBO.getUserToolBOList());
		set("tid", taskId);
		set("tt", userTaskBO.getTaskType());

		return this.render();
	}
	
public Response notifyServer() {
		
		this.taskService.updateTaskFinish(getUid(), 1, new EventHandle() { 
			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget,
					Map<String, String> params) {
				if(taskTarget == TaskTargetType.FACEBOOK_COMMENT) {
					return true;
				} else {
					return false;
				}
			}
			
		});
		return this.render();
	}
}
