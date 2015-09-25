package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
@Compress
public class HeroColorRankBO extends BaseRankBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Mapper(name = "rk")
	private int rank;

	@Mapper(name = "img")
	private int imgId;

	@Mapper(name = "hn")
	private String heroName;
	
	@Mapper(name = "av")
	protected int attackValue;
	
	@Mapper(name = "dv")
	protected int defenceValue;
	
	@Mapper(name = "hp")
	protected int hp;


	public int getDefenceValue() {
		return defenceValue;
	}

	public void setDefenceValue(int defenceValue) {
		this.defenceValue = defenceValue;
	}
	
	public int getAttackValue() {
		return attackValue;
	}

	public void setAttackValue(int attackValue) {
		this.attackValue = attackValue;
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Mapper(name = "hl")
	private int heroLevel;
	
	@Mapper(name = "shid")
	private int systemHeroId;
	
	@Mapper(name = "un")
	private String username;

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}
}
