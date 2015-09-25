package com.lodogame.game.dao.clearcache;
/**
 * 登出的时候需要清除缓存的接口
 * @author foxwang
 *
 */
public interface ClearCacheOnLoginOut {
    //清理缓存
	public void clearOnLoginOut(String userId) throws Exception;
}
