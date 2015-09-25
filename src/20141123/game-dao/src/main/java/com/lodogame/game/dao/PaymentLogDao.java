package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;

import com.lodogame.model.PaymentLog;

/**
 * 
 * @author jacky
 * 
 */
public interface PaymentLogDao {

	/**
	 * 添加充值记录
	 * 
	 * @param paymentLog
	 */
	public void add(PaymentLog paymentLog);

	/**
	 * 获取充值总数
	 * 
	 * @param userId
	 * @return
	 */
	public int getPaymentTotal(String userId);

	/**
	 * 获取充值金币总数
	 * 
	 * @param userId
	 * @return
	 */
	public int getPaymentTotalGold(String userId);

	/**
	 * 根据时间及金额区间获取用户订单
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param payMoney
	 * @param nextPayMoney
	 * @return
	 */
	public List<PaymentLog> getPaymenList(String userId, Date startTime, Date endTime, int payMoney, int nextPayMoney);
	
	/**
	 * 根据时间及元宝区间获取用户订单
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param payMoney
	 * @param nextPayMoney
	 * @return
	 */
	public List<PaymentLog> getPaymenListByGold(String userId, Date startTime, Date endTime, int payGold, int nextPayGold);

	/**
	 * 获取用户某段时间内的充值总金额
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int getPaymentTotalByTime(String userId, Date startTime, Date endTime);
	
	/**
	 * 获取用户某段时间内的充值总金额
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int getPaymentTotalGoldByTime(String userId, Date startTime, Date endTime);
	

	/**
	 * 获取大于或等于payMoney金额的订单
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param payMoney
	 * @return
	 */
	List<PaymentLog> getPaymenList(String userId, Date startTime, Date endTime, int payMoney);
	
	/**
	 * 获取大于或等于gold金额的订单
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param payMoney
	 * @return
	 */
	List<PaymentLog> getPaymenListByGold(String userId, Date startTime, Date endTime, int payMoney);

}
