package com.lodogame.game.dao.impl.mysql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserForcesCount;

public class UserForcesDaoMysqlImpl implements UserForcesDao {

	public final static String columns = "*";
	
	private static final String tablePrex = "user_forces";
	private static final int tableCount = 128;
	@Autowired
	private Jdbc jdbc;

	public List<UserForces> getUserForcesList(String userId, int sceneId) {

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		String sql = "SELECT " + columns + " FROM " + TableUtils.getTableName(userId, tablePrex, tableCount) + " WHERE user_id = ? ";
		if (sceneId > 0) {
			sql += " and scene_id = ?";
			parameter.setInt(sceneId);
		}

		return this.jdbc.getList(sql, UserForces.class, parameter);
	}
    /**
     * 此方法只能添加 userid一样的list 不然会出现插入出问题哦
     * @param forcesList
     */
	public void add(List<UserForces> forcesList) {
		 if(forcesList==null||forcesList.size()==0){
			 return;
		 }
		 this.jdbc.insert(TableUtils.getTableName(forcesList.get(0).getUserId(), tablePrex, tableCount), forcesList);
	}

	@Override
	public boolean updateStatus(String userId, int forcesId, int status, int times) {

		String sql = "UPDATE " + TableUtils.getTableName(userId, tablePrex, tableCount) + " SET status = ?,  times = times + ?, updated_time = now() WHERE user_id = ? AND forces_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(status);
		parameter.setInt(times);
		parameter.setString(userId);
		parameter.setInt(forcesId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean add(UserForces userForces) {
		return this.jdbc.insert(TableUtils.getTableName(userForces.getUserId(), tablePrex, tableCount), userForces)>0;
	}

	@Override
	public UserForces getUserCurrentForces(String userId, int forcesType) {

		String sql = "SELECT " + columns + " FROM " + TableUtils.getTableName(userId, tablePrex, tableCount) + " WHERE user_id = ? AND forces_type = ? ORDER BY forces_id DESC LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(forcesType);

		return this.jdbc.get(sql, UserForces.class, parameter);
	}

	@Override
	public UserForces get(String userId, int forcesId) {

		String sql = "SELECT " + columns + " FROM " + TableUtils.getTableName(userId, tablePrex, tableCount) + " WHERE user_id = ? AND forces_id = ? LIMIT 1 ";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(forcesId);

		return this.jdbc.get(sql, UserForces.class, parameter);
	}

	@Override
	public boolean updateTimes(String userId, int forcesId, int times) {

		String sql = "UPDATE " + TableUtils.getTableName(userId, tablePrex, tableCount) + "  SET times = ? , updated_time = now() WHERE user_id = ? AND forces_id = ? ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setString(userId);
		parameter.setInt(forcesId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean updateTimes(String uid, int times, List<Integer> forcesIds) {
		String sql = "UPDATE " + TableUtils.getTableName(uid, tablePrex, tableCount) + "  SET times = ? , updated_time = now() WHERE user_id = ? AND forces_id in ";
		
		String forcesIdWhere = "";
		for(Integer fid : forcesIds){
			forcesIdWhere += fid + ", ";
		}
		
		forcesIdWhere = forcesIdWhere.substring(0, forcesIdWhere.length() - 2);
		
		sql += "(" + forcesIdWhere + ")";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(times);
		parameter.setString(uid);

		return this.jdbc.update(sql, parameter) > 0;
	}
	
	@Override
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size) {

		List<UserForcesCount> list = new ArrayList<UserForcesCount>();
		for(int i=0;i<tableCount;i++){
			String sql = "select user_id, count(forces_id) as forces_count from user_forces_"+i+" where forces_type = 1 or forces_type = 2 group by user_id order by forces_count desc limit ?, ?";
			SqlParameter param = new SqlParameter();
			param.setInt(offset);
			param.setInt(size);
			list.addAll(this.jdbc.getList(sql, UserForcesCount.class, param));
		}
		Collections.sort(list,new Comparator<UserForcesCount>() {
			@Override
			public int compare(UserForcesCount o1, UserForcesCount o2) {
				if(o1.getForcesCount()>o2.getForcesCount()){
					return -1;
				}else if(o1.getForcesCount()<o2.getForcesCount()){
					return 1;
				}
				return 0;
			}
		});
		return list.subList(offset, offset+size);

	}

	@Override
	public boolean resetForcesTimes(String userId, List<Integer> sceneIdList) {

		String sql = "UPDATE " + TableUtils.getTableName(userId, tablePrex, tableCount) + " SET times = 0 WHERE user_id = ? AND scene_id in (" + StringUtils.join(sceneIdList, ",") + ")";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public long getAmendEmbattleTime(String userId) {
		return 0;
	}

	@Override
	public void setAmendEmbattleTime(String userId, long timestamp) {

	}

}
