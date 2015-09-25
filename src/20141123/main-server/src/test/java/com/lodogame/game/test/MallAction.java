package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lodogame.game.server.request.Message;

public class MallAction extends BaseTestAction {

	public void getMallList(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Mall.getMallList");
		HashMap<String, Object> map = new HashMap<String, Object>();
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
		MallAction t = new MallAction();

		u.login(socket);
		t.getMallList(socket);

		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		while (true) {
			read(in);
			Thread.sleep(100);
		}

	}
}
