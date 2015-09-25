package com.lodogame.ldsg.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.game.dao.ServerStatusDao;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;

@Controller
public class ApiController {

	@Autowired
	protected ServerStatusDao serverStatusDao;

	@RequestMapping("/webApi/setServerStatus.do")
	public ModelAndView setServerStatus(HttpServletRequest req) {
		String whiteList = req.getParameter("whiteList");
		String sign = req.getParameter("sign");
		int status = Integer.parseInt(req.getParameter("status"));
		int id = Integer.parseInt(req.getParameter("id"));
		long timestamp = Long.parseLong(req.getParameter("timestamp"));

		checkSign(timestamp, sign);

		serverStatusDao.setServerStatus(id, status, whiteList);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	private void checkSign(long timestamp, String sign) {
		long now = System.currentTimeMillis();
		if ((now - timestamp) > 1000 * 60) {
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求时间超出范围（10s）");
		}
		String md5 = EncryptUtil.getMD5(timestamp + Config.ins().getSignKey());
		if (!md5.toLowerCase().equals(sign.toLowerCase())) {
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求签名不正确");
		}
	}
}
