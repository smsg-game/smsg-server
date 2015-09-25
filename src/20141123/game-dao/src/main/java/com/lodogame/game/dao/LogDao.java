package com.lodogame.game.dao;

/**
 * 用户金币，银币相关日志(除了金币，银币用独立的日志，其它的像经验，军令这些都放在tool log)
 * 
 * @author jacky
 * 
 */
public interface LogDao {

	/**
	 * 银币日志
	 * 
	 * @param userId
	 *            用户ID
	 * @param useType
	 *            使用类型,见(CopperUserType)
	 * @param amount
	 *            数量
	 * @param flg
	 *            1 增加 -1 减少
	 * @param success
	 *            是否成功 1 成功 0 失败
	 */
	public void copperLog(String userId, int useType, int amount, int flg, boolean success);

	/**
	 * 金币日志
	 * 
	 * @param userId
	 * @param useType
	 * @param amount
	 * @param flag
	 * @param success
	 */
	public void goldLog(String userId, int useType, int amount, int flag, boolean success, long beforeAmount, long afterAmount);

	/**
	 * 经验日志
	 * 
	 * @param userId
	 * @param useType
	 * @param amount
	 * @param success
	 */
	public void expLog(String userId, int useType, int amount, boolean success);

	/**
	 * 道具日志
	 * 
	 * @param userId
	 * @param toolType
	 * @param toolId
	 * @param num
	 * @param useType
	 * @param success
	 */
	public void toolLog(String userId, int toolType, int toolId, int num, int useType, int flag, String extinfo, boolean success);

	/**
	 * 武将日志
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @param useType
	 * @param flag
	 * @param heroExp
	 * @param heroLevel
	 */
	public void heroLog(String userId, String userHeroId, int systemHeroId, int useType, int flag, int heroExp, int heroLevel);

	/**
	 * 玩家升级日志
	 * 
	 * @param userId
	 * @param level
	 * @param exp
	 */
	public void levelUpLog(String userId, int level, int exp);

}
