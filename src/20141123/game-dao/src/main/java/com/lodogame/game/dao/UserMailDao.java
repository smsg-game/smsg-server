package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;

import com.lodogame.model.UserMail;

public interface UserMailDao {

	/**
	 * 根据系统邮件获取用户邮件
	 * 
	 * @param userId
	 * @param systemMailId
	 * @return
	 */
	public UserMail getBySystemMailId(String userId, String systemMailId);

	/**
	 * 更新为已读
	 * 
	 * @param userId
	 * @param userMailId
	 * @param status
	 * @return
	 */
	public boolean updateStatus(String userId, int userMailId, int status);

	/**
	 * 更新邮件状态
	 * 
	 * @param userId
	 * @param userMailId
	 * @param status
	 * @return
	 */
	public boolean updateStatus(String userId, List<Integer> userMailIdList, int status);

	/**
	 * 更新为已领取
	 * 
	 * @param userId
	 * @param userMailId
	 * @return
	 */
	public boolean updateReceiveStatus(String userId, int userMailId, int status);

	/**
	 * 获取邮件列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserMail> getList(String userId);

	/**
	 * 添加邮件
	 * 
	 * @param userMailList
	 * @return
	 */
	public boolean add(List<UserMail> userMailList);

	/**
	 * 获取单个邮件
	 * 
	 * @param userId
	 * @param userMailId
	 * @return
	 */
	public UserMail get(String userId, int userMailId);

	/**
	 * 获取最后加入系统邮件时间
	 * 
	 * @param userId
	 * @return
	 */
	public Date getLastReceiveTime(String userId);

	/**
	 * 设置最后读取系统邮件时间
	 * 
	 * @param userId
	 * @param timestamp
	 * @return
	 */
	public boolean setLastReceiveTime(String userId, Date date);
	
	/**
	 * 判断是否已经五星评价过了
	 * @param userId
	 * @return
	 */
	public boolean isZaned(String userId);
	
	/**
	 * 设置玩家已经五星评价
	 * @param userId
	 * @return
	 */
	public boolean setZaned(String userId);

}
