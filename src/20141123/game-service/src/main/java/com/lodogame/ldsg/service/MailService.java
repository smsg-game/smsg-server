package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserMailBO;

/**
 * 邮件相关service
 * 
 * @author jacky
 * 
 */
public interface MailService {

	/**
	 * 邮件不存在
	 */
	public final static int RECEIVE_MAIL_ERROR_MAIL_NOT_EXISTS = 2001;

	/**
	 * 邮件已经领取过
	 */
	public final static int RECEIVE_MAIL_ERROR_MAIL_HAD_RECEIVE = 2002;

	/**
	 * 邮件没有奖励
	 */
	public final static int RECEIVE_MAIL_ERROR_MAIL_NOT_REWARD = 2003;

	/**
	 * 领取邮件附件
	 * 
	 * @param userId
	 * @param userMailId
	 * @return
	 */
	public CommonDropBO receive(String userId, int userMailId);

	/**
	 * 读取邮件
	 * 
	 * @param userId
	 * @param userMailId
	 */
	public void read(String userId, int userMailId);

	/**
	 * 删除邮件
	 * 
	 * @param userId
	 * @param userMailId
	 */
	public void delete(String userId, int userMailId);

	/**
	 * 获取用户邮件列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserMailBO> getMailList(String userId, int lastId);

	/**
	 * 是否有新的邮件
	 * 
	 * @param userId
	 * @return
	 */
	public boolean hasNewMail(String userId);
	
	/**
	 * 删除所有已读并领取了附件的邮件
	 * 
	 * @param userId
	 */
	public List<Integer> delete(String userId);
	
	public void notifyNewMail(List<String> userIdList);

	/**
	 * 发送系统邮件
	 * 
	 * @param title
	 * @param content
	 * @param toolIds
	 * @param target
	 * @param userLodoIds
	 */
	public void send(String title, String content, String toolIds, int target, String userLodoIds, String sourceId, Date date, String partner);
}
