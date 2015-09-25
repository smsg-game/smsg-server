package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.AwardBO;
import com.lodogame.ldsg.bo.ChooseResultBO;
import com.lodogame.ldsg.bo.CityBo;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.bo.UserWarInfoBo;
import com.lodogame.ldsg.bo.WarAllCDBO;
import com.lodogame.ldsg.bo.WarAttackRankBO;
import com.lodogame.ldsg.bo.WarAwardBo;
import com.lodogame.ldsg.bo.WarEnterBo;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.WarService;
import com.lodogame.model.User;
import com.lodogame.model.UserTool;
import com.lodogame.model.UserWarInfo;

/**
 * 国战
 * 
 * @author Candon 2014-02-10
 * 
 */
public class WarAction extends LogicRequestAction {

	@Autowired
	private WarService warService;

	@Autowired
	private UserService userService;

	@Autowired
	private PushHandler pushHandler;

	private static final Logger LOG = Logger.getLogger(WarAction.class);

	/**
	 * 国战入口
	 * 
	 * @return
	 */
	public Response enter() {
		String userId = this.getUid();
		WarEnterBo warEnterBo = this.warService.enter(userId);
		set("wb", warEnterBo);
		return this.render();
	}

	/**
	 * 领取一座城池停留30秒奖励
	 * 
	 * @return
	 */
	public Response drawStay() {
		String userId = this.getUid();
		Integer point = this.getInt("po", 11);
		CommonDropBO commonDropBO = this.warService.drawStay(userId, point);

		if (commonDropBO.isNeedPushUser()) {
			pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);
		return this.render();
	}

	/**
	 * 选择国家
	 * 
	 * @return
	 */
	public Response choose() {
		String userId = this.getUid();
		int cid = this.getInt("cid", 0);
		ChooseResultBO chooseResultBO = this.warService.choose(userId, cid);
		set("crb", chooseResultBO);
		return this.render();
	}

	/**
	 * 城池状态
	 * 
	 * @return
	 */
	public Response status() {
		return this.render();
	}

	/**
	 * 清除行动CD
	 * 
	 * @return
	 */
	public Response clearActionCD() {
		String userId = this.getUid();
		int type = this.getInt("ct", 0);
		this.warService.clearActionCD(userId, type);
		return this.render();
	}

	/**
	 * 战斗
	 * 
	 * @return
	 */
	public Response attackCity() {
		final String userId = this.getUid();
		final Integer point = this.getInt("po", 0);

		this.warService.attackCity(userId, point, new EventHandle() {
			@Override
			public boolean handle(Event event) {

				int status = event.getInt("st");
				if (status == 2) {
					String report = event.getString("report");
					int flag = event.getInt("flag");
					set("rp", report);
					set("rf", flag);
					set("auid", getUid());
					set("tp", 6);
					set("duid", event.getString("duid"));
					set("aun", event.getString("aun"));
					set("dun", event.getString("dun"));
					set("uws", event.getString("uws"));
					set("acid", event.getInt("acid"));
					set("occ", event.getInt("occ"));
					set("dcid", event.getInt("dcid"));
					set("dpo", point);
					set("isc", 1);

				} else {

					UserWarInfoBo userWarInfoBo = warService.getUserWarInfoBo(userId);
					set("uws", userWarInfoBo);

				}

				set("st", status);

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}
				return true;
			}
		});

		// UserWarInfoBo userWarInfoBo =
		// this.warService.getUserWarInfoBo(userId);
		// set("uws", userWarInfoBo);
		// return this.render();

		return null;
	}

	public Response clearLiftCD() {
		String userId = this.getUid();
		this.warService.clearLiftCD(userId);
		return this.render();
	}

	public Response inspire() {
		String userId = this.getUid();
		this.warService.inspire(userId);
		return this.render();
	}

	public Response look() {
		String userId = this.getUid();
		List<CityBo> list = this.warService.look(userId);
		set("cl", list);
		return this.render();
	}

	public Response getCDAndAttackNum() {
		String userId = this.getUid();
		WarAllCDBO warAllCDBO = this.warService.getCDAndAttackNum(userId);
		set("wab", warAllCDBO);
		return this.render();
	}

	public Response getAwardList() {
		List<WarAwardBo> list = this.warService.getAwardList();
		set("al", list);
		return this.render();
	}

	/**
	 * 领取击杀排行榜数据
	 * 
	 * @return
	 */
	public Response receive() {

		String userId = this.getUid();

		CommonDropBO commonDropBO = this.warService.receive(userId);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();
	}

	public Response exchange() {

		int aid = getInt("aid", 0);
		int num = getInt("num", 0);

		if (num <= 0) {
			return null;
		}

		String userId = getUid();
		CommonDropBO commonDropBO = this.warService.exchange(userId, aid, num);
		User user = userService.get(userId);
		set("dr", commonDropBO);
		set("num", user.getReputation());
		return this.render();
	}

	public Response exitWar() {
		String userId = this.getUid();
		this.warService.exitWar(userId);
		return this.render();
	}

	public Response getAttackRankList() {
		String userId = this.getUid();
		List<WarAttackRankBO> rl = this.warService.getWarAttackRankBOList(userId);
		set("rl", rl);
		return this.render();
	}

}
