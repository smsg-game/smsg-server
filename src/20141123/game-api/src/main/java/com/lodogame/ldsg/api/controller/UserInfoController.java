package com.lodogame.ldsg.api.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.AdminSendAttachBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.model.SystemMallDiscountActivity;
import com.lodogame.model.SystemMallDiscountItems;
import com.lodogame.model.User;

/**
 * 用户操作及查询接口
 * 
 * @author foxwang
 * 
 */
@Controller
public class UserInfoController extends BaseController {
	@Autowired
	private GameApiService gameApiService;

	/**
	 * 查询玩家英雄列表
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/gameApiAdmin/getUserHeroList.do")
	public ModelAndView getUserHeroList(String userId) {
		List<UserHeroBO> reslut = gameApiService.getUserHeroList(userId);
		return this.renderJson(reslut);
	}

	/**
	 * 查询玩家装备列表
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/gameApiAdmin/getUserEquipList.do")
	public ModelAndView getUserEquipList(String userId) {
		List<UserEquipBO> reslut = gameApiService.getUserEquipList(userId);
		return this.renderJson(reslut);
	}

	/**
	 * 查询玩家道具列表
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/gameApiAdmin/getUserToolList.do")
	public ModelAndView getUserToolList(String userId) {
		List<UserToolBO> reslut = gameApiService.getUserToolList(userId);
		return this.renderJson(reslut);
	}

	/**
	 * 查询玩家信息
	 * 
	 * @param userId
	 * @param sign
	 * @return
	 */
	@RequestMapping(value = "/gameApiAdmin/getUserInfo.do")
	public ModelAndView getUserInfo(String userId) {
		if (StringUtils.isBlank(userId)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}
		User user = gameApiService.getUserInfo(userId);
		return this.renderJson(user);
	}

	/**
	 * 发装备或道具
	 * 
	 * @param isAllUser
	 *            1全服 2指定用户
	 * @param idStr
	 *            要发放的用户id 以逗号隔开 id1,id2,id3
	 * @param content
	 *            道具或装备信息bean AdminSendAttachBO
	 * @return
	 */
	@RequestMapping(value = "/gameApiAdmin/addEquipMentOrTools.do")
	public ModelAndView addEquipMentOrTools(int isAllUser, String userIdStr, String content) {
		if ((isAllUser != 1 && isAllUser != 2) || StringUtils.isBlank(content) || (isAllUser == 2 && StringUtils.isBlank(userIdStr))) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}
		List<AdminSendAttachBO> sendAttachList = Json.toList(content, AdminSendAttachBO.class);
		boolean isSendAll = (isAllUser == 1 ? true : false);
		String[] playerIds = userIdStr.split(",");
		// 全服发放装备或道具
		Map<String, List<AdminSendAttachBO>> result = gameApiService.addToolsOrEquipMent(sendAttachList, playerIds, isSendAll);
		return this.renderJson(result);
	}

	/**
	 * 添加商城打折活动中的打折商品
	 * 
	 * @param mallIds
	 *            某次打折活动中，参加打折活动的商品id
	 * @param discounts
	 *            参加打折活动的商品的折扣率，和 mallIds 一一对应
	 * @return
	 */
	@RequestMapping(value = "/gameApiAdmin/addMallDiscountItems.do")
	public ModelAndView addMallDiscountItems(String activityId, String mallIds, String discounts) {
		List<SystemMallDiscountItems> itemList = gameApiService.addMallDiscountItems(activityId, mallIds, discounts);
		return this.renderJson(itemList);
	}

	/**
	 * 添加商城打折活动信息
	 * 
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/gameApiAdmin/addMallDiscount.do")
	public ModelAndView addMallDiscount(String activityId, String startTime, String endTime) throws ParseException {

		SystemMallDiscountActivity activity = gameApiService.addMallDiscount(activityId, startTime, endTime);
		return this.renderJson(activity);
	}

	/**
	 * 删除打折活动信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gameApiAdmin/delActivity.do")
	public ModelAndView delActivity(String activityId) {
		gameApiService.delActivity(activityId);
		return this.renderJson(activityId);
	}

	/**
	 * 删除打折活动商品
	 */
	@RequestMapping(value = "/gameApiAdmin/delItems.do")
	public ModelAndView delItems(String activityId) {
		gameApiService.delItems(activityId);
		return this.renderJson(activityId);
	}

	@RequestMapping(value = "/gameApiAdmin/addUserGold.do")
	public ModelAndView addUserGold(int gold, String userIdStr) {
		if (gold == 0 || StringUtils.isBlank(userIdStr)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数错误");
		}
		String[] userIdArr = userIdStr.split(",");
		String result = gameApiService.addGoldByUserIds(gold, userIdArr);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("rg", 1000);
		return this.renderJson(map);
	}

	/**
	 * 用户封号
	 */
	@RequestMapping(value = "/gameApiAdmin/banUser.do")
	public ModelAndView banUser(String userId, String dueTime) {

		Map<String, Object> map = new HashMap<String, Object>();
		int rt = 1000;

		boolean success = gameApiService.banUser(userId, dueTime);
		if (!success) {
			rt = 2001;
		}

		map.put("rt", rt);
		return this.renderJson(map);
	}

	/**
	 * 更新 VIP 等级
	 */
	@RequestMapping(value = "/gameApiAdmin/assignVipLevel.do")
	public ModelAndView updateVipLevel(String userId, Integer vipLevel) {
		Map<String, Object> map = new HashMap<String, Object>();

		int rt = 1000;
		boolean success = gameApiService.assignVipLevel(userId, vipLevel);
		if (!success) {
			rt = 2001;
		}
		map.put("rt", rt);
		return this.renderJson(map);
	}

}
