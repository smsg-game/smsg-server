package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 排行榜基础数据
 * 
 * @author chenjian
 * 
 */
@Compress
public class BaseRankBO {
	
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
}
