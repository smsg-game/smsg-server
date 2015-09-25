package com.lodogame.ldsg.bo;

import java.math.BigDecimal;

import com.lodogame.ldsg.annotation.Mapper;

/**
 * 首冲充值最高金额可以获得的元宝
 * 
 * @author chengevo
 * 
 */
public class MaxGoldSetBO {

	/**
	 * 充值金额
	 */
	@Mapper(name = "money")
	BigDecimal money;

	/**
	 * 可以获得的元宝
	 */
	@Mapper(name = "gold")
	int gold;

	@Mapper(name = "id")
	int id;

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
