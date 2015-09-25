package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.PackageInfoDao;
import com.lodogame.model.IncrStaticPackageInfo;
import com.lodogame.model.PackageInfo;
import com.lodogame.model.StaticFileInfo;
import com.lodogame.model.StaticPackageInfo;
import com.lodogame.model.StaticPackageUser;

public class PackageInfoDaoMysqlImpl implements PackageInfoDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "package_info";

	public final static String columns = "*";

	@Override
	public boolean add(PackageInfo packageInfo) {
		return this.jdbc.insert(packageInfo) > 0;
	}

	@Override
	public PackageInfo getLast(int pkgType, String partnerId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE pkg_type = ? and partner_id = ? ORDER BY ID DESC LIMIT 1";
		SqlParameter paramter = new SqlParameter();
		paramter.setInt(pkgType);
		paramter.setString(partnerId);
		return this.jdbc.get(sql, PackageInfo.class, paramter);
	}
	
	@Override
	public PackageInfo getLastByTest(int pkgType, int isTest, String partnerId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE pkg_type = ? and is_test = ? and partner_id = ? ORDER BY ID DESC LIMIT 1";
		SqlParameter paramter = new SqlParameter();
		paramter.setInt(pkgType);
		paramter.setInt(isTest);
		paramter.setString(partnerId);
		return this.jdbc.get(sql, PackageInfo.class, paramter);
	}

	@Override
	public String getIncreStaticPackageUrl(String oldVersion, String newVersion) {
		String sql = "SELECT * FROM incr_static_package_info WHERE old_version = ? AND new_version = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(oldVersion);
		parameter.setString(newVersion);
		IncrStaticPackageInfo info = this.jdbc.get(sql, IncrStaticPackageInfo.class, parameter);
		return info.getUrl();
	}

	@Override
	public StaticFileInfo getStaticFile(String version, String md5) {
		String sql = "SELECT * FROM static_file_info WHERE version = ? AND md5 = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(version);
		parameter.setString(md5);
		
		return this.jdbc.get(sql, StaticFileInfo.class, parameter);
	}

	@Override
	public List<StaticFileInfo> getStaticFiles(String version) {
		String sql = "SELECT * FROM static_file_info WHERE version = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(version);
		
		return this.jdbc.getList(sql, StaticFileInfo.class, parameter);
	}

	
	@Override
	public boolean addStaticPackageInfo(StaticPackageInfo staticPackage) {
		return this.jdbc.insert(staticPackage) > 0;
	}

	@Override
	public List<StaticPackageInfo> getStaticPackages(int num) {
		String sql = "SELECT * FROM static_package_info ORDER BY id DESC LIMIT ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setInt(num);
		return this.jdbc.getList(sql, StaticPackageInfo.class, parameter);
	}

	@Override
	public boolean addIncrStaticPackage(IncrStaticPackageInfo packageInfo) {
		return this.jdbc.insert(packageInfo) > 0;
	}

	@Override
	public boolean addStaticFile(StaticFileInfo staticFileInfo) {
		return this.jdbc.insert(staticFileInfo) > 0;
	}

	@Override
	public StaticPackageUser getFromWhitelist(String uid) {
		String sql = "SELECT * FROM static_package_user WHERE uid = ?";
		SqlParameter parameter = new SqlParameter();
		parameter.setString(uid);
		return this.jdbc.get(sql, StaticPackageUser.class, parameter);
	}
}
