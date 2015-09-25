package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserEquip;

public interface UserEquipDao {

	/**
	 * 添加用户装备
	 * 
	 * @param userEquipment
	 * @return
	 */
	public boolean add(UserEquip userEquip);

	/**
	 * 批量增加装备
	 * 
	 * @param userEquipList
	 * @return
	 */
	public boolean addEquips(List<UserEquip> userEquipList);

	/**
	 * 获取用户装备列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEquip> getUserEquipList(String userId);
	

	/**
	 * 获取用户某种类型的装备
	 * @param userId
	 * @param equipId
	 * @return
	 */
	public List<UserEquip> getUserEquipList(String userId, int equipId);

	/**
	 * 设置用户装备列表
	 * 
	 * @param userId
	 * @param userEquipList
	 */
	public void setUserEquipList(String userId, List<UserEquip> userEquipList);

	/**
	 * 获取武将身上的装备列表
	 * 
	 * @param userHeroId
	 * @return
	 */
	public List<UserEquip> getHeroEquipList(String userId, String userHeroId);

	/**
	 * 获取用户单件装备
	 * 
	 * @param userEquipId
	 * @return
	 */
	public UserEquip get(String userId, String userEquipId);

	/**
	 * 更新用户装备的穿戴武将
	 * 
	 * @param userEquipId
	 * @param userHeroId
	 * @return
	 */
	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId);

	/**
	 * 用户装备等级上升
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param addLevel
	 * @param maxLevel
	 * @return
	 */
	public boolean updateEquipLevel(String userId, String userEquipId, int addLevel, int maxLevel);

	/**
	 * 删除用户装备
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public boolean delete(String userId, String userEquipId);

	/**
	 * 批量删除用户装备
	 * 
	 * @param userId
	 * @param userEquipIdList
	 * @return
	 */
	public boolean delete(String userId, List<String> userEquipIdList);

	/**
	 * 更新装备ID(进阶)
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param equipId
	 * @return
	 */
	public boolean updateEquipId(String userId, String userEquipId, int equipId);

	/**
	 * 获取用户装备长度
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserEquipCount(String userId);
	
	/**
	 * 查询用户某一类型的装备，并按照 equip_level 正序排序
	 * @param userId
	 * @param toolId
	 * @return
	 */
	public List<UserEquip> listUserEquipByLevelAsc(String userId, int equipId);
	
	public boolean updateUserDeify(String userId, int equipId, int equipLevel, String userEquipId);
}
