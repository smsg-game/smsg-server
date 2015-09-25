package com.lodogame.ldsg.action;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.ActivityDrawScoreRankBo;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ActivityDrawService;

/**
 * 活动抽奖
 * 
 * @author Candon
 * 
 */
public class ActivityDrawAction extends LogicRequestAction {

	@Autowired
	private ActivityDrawService activityDrawService;

	@Autowired
	private PushHandler pushHandler;

	public Response draw() {

		int time = this.getInt("time", 1);
		String userId = this.getUid();

		CommonDropBO commonDropBO = activityDrawService.draw(userId, time);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		set("sc", activityDrawService.getUserDrawScore(userId));

		return this.render();
	}

	public Response drawData() {

		String userId = this.getUid();

		set("sc", activityDrawService.getUserDrawScore(userId));
		set("ln", activityDrawService.getUserLuckOrder(userId));
		set("drl", activityDrawService.getSystemActivityDrawShowBOList());

		return this.render();
	}

	public Response drawRank() {

		List<ActivityDrawScoreRankBo> list = activityDrawService.getRank();

		set("sr", list);

		return this.render();
	}
}
