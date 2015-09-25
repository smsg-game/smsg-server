package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 招募返利奖励对象
 * @author Candon
 *
 */

@Compress
public class TavernTebatelBo {
	@Mapper(name = "id")
	private int id;
	
	@Mapper(name = "ts")
	private int times;
	
	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBOs = new ArrayList<DropToolBO>();
	
	@Mapper(name = "tp")
	private int type ;
	
	@Mapper(name = "ig")
	private int isGet;
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsGet() {
		return isGet;
	}
	public void setIsGet(int isGet) {
		this.isGet = isGet;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public List<DropToolBO> getDropToolBOs() {
		return dropToolBOs;
	}
	public void setDropToolBOs(List<DropToolBO> dropToolBOs) {
		this.dropToolBOs = dropToolBOs;
	}
	
	
}
