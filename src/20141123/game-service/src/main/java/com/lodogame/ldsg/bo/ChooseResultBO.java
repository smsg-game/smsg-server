package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

import flex.messaging.io.ArrayList;

@Compress
public class ChooseResultBO {
	@Mapper(name = "rf")
	private int rf;
	@Mapper(name = "hl")
	private List<Integer> list = new ArrayList();
	public int getRf() {
		return rf;
	}
	public void setRf(int rf) {
		this.rf = rf;
	}
	public List<Integer> getList() {
		return list;
	}
	public void setList(List<Integer> list) {
		this.list = list;
	}
	
	
}
