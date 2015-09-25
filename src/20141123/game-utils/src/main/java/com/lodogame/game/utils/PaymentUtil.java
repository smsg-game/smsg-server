package com.lodogame.game.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class PaymentUtil {

	// public static Map<Integer, Integer> parserRate(String strRate){
	// Map<Integer, Integer> rates = new HashMap<Integer, Integer>();
	// if(StringUtils.isBlank(strRate)){
	// return rates;
	// }
	//
	// String strs[] = strRate.split("\\|");
	// for(String str : strs){
	// String infos[] = str.split(",");
	// if(infos.length != 2){
	// continue;
	// }
	// int gold = Integer.parseInt(infos[0]);
	// double rate = Double.parseDouble(infos[1]);
	// rates.put(gold, (int)(gold*rate));
	// }
	// return rates;
	// }

	public static double parserRate(String strRate) {
		try {
			return Double.parseDouble(strRate.trim());
		} catch (Exception e) {
			return 0d;
		}
	}
}
