package com.lodogame.ldsg.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.game.dao.LogPoolDao;
import com.lodogame.model.ILog;

public class LogOperatorHandlerImpl implements LogOperatorHandler {

	private static final Logger logger = Logger.getLogger(LogOperatorHandlerImpl.class);

	@Autowired
	private ThreadPoolTaskExecutor logOperatorExecutor;

	@Autowired
	private LogPoolDao logPoolDao;

	@Override
	public void handle(ILog log) {
		LogHandle handle = LogHandlerFactory.getInstance().getHandler(log.getClass());
		if (handle != null) {
			handle.handle(log);
		}
	}

	public void init() {
		new Thread(new WorkThread()).start();
	}

	class WorkThread implements Runnable {
		@Override
		public void run() {

			boolean go = true;

			while (go) {

				try {

					final ILog log = logPoolDao.get();
					if (log != null) {
						Runnable runnable = new Runnable() {
							@Override
							public void run() {
								handle(log);
							}
						};
						logOperatorExecutor.execute(runnable);
					}

				} catch (InterruptedException ie) {
					go = false;
				} catch (Throwable t) {
					logger.error(t.getMessage(), t);
					try {
						Thread.sleep(1000 * 1);
					} catch (InterruptedException iie) {
						logger.error(iie);
					}

				}

			}

		}
	}
}
