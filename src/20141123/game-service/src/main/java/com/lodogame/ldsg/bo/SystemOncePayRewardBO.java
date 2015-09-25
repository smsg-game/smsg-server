package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemOncePayRewardBO {

	@Mapper(name = "id")
	private int id;

	@Mapper(name = "pm")
	private int payMoney;

	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBoList;

	@Mapper(name = "tl")
	private int timesLimit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}

	public List<DropToolBO> getDropToolBoList() {
		return dropToolBoList;
	}

	public void setDropToolBoList(List<DropToolBO> dropToolBoList) {
		this.dropToolBoList = dropToolBoList;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(int timesLimit) {
		this.timesLimit = timesLimit;
	}

}
