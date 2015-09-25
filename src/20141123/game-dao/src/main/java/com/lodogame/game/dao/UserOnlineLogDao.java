package com.lodogame.game.dao;

import java.util.Date;

import com.lodogame.model.UserOnlineLog;

/**
 * 用户在线日志dao
 * 
 * @author jacky
 * 
 */
public interface UserOnlineLogDao {

	/**
	 * 添加用户在线日志
	 * 
	 * @param userOnlineLog
	 * @return
	 */
	public boolean add(UserOnlineLog userOnlineLog);

	/**
	 * 获取最后一次登录日志
	 * 
	 * @param userId
	 * @return
	 */
	public UserOnlineLog getLastOnlineLog(String userId);

	/**
	 * 更新登出时间
	 * 
	 * @param userId
	 * @param logId
	 * @param logoutTime
	 * @return
	 */
	public boolean updateLogoutTime(String userId, int logId, Date logoutTime);

	/**
	 * 获取用户的在线时长
	 * 
	 * @param userId
	 * @return
	 */
	public long getUserOnline(String userId);

	/**
	 * 获取用户从某一时间开始的在线时长
	 * 
	 * 例如, 用户在这两个时间段登录：
	 * 
	 * <li>2013-12-06 16:58:03 - 2013-12-06 17:00:44 在线时间：161秒</li>
	 * <li>2013-12-06 17:14:01 - 2013-12-06 17:15:44 在线时间：103秒</li>
	 * 
	 * 当 startTime 为 2013/12/06 16:00:00 时，使用此函数求得的在线时长是 161+103 秒
	 * 
	 * @param userId
	 * @param startTime 
	 * @return 从 startTime 开始计算的在线时长，以秒为单位
	 */
	public long getUserOnline(String userId, Date startTime);

	/**
	 * 获取用户最后一次登录时间距离现在时间的时间差
	 * （注意不是用户最后一次登录时登录时间和退出时间之差）
	 * 
	 * @param userId
	 * @return 时间差，以秒为单位。例如：
	 * <p>最后一次登录时间：2013-10-09 11:51:26</p>
	 * <p>最后一次登录的退出时间：2013-10-09 12:12:35</p>
	 * <p>现在时间：2013-12-13 14:31:58</p>
	 * <p>得到的结果是 5625631 秒</p>
	 * 
	 * 
	 */
	public long getLastOnline(String userId);

	/**
	 * 是否有登录
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public boolean isLogin(String userId, Date date);
	


}
