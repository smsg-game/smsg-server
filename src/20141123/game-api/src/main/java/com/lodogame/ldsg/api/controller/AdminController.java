package com.lodogame.ldsg.api.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.Assert;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.SystemHero;
import com.lodogame.model.SystemTool;

/**
 * 后台接口支持
 * 
 * @author CJ
 * 
 */
@Controller
public class AdminController extends BaseController {

	private static final Logger log = Logger.getLogger(AdminController.class);

	@Autowired
	private GameApiService gameApiService;

	@RequestMapping(value = "/gameApi/addGold.do", method = RequestMethod.POST)
	public ModelAndView addGold(int gold, String playerIds, long timestamp, String sign) {
		if (gold == 0 || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign(Integer.toString(gold), playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");

		String result = gameApiService.addGold(gold, playerIdArr);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("rg", 1000);
		return this.renderJson(map);
	}

	private void checkSign(String item, String playerIds, long timestamp, String sign) {
		long now = System.currentTimeMillis();
		if ((now - timestamp) > 1000 * 60) {
			log.info("请求时间超出范围：" + timestamp + "(" + new Date(timestamp) + ")");
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求时间超出范围（10s）");
		}
		String md5 = EncryptUtil.getMD5(item + playerIds + timestamp + Config.ins().getSignKey());
		if (!md5.toLowerCase().equals(sign.toLowerCase())) {
			log.info("请求签名不正确：" + sign + "(" + md5 + ")");
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求签名不正确");
		}
	}

	@RequestMapping(value = "/gameApi/addCopper.do", method = RequestMethod.POST)
	public ModelAndView addCopper(int copper, String playerIds, long timestamp, String sign) {
		if (copper == 0 || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign("" + copper, playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");
		String result = "";
		if (copper > 0) {
			result = gameApiService.addCopper(copper, playerIdArr);
		} else {
			result = gameApiService.reduceCopper(copper, playerIdArr);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("rc", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/addHeroBag.do", method = RequestMethod.POST)
	public ModelAndView addHeroBag(String playerIds, long timestamp, String sign, int nums) {
		if (nums == 0 || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign("", playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");
		String result = gameApiService.addUserHeroBag(playerIdArr, nums);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("rh", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/addEquipBag.do", method = RequestMethod.POST)
	public ModelAndView addEquipBag(String playerIds, long timestamp, String sign, int nums) {
		if (nums == 0 || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign("", playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");
		String result = gameApiService.addUserEquipBag(playerIdArr, nums);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("re", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/addHero.do", method = RequestMethod.POST)
	public ModelAndView addHeros(String heros, String playerIds, long timestamp, String sign) {
		if (StringUtils.isBlank(heros) || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign(heros, playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");
		String[] heroIdArr = heros.split(",");
		String result = gameApiService.addHeros(heroIdArr, playerIdArr);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("rh", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/addEquip.do", method = RequestMethod.POST)
	public ModelAndView addEquips(String equipIds, String playerIds, long timestamp, String sign) {
		if (StringUtils.isBlank(equipIds) || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign(equipIds, playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");
		String[] equipIdArr = equipIds.split(",");
		String result = gameApiService.addEquips(equipIdArr, playerIdArr);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("re", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/addTool.do", method = RequestMethod.POST)
	public ModelAndView addTool(String toolIds, String playerIds, long timestamp, String sign) {
		if (StringUtils.isBlank(toolIds) || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}

		checkSign(toolIds, playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");
		String[] toolIdArr = toolIds.split(",");
		String result = gameApiService.addTools(toolIdArr, playerIdArr);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("rt", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/addTools.do", method = RequestMethod.POST)
	public ModelAndView addTools(String toolIds, String playerIds, long timestamp, int nums, String sign) {
		if (StringUtils.isBlank(toolIds) || StringUtils.isBlank(playerIds) || StringUtils.isBlank(sign) || nums == 0) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}
		checkSign(toolIds + nums, playerIds, timestamp, sign);

		String[] playerIdArr = playerIds.split(",");
		String[] toolIdArr = toolIds.split(",");
		String result = gameApiService.addTools(toolIdArr, playerIdArr, nums);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("rt", 1000);
		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/sendSysMsg.do")
	public ModelAndView sendMsg(String content, long timestamp, String sign, String partnerIds) {

		checkSign(content, "", timestamp, sign);

		gameApiService.sendSysMsg(content, partnerIds);

		return this.renderJson();
	}

	@RequestMapping("/gameApi/getAllSytemHero.do")
	public ModelAndView getAllSystemHero() {
		List<SystemHero> list = gameApiService.getAllSystemHero();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt", list);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/sendMail.do")
	public ModelAndView sendMail(String title, String content, String toolIds, String lodoIds, Integer target, String sourceId, Long date, String partner) {

		Map<String, Object> map = new HashMap<String, Object>();

		String message = "发送成功";
		int rt = 1000;

		try {

			Assert.notEmtpy(title, ServiceReturnCode.FAILD, "邮件标题不能为空.title[" + title + "]");
			Assert.notEmtpy(content, ServiceReturnCode.FAILD, "邮件内容不能为空.content[" + content + "]");
			Assert.notEmtpy(target, ServiceReturnCode.FAILD, "目标类型不能为空.target[" + target + "]");
			Assert.notEmtpy(sourceId, ServiceReturnCode.FAILD, "源ID不能为空.sourceId[" + sourceId + "]");
			Assert.notEmtpy(sourceId, ServiceReturnCode.FAILD, "渠道商 id 不能为空.sourceId[" + partner + "]");

			Date mailDate = new Date(date);

			this.gameApiService.sendMail(title, content, toolIds, target, lodoIds, sourceId, mailDate, partner);

		} catch (ServiceException se) {
			rt = se.getCode();
			message = se.getMessage();
		} catch (Exception se) {
			rt = 3000;
			message = se.getMessage();
		}

		map.put("rt", rt);
		map.put("msg", message);

		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/getAllSystemEquip.do")
	public ModelAndView getAllSystemEquip() {
		List<SystemEquip> list = gameApiService.getAllSystemEquip();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt", list);
		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/getAllOtherTool.do")
	public ModelAndView getAllOtherTool() {
		List<SystemTool> list = gameApiService.getAllOtherTool();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt", list);
		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/addActivity.do")
	public ModelAndView addActivity(int activityId, int activityType, String activityName, String activityDesc, String startTime, String endTime, String param, String openWeeks, int display, int sort) {
		System.out.println(activityId);
		SystemActivity systemActivity = new SystemActivity();
		systemActivity.setActivityId(activityId);
		systemActivity.setActivityType(activityType);
		systemActivity.setActivityName(activityName);
		systemActivity.setActivityDesc(activityDesc);
		systemActivity.setStartTime(DateUtils.str2Date(startTime));
		systemActivity.setEndTime(DateUtils.str2Date(endTime));
		systemActivity.setParam(param);
		systemActivity.setOpenWeeks(openWeeks);
		systemActivity.setDisplay(display);
		systemActivity.setSort(sort);
		boolean rec = gameApiService.addActivity(systemActivity);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", rec ? 1000 : 3000);
		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/modifyActivity.do")
	public ModelAndView modifyActivity(int activityId, int activityType, String activityName, String activityDesc, String startTime, String endTime, String param, String openWeeks, int display, int sort) {
		SystemActivity systemActivity = new SystemActivity();
		systemActivity.setActivityId(activityId);
		systemActivity.setActivityType(activityType);
		systemActivity.setActivityName(activityName);
		systemActivity.setActivityDesc(activityDesc);
		systemActivity.setStartTime(DateUtils.str2Date(startTime));
		systemActivity.setEndTime(DateUtils.str2Date(endTime));
		systemActivity.setParam(param);
		systemActivity.setOpenWeeks(openWeeks);
		systemActivity.setDisplay(display);
		systemActivity.setSort(sort);
		boolean rec = gameApiService.modifyActivity(systemActivity);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", rec ? 1000 : 3000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/dataSync.do", method = RequestMethod.POST)
	public ModelAndView dataSync(String table, String sqls) {

		log.debug("table[" + table + "], sqls[" + sqls + "]");

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			this.gameApiService.dataSync(table, sqls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("rc", 3000);
		}

		map.put("rc", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/updateUserLevel.do", method = RequestMethod.POST)
	public ModelAndView addExp(String userId, Integer level, Integer exp) {

		log.debug("userId[" + userId + "], level[" + level + "], exp[" + exp + "]");

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			this.gameApiService.updateUserLevel(userId, level, exp);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("rc", 3000);
		}

		map.put("rc", 1000);
		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/modifyForcesTimes.do")
	public ModelAndView modifyForces(int forcesId, int times) {
		int rec = gameApiService.modifyForcesTimes(forcesId, times);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", rec > 0 ? 1000 : 3000);
		return this.renderJson(map);
	}

}
