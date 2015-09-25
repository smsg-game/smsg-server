package com.lodogame.ldsg.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.game.utils.json.Json;


@Controller
public class WebApiUserController {

	private static final Logger LOG = Logger.getLogger(WebApiUserController.class);

	private final static String PARTNER_ID = "10000";  
	private final static String HTTP_URL_HEAD = "http://";
//	private final static String HOST = "ldsg.lodogame.com:8088";
	private final static String HOST = "wapi.andr.sg.xworldgame.com:8080";
	
	private final static String[] firstNameArr = { "袁", "上官", "轩辕", "蒋", "邓", "龙",
			"刘", "公坚", "钟", "公孙", " ", "第五", "南荣", "端木", "左丘", "赫连", "段", "吴",
			"方", "贺", "宋", "贾", "陈", "苏", "李", "金", "西门", "鲜于", "卢", "汤", "唐",
			"韩", "周", "武", "马", "拓跋", "雷", "司徒", "任", "慕容", "呼延", "汪", "欧阳",
			"万", "沈", "程", "熊", "子车", "东里", "龚", "蔡", "独孤", "南门", "朱", "侯",
			"黄", "何", "余", "付", "姚", "江", "董", "长孙", "秦", "彭", "东方", "史", "赵",
			"叶", "皇甫", "东宫", "梁", "覃", "尉迟", "王", "仲孙", "太史", "孙", "崔", "林",
			"高", "薛", "张", "谢", "莫", "仲长", "冯", "邱", "田", "邵", "邹", "百里", "戴",
			"漆雕", "陆", "达奚", "郑", "孔", "潘", "毛", "郝", "杜", "胡", "司空", "乐正",
			"杨", "闫", "韦", "郭", "巫马", "石", "陶", "曹", "子书", "钱", "令狐", "顾", "丁",
			"范", "司马", "南宫", "夏", "于", "向", "徐", "吕", "肖", "子桑", "严", "谭",
			"诸葛", "尹", "许", "东郭", "夏侯", "魏", "黎", "东皇", "罗", "廖", "钟离", "姜",
			"孟", "宇文", "白", "曾" };
	
	private final static String[] secondNameArr = { "怀", "璃", "尘", "宝", "贞", " ",
			"梦", "纯", "冉", "临", "芷", "然", "方", "永", "宽", "勇", "珍", "济", "科",
			"苗", "凡", "知", "士", "语", "雯", "韵", "晴", "绿", "蕾", "愁", "熙", "列",
			"悠", "弥", "莲", "岸", "允", "寂", "雄", "鹏", "寒", "何", "孝", "景", "问",
			"更", "奕", "宇", "渊", "斓", "朔", "宗", "军", "业", "芝", "攸", "壁", "雅",
			"蓉", "秋", "苍", "柏", "滕", "诗", "峙", "民", "以", "彤", "桦", "徽", "兰",
			"滨", "智", "龄", "吉", "霏", "昕", "颖", "毅", "初", "伟", "春", "群", "悦",
			"伯", "冰", "劲", "真", "妙", "爽", "精", "胆", "汉", "才", "双", "擎", "槐",
			"柔", "南", "衡", "雪", "瓮", "捷", "建", "紫", "言", "成", "皓", "尔", "化",
			"龙", "丘", "怜", "文", "碧", "莱", "娰", "涵", "琴", "弈", "边", "常", "菁",
			"荆", "蕊", "行", "发", "策", "凝", "菡", "代", "僧", "武", "恬", "菱", "松",
			"布", "如", "历", "程", "笑", "易", "醒", "炜", "冲", "念", "晓", "静", "默",
			"惜", "江", "连", "轩", "雨", "修", "平", "莫", "争", "月", "明", "弘", "宛",
			"夜", "丞", "贤", "木", "箫", "琪", "礼", "心", "采", "友", "子", "恒", "公",
			"英", "石", "曹", "寻", "烽", "圃", "备", "逍", "岚", "思", "君", "生", "辨",
			"唯", "亮", "傲", "丹", "权", "荷", "义", "凌", "敏", "从", "剑", "约", "姜",
			"回", "牡", "佐", "卫", "恭", "台", "长", "漂", "醉", "昌", "京", "斑", "香",
			"沛", "枝", "安", "露", "娴", "康", "逸", "羽", "昼", "寅", "秉", "半", "苏",
			"青", "敖", "恨", "瑰", "峻", "幼", "腾", "琛", "朝", "贡", "天", "储", "含",
			"柳", "桃", "奉", "烈", "珊", "桓", "晗", "曜", "迢", "淦", "豫", "道", "诺",
			"曼", "承", "攀", "妃", "昂", "林", "涛", "刚", "霜", "玟", "素", "航", "亭",
			"环", "决", "甲", "德", "弼", "访", "水", "铁", "萍", "旋", "菏", "莹", "志",
			"若", "孤", "全", "彬", "可", "寿", "正", "震", "不", "炎", "向", "澜", "娥",
			"甫", "萱", "新", "画", "丽", "元", "均", "风", "叔", "远", "飞", "无", "玉",
			"峦", "亦", "盼", "山", "关", "仲", "婵", "白", "翼", "卿", "征", "璇", "厉",
			"皋", "研", "悟", "映", "钧", "太", "听", "丰", "育", "伶", "容", "乐", "靖",
			"黛", "楚", "瑜", "烟", "任", "祺", "和", "休", "贝", "萧", "原", "谷", "忆",
			"卉", "之", "慕", "裔", "黎", "影", "薇", "庭", "云", "世", "通", "传", "茨",
			"枫", "釜", "雁", "畏", "荣", "麦", "齐", "蓝", "镜", "巧", "书", "铭", "敬",
			"并", "竹", "泽", "懿", "庆", "名", "夏", "昑", "则", "望", "欣", "度", "颜",
			"泉", "落", "儿", "千", "又", "惊", "翠", "季", "遥", "尧", "翰", "灵", "海",
			"穹", "彻" };

	
	@RequestMapping("/webApi/create.do")
	public ModelAndView create(HttpServletRequest req, HttpServletResponse res) {
		
		
		String partnerUserId = req.getParameter("partnerUserId");
		String serverId = req.getParameter("serverId");
		String username = getUserName();
		
		Map<String, String> paraMap = new HashMap<String, String>();
		
	
//		String url = HTTP_URL_HEAD + HOST + "/gameApi/create.do";
		String url = HTTP_URL_HEAD + serverId + "." + HOST + "/gameApi/create.do";
//		String url = "http://119.29.6.198:8088/gameApi/gameApi/create.do";
		
		
		paraMap.put("partnerId", PARTNER_ID);
		paraMap.put("partnerUserId", partnerUserId);
		paraMap.put("username", username);
		paraMap.put("serverId", serverId);
		
		LOG.info("请求地址：" + url);
		LOG.info("请求参数：" + paraMap.toString());
		
		String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.POST);
		
