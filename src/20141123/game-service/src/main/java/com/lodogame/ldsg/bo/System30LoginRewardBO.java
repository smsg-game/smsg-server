package com.lodogame.ldsg.bo;

import java.util.List;
import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class System30LoginRewardBO {
	
	@Mapper(name = "day")
	private int day;
	
	@Mapper(name = "list")
	private List<DropToolBO> dropToolBOList;

	public List<DropToolBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropToolBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}
