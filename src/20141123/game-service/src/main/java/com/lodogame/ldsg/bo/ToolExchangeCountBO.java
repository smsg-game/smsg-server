package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ToolExchangeCountBO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Mapper(name = "uextId")
	private int toolExchangeId;
	
	@Mapper(name = "ut")
	private int times;
	
	@Mapper(name = "st")
	private int max;

	public int getToolExchangeId() {
		return toolExchangeId;
	}

	public void setToolExchangeId(int toolExchangeId) {
		this.toolExchangeId = toolExchangeId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	

}