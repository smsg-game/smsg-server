package com.lodogame.ldsg.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.game.dao.PackageInfoDao;
import com.lodogame.ldsg.partner.service.PartnerService;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.model.PackageInfoBO;
import com.lodogame.model.StaticPackageInfo;

/**
 * 游戏服务器WEB
 * 
 * @author CJ
 * 
 */
@Controller
public class PackageController {

	@Autowired
	private PartnerServiceFactory serviceFactory;
	@Autowired
	private PackageInfoDao packageInfoDao;

	private static final Logger LOG = Logger.getLogger(PackageController.class);

	/**
	 * 比较客户端和服务器中静态文件的版本信息，返回给客户端静态文件增量包的下载地址 如果最新的静态文件包是测试版，则只有在白名单中的用户可以
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/webApi/checkStaticFiles.do")
	public ModelAndView checkStaticFiles(HttpServletRequest req, HttpServletResponse res) throws IOException {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> dt = new HashMap<String, Object>();

		String clSign = req.getParameter("clSign");
		String uid = req.getParameter("fr");

		List<StaticPackageInfo> info = packageInfoDao.getStaticPackages(1);

		if (!info.isEmpty()) {

			StaticPackageInfo packageInfo = info.get(0);

			if (!clSign.equalsIgnoreCase(packageInfo.getSignature())) {
				String ips = PackageConfig.instance().getIps();

				// 查询下载地址
				String[] ipArr = null;
				if (StringUtils.isBlank(ips)) {
					ipArr = new String[1];
					ipArr[0] = "api2.ldsg.lodogame.com";
				} else {
					ipArr = ips.split(",");
				}
				int idx = RandomUtils.nextInt(ipArr.length);
				String url = "http://" + ipArr[idx] + packageInfo.getUrl();

				// 查询用户是否在白名单内
				String whiteList = packageInfo.getWhiteList();
				String[] lists = whiteList.split(",");
				Arrays.sort(lists);
				int index = Arrays.binarySearch(lists, uid);

				if (packageInfo.getIsTest() == 1 && index >= 0) { // 如果是测试版
					dt.put("url", url);
				} else if (packageInfo.getIsTest() == 0) {
					dt.put("url", url);
				}
			}
		}

		Map<String, Object> dtt = new HashMap<String, Object>();
		dtt.put("dt", dt);
		map.put("dt", dtt);
		map.put("rc", 1000);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/webApi/checkVersion.do")
	public ModelAndView checkVersion(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String ip = req.getRemoteAddr();

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> dt = new HashMap<String, Object>();
		String version = req.getParameter("version");
		String partnerId = req.getParameter("partnerId");
		String qn = req.getParameter("qn");

		String fr = req.getParameter("fr");
		String mac = req.getParameter("mac");
		PartnerService ps = serviceFactory.getBean(partnerId);
		PackageInfoBO bo = ps.checkVersion(version, fr, mac, partnerId, ip);

		dt.put("ver", bo.getVersion());

		dt.put("desc", bo.getDescribe());

		dt.put("resUrl", packUrl(bo.getResUrl()));

		if (StringUtils.isEmpty(qn)) {
			qn = "default";
		}

		String apkUrl = "";

		if (StringUtils.isNotBlank(bo.getApkUrl())) {
			apkUrl = bo.getApkUrl().replace("${qn}", qn);
			dt.put("apkUrl", packUrl(apkUrl));
		} else {
			dt.put("apkUrl", "");
		}

		// fix bug
		Map<String, Object> dtt = new HashMap<String, Object>();
		dtt.put("dt", dt);

		map.put("dt", dtt);

		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	private String packUrl(String urlStr) {

		if (urlStr != null && (urlStr.startsWith("http://") || urlStr.startsWith("https://"))) {
			// return urlStr + "?" + UUID.randomUUID().toString();
			return urlStr;
		}

		String ips = PackageConfig.instance().getIps();
		String[] ipArr = null;
		if (StringUtils.isBlank(ips) || StringUtils.isBlank(urlStr)) {
			return "";
		}
		ipArr = ips.split(",");
		int idx = RandomUtils.nextInt(ipArr.length);
		// String url = "http://" + ipArr[idx] + urlStr + "?" +
		// UUID.randomUUID().toString();
		String url = "http://" + ipArr[idx] + urlStr;// + "?" +
														// UUID.randomUUID().toString();
		return url;
	}

	@RequestMapping("/webApi/updatePackageConfig.do")
	public ModelAndView updatePackageConfig() {
		Map<String, Object> map = new HashMap<String, Object>();

		PackageConfig.instance().reload();

		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

}
