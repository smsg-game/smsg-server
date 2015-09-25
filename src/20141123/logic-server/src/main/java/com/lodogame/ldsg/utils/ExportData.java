package com.lodogame.ldsg.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lodogame.game.dao.ExportDataDao;

public class ExportData {

	private ApplicationContext applicationContext;

	private String[] daoBeans = { "systemEquipDaoMysqlImpl", "systemHeroDaoMysqlImpl", "systemHeroUpgradeDaoMysqlImpl", "systemLevelExpDaoMysqlImpl", "systemHeroUpgradeToolDaoMysqlImpl",
			"systemForcesMonsterDaoMysqlImpl", "systemMonsterDaoMysqlImpl", "systemTaskDaoMysqlImpl", "systemSceneDaoMysqlImpl" };

	public ExportData() {
		init();
		clean();
	}
	
	private void clean(){
		File f = new File("d:\\export\\");
		if(f.exists() && f.isDirectory()){
			File[] fs = f.listFiles();
			for(int i = 0; i < fs.length; i++){
				fs[i].deleteOnExit();
			}
		}
	}

	private void init() {
		String[] locations = { "applicationContext.xml" };
		applicationContext = new ClassPathXmlApplicationContext(locations);
	}

	public void start() throws IOException {
		for (int i = 0; i < daoBeans.length; i++) {
			exportData(daoBeans[i]);
		}
	}

	private void exportData(String beanName){
		ExportDataDao dao = (ExportDataDao) applicationContext.getBean(beanName);
		String jsonData = dao.toJson();
		File f = new File("d:\\export\\" + beanName + ".json");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			bw.write(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			IOUtils.closeQuietly(bw);
		}
		System.out.println(jsonData);
	}

	public static void main(String[] args) throws IOException {
		new ExportData().start();
	}
}
