package com.lodogame.ldsg.handler;

import java.util.Map;

public interface DeifyPushHandler {

	public void pushTowerList(String userId);
	
	public void pushDeifyStatus(String userId);
	
	public void pushRoomList(String userId, int towerId);
	
	public void pushProtected(String userId, int towerId);
	
	public void pushReport(String userId, Map<String, String> param);
}
