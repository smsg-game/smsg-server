package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.DeifyRoomInfoBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.DeifyService;
import com.lodogame.ldsg.service.HeroService;

/**
 * 修神
 */
public class DeifyAction extends LogicRequestAction {

	@Autowired
	private PushHandler pushHandler;
	
	@Autowired
	private HeroService heroService;
	
	@Autowired
	private DeifyService deifyService;
	
	public Response getTowerList() {
		Map<String, Object> rt = deifyService.getTowerList(getUid());
		set("twls", rt.get("twls"));
		set("cd", rt.get("cd"));
		set("utid", rt.get("utid"));
		return this.render();
	}

	public Response getRoomList() {
		int towerId = getInt("twid", 0);
		List<DeifyRoomInfoBO> roomInfoBOList = deifyService.getRoomList(getUid(), towerId);
		set("rol", roomInfoBOList);
		return this.render();
	}
	
	public Response occupy() {
		final int towerId = getInt("twid", 0);
		final int roomId = getInt("rid", 0);
		
		Map<String, Object> rt = deifyService.occupy(getUid(), towerId, roomId, new EventHandle() {
			
			@Override
			public boolean handle(Event event) {
				if (event instanceof BattleResponseEvent) {
					set("rf", event.getInt("flag"));
					set("rp", event.getString("report"));
					set("tp", 10);
					set("atu", event.getObject("atu"));
					set("defu", event.getObject("defu"));
					set("bgid", event.getObject("bgid")); 
					set("rol", event.getObject("rol"));
					set("twid", towerId);
					set("rid", roomId);
					set("utid", event.getObject("utid"));
					set("cd", event.getObject("cd"));
					set("aod", event.getObject("aod"));
				} 
				
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
		
		if (rt != null) {
			set("rol", rt.get("rol"));
			set("twid", towerId);
			set("rid", roomId);
			set("utid", rt.get("utid"));
			set("cd", rt.get("cd"));
			pushHandler.pushUser(getUid());
			return this.render();
		}
		pushHandler.pushUser(getUid());

		return null;
	}

	/**
	 * 激活化神节点
	 * 
	 * @return
	 */
	public Response activateNode() {
		String userHeroId = this.getString("uhid", null);
		int systemDeifyNodeid = this.getInt("dnl", 0);
		
		String userId = getUid();
		
		int mindNum = this.deifyService.activateNode(userId, userHeroId, systemDeifyNodeid);
		UserHeroBO userHeroBO = this.heroService.getUserHeroBO(userId, userHeroId);
		set("hero", userHeroBO);
		set("nm", mindNum);
		pushHandler.pushUser(getUid());
		return this.render();
	}
	
	
	/**
	 * 开启双倍修炼
	 * @return
	 */
	public Response doubleProfit() {
		int type = getInt("type", 0);
		DeifyRoomInfoBO roomInfoBO = deifyService.doubleProfit(getUid(), type);
		
		set("ro", roomInfoBO);
		pushHandler.pushUser(getUid());
		return this.render();
	}
	
	public Response protect() {
		int type = getInt("type", 0);
		DeifyRoomInfoBO roomInfoBO = deifyService.protect(getUid(), type);
		set("ro", roomInfoBO);
		pushHandler.pushUser(getUid());
		return this.render();
	}
	
	/**
	 * 获取玩家修炼状态
	 */
	public Response getDeifyStatus() {
		Map<String, Object> rt = deifyService.getDeifyStatus(getUid());
		set("st", rt.get("st"));
		set("snum", rt.get("snum"));
		
		return this.render();
	}
	
	public Response checkLevel() {
		int towerId = getInt("twid", 0);
		deifyService.checkLevel(getUid(), towerId);
		set("twid", towerId);
		return this.render();
	}
	
	/**
	 *  用户神将列表
	 * @return
	 */
	public Response getDeifyPeopleList(){
		String userId = getUid();
		List<UserHeroBO> list = deifyService.getDeifyPeopleList(userId);
		set("dpls", list);
		return this.render();
	}
	
	/**
	 * 挑战器灵列表
	 * @return
	 */
	public Response getDeifyForcesList(){
		String userId = getUid();
		int heroId = this.getInt("hid", 0);
		int type = this.getInt("tp", 0);
		
		List<UserForcesBO> list = deifyService.getDeifyForcesList(userId, heroId, type);
		
		set("fs", list);
		set("hid", heroId);
		return this.render();
	}
	
	/**
	 * 注灵战斗
	 * @return
	 */
	public Response deifyBattle(){
		
		final String userId = getUid();
		String userHeroId = this.getString("uhid", "");
		final int forcesId = this.getInt("fid", 0);
		deifyService.deifyBattle(userId, userHeroId, forcesId, new EventHandle() {
			
			@Override
			public boolean handle(Event event) {
				
				String report = event.getString("report");
				int flag = event.getInt("flag");
				if(flag == 1 || flag == 2){
					flag = 1;
					pushHandler.pushUser(userId);
				}
				
				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", 11);
				set("dr", event.getObject("forcesDropBO"));
				set("bgid", event.getInt("bgid"));
				set("fid", forcesId);
				
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
		return null;
	}
	
	public Response buyDeifyTime() {
		int type = getInt("type", 0);
		DeifyRoomInfoBO roomInfoBO = deifyService.buyDeifyTime(getUid(), type);
		
		set("ro", roomInfoBO);
		pushHandler.pushUser(getUid());
		
		return this.render();
	}
	
	/**
	 * 进入锻造界面
	 * @return
	 */
	public Response forgePre(){
		String userId = getUid();
		String userHeroId = this.getString("uhid", "");
		int type = this.getInt("tp", 0);
		Map<String, Object> map = deifyService.forgePre(userId, userHeroId, type);
		set("ina", map.get("ina"));
		set("an", map.get("an"));
		set("han", map.get("han"));
		set("cop", map.get("cop"));
		set("hcop", map.get("hcop"));
		set("did", map.get("did"));
		set("ann", map.get("ann"));
		set("eid", map.get("eid"));
		set("nen", map.get("nen"));
		return this.render();
	}
	
	/**
	 * 锻造
	 * @return
	 */
	public Response forge(){
		
		String userId = getUid();
		String userHeroId = this.getString("uhid", "");
		int type = this.getInt("tp", 0);
		
		Map<String, Object> map = deifyService.forge(userId, userHeroId, type);
		set("ina", map.get("ina"));
		set("an", map.get("an"));
		set("han", map.get("han"));
		set("cop", map.get("cop"));
		set("hcop", map.get("hcop"));
		set("did", map.get("did"));
		set("hbo", map.get("hbo"));
		set("eq", map.get("eq"));
		set("nan", map.get("nan"));
		set("ntid", map.get("ntid"));
		
		pushHandler.pushUser(getUid());
		
		return this.render();
	}
	
	/**
	 * 进入注灵界面
	 * @return
	 */
	public Response animaPre(){
		String userId = getUid();
		String userhEquipId = this.getString("ueid", "");
		int type = this.getInt("tp", 0);
		
		Map<String, Object> map = deifyService.animaPre(userId, userhEquipId, type);
		
		set("da", map.get("da"));
		set("ul", map.get("ul"));
		set("hul", map.get("hul"));
		set("sls", map.get("sls"));
		set("pls", map.get("pls"));
		set("ueid", map.get("ueid"));

		return this.render();
	}
	
	/**
	 * 注灵
	 * @return
	 */
	public Response anima(){
		
		String userId = getUid();
		String userEquipId = this.getString("ueid", "");
		int successToolId = this.getInt("suid", 0);
		int productToolId = this.getInt("pid", 0);

		
		Map<String, Object> map = deifyService.anima(userId, userEquipId, successToolId == 0 ? 0:1, successToolId, productToolId == 0? 0:1, productToolId);
		
		set("da", map.get("da"));
		set("ul", map.get("ul"));
		set("hul", map.get("hul"));
		set("eq", map.get("eq"));
		set("hbo", map.get("hbo"));
		set("sls", map.get("sls"));
		set("pls", map.get("pls"));
		set("ueid", userEquipId);
		set("iss", map.get("iss"));
		
		set("stn", successToolId == 0 ? 0:1);
		set("ptn", productToolId == 0 ? 0:1);
		set("suid", successToolId);
		set("pid", productToolId);
		
		set("ugid", map.get("ugid"));
		set("unm", map.get("unm"));
		
		return this.render();
	}

}
