package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class PkGroupAwardGrantBO {
	@Mapper(name ="gid")
	private int gid;
	@Mapper(name ="grk")
	private int grank;
	@Mapper(name ="ie")
	private int isget;
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getGrank() {
		return grank;
	}
	public void setGrank(int grank) {
		this.grank = grank;
	}
	public int getIsget() {
		return isget;
	}
	public void setIsget(int isget) {
		this.isget = isget;
	}
	
}
