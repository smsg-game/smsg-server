package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lodogame.game.server.request.Message;

public class EquipmentAction extends BaseTestAction{
	
	public void loadEquipments(Socket socket) throws IOException{
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Equipment.loadEquipments");
		HashMap<String, Object> map = new HashMap<String, Object>();
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}
	
	public void dress(Socket socket) throws IOException{
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Equipment.dress");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uhid", "58cd296df9084f60863d0db1db469146");
		map.put("dp", 1);
		map.put("ueid", "68852bfb64324f47a134395da6204347");
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
		Socket socket = new Socket("192.168.1.10", 10000);
		UserAction u = new UserAction();
		EquipmentAction e = new EquipmentAction();
		u.login(socket);
		e.loadEquipments(socket);
//		e.dress(socket);
//		u.logout(socket);
	}
}
