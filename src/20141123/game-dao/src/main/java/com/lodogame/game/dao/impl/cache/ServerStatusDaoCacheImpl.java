package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.game.dao.ServerStatusDao;
import com.lodogame.game.utils.PartnerUtil;
import com.lodogame.model.ServerStatus;

public class ServerStatusDaoCacheImpl implements ServerStatusDao {

	private Map<Integer, ServerStatus> serverStatsuMap = new HashMap<Integer, ServerStatus>();

	private ServerStatusDao serverStatusDaoMysqlImpl;

	public void setServerStatusDaoMysqlImpl(ServerStatusDao serverStatusDaoMysqlImpl) {
		this.serverStatusDaoMysqlImpl = serverStatusDaoMysqlImpl;
	}

	@Override
	public ServerStatus getServerStatus(String partnerId) {
		int id = PartnerUtil.getPartnerIdPre(partnerId);
		ServerStatus serverStatus = serverStatsuMap.get(id);
		if (serverStatus == null) {
			serverStatus = this.serverStatusDaoMysqlImpl.getServerStatus(partnerId);
			if (serverStatus != null) {
				serverStatsuMap.put(id, serverStatus);
			}
		}
		return serverStatus;
	}

	@Override
	public void setServerStatus(int id, int status, String whiteList) {
		this.serverStatusDaoMysqlImpl.setServerStatus(id, status, whiteList);
		serverStatsuMap.clear();
	}

	@Override
	public boolean isWhiteIp(String partnerId, String ip) {
		int id = PartnerUtil.getPartnerIdPre(partnerId);
		ServerStatus serverStatus = serverStatsuMap.get(id);
		if (serverStatus != null) {
			return serverStatus.getWhiteList().indexOf(ip) >= 0;
		}
		return false;
	}

}
