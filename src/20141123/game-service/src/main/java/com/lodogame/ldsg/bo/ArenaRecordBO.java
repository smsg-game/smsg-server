package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ArenaRecordBO {

	/**
	 * 类型
	 */
	@Mapper(name = "tp")
	private int type;

	/**
	 * 进攻方名字
	 */
	@Mapper(name = "aun")
	private String attackUsername;

	/**
	 * 防守方名字
	 */
	@Mapper(name = "dun")
	private String defenseUsername;

	/**
	 * 进攻方用户ID
	 */
	private String attackUserId;

	/**
	 * 防守方用户ID
	 */
	private String defenseUserId;

	/**
	 * 当前连胜(胜利一方的连胜)
	 */
	@Mapper(name = "wc")
	private int winCount;

	/**
	 * 标志
	 */
	@Mapper(name = "flg")
	private int flag;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAttackUsername() {
		return attackUsername;
	}

	public void setAttackUsername(String attackUsername) {
		this.attackUsername = attackUsername;
	}

	public String getDefenseUsername() {
		return defenseUsername;
	}

	public void setDefenseUsername(String defenseUsername) {
		this.defenseUsername = defenseUsername;
	}

	public String getAttackUserId() {
		return attackUserId;
	}

	public void setAttackUserId(String attackUserId) {
		this.attackUserId = attackUserId;
	}

	public String getDefenseUserId() {
		return defenseUserId;
	}

	public void setDefenseUserId(String defenseUserId) {
		this.defenseUserId = defenseUserId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

}
