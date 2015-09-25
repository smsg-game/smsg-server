package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lodogame.game.server.request.Message;

public class TavernAction extends BaseTestAction {
	public void draw(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Tavern.draw");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tp", 0);
		map.put("ts", 10);
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = new Socket("127.0.0.1", 10000);
		UserAction u = new UserAction();
		TavernAction t = new TavernAction();
		u.login(socket);
		t.draw(socket);
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		while (true) {
			read(in);
			Thread.sleep(100);
		}
	}
}
