package com.lodogame.model;

import java.io.Serializable;

public class SystemScene implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int sceneId;
	public String sceneName;
	public int openLevel;
	public String levelInfo;
	public String heroInfo;
	public int imgId;

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public String getLevelInfo() {
		return levelInfo;
	}

	public void setLevelInfo(String levelInfo) {
		this.levelInfo = levelInfo;
	}

	public String getHeroInfo() {
		return heroInfo;
	}

	public void setHeroInfo(String heroInfo) {
		this.heroInfo = heroInfo;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

}
