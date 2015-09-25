package com.lodogame.ldsg.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.PartnerApkUrlDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.model.PartnerApkUrl;
import com.lodogame.model.UserMapper;

/**
 * 关卡相关action
 * 
 * @author jacky
 * 
 */

public class SystemAction extends LogicRequestAction {

	private static final Logger logger = Logger.getLogger(SystemAction.class);

	private static final String defalutPartnerID = "1004";

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private PartnerApkUrlDao partnerApkUrlDao;

	@Autowired
	private MessageService messageService;

	public Response getTime() {

		long time = System.currentTimeMillis();

		logger.debug("当前系统时间.uid[" + getUid() + "]time[" + time + "]");

		this.set("st", time);

		return this.render();
	}

	public Response getApkUrl() {

		UserMapper userMapper = this.userMapperDao.get(getUid());

		PartnerApkUrl partnerApkUrl = this.partnerApkUrlDao.getByPartnerId(userMapper.getPartnerId());
		if (partnerApkUrl == null) {
			partnerApkUrl = this.partnerApkUrlDao.getByPartnerId(defalutPartnerID);
		}

		set("url", partnerApkUrl != null ? partnerApkUrl.getUrl() : "");

		return this.render();
	}

	public Response sendMsg() {

		String msg = this.getString("msg", "");
		String userId = this.getUid();

		messageService.sendHornMsg(userId, msg);

		return this.render();
	}
}
