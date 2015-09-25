package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.game.server.request.Message;

public class HeroAction extends BaseTestAction {

	/*
	 * public void loadHeros(Socket socket) throws IOException{
	 * BufferedOutputStream out = new
	 * BufferedOutputStream(socket.getOutputStream()); BufferedInputStream in =
	 * new BufferedInputStream(socket.getInputStream()); Message message = new
	 * Message(); message.setAct("Hero.loadHeros"); HashMap<String, Object> map
	 * = new HashMap<String, Object>(); map.put("ft", 0); message.setDt(map);
	 * outMsg(out, message); read(in); }
	 */

	public void upgradePre(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Hero.upgradePre");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uhid", "58cd296df9084f60863d0db1db469146");
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void upgrade(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Hero.upgrade");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uhid", "58cd296df9084f60863d0db1db469146");
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void sell(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Hero.sell");
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<String> uhids = new ArrayList<String>();
		uhids.add("22edb6806aee4be2a18f117f1b98f1ba");
		uhids.add("284d31134a164882b34460b361052129");
		map.put("uhids", uhids);
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void devourPre(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		Message message = new Message();
		message.setAct("Hero.devourPre");
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<String> uhids = new ArrayList<String>();

		uhids.add("22edb6806aee4be2a18f117f1b98f1ba");
		// uhids.add("284d31134a164882b34460b361052129");
		map.put("uhids", uhids);
		map.put("uhid", "284d31134a164882b34460b361052129");
		message.setDt(map);
		outMsg(out, message);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = new Socket("127.0.0.1", 10000);
		UserAction u = new UserAction();
		HeroAction h = new HeroAction();
		u.login(socket);
		h.devourPre(socket);
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		while (true) {
			read(in);
			Thread.sleep(100);
		}
		// h.upgrade(socket);
		// h.sell(socket);
		// for(int i = 0; i < 100; i++){
		// h.loadHeros(socket);
		// }
	}
}
