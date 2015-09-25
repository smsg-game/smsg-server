package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class AwardBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 掉落英雄列表
	 */
	@Mapper(name="hls")
	private List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

	/**
	 * 掉落装备列表
	 */
	@Mapper(name="eqs")
	private List<UserEquipBO> userEquipBOList = new ArrayList<UserEquipBO>();
	
	/**
	 * 掉落道具列表
	 */
	@Mapper(name="tls")
	private List<UserToolBO> userToolBOList = new ArrayList<UserToolBO>();

	/**
	 * 经验
	 */
	@Mapper(name="exp")
	private int exp;

	/**
	 * 银币
	 */
	@Mapper(name="co")
	private int copper;
	
	public List<UserHeroBO> getUserHeroBOList() {
		return userHeroBOList;
	}

	public void setUserHeroBOList(List<UserHeroBO> userHeroBOList) {
		this.userHeroBOList = userHeroBOList;
	}

	public List<UserEquipBO> getUserEquipBOList() {
		return userEquipBOList;
	}

	public void setUserEquipBOList(List<UserEquipBO> userEquipBOList) {
		this.userEquipBOList = userEquipBOList;
	}
	
	public List<UserToolBO> getUserToolBOList() {
		return userToolBOList;
	}

	public void setUserToolBOList(List<UserToolBO> userToolBOList) {
		this.userToolBOList = userToolBOList;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}
}
