package com.lodogame.model;

import java.util.Date;

public class DataFileSign {
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 文件创建时间
	 */
	private Date fileCreateTime;
	/**
	 * 文件签名
	 */
	private String sign;
	/**
	 * 签名类型, 0：正式（默认），1：测试
	 */
	private int type;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getFileCreateTime() {
		return fileCreateTime;
	}

	public void setFileCreateTime(Date fileCreateTime) {
		this.fileCreateTime = fileCreateTime;
	}
}