		Map<String, Object> ret = Json.toObject(jsonStr, Map.class);
		
		int rc = (Integer) ret.get("rc");
		if (rc == 3000) {
			LOG.info("rc = " + rc + " 用户创建失败,usermapper表没记录");
		} else if (rc == 2003) {
			LOG.info("rc = " + rc + " 用户已经存在");
		} else if (rc == 1000) {
			LOG.info("rc = " + rc + " 用户创建成功");
		}
		
		Map<String, Object> rt = new HashMap<String, Object>();
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		
		if (ret == null || rc != 1000) {
			ret.put("rc", "2001");
			ret.put("msg", "失败");
		}
		
		view.setAttributesMap(ret);
		modelView.setView(view);
		return modelView;
	}
	
	@RequestMapping("/webApi/testLogin.do")
	public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
		
		String partnerUserId = req.getParameter("partnerUserId");
		String serverId = req.getParameter("serverId");
		
		Map<String, String> paraMap = new HashMap<String, String>();
		
//		String url = HTTP_URL_HEAD + HOST + "/gameApi/login.do";
		String url = HTTP_URL_HEAD + serverId + "." + HOST + "/gameApi/login.do";
//		String url = "http://localhost:8088/gameApi/gameApi/login.do";
				
		paraMap.put("partnerUserId", partnerUserId);
		paraMap.put("serverId", serverId);
		
		LOG.info("请求地址：" + url);
		LOG.info("请求参数：" + paraMap.toString());
		
		String jsonStr = UrlRequestUtils.execute(url, paraMap, UrlRequestUtils.Mode.POST);
		
		Map<String, Object> ret = Json.toObject(jsonStr, Map.class);
		
		int rc = (Integer) ret.get("rc");
		
		if (rc == 2002) {
			LOG.info("用户已经被封号 partnerUserId[" + partnerUserId + "]");
			ret.put("rc", 2001);
			ret.put("msg", "登录失败");
		} else if (rc != 1000) {
			ret.put("rc", 2001);
			ret.put("msg", "登录失败");
		}
		
		Map<String, Object> rt = new HashMap<String, Object>();
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
			
		view.setAttributesMap(ret);
		modelView.setView(view);
		return modelView;
		
	}
	
	private String getUserName() {
		int fs = firstNameArr.length;
		int ss = secondNameArr.length;
		
		Random r = new Random();
		int firstNameIndex = r.nextInt(fs);
		int secondNameIndex = r.nextInt(ss);
		
		
		String firstName = firstNameArr[firstNameIndex];
		String secondName = secondNameArr[secondNameIndex];
		String name = firstName+secondName;
		
		return name;
	}
}
