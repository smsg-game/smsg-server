package com.lodogame.game.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.jboss.netty.channel.SimpleChannelHandler;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.json.Json;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class NIOClient extends SimpleChannelHandler {
	public final static SerializationContext context = new SerializationContext();
	public final static Amf3Output amf3out = new Amf3Output(context);
	public static int cnt = 1;
	public static int cnt2 = 1;
	public final static int TOTAL = 1000;
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// String commend = "{\"handler\": \"testHandler\"}";
		// System.out.println(new Integer(1000).byteValue());
		long time1 = System.currentTimeMillis();
//		List<Socket> list = new ArrayList<Socket>();
		for(int i = 0; i < TOTAL; i++){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						run2();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
			Thread.sleep(10);
		}
		while(cnt2 < TOTAL){
			Thread.sleep(500);
		}
		long time2 = System.currentTimeMillis();
		System.out.println((time2 - time1) / 1000);
//		Thread.sleep(10000);
	}

	private static void run2() throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 8888);
		socket.setSoTimeout(100000);
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		outMsg(out);
		read2(in);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		outMsg(out);
		readMsg(in, socket, out);
	}

	private static void readMsg(BufferedInputStream in, Socket socket, BufferedOutputStream out)
			throws IOException {
		while(cnt2 < TOTAL){
			read2(in);
		}
	}

	private static void read2(BufferedInputStream in) throws IOException {
		byte[] lenBuf = new byte[4];
		in.read(lenBuf);
		int l = Constant.bytesToInt(lenBuf);
		byte[] buf = new byte[l];
		in.read(buf);
		System.out.println(new String(buf, Charset.forName("utf-8")));
	}

	private static void outMsg(BufferedOutputStream out) throws IOException {
		Message message = new Message();
		message.setAct("test");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("reportId", 1212);
		synchronized (NIOClient.class) {
			map.put("testField", cnt++);
			message.setAttribate("testField", cnt++);
		}
		
//			map.put("sid", UUID.randomUUID().toString());
		byte[] b = Json.toJson(message).getBytes(Charset.forName("utf-8"));
		byte[] b1 = Constant.intToBytes(b.length);
		out.write(b1);
		out.write(b);
		out.flush();
	}

	/**
	 * amf 格式编码
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static byte[] amf3Encode(Object msg) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		amf3out.setOutputStream(stream);
		amf3out.writeObject(msg);
		amf3out.flush();
		byte bytes[] = stream.toByteArray();
		return bytes;
	}
}
