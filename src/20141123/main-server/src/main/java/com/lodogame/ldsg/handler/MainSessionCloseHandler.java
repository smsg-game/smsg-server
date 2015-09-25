package com.lodogame.ldsg.handler;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.session.Session;
import com.lodogame.game.server.session.SessionCloseHandler;
import com.lodogame.ldsg.bo.OfflineUserToken;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.manager.OfflineTokenManager;
import com.lodogame.ldsg.manager.TokenManager;

public class MainSessionCloseHandler implements SessionCloseHandler {

	private final static Logger logger = Logger.getLogger(MainSessionCloseHandler.class);

	@Override
	public void handler(Session s) {
		if (s != null) {
			UserToken ut = (UserToken) s.getAttribute("userToken");
			if (ut != null && !StringUtils.isBlank(ut.getToken())) {

				TokenManager.getInstance().removeToken(ut.getToken());

				// 把离线用户token保存起来
				OfflineUserToken offlineUserToken = new OfflineUserToken();
				offlineUserToken.setLogoutTime(System.currentTimeMillis());
				offlineUserToken.setPartnerId(ut.getPartnerId());
				offlineUserToken.setPartnerUserId(ut.getPartnerUserId());
				offlineUserToken.setToken(ut.getToken());
				offlineUserToken.setUserId(ut.getUserId());
				offlineUserToken.setServerId(ut.getServerId());

				OfflineTokenManager.getInstance().setToken(offlineUserToken.getToken(), offlineUserToken);

				Session logicSession = ServerConnectorMgr.getInstance().getServerSession("logic");
				if (logicSession != null) {
					// 发送退出消息到逻辑服
					Message msg = new Message();
					msg.setAct("User.logout");
					msg.setAttribate("uid", ut.getUserId());
					if (logicSession != null) {
						try {
							logicSession.send(msg);
						} catch (IOException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			}
		}
	}

}
