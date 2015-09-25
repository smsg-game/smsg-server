package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lodogame.game.server.request.Message;

public class BattleAction extends BaseTestAction {
	public void fight(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Battle.fight");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tp", 1);
		map.put("pa", "101");
		map.put("bc", 10);
		message.setDt(map);
		outMsg(out, message);
		read(in);
		read(in);
		read(in);
		read(in);
		read(in);
		read(in);
		read(in);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("192.168.1.154", 10000);
		UserAction u = new UserAction();
		BattleAction h = new BattleAction();
		u.login(socket);
		h.fight(socket);

		// u.logout(socket);
	}
}
