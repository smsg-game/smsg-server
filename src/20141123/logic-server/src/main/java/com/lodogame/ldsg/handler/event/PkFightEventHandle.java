package com.lodogame.ldsg.handler.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.PkGroupAwardLogDao;
import com.lodogame.game.dao.UserPkInfoDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PkFightEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.helper.PkHelper;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.PkGroupAwardLog;
import com.lodogame.model.User;
import com.lodogame.model.UserPkInfo;

public class PkFightEventHandle implements EventHandle {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private PkService pkService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserPkInfoDao userPkInfoDao;
	
	@Autowired
	private PkGroupAwardLogDao pkGroupAwardLogDao;
	
	@Override
	public boolean handle(Event event) {
		if(!(event instanceof PkFightEvent)){
			return true;
		}
		String userId = event.getUserId();
		User user = userService.get(userId);
		String attack = event.getString("attack");
		String defense = event.getString("defense");
		int rank = event.getInt("rank");
		int attackLevel = event.getInt("attackLevel");
		int defenseLevel = event.getInt("defenseLevel");
		String defenseUserId = event.getString("defenseUserId");
		UserPkInfo userPkInfo = userPkInfoDao.getByUserId(userId);
		
		if(rank == 1){
			messageService.sendPkTotalFirst(userId, attack, defense);
		}
		
		if(PkHelper.getGroup(attackLevel) == PkHelper.getGroup(defenseLevel) && pkService.isGroupFirst(defenseUserId)){
			messageService.sendPkGroupFirst(userId, attack, defense, PkHelper.getGroupTitle(PkHelper.getGroup(attackLevel)));
		}
		
		if(userPkInfo.getTimes()>0){
			PkGroupAwardLog pkGroupAwardLog = pkGroupAwardLogDao.get(userId);
			if(userPkInfo.getTimes()>=20||userPkInfo.getTimes()>=30){
				messageService.sendPkGoWin(pkGroupAwardLog.getTitle(),attack);
			}else if(userPkInfo.getTimes()>=50){
				messageService.sendPkGoWinF(pkGroupAwardLog.getTitle(), user.getUsername(), userPkInfo.getTimes());
			}
		}
		return true;
	}
	
	public void init() {
		EventHandleFactory.getInstance().register(this.getClass().getSimpleName(), this);
	}
}
