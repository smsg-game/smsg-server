package com.lodogame.ldsg.handler;

import java.util.List;

import com.lodogame.ldsg.bo.ContestTeamBO;
import com.lodogame.ldsg.bo.ContestTeamInfoBO;

public interface ContestPushHandler {

	/**
	 * 擂台赛状态发生改变时，推送消息
	 * @param userId
	 */
	public void pushStatus(String userId);
	
	/**
	 * 一场比赛打完后推送消息
	 */
	public void pushRoundFinished(String userId);
	
	/**
	 * 玩家下注后更新下注信息
	 * @param userId
	 */
	public void pushBet(String userId);

	
	/**
	 * 推送用户报名信息
	 * @param userId
	 */
	public void pushUserReg(String userId);


}
