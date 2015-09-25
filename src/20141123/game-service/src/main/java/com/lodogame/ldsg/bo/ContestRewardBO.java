package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ContestRewardBO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Mapper(name = "id")
	private int id;
	
	@Mapper(name = "doc")
	private int dayOfContest;
	
	@Mapper(name = "flag")
	private int flag;
	
	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBOList = new ArrayList<DropToolBO>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDayOfContest() {
		return dayOfContest;
	}

	public void setDayOfContest(int dayOfContest) {
		this.dayOfContest = dayOfContest;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List<DropToolBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropToolBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}

}
