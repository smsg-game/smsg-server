package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.lodogame.game.server.request.Message;

public class SceneAction extends BaseTestAction {
	public void loadForces(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Scene.loadForces");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sid", 1);
		message.setDt(map);
		outMsg(out, message);
	}
	
	public void loadScenes(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Scene.loadScenes");
		HashMap<String, Object> map = new HashMap<String, Object>();
		message.setDt(map);
		outMsg(out, message);
	}
	
	public void sweep(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Scene.sweep");
		message.setAttribate("fid", 101);
		message.setAttribate("ts", 2);
		outMsg(out, message);
	}
	
	public void receiveSweep(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Scene.receiveSweep");
		outMsg(out, message);
	}
	
	public void stopSweep(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Scene.stopSweep");
		outMsg(out, message);
	}
	
	public void finishSweep(Socket socket) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		Message message = new Message();
		message.setAct("Scene.finishSweep");
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
		SceneAction s = new SceneAction();
		u.login(socket);
		s.sweep(socket);
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		while (true) {
			read(in);
			Thread.sleep(100);
		}
	}
}
