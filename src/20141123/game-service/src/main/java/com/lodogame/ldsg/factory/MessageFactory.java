package com.lodogame.ldsg.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.bo.Message;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.MessageType;
import com.lodogame.ldsg.helper.MessageHelper;

/**
 * 跑马灯消息工厂类
 * 
 * @author jacky
 * 
 */
public class MessageFactory {

	public static Logger LOG = Logger.getLogger(MessageFactory.class);

	private static Map<Integer, List<Message>> templates = new HashMap<Integer, List<Message>>();

	private static MessageFactory messageFactory;

	private static Properties prop;

	private boolean inited;

	public List<Message> getMessage(int msgType, Map<String, String> params) {

		if (templates.containsKey(msgType)) {
			List<Message> msgList = templates.get(msgType);
			return render(msgList, params);
		}
		return null;
	}

	private List<Message> render(List<Message> msgList, Map<String, String> params) {

		List<Message> ml = new ArrayList<Message>();
		for (Message message : msgList) {

			Message msg = new Message();
			String text = message.getTxt();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (entry.getValue() == null) {
					LOG.error("参数为空.key[" + entry.getKey() + "]");
					text = text.replace("#" + entry.getKey() + "#", "");
				} else {
					text = text.replace("#" + entry.getKey() + "#", entry.getValue());
				}
			}
			msg.setTxt(text);
			msg.setColor(message.getColor());

			ml.add(msg);
		}

		return ml;

	}

	public static MessageFactory ins() {
		synchronized (MessageFactory.class) {
			if (messageFactory == null) {
				messageFactory = new MessageFactory();
			}
		}
		return messageFactory;
	}

	private MessageFactory() {

		try {
			initConfig();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void initConfig() throws IOException {

		if (inited) {
			return;
		}
		inited = true;

		int isHK = Config.ins().getIsHK();
		
		if (isHK ==1) { 
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("message_ZH_HK.properties"));
		} else if (isHK == 0){
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("message_ZH_CN.properties"));

		}

		for (Object key : prop.keySet()) {

			String s = key.toString();

			int messageType = MessageType.getMessageType(s);
			List<Message> messageList = MessageHelper.parse(new String(prop.getProperty(s).getBytes("ISO8859-1"), "utf8"));
			templates.put(messageType, messageList);
		}
	}

	public static void main(String[] args) {

	}
}
