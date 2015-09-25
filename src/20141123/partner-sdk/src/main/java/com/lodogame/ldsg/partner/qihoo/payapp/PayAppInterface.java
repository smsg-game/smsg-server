/**
 * 
 */

package com.lodogame.ldsg.partner.qihoo.payapp;

import java.util.HashMap;

public interface PayAppInterface {

	//返回app_key
	public String getAppKey();

	//返回app_secret
	public String getAppSecret();

	//订单是否需要被处理(接下来是否需要调用processOrder)
	public Boolean isValidOrder(HashMap<String,String> orderParams);

	//处理订单，发货或者增加游戏中的游戏币
	public void processOrder(HashMap<String,String> orderParams);
}
