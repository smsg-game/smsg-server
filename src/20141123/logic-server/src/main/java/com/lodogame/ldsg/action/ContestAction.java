package com.lodogame.ldsg.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.ContestFightResultBO;
import com.lodogame.ldsg.bo.ContestRankBO;
import com.lodogame.ldsg.bo.ContestTeamBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.model.ContestFightResult;


/**
 * 擂台赛相关的 action
 * @author liaocheng
 *
 */
public class ContestAction extends LogicRequestAction {
	private static final Logger LOG = Logger.getLogger(EmbattleAction.class);
	
	@Autowired
	private ContestService contestService; 
	
	@Autowired
	private PushHandler pushHandler;
	
	/**
	 * 获取擂台赛进行到哪个阶段
	 * 
	 * 因为用户在点击进入『擂台赛』时，最先调用的就是这个接口，因此在这个接口中首先检查玩家是否达到40级，如果没有，抛出『
	 * 未达到等级』异常
	 * 
	 * @return
	 */
	public Response getStatus() {
		
		int status = contestService.getStatus(getUid());
		
		set("st", status);
		
		return this.render();
	}
	
	/**
	 * 进入报名页面
	 */
	public Response enterReg() {
		
		Map<String, Object> rt = contestService.enterReg();
		
		set("ct", rt.get("ct"));
		set("tl", rt.get("tl"));
		set("se", rt.get("se"));
		
		return this.render();
	}
	
	/**
	 * 玩家注册
	 */
	public Response register() {
		
		String userId = getUid();
		
		contestService.register(userId);
		
		return this.render();
	}
	
	/**
	 * 进入对战、战斗、下注界面
	 */
	public Response enterVersus() {
		Map<String, Object> map = contestService.enterVersus(getUid());
		
		set("rid", map.get("rid"));
		set("st", map.get("st"));
		set("se", map.get("se"));
		set("tl", map.get("tl"));
		
		return this.render();
	}
	
	/**
	 * 下注
	 */
	public Response bet() {
		String userId = getUid();
		
		String betOnUserId = getString("buid", "");
		contestService.bet(userId, betOnUserId);
		this.pushHandler.pushUser(userId);
		return this.render();
	}
	
	/**
	 * 获取上届排名
	 * @return
	 */
	public Response getLastSessionRank() {
		List<ContestRankBO> rankBOList = contestService.getLastSessionRank();
		set("rl", rankBOList);
		return this.render();
	}
	
	/**
	 * 进入领奖界面
	 */
	public Response enterRecReward() {
		Map<String, Object> map = contestService.enterRecReward(getUid());
		
		set("st", map.get("st"));
		set("cd", map.get("cd"));
		set("se", map.get("se"));
		set("cls", map.get("cls"));
		
		return this.render();
	}

	/**
	 * 领奖
	 */
	public Response recReward() {
		int rewardId= getInt("id", 0);
		CommonDropBO commonDropBO = contestService.recReward(getUid(), rewardId);
		set("dr", commonDropBO);
		set("id", rewardId);
		return this.render();
	}
	
	/**
	 * 获取上一场比赛的结果，并且返回下一场比赛的战斗队列。如果上一场比赛是128强，则返回用户所在小组（青龙、白虎等等）的战斗结果，
	 * 如果用户不再其中任何一个小队中，怎随机返回四个小队中的一个。
	 */
	public Response getRoundResult() {
		int round = getInt("round", 0);
		List<ContestTeamBO> teamBOList = contestService.getRoundResult(getUid(), round);
		set("tls", teamBOList);
		return this.render();
	}
	
	public Response getUserInfo() {
		String userId = getString("userId", "");
		Map<String, Object> rt = contestService.getUserInfo(userId);
		set("nk", rt.get("nk"));
		set("pid", rt.get("pid"));
		set("level", rt.get("level"));
		set("hls", rt.get("hls"));
		return this.render();
	}
	
	public Response getReport() {
		String contestId = getString("ctid", "");
		ContestFightResultBO fightResultBO = contestService.getReport(contestId);
		set("bto", fightResultBO);
		return this.render();
	}
}
