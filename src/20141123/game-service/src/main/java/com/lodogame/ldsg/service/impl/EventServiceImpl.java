package com.lodogame.ldsg.service.impl;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.EventServcie;

public class EventServiceImpl implements EventServcie {

	private static final Logger logger = Logger.getLogger(EventServiceImpl.class);

	private BlockingQueue<Event> eventQueue = new ArrayBlockingQueue<Event>(64);

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Override
	public void dispatchEvent(Event event) {
		eventQueue.add(event);
	}

	/**
	 * 处理事件
	 * 
	 * @param event
	 */
	private void handle(final Event event) {

		Runnable task = new Runnable() {

			@Override
			public void run() {
				Iterator<EventHandle> iterator = EventHandleFactory.getInstance().iterator();
				while (iterator.hasNext()) {
					EventHandle handle = iterator.next();
					handle.handle(event);
				}
			}
		};

		taskExecutor.execute(task);

	}

	@Override
	public void init() {

		new Thread(new Runnable() {

			public void run() {
				while (true) {

					logger.debug("事件处理线程");

					try {
						Event event = eventQueue.take();
						System.out.println(event);
						handle(event);

					} catch (Throwable t) {
						logger.error(t.getMessage(), t);

						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
							logger.error(ie.getMessage(), ie);
						}

					}

				}
			}

		}).start();

	}

}
