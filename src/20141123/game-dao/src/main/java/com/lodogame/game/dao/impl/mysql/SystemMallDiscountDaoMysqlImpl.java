package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.SystemMallDiscountDao;
import com.lodogame.model.SystemMallDiscountActivity;
import com.lodogame.model.SystemMallDiscountItems;

public class SystemMallDiscountDaoMysqlImpl implements SystemMallDiscountDao{

	@Autowired
	private Jdbc jdbc;
	private final static String table = "system_mall_discount_items";
	
	@Override
	public List<SystemMallDiscountItems> getDiscountItems(String activityId) {
		String sql = "SELECT discount FROM " + table + " WHERE activity_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(activityId);
		
		return this.jdbc.getList(sql, SystemMallDiscountItems.class, parameter);
	}

	@Override
	public List<SystemMallDiscountItems> getAllDiscountItems() {
		String sql = "SELECT * FROM " + table + " ORDER BY mall_id";
		
		return this.jdbc.getList(sql, SystemMallDiscountItems.class);
	}

	@Override
	public List<SystemMallDiscountActivity> getAllActivity() {
		String sql = "SELECT * FROM system_mall_discount_activity ORDER BY id";
		return this.jdbc.getList(sql, SystemMallDiscountActivity.class);
	}

	@Override
	public boolean addDiscountActivity(SystemMallDiscountActivity activity) {
		return this.jdbc.insert(activity) > 0;
	}

	@Override
	public boolean addDiscountItem(SystemMallDiscountItems item) {
		return this.jdbc.insert(item) > 0;
	}

	@Override
	public boolean delActivity(String activityId) {
		String sql = "DELETE FROM system_mall_discount_activity WHERE activity_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(activityId);
		return this.jdbc.update(sql, parameter) > 0;
	
		
	}

	@Override
	public boolean delItems(String activityId) {
		String sql = "DELETE FROM system_mall_discount_items WHERE activity_id = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(activityId);
		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public SystemMallDiscountItems getDiscountItem(String activityId, int mallId) {
		String sql = "SELECT FROM system_mall_discount_items WHERE activity_id = ? AND mall_id = ? ";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(activityId);
		parameter.setInt(mallId);
		
		return this.jdbc.get(sql, SystemMallDiscountItems.class, parameter);
	}

}
