import com.lodogame.game.server.GameServer;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.utils.ProcessUtils;
import com.lodogame.ldsg.Config;
import com.lodogame.ldsg.handler.MainSessionCloseHandler;

public class Start {

//	private static Logger logger = Logger.getLogger(Start.class);
	
	// 此地址作废！
//	private static String url = "http://admin.sg.fantingame.com/web/main_server_status_report";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameServer mainServer = new GameServer(args.length > 1 ? args[0] : "");
		if (args.length == 2) {
			ProcessUtils.outputPid("/data/run-" + args[1] + "/main-server.pid");
		}

		if (args.length == 3) {
			if ("true".equalsIgnoreCase(args[2])) {
				Config.isTest = true;
			}
		}
		mainServer.start();
		DefaultSessionManager.getInstance().setSessionCloseHandler(new MainSessionCloseHandler());
		ServerStatusMonitor montior = new ServerStatusMonitor();
		Thread t = new Thread(montior);
		t.start();

//		final int port = mainServer.getPort();
//
//		new Thread(new Runnable() {
//			public void run() {
//				while (true) {
//					monitor(port);
//				}
//			}
//
//		}).start();

	}
/*
	static private void monitor(int port) {

		try {

			Session session = ServerConnectorMgr.getInstance().getServerSession("logic");

			int status = session != null ? 1 : 0;

			Map<String, String> params = new HashMap<String, String>();
			params.put("port", String.valueOf(port));
			params.put("status", String.valueOf(status));
			String jsonStr = UrlRequestUtils.execute(url, params, UrlRequestUtils.Mode.GET);
			logger.debug("ret:" + jsonStr);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException ie) {
			logger.error(ie.getMessage());
		}

	}*/
}
