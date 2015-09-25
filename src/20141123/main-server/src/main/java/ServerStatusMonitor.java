

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.ServerConnectorSessionMgr;
import com.lodogame.game.server.vo.SystemStatus;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;


/**
 * 服务器状态监控
 * @author CJ
 *
 */
public class ServerStatusMonitor implements Runnable{
	
	private final static Logger logger = Logger.getLogger(ServerStatusMonitor.class);
	@Override
	public void run() {
		while(true){
			try {
				SystemStatus systemStatus = new SystemStatus();
				int connCount = DefaultSessionManager.getInstance().getAllSession().size();
				int connectorCount = ServerConnectorSessionMgr.getInstance().getAllSession().size();
				Map<String, List<String>> servers = ServerConnectorMgr.getInstance().getServerStruts();
				systemStatus.setConnCount(connCount);
				systemStatus.setServerConnCount(connectorCount);
				systemStatus.setServerStruts(Json.toJson(servers));
				JedisUtils.setString(RedisKey.getSystemStatusKey(), Json.toJson(systemStatus));
				Thread.sleep(10 * 1000);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
}
