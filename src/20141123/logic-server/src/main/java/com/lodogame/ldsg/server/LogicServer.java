package com.lodogame.ldsg.server;

import com.lodogame.game.server.GameServer;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.utils.ProcessUtils;

/**
 * 逻辑服务器
 * 
 * @author jacky
 * 
 */

public class LogicServer {

	public static void main(String[] args) {
		GameServer gserver = new GameServer(args.length > 1 ? args[0] : "");
		if(args.length == 2){
			ProcessUtils.outputPid("/data/run-" + args[1] + "/logic-server.pid");
		}
		gserver.start();
		DefaultSessionManager.getInstance().setSessionCloseHandler(null);
	}

}
