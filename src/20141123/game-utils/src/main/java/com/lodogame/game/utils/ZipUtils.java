package com.lodogame.game.utils;

/**
 *  处理 zip 压缩包的工具类
 */
import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtils {
	/**
	 * 压缩文件夹
	 * @param dest 要生成的 zip 文件名（包含路径），例如：/user/local/test.zip
	 * @param sourse 要压缩的文件夹（文件）路径，例如：/user/local/test/
	 * @throws IOException
	 */
	public static void zipFiles(String dest, String source) {
		try {
			ZipFile zipFile = new ZipFile(dest);
			ZipParameters parameter = new ZipParameters();
			parameter.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式(默认值)
			parameter.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 普通级别
			zipFile.addFolder(new File(source), parameter);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解压文件，返回解压后的路径，解压路径末尾不带 "/"
	 * 例如，压缩包文件名为 1.0.1.zip，解压路径为 /usr/local，返回解压后路径：/usr/local/1.0.1
	 * @param zipFileName
	 * @param outputDirectory
	 * @throws Exception
	 */
	public static String unzip(String zipFileName, String outputDirectory) throws Exception {
		ZipFile zipFile = new ZipFile(zipFileName);          
		zipFile.extractAll(outputDirectory);  
		String filename = new File(zipFileName).getName();
		
		return outputDirectory + "/" + DirectoryUtils.getFileNameNoEx(filename);
	}
}
