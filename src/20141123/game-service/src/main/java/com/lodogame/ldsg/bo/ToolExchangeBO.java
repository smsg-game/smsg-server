package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ToolExchangeBO {

	@Mapper(name = "id")
	private int id;

	@Mapper(name = "prit")
	private List<DropToolBO> preDropToolBOList;

	@Mapper(name = "ptit")
	private List<DropToolBO> postDropToolBOList;

	@Mapper(name = "ts")
	private int times;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<DropToolBO> getPreDropToolBOList() {
		return preDropToolBOList;
	}

	public void setPreDropToolBOList(List<DropToolBO> preDropToolBOList) {
		this.preDropToolBOList = preDropToolBOList;
	}

	public List<DropToolBO> getPostDropToolBOList() {
		return postDropToolBOList;
	}

	public void setPostDropToolBOList(List<DropToolBO> postDropToolBOList) {
		this.postDropToolBOList = postDropToolBOList;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
