package com.lodogame.ldsg.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.config.Config;

/**
 * 系统控制器，用户对服务器进行一些操作
 * 
 * @author CJ
 * 
 */
@Controller
public class SystemController extends BaseController {
	private static final Logger log = Logger.getLogger(SystemController.class);

	/**
	 * 获取服务器状态
	 * 
	 * @return
	 */
	@RequestMapping("/server/getStatus")
	public ModelAndView getServerStatus() {
		String serverStatusStr = JedisUtils.get(RedisKey.getSystemStatusKey());
		Map<String, Object> serverStatus = Json.toObject(serverStatusStr, HashMap.class);
		String serverStrutsStr = (String) serverStatus.get("serverStruts");
		Map<String, List> serverStruts = Json.toObject(serverStrutsStr, HashMap.class);
		serverStatus.put("serverStruts", serverStruts);
		return new ModelAndView("server/status", serverStatus);
	}

	@RequestMapping("/server/restart")
	public ModelAndView restart() {
		log.debug("isDebug:" + Config.ins().isDebug());
		if (Config.ins().isDebug()) {
			Runtime runTime = Runtime.getRuntime();
			Process pro = null;
			try {
				pro = runTime.exec("/data/shell/run.sh");
				JedisUtils.flushAll();
				log.debug("执行重启指令完成");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		return this.renderJson();
	}
}
