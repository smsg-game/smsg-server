package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.Color;
import com.lodogame.ldsg.bo.Message;

public class MessageHelper {

	public static Pattern pattern = Pattern.compile("<color:([^>]*)>([^<]*)</color>");

	public static List<Message> parse(String s) {

		List<Message> list = new ArrayList<Message>();

		Matcher matcher = pattern.matcher(s);

		while (matcher.find()) {

			Message message = new Message();
			message.setTxt(matcher.group(2));
			String[] ss = matcher.group(1).split(",");
			int r = Integer.parseInt(ss[0]);
			int g = Integer.parseInt(ss[1]);
			int b = Integer.parseInt(ss[2]);
			Color color = new Color(r, g, b);
			message.setColor(color);

			list.add(message);
		}

		System.out.println(Json.toJson(list));

		return list;
	}

	public static void main(String[] args) {
		System.out.println(Json.toJson(parse("<color:255,255,255>这是红色</color>")));
	}
}
