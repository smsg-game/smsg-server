/**
 * 
 */

package com.lodogame.ldsg.partner.qihoo.payapp;

import java.util.HashMap;

public class PayAppDemo implements PayAppInterface {
	//TODO::需要修改为应用自身的app_key
	private String _appKey = "8689e00460eabb1e66277eb4232fde6d";
	//TODO::需要修改为应用自身的app_secret(服务器之间通讯使用)
	private String _appSecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	//TODO::人民币-游戏货币的兑换比例
	private int _cashRate = 10;

	
	public PayAppDemo()
	{
	}
	
	@Override
	public String getAppKey() {
		return this._appKey;
	}

	@Override
	public String getAppSecret() {
		return this._appSecret;
	}

	@Override
	public Boolean isValidOrder(HashMap<String, String> orderParams) {
		String orderId;

		orderId = orderParams.get("app_order_id");
		if (orderId == null || orderId.equals("")) {
			orderId = orderParams.get("order_id");
		}

		HashMap<String, String> order = this._getOrder(orderId);
		if (order == null) {
			return false;
		}

		String orderProcessed = order.get("processed");
		if (orderProcessed == null) {
			return true;
		}

		if (orderProcessed.equals("")) {
			return true;
		}

		return false;
	}

	//TODO::从数据库中获取订单
	private HashMap<String, String> _getOrder(String orderId) {
		
		HashMap<String, String> order = new HashMap<String, String>();
		order.put("order_id", orderId);
		//该订单是否已经处理过
		//没有处理过
		order.put("processed", "");
		//如果已经处理过，
		//order.put("processed", "1");
		return order;
	}

	//处理订单，发货或者增加游戏中的游戏币
	@Override
	public void processOrder(HashMap<String, String> orderParams) {
		Boolean re = this._updateOrder(orderParams);
		if (re) {
			return;
		}

		this._addCash(orderParams);
	}

	//TODO::更新数据库中的订单状态。
	private Boolean _updateOrder(HashMap<String, String> orderParams) {
		//更新订单,标识为已经处理，避免重复处理
		//如果更新订单状态失败,记录异常，以便再次处理。再次处理的逻辑需应用自己处理
		return true;
	}

	//TODO::发货或者增加游戏中的货币
	private Boolean _addCash(HashMap<String, String> orderParams) {
		
		//如果发货失败，记录异常，以便再次处理。处理的逻辑需应用自己处理。
		//充值金额，以人民币分为单位。例如2000代表20元
		String strAmount = orderParams.get("amount");
		int amount = Integer.parseInt(strAmount);
		//兑换比例(人民币兑换游戏货币，_cashRate==10,表示1元人民币可兑换10游戏货币)
		int gameCashNum = amount / 100 * this._cashRate;
		return true;
	}
}
