package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class PkGroupAwardLogBo {

	@Mapper(name = "gr")
	private int grank;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBOList;

	public int getGrank() {
		return grank;
	}

	public void setGrank(int grank) {
		this.grank = grank;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<DropToolBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropToolBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}

}
