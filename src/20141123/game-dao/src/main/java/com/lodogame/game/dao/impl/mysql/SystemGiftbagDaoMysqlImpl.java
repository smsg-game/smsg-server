package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemGiftbagDao;
import com.lodogame.model.GiftbagDropTool;
import com.lodogame.model.SystemGiftbag;

public class SystemGiftbagDaoMysqlImpl implements SystemGiftbagDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public SystemGiftbag getFirstPayGiftbag() {

		String sql = "SELECT * FROM system_giftbag WHERE type = 2 ";

		SqlParameter parameter = new SqlParameter();

		return this.jdbc.get(sql, SystemGiftbag.class, parameter);

	}

	@Override
	public SystemGiftbag getVipGiftbag(int vipLevel) {

		String sql = "SELECT * FROM system_giftbag WHERE type = 1 AND vip_level = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(vipLevel);

		return this.jdbc.get(sql, SystemGiftbag.class, parameter);
	}

	@Override
	public SystemGiftbag getCodeGiftBag(int subType, int giftBagType) {

		String sql = "SELECT * FROM system_giftbag WHERE type = ? AND sub_type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(giftBagType);
		parameter.setInt(subType);

		return this.jdbc.get(sql, SystemGiftbag.class, parameter);
	}

	@Override
	public List<GiftbagDropTool> getGiftbagDropToolList(int giftbagId) {

		String sql = "SELECT * FROM giftbag_drop_tool where giftbag_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(giftbagId);

		return this.jdbc.getList(sql, GiftbagDropTool.class, parameter);

	}

	@Override
	public SystemGiftbag getOnlineGiftBag(int subType) {

		String sql = "SELECT * FROM system_giftbag WHERE type = 4 AND sub_type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(subType);

		return this.jdbc.get(sql, SystemGiftbag.class, parameter);
	}

	/**
	 * 获取所有giftbag中的所有数据
	 * 
	 * @return
	 */
	public List<SystemGiftbag> getAll() {
		String sql = "SELECT * FROM system_giftbag";

		return this.jdbc.getList(sql, SystemGiftbag.class, null);

	}

	/**
	 * 获取所有giftbag_drop_tool中的所有数据
	 * 
	 * @return
	 */
	public List<GiftbagDropTool> getAllDropTool() {
		String sql = "SELECT * FROM giftbag_drop_tool";
		return this.jdbc.getList(sql, GiftbagDropTool.class, null);

	}

	@Override
	public SystemGiftbag getOncePayReward(int type, int subType) {
		String sql = "SELECT * FROM system_giftbag WHERE type = ? AND sub_type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);
		parameter.setInt(subType);

		return this.jdbc.get(sql, SystemGiftbag.class, parameter);
	}

	@Override
	public SystemGiftbag getTotalPayReward(int type, int subType) {
		String sql = "SELECT * FROM system_giftbag WHERE type = ? AND sub_type = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(type);
		parameter.setInt(subType);

		return this.jdbc.get(sql, SystemGiftbag.class, parameter);
	}
}
