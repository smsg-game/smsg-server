package com.lodogame.ldsg.web.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.game.dao.ActiveCodeDao;
import com.lodogame.game.dao.ServerStatusDao;
import com.lodogame.game.dao.VersionServerDao;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.partner.sdk.QiHooSdk;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.sdk.GameServerCache;
import com.lodogame.ldsg.service.WebApiService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.model.GameServer;
import com.lodogame.model.Notice;
import com.lodogame.model.VersionServer;

@Controller
public class LoginController {

	private static Logger LOG = Logger.getLogger(LoginController.class);

	@Autowired
	private ActiveCodeDao activeCodeDao;

	@Autowired
	private VersionServerDao versionServerDao;

	@Autowired
	private WebApiService webApiService;

	@Autowired
	private PartnerServiceFactory serviceFactory;

	private Map<String, List<GameServer>> serverMap;

	@Autowired
	protected ServerStatusDao serverStatusDao;
	
	
	public void getRequstParams(HttpServletRequest req){
		String reqParams="";
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
			reqParams = params.substring(0, params.length() - 1);
		}
		
		LOG.info("request params : " + reqParams);
	}
	
	@RequestMapping("/webApi/login.do")
	public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
		String token = req.getParameter("token");
		String partnerId = req.getParameter("partnerId");
		String serverId = req.getParameter("serverId");
		String timestamp = req.getParameter("timestamp");
		String sign = req.getParameter("sign");
		String imei = req.getParameter("fr");
		String mac = req.getParameter("mac");
		String idfa = req.getParameter("idfa");
		
		getRequstParams(req);

		PartnerService ps = serviceFactory.getBean(partnerId);
	    LOG.info("渠道:"+partnerId+":"+ps.getClass().getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			Map<String, String> params = new HashMap<String, String>();
			params.put("imei", imei);
			params.put("mac", mac);
			params.put("idfa", idfa);

			UserToken userToken = ps.login(token, partnerId, serverId, Long.parseLong(timestamp), sign, params);
			if (userToken != null) {
				map.put("rc", WebApiService.SUCCESS);
				Map<String, String> data = new HashMap<String, String>();
				data.put("tk", userToken.getToken());
				data.put("uid", userToken.getUserId());
				data.put("puid", userToken.getPartnerUserId());
				data.put("ptk", userToken.getPartnerToken());
				data.put("exti", userToken.getExtInfo());
				Notice notice = webApiService.getNotice(serverId, partnerId);
				if (notice != null && notice.getIsEnable() == 1) {
					data.put("title", notice.getTitle());
					data.put("notice", notice.getContent());
				} else {
					notice = webApiService.getNotice(serverId, "all");
					if(notice != null && notice.getIsEnable() == 1){
						data.put("title", notice.getTitle());
						data.put("notice", notice.getContent());
					}else{
						notice = webApiService.getNotice("all", "all");
						if(notice != null && notice.getIsEnable() == 1){
							data.put("title", notice.getTitle());
							data.put("notice", notice.getContent());
						}
					}
				}
				map.put("dt", data);
			} else {
				map.put("rc", WebApiService.UNKNOWN_ERROR);
			}
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", e.getCode());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", WebApiService.UNKNOWN_ERROR);
		}
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		LOG.debug("parnterId:" + partnerId + "," + Json.toJson(map));
		return modelView;
	}

	@RequestMapping("/webApi/isActive.do")
	public ModelAndView isActive(HttpServletRequest req, HttpServletResponse res) {
		String uuid = req.getParameter("uuid");
		String partnerId = req.getParameter("partnerId");
		Map<String, Object> map = new HashMap<String, Object>();
		PartnerService ps = serviceFactory.getBean(partnerId);
		try {
			boolean isActive = ps.isActive(uuid, partnerId);
			map.put("rc", isActive ? WebApiService.SUCCESS : WebApiService.ACTIVE_FAILD);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", e.getCode());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", WebApiService.UNKNOWN_ERROR);
		}
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		LOG.debug(Json.toJson(map));
		return modelView;
	}

	@RequestMapping("/webApi/active.do")
	public ModelAndView active(HttpServletRequest req, HttpServletResponse res) {
		String uuid = req.getParameter("uuid");
		String partnerId = req.getParameter("partnerId");
		String code = req.getParameter("code");
		PartnerService ps = serviceFactory.getBean(partnerId);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			boolean success = ps.active(uuid, code, partnerId);
			map.put("rc", success ? WebApiService.SUCCESS : WebApiService.ACTIVE_FAILD);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", e.getCode());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", WebApiService.UNKNOWN_ERROR);
		}
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		LOG.debug(Json.toJson(map));
		return modelView;
	}

	/**
	 * 获取服务器列表
	 * 
	 * @return
	 */
	// TODO 实现获取服务器列表
	@RequestMapping("/webApi/getServerList.do")
	public ModelAndView getServerList(HttpServletRequest req) {
		String partnerId = StringUtils.isBlank(req.getParameter("partnerId")) ? "1001" : req.getParameter("partnerId");
		List<GameServer> serverList = null;
		String version = req.getParameter("version");
		VersionServer versionServer = null;

		if (!StringUtils.isBlank(version)) {
			versionServer = versionServerDao.getVersionServer(version);
		}
		serverList = GameServerCache.getInstance().getGameServers(partnerId);
		if (serverList != null && serverList.size() > 0) {

			boolean isSpceImei = false;
			if (req.getParameter("fr") != null && activeCodeDao.isBlackImei(req.getParameter("fr"), partnerId)) {
				isSpceImei = true;
			}

			if (req.getParameter("mac") != null && activeCodeDao.isBlackImei(req.getParameter("mac"), partnerId)) {
				isSpceImei = true;
			}

			String ip = req.getRemoteAddr();

			if (this.serverStatusDao.isWhiteIp(partnerId, ip)) {
				isSpceImei = true;
			}

			// 处理IMEI白名单
			List<GameServer> serverListForSpecImei = new ArrayList<GameServer>(serverList.size());
			for (GameServer gs : serverList) {

				// IMEI白名单中的GameServer状态都需要为1
				int status = gs.getStatus();
				if (status == 100) {
					if (isSpceImei) {
						status = 1;
					} else {
						continue;
					}
				}

				String ind = gs.getServerId().replaceAll("[a-zA-Z]+", "");
				if (versionServer != null) {
					if (versionServer.getServerId().indexOf(gs.getServerId()) < 0) {
						continue;
					}
				}
				GameServer gameServerForSpecImei = new GameServer();
				gameServerForSpecImei.setServerId(gs.getServerId());
				gameServerForSpecImei.setServerName(ind + "-" + gs.getServerName());
				gameServerForSpecImei.setServerPort(gs.getServerPort());
				gameServerForSpecImei.setStatus(status);
				gameServerForSpecImei.setOpenTime(gs.getOpenTime());
				gameServerForSpecImei.setServerHost(gs.getServerHost());

				serverListForSpecImei.add(gameServerForSpecImei);

			}

			Collections.sort(serverListForSpecImei, new Comparator<GameServer>() {

				@Override
				public int compare(GameServer o1, GameServer o2) {
					if (o1.getOpenTime() > o2.getOpenTime()) {
						return -1;
					} else if (o1.getOpenTime() < o2.getOpenTime()) {
						return 1;
					}

					return 0;
				}

			});

			// 使用为IMEI白名单定制的ServerList
			serverList = serverListForSpecImei;

		} else {
			serverList = new ArrayList<GameServer>();
			GameServer gs = new GameServer();
			gs.setServerId("d1");
			gs.setServerName("测试服务器-潮哥");
			gs.setStatus(1);
			serverList.add(gs);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sl", serverList);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/webApi/getSysTime.do")
	public ModelAndView getSysTime() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Long> tMap = new HashMap<String, Long>();
		tMap.put("t", System.currentTimeMillis());
		map.put("rc", 1000);
		map.put("dt", tMap);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/webApi/updateConfigs.do")
	public ModelAndView updateServers() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			GameServerCache.getInstance().reload();
			Properties properties = new Properties();
			properties.load(QiHooSdk.class.getResourceAsStream("/sdk.properties"));
			Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
			Set<String> set = new HashSet<String>();
			while (iterator.hasNext()) {
				try {
					Entry<Object, Object> entry = iterator.next();
					String className = StringUtils.split(entry.getKey().toString(), ".")[0];
					if (!set.contains(className)) {
						Class sdkClass = Class.forName("com.lodogame.ldsg.partner.sdk." + className);
						if (sdkClass == null) {
							continue;
						}
						Method instanceMethod = sdkClass.getMethod("instance", new Class[] {});
						Method reloadMethod = sdkClass.getMethod("reload", new Class[] {});
						Object object = instanceMethod.invoke(null, null);
						reloadMethod.invoke(object, null);
						LOG.info(className + " reload!");
						set.add(className);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// QiHooSdk.instance().reload();
		// BaiduSdk.instance().reload();
		// UcSdk.instance().reload();
		// LenovoSdk.instance().reload();
		// EasouSdk.instance().reload();
		// PartnerConfig.ins().reload();
		// BaiduZsSdk.instance().reload();
		// AnZhiSdk.instance().reload();
		// AppChinaSdk.instance().reload();
		// AppleSdk.instance().reload();
		// ChangWanSdk.instance().reload();
		// ChinaMobileSdk.instance().reload();
		// DangleSdk.instance().reload();
		// DuokuSdk.instance().reload();
		// HucnSdk.instance().reload();
		// SanqiWanwanSdk.instance().reload();
		// ShiJiaSdk.instance().reload();
		// XiaomiSdk.instance().reload();
		// KuaiBoSdk.instance().reload();
		// AnZhiAdSdk.instance().reload();
		// PPSSdk.instance().reload();
		// KuWoSdk.instance().reload();
		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping(value = "/webApi/serverList.do", method = RequestMethod.POST)
	public ModelAndView serverList(String servers, String partnerId, long timestamp, String sign) {
		if (StringUtils.isBlank(servers) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign(servers, partnerId, timestamp, sign);

		List<GameServer> serverList = Json.toList(servers, GameServer.class);

		if (serverMap == null) {
			serverMap = new HashMap<String, List<GameServer>>();
		}

		serverMap.put(partnerId, serverList);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	private void checkSign(String item, String playerIds, long timestamp, String sign) {
		long now = System.currentTimeMillis();
		if ((now - timestamp) > 1000 * 60) {
			LOG.info("请求时间超出范围：" + timestamp + "(" + new Date(timestamp) + ")");
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求时间超出范围（10s）");
		}
		String md5 = EncryptUtil.getMD5(item + playerIds + timestamp + Config.ins().getSignKey());
		if (!md5.toLowerCase().equals(sign.toLowerCase())) {
			LOG.info("请求签名不正确：" + sign + "(" + md5 + ")");
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求签名不正确");
		}
	}

	public WebApiService getWebApiService() {
		return webApiService;
	}

	public void setWebApiService(WebApiService webApiService) {
		this.webApiService = webApiService;
	}

}
