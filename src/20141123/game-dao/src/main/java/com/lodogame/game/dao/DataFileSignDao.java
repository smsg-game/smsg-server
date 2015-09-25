package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.DataFileSign;

/**
 * 数据文件签名
 * 
 * @author chenjian
 * 
 */
public interface DataFileSignDao {
	
	/**
	 * 获取签名列表
	 * @param type
	 * @return
	 */
	List<DataFileSign> getSignList(Integer type);

}
