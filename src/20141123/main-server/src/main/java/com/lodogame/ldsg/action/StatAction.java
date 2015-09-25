package com.lodogame.ldsg.action;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.lodogame.game.server.response.Response;

/**
 * 统计接口
 * 
 * @author CJ
 * 
 */
public class StatAction extends ProxyBaseAction {

	private static Logger logger = Logger.getLogger("stat");
	// 事件数据对象
	private String edt;

	/**
	 * 记录统计
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response log() throws IOException {
		logger.info(edt);
		return null;
	}

	public String getEdt() {
		return edt;
	}

	public void setEdt(String edt) {
		this.edt = edt;
	}

	public static void main(String[] args) {
		logger.debug("test");
	}
}
