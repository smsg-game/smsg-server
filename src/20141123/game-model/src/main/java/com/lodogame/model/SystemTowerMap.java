package com.lodogame.model;

public class SystemTowerMap {
	private int towerMapId;
	private String blockMask;
	private int startPos;
	private int endPos;
	private String grid;
	private int width;
	private int height;

	public int getTowerMapId() {
		return towerMapId;
	}

	public void setTowerMapId(int towerMapId) {
		this.towerMapId = towerMapId;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getBlockMask() {
		return blockMask;
	}

	public void setBlockMask(String blockMask) {
		this.blockMask = blockMask;
	}
}
