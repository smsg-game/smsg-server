package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.json.Json;

public class BaseTestAction {
	
	protected void outMsg(BufferedOutputStream out, Message message) throws IOException {
		byte[] b = Json.toJson(message).getBytes(Charset.forName("utf-8"));
		byte[] b1 = Constant.intToBytes(b.length);
		System.out.println(b.length);
		out.write(b1);
		out.write(b);
		out.flush();
	}
	
	protected static void read(BufferedInputStream in) throws IOException {
		byte[] lenBuf = new byte[4];
		in.read(lenBuf);
		int l = Constant.bytesToInt(lenBuf);
		System.out.println(l);
		byte[] buf = new byte[l];
		in.read(buf);
		System.out.println(new String(buf, Charset.forName("utf-8")));
		
	}
}
