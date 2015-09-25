package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.IncrStaticPackageInfo;
import com.lodogame.model.PackageInfo;
import com.lodogame.model.StaticFileInfo;
import com.lodogame.model.StaticPackageInfo;
import com.lodogame.model.StaticPackageUser;

/**
 * 
 * @author jacky
 * 
 */
public interface PackageInfoDao {

	/**
	 * 添加一个包信息
	 * 
	 * @param packageInfo
	 * @return
	 */
	public boolean add(PackageInfo packageInfo);

	/**
	 * 根据是否测试 获取最后一个
	 * @param pkgType
	 * @param isTest
	 * @return
	 */
	PackageInfo getLastByTest(int pkgType, int isTest, String partnerId);

	/**
	 * 根据类型获取最后一个
	 * @param pkgType
	 * @return
	 */
	PackageInfo getLast(int pkgType, String partnerId);

	/**
	 * 获取新旧版本间增量更新包的 URL 地址
	 * @return
	 */
	public String getIncreStaticPackageUrl(String oldVersion, String newVersion);

	/**
	 * 在指定版本的静态文件中，查询指定 MD5 值的文件
	 * @param version
	 * @param md5
	 * @return
	 */
	public StaticFileInfo getStaticFile(String version, String md5);

	/**
	 * 获取某一版本静态文件包中所有的文件
	 * @param newVersion
	 * @return
	 */
	public List<StaticFileInfo> getStaticFiles(String version);
	
	/**
	 * 添加静态文件包的信息
	 * @param staticFilePackage
	 * @return
	 */
	public boolean addStaticPackageInfo(StaticPackageInfo staticFilePackage);
	
	/**
	 * 查询上传的静态文件包信息，按时间倒序排序
	 * @param num 要查询的包的数量
	 * @return
	 */
	public List<StaticPackageInfo> getStaticPackages(int num);
	
	/**
	 * 保存增量包信息
	 */
	public boolean addIncrStaticPackage(IncrStaticPackageInfo packageInfo);

	/**
	 * 保存单个静态文件信息
	 * @param staticFile
	 */
	public boolean addStaticFile(StaticFileInfo staticFile);

	/**
	 * 从白名单中查询用户，只有白名单上的用户才能下载测试版的静态数据 
	 * @param uid
	 * @return
	 */
	public StaticPackageUser getFromWhitelist(String uid);
}
