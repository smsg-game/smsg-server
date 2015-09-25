package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class BattleStartBO {
	@Mapper(name = "tp")
	private int type;
	
	@Mapper(name = "ist")
	private int isStart;
	
	@Mapper(name = "iet")
	private int isEnd;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsStart() {
		return isStart;
	}

	public void setIsStart(int isStart) {
		this.isStart = isStart;
	}

	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}
	
	
	
}
