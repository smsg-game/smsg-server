package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 走马灯消息对象
 * 
 * @author jacky
 * 
 */

@Compress
public class Message {

	@Mapper(name = "txt")
	private String txt;

	@Mapper(name = "cor")
	private Color color;

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
