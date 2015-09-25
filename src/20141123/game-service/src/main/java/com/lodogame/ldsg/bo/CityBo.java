package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class CityBo {

	// 城池坐标
	@Mapper(name = "po")
	private String point;

	// 归属国家ID，默认为0
	@Mapper(name = "cid")
	private int countryID = -1;

	// 城池人数
	@Mapper(name = "pn")
	private int peopleNum = 0;

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public int getCountryID() {
		return countryID;
	}

	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}

	public int getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}

}
