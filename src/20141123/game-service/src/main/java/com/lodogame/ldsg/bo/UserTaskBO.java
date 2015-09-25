package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 用户任务BO
 * 
 * @author jacky
 * 
 */
@Compress
public class UserTaskBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统任务ID
	 */
	@Mapper(name = "tid")
	private int systemTaskId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 任务类型
	 */
	@Mapper(name = "tt")
	private int taskType;

	/**
	 * 需要完成次数
	 */

	@Mapper(name = "nct")
	private int needFinishTimes;

	/**
	 * 已经完成次数
	 */
	@Mapper(name = "ct")
	private int finishTimes;

	/**
	 * 任务状态(0 未完成 1 已经完成 2 已领取)
	 */
	@Mapper(name = "st")
	private int status;

	/**
	 * 排序
	 */
	@Mapper(name = "sort")
	private int sort;

	/**
	 * 任务名称
	 */
	@Mapper(name = "tn")
	private String taskName;

	/**
	 * 描述
	 */
	@Mapper(name = "desc")
	private String taskDesc;

	@Mapper(name = "img")
	private int imgId;

	/**
	 * 掉落道具列表
	 */
	@Mapper(name = "dr")
	private List<DropToolBO> dropToolBO;

	public int getSystemTaskId() {
		return systemTaskId;
	}

	public List<DropToolBO> getDropToolBO() {
		return dropToolBO;
	}

	public void setDropToolBO(List<DropToolBO> dropToolBO) {
		this.dropToolBO = dropToolBO;
	}

	public void setSystemTaskId(int systemTaskId) {
		this.systemTaskId = systemTaskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getNeedFinishTimes() {
		return needFinishTimes;
	}

	public void setNeedFinishTimes(int needFinishTimes) {
		this.needFinishTimes = needFinishTimes;
	}

	public int getFinishTimes() {
		return finishTimes;
	}

	public void setFinishTimes(int finishTimes) {
		this.finishTimes = finishTimes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

}
