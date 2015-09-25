package com.lodogame.model;

import java.io.Serializable;

/**
 * 鬼将化神进阶信息
 * 
 * @author zyz
 * 
 */
public class SystemHeroDeifyUpgrade implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 化神前鬼将ID
	 */
	private int beforeDeifySystemHeroId;
	
	/**
	 * 化神的神将ID
	 */
	private int afterDeifySystemHeroId;

	public int getBeforeDeifySystemHeroId() {
		return beforeDeifySystemHeroId;
	}

	public void setBeforeDeifySystemHeroId(int beforeDeifySystemHeroId) {
		this.beforeDeifySystemHeroId = beforeDeifySystemHeroId;
	}

	public int getAfterDeifySystemHeroId() {
		return afterDeifySystemHeroId;
	}

	public void setAfterDeifySystemHeroId(int afterDeifySystemHeroId) {
		this.afterDeifySystemHeroId = afterDeifySystemHeroId;
	}
	
	
}
