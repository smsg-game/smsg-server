package com.lodogame.ldsg.action;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;

import com.lodogame.ldsg.bo.FactionBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.FactionService;

/**
 * 帮派action
 * 
 * @author zyz
 * 
 */

public class FactionAction extends LogicRequestAction {


	private static final Logger logger = Logger.getLogger(FactionAction.class);
	
	@Autowired
	private FactionService factionService;
	
	@Autowired
	private PushHandler pushHandler;


	/**
	 * 创建帮派
	 * 
	 * @return
	 */
	public Response createFaction() {

		String userId = getUid();

		String factionName = this.getString("fn", null);
		
		logger.debug("创建帮派.uid[" + userId + "]");
		
		this.factionService.createFaction(userId, factionName);
		
		pushHandler.pushUser(userId);

		return this.render();
	}

	/**
	 * 进入帮派
	 * 
	 * @return
	 */
	public Response enterFaction() {

		String userId = getUid();

		logger.debug("进入帮派.uid[" + userId + "]");
		
		Map map =  this.factionService.enter(userId);
		
		set("mls", map.get("mls"));
		set("fi", map.get("fi"));
		set("als", map.get("als"));
		set("fls", map.get("fls"));
		
		return this.render();
	}
	
	/**
	 * 申请加入帮派
	 * 
	 * @return
	 */
	public Response applyForFaction() {

		String userId = getUid();

		int factionId = this.getInt("fid", 0);
		
		logger.debug("申请加入帮派.uid[" + userId + "]");

		this.factionService.applyForFaction(userId, factionId);
		
		return this.render();
	}
	
	/**
	 * 是否批准加入帮派
	 * 
	 * @return
	 */
	public Response approveAddFaction() {

		String userId = getUid();
		
		int pid = this.getInt("pid", 0);

		int flag = this.getInt("flag", 0);
		
		logger.debug("是否批准加入帮派.uid[" + userId + "]");
		
		this.factionService.approveAddFaction(userId, String.valueOf(pid), flag);
		
        set("pid", pid);
        
        set("flag",flag);
		
		return this.render();
	}
	
	/**
	 * 退出帮派
	 * 
	 * @return
	 */
	public Response quitFaction() {

		String userId = getUid();

		logger.debug("退出帮派.uid[" + userId + "]");
		
		this.factionService.quitFaction(userId);

		return this.render();
	}
	
	/**
	 * 开除成员
	 * 
	 * @return
	 */
	public Response kickFaction() {

		String userId = getUid();
		
		int pid = this.getInt("pid", 0);

		logger.debug("开除帮派成员.uid[" + userId + "]");

		this.factionService.kickFaction(userId, String.valueOf(pid));
		
		set("pid", pid);
		
		return this.render();
	}
	
	/**
	 * 保存帮派公告
	 * 
	 * @return
	 */
	public Response saveFactionNotice() {

		String userId = getUid();

		String factionNotice = this.getString("fn", null);
		
		logger.debug("保存帮派公告.uid[" + userId + "]");

		this.factionService.saveFactionNotice(userId, factionNotice);
		
		set("fn", factionNotice);
		
		return this.render();
	}
	
	/**
	 * 获取指定页帮派列表
	 * 
	 * @return
	 */
	public Response getFactionListByPage() {

		String userId = getUid();

		int pageNum = this.getInt("pn", 0);
		
		logger.debug("获取指定页帮派列表.uid[" + userId + "]");
		
		List<FactionBO> fls = this.factionService.getFactionListByPage(userId, pageNum);

		set("fls", fls);
		 
		return this.render();
	}
	
}
