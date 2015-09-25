package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lodogame.game.server.request.Message;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.manager.TokenManager;

public class UserAction extends BaseTestAction {

	public void login(Socket socket) throws IOException {
		createUserToken();
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

		Message message = new Message();
		message.setAct("User.login");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tk", "tk00000015");
		map.put("uid", "87bfeee8803547a1b2be534845896678");
		map.put("puid", "110");
		map.put("pid", "119");
		map.put("sid", "s1");
		map.put("ut", 0);
		message.setDt(map);
		//outMsg(out, message);
	}

	public static void createUserToken() {
		UserToken ut = new UserToken();
		ut.setToken("tktktktktk");
		ut.setPartnerUserId("110");
		ut.setPartnerId("119");
		ut.setServerId("s1");
		ut.setUserId("5349499dea96497db4a2e203ef064b15");
		TokenManager.getInstance().setToken(ut.getToken(), ut);
	}

	public void buyCopper(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("User.buyCopper");
		HashMap<String, Object> map = new HashMap<String, Object>();
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void buyBag(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("User.buyBag");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tp", 2);
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void loadInfo(Socket socket) throws UnknownHostException, IOException {
		createUserToken();
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("User.loadInfo");
		HashMap<String, Object> map = new HashMap<String, Object>();
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void logout(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("User.logout");
		HashMap<String, Object> map = new HashMap<String, Object>();
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void create(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("User.create");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sh", 1);
		map.put("rn", "测试账号测试");
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	public void checkName(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("User.checkName");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("un", "测试账号");
		message.setDt(map);
		outMsg(out, message);
		read(in);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// Socket socket = new Socket("110.76.45.122", 10000);
		Socket socket = new Socket("127.0.0.1", 10000);
		UserAction u = new UserAction();
		u.login(socket);
		u.checkName(socket);
//		u.buyBag(socket);
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		while (true) {
			read(in);
			Thread.sleep(100);
		}
		// u.loadInfo(socket);
		// u.create(socket);
		// u.loadInfo(socket);
		// u.logout(socket);
		// createUserToken();
	}
}
