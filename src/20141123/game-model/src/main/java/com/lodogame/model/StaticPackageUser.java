package com.lodogame.model;

import java.io.Serializable;

/**
 * 增量更新静态数据用户白名单，只有白名单上的用户才可以下载测试版的数据
 * @author chengevo
 *
 */
public class StaticPackageUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
