package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.AwardBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.PkGroupAwardGrantBO;
import com.lodogame.ldsg.bo.PkGroupAwardLogBo;
import com.lodogame.ldsg.bo.PkGroupFirstBo;
import com.lodogame.ldsg.bo.PkInfoBO;
import com.lodogame.ldsg.bo.PkPlayerBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.PkHelper;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.model.UserPkInfo;

public class PkAction extends LogicRequestAction {

	@Autowired
	private PkService pkService;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private UserPkInfoDao userPkInfoDao;

	// 总排名入口
	public Response enterPk() {
		String userId = getUid();
		pkService.enterPk(userId, new EventHandle() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean handle(Event event) {
				PkInfoBO pkInfoBo = (PkInfoBO) event.getObject("pkInfoBo");
				List<PkPlayerBO> pkPlayers = (List<PkPlayerBO>) event.getObject("pkPlayers");
				set("upi", pkInfoBo);
				set("ps", pkPlayers);

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}
				return true;
			}
		}, 1);
		return null;
	}

	// 组排名入口
	public Response groupEnterPk() {
		String userId = getUid();
		pkService.enterPk(userId, new EventHandle() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean handle(Event event) {
				PkInfoBO pkInfoBo = (PkInfoBO) event.getObject("pkInfoBo");
				List<PkPlayerBO> pkPlayers = (List<PkPlayerBO>) event.getObject("pkPlayers");
				PkGroupAwardGrantBO pkGroupAwardGrantBO = (PkGroupAwardGrantBO) event.getObject("ig");
				set("upi", pkInfoBo);
				set("ps", pkPlayers);
				set("ig", pkGroupAwardGrantBO);

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}
				return true;
			}
		}, 0);
		return null;
	}

	/**
	 * 获取用户争霸赛信息
	 * 
	 * @return
	 */
	public Response getUserPkInfo() {
		String userId = getUid();
		PkInfoBO pkInfoBo = pkService.getUserPkInfo(userId);
		set("rk", pkInfoBo.getRank());
		set("sc", pkInfoBo.getScore());
		set("pkt", pkInfoBo.getPkTimes());
		return this.render();
	}

	/**
	 * 读取可攻击列表
	 * 
	 * @return
	 */
	public Response loadPlayers() {
		String userId = getUid();
		int isGroup = getInt("gr", 0);
		List<PkPlayerBO> list = pkService.loadPlayers(userId, isGroup);
		set("ps", list);
		return this.render();
	}

	/**
	 * 读取前十排行
	 * 
	 * @return
	 */
	public Response loadRank() {

		List<PkPlayerBO> list = pkService.loadTopPlayers();
		set("ps", list);
		return this.render();

	}

	/**
	 * 购习争霸次数
	 * 
	 * @return
	 */
	public Response buyPkTimes() {

		String userId = getUid();

		this.pkService.buyPkTimes(userId);

		this.pushHandler.pushUser(userId);

		PkInfoBO pkInfoBO = this.pkService.getUserPkInfo(userId);

		set("upi", pkInfoBO);

		return this.render();
	}

	/**
	 * 兑换奖励
	 * 
	 * @return
	 */
	public Response exchange() {
		String userId = getUid();
		int aid = getInt("aid", 0);
		AwardBO awardBo = pkService.exchange(userId, aid);
		PkInfoBO pkInfoBo = pkService.getUserPkInfo(userId);
		set("sc", pkInfoBo.getScore());
		set("ad", awardBo);
		return this.render();
	}

	/**
	 * 批量兑换奖励
	 * 
	 * @return
	 */
	public Response batchExchange() {
		String userId = getUid();
		int num = getInt("num", 0);
		int aid = getInt("aid", 0);

		AwardBO awardBO = pkService.batchExchange(userId, aid, num);
		PkInfoBO pkInfoBo = pkService.getUserPkInfo(userId);
		set("sc", pkInfoBo.getScore());
		set("ad", awardBO);
		return this.render();
	}

	public Response showGroupTen() {
		String userId = getUid();
		int gid = getInt("gid", 1);

		List<PkPlayerBO> list = pkService.getGrankTen(gid);

		set("ps", list);
		set("gr", pkService.getUserGrank(userId, gid));
		return this.render();
	}

	public Response showTotalTen() {
		String userId = getUid();

		List<PkPlayerBO> list = pkService.getTotalTen();

		set("ps", list);
		set("rn", userPkInfoDao.getByUserId(userId).getRank());
		return this.render();
	}

	/**
	 * 显示每组第一名
	 * 
	 * @return
	 */
	public Response showGroup() {

		String userId = getUid();
		List<PkGroupFirstBo> list = pkService.getGrankFirst();
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		userPkInfo.setgRank(pkService.getUserGrank(userId, PkHelper.getGroup(userPkInfo.getUser_level())));

		set("pf", list);
		set("grn", userPkInfo.getgRank());
		set("rn", userPkInfo.getRank());
		set("ti", userPkInfo.getTimes());

		return this.render();
	}

	public Response showGroupAwardLog() {
		int groupId = getInt("gid", 1);
		List<PkGroupAwardLogBo> list = pkService.getAwardLogBos(groupId);

		set("pg", list);

		return this.render();
	}

	public Response receiveAward() {

		String userId = getUid();
		CommonDropBO commonDropBO = pkService.updateisGet(userId);

		if (commonDropBO.isNeedPushUser()) {
			this.pushHandler.pushUser(userId);
		}

		set("dr", commonDropBO);

		return this.render();

	}
}
