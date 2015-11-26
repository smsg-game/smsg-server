package com.lodogame.ldsg.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.manager.TokenManager;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;
import com.lodogame.model.UserMapper;

/**
 * 游戏服务器API
 * 
 * @author CJ
 * 
 */
@Controller
public class UserController {

	private static final Logger LOG = Logger.getLogger(UserController.class);

	@Autowired
	private GameApiService gameApiService;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private UserService userService;

	@RequestMapping("/gameApi/create.do")
	public ModelAndView create(HttpServletRequest req, HttpServletResponse res) {

		String username = req.getParameter("username");
		String partnerUserId = req.getParameter("partnerUserId");
		String serverId = req.getParameter("serverId");

		UserMapper userMapper = gameApiService.loadUserMapper(partnerUserId, serverId, "1004", "bgf1278_12296_001", null, null, null);
		boolean success = gameApiService.createUser(username, userMapper.getUserId());

		Map<String, Object> map = new HashMap<String, Object>();

		if (success) {
			map.put("rc", 1000);
			map.put("msg", "成功");
		} else {
			map.put("rc", 2001);
			map.put("msg", "失败");
		}

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/gameApi/login.do")
	public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> map = new HashMap<String, Object>();

		String partnerUserId = req.getParameter("partnerUserId");
		String serverId = req.getParameter("serverId");
		String userIp = req.getRemoteAddr();

		UserMapper userMapper = userMapperDao.getByPartnerUserId(partnerUserId, "1004", serverId);

		if (userMapper != null) {
			userService.login(userMapper.getUserId(), userIp);
			map.put("rc", 1000);
			map.put("msg", "登录成功");

		} else {
			map.put("rc", 2001);
			map.put("msg", "登录失败");
		}

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/gameApi/loadUserToken.do")
	public ModelAndView loadUserToken(String partnerUserId, String partnerId, String serverId, String qn, String imei, String mac, String idfa) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserMapper userMapper = gameApiService.loadUserMapper(partnerUserId, serverId, partnerId, qn, imei, mac, idfa);
			UserToken userToken = createUserToken(userMapper);
			map.put("userToken", userToken);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	private UserToken createUserToken(UserMapper userMapper) {
		UserToken ut = new UserToken();
		ut.setToken(EncryptUtil.getMD5(userMapper.getUserId() + System.currentTimeMillis() + UUID.randomUUID()));
		ut.setPartnerUserId(userMapper.getPartnerUserId());
		ut.setPartnerId(userMapper.getPartnerId());
		ut.setServerId(userMapper.getServerId());
		ut.setUserId(userMapper.getUserId());
		User user = null;
		try {
			user = userService.get(userMapper.getUserId());
		} catch (Exception e) {
		}
		if(user!=null){
			ut.setExtInfo(user.getLevel()+":"+user.getUsername());
		}else{
			ut.setExtInfo(1+":");
		}
		TokenManager.getInstance().setToken(ut.getToken(), ut);
		return ut;
	}

	public GameApiService getGameApiService() {
		return gameApiService;
	}

	public void setGameApiService(GameApiService gameApiService) {
		this.gameApiService = gameApiService;
	}
}
