package com.lodogame.game.dao.impl.cache;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.lodogame.game.dao.LogPoolDao;
import com.lodogame.model.ILog;

public class LogPoolDaoCacheImpl implements LogPoolDao {

	BlockingQueue<ILog> queue = new LinkedBlockingDeque<ILog>();

	@Override
	public ILog get() throws InterruptedException {
		return queue.take();
	}

	@Override
	public void add(ILog log) {
		this.queue.add(log);
	}

}
