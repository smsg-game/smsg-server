package com.lodogame.ldsg.web.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
	
	/**
	 * 获取平台回调订单源
	 * @param req
	 * @return
	 */
	public static String getOrderSource(HttpServletRequest req){
		String callBackOrderSource="";
		StringBuilder params = new StringBuilder();
		Enumeration paramNames = req.getParameterNames();
		while ((paramNames != null) && (paramNames.hasMoreElements())) {
			String paramName = (String) paramNames.nextElement();
			String[] values = req.getParameterValues(paramName);
			if ((values == null) || (values.length == 0))
				continue;
			if (values.length == 1)
				params.append(paramName + "=" + values[0] + "&");
			else {
				for (int i = 0; i < values.length; i++) {
					params.append(paramName + "=" + values[i] + "&");
				}
			}
		}
		if (params.length() > 0) {
			callBackOrderSource = params.substring(0, params.length() - 1);
		}
		return callBackOrderSource;
	}
		

}
