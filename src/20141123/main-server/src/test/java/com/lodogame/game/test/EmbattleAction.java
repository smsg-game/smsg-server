package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lodogame.game.server.request.Message;

public class EmbattleAction extends BaseTestAction {
	public void change(Socket socket) throws IOException{
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Embattle.change");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uhid", "58cd296df9084f60863d0db1db469146");
		map.put("pos", 1);
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("110.76.45.122", 10000);
		UserAction u = new UserAction();
		EmbattleAction h = new EmbattleAction();
		u.login(socket);
		h.change(socket);
		u.logout(socket);
	}
}
