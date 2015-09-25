package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Mapper;
/**
 * 用户装备修改穿戴人bo 如果userHeroId为null 则说明被脱了下来
 * @author foxwang
 *
 */
public class UserEquipMentChangeBO {
	/**
	 * 用户装备ID(唯一ID)
	 */
	@Mapper(name = "ueid")
	private String userEquipId;
	/**
	 * 穿戴的武将ID
	 */
	@Mapper(name = "uhid")
	private String userHeroId;
	
	public String getUserEquipId() {
		return userEquipId;
	}
	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}
	public String getUserHeroId() {
		return userHeroId;
	}
	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}
	public UserEquipMentChangeBO(String userEquipId, String userHeroId) {
		super();
		this.userEquipId = userEquipId;
		this.userHeroId = userHeroId;
	}
	public UserEquipMentChangeBO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
