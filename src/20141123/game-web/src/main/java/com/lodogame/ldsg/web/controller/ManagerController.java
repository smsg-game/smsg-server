package com.lodogame.ldsg.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.controller.PackageConfig;
import com.lodogame.game.dao.PackageInfoDao;
import com.lodogame.game.utils.MD5;
import com.lodogame.game.utils.SignatureUtils;
import com.lodogame.game.utils.ZipUtils;
import com.lodogame.ldsg.sdk.AdminApiSdk;
import com.lodogame.ldsg.service.WebApiService;
import com.lodogame.model.GameServer;
import com.lodogame.model.IncrStaticPackageInfo;
import com.lodogame.model.Notice;
import com.lodogame.model.Partner;
import com.lodogame.model.StaticFileInfo;
import com.lodogame.model.StaticPackageInfo;

@Controller
public class ManagerController {

	private final static Logger LOG = Logger.getLogger(ManagerController.class);

	@Autowired
	private WebApiService webApiService;
	@Autowired
	private PackageInfoDao packageInfoDao;

	private final static String PACKAGE_URL_PREFIX = "http://";
	
	@RequestMapping("/manage/index.do")
	public ModelAndView index() {
		return new ModelAndView("/manage/index");
	}

	@RequestMapping("/manage/login.do")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, String username, String password) throws IOException {
		if (webApiService.managerLogin(username, password)) {
			request.getSession().setAttribute("login", true);
			// return new ModelAndView("/manage/version");
			response.sendRedirect("/manage/version.do");
			return null;
		} else {
			return new ModelAndView("/manage/index");
		}
	}
	
	@RequestMapping("/manage/notice.do")
	public ModelAndView notice(HttpServletRequest req) {
		
		List<GameServer> serverList = loadServers();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String serverId = req.getParameter("serverId");
		String partnerId = req.getParameter("partnerId");
		
		Notice notice = webApiService.getNotice(serverId,partnerId);
		
		if(notice == null){
			notice = webApiService.getNotice(serverId, "all");
			if(notice == null){
				notice = new Notice();
				notice.setServerId(serverId);
				notice.setPartnerId(partnerId);
			}
		}
		map.put("notice", notice);
		map.put("servers", serverList);
		map.put("partners", AdminApiSdk.getInstance().loadPartners());
		return new ModelAndView("/manage/notice", map);
	}

	private List<GameServer> loadServers() {
		List<Partner> partners = AdminApiSdk.getInstance().loadPartners();
		List<GameServer> serverList = new ArrayList<GameServer>();
		for(Partner partner : partners){
			List<GameServer> gss = AdminApiSdk.getInstance().loadServers(partner.getpNum());
			for(GameServer gs : gss){
				if(!containsServer(serverList, gs)){
					serverList.add(gs);
				}
			}
		}
		return serverList;
	}
	
	@RequestMapping("/manage/updateNotice.do")
	public ModelAndView updateNotice(HttpServletRequest req){
		String serverId = req.getParameter("serverId");
		String partnerId = req.getParameter("partnerId");
		int isEnable = Integer.parseInt(req.getParameter("isEnable"));
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		Notice notice = webApiService.updateNotice(serverId, isEnable, title, content, partnerId);
		List<GameServer> serverList = loadServers();
		Map<String, Object> map = new HashMap<String, Object>();
		if(notice != null){
			map.put("notice", notice);
		}
		map.put("servers", serverList);
		map.put("partners", AdminApiSdk.getInstance().loadPartners());
		return new ModelAndView("/manage/notice", map);
	}
	
	/**
	 * 判断服务器列表是否包含某一个服务器
	 * @param serverList
	 * @param gs
	 * @return
	 */
	private boolean containsServer(List<GameServer> serverList, GameServer gs){
		for(GameServer s : serverList){
			if(isSameServer(s, gs)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断服务器是否相同，只以sid判断
	 * @param s1
	 * @param s2
	 * @return
	 */
	private boolean isSameServer(GameServer s1, GameServer s2){
		if(s1 != null && s2 != null){
			return s1.getServerId().equals(s2.getServerId());
		}
		return false;
	}
	@RequestMapping("/manage/version.do")
	public ModelAndView version() {
		List<Partner> partners = AdminApiSdk.getInstance().loadPartners();
		Map<String, List<Partner>> map = new HashMap<String, List<Partner>>();
		map.put("partners", partners);
		return new ModelAndView("/manage/version", map);
	}

	@RequestMapping("/manage/uploadPackage.do")
	public ModelAndView uploadPackage(HttpServletRequest request, HttpServletResponse response, String version, String versions, String frs, Integer isTest, String describe, Integer pkgType, String partnerId,
			@RequestParam MultipartFile fullPackage, @RequestParam MultipartFile upgradePackage) throws IOException {
		LOG.debug("当前版本：" + version + "兼容版本：" +  versions);
		
		String packagePath = PackageConfig.instance().getPackagePath();
		String packageUrl = PackageConfig.instance().getPackageUrl();
		

		String verDirName = version + partnerId;
		if (pkgType == 0) {
			verDirName = "apk" + version;
		}

		File verDir = new File(packagePath, verDirName);
		FileUtils.forceMkdir(verDir);
		String fullUrl = createPackage(packagePath, packageUrl, verDirName, fullPackage);
		String upgradeUrl = createPackage(packagePath, packageUrl, verDirName, upgradePackage);
		webApiService.addVersion(version, versions, frs, isTest, describe, pkgType, partnerId, fullUrl, upgradeUrl);
		response.sendRedirect("/manage/version.do");
		return null;
	}
	
	@RequestMapping("/manage/staticPackage.do")
	public ModelAndView staticPackage() {
		 return new ModelAndView("/manage/uploadStaticPackage");
	}
	
	/**
	 * 上传静态文件
	 * @throws Exception 
	 */
	@RequestMapping("/manage/uploadStaticPackage.do")
	public ModelAndView uploadStaticPackage(HttpServletRequest request, HttpServletResponse response, 
				String version, String whiteList, Integer isTest, String describe, @RequestParam MultipartFile staticPackage) throws Exception {
		// 文件上传到服务器的目录的绝对路径
		String packagePath = PackageConfig.instance().getPackagePath();
		String packageUrl = PackageConfig.instance().getPackageUrl();

		// 上传文件，并得到文件的下载路径，例如: /package/data/test.zip，最后在客户端请求时随机加上下载的ip地址
		String url = createPackage(packagePath, packageUrl, version, staticPackage);

		// 解压压缩包，并生成该文件夹的 MD5 签名
		String extrPath = packagePath + packageUrl + "/" + version; // 解压路径
		String zipFilePath = extrPath + "/" + staticPackage.getOriginalFilename(); // 压缩包路径
		String dir = ZipUtils.unzip(zipFilePath, extrPath);
		String signature = SignatureUtils.computeSign(dir);
		
		// 将文件夹的信息写入数据库
		StaticPackageInfo packageInfo = new StaticPackageInfo();
		packageInfo.setIsTest(isTest);
		packageInfo.setUrl(url);
		packageInfo.setSignature(signature);
		packageInfo.setVersion(version);
		packageInfo.setWhiteList(whiteList);
		packageInfo.setDescribe(describe);

		packageInfoDao.addStaticPackageInfo(packageInfo);
		return new ModelAndView("/manage/staticPackageUploadSucceed");
	}

	private String createPackage(String packagePath, String packageUrl, String verDirName, MultipartFile file) throws IOException {
		String dest = packagePath + packageUrl + "/" + verDirName;
		
		String url = "";
		if (!file.isEmpty()) {
			LOG.debug("文件长度: " + file.getSize());
			LOG.debug("文件类型: " + file.getContentType());
			LOG.debug("文件名称: " + file.getName());
			LOG.debug("文件原名: " + file.getOriginalFilename());
			LOG.debug("========================================");
			InputStream uploadIn = null;
			OutputStream fileOut = null;
			try {
				uploadIn = file.getInputStream();
				FileUtils.forceMkdir(new File(dest));
				fileOut = new FileOutputStream(new File(dest, file.getOriginalFilename()));
				IOUtils.copy(uploadIn, fileOut);
				//文件的下载目录有两部分构成：1）根目录（即paclage.url的值），2）文件名称
				url = packageUrl + "/" + verDirName + "/" + file.getOriginalFilename();
			} catch (Exception e) {
				LOG.error(e);
			} finally {
				IOUtils.closeQuietly(uploadIn);
				IOUtils.closeQuietly(fileOut);
			}
		}
		return url;
	}

	public WebApiService getWebApiService() {
		return webApiService;
	}

	public void setWebApiService(WebApiService webApiService) {
		this.webApiService = webApiService;
	}
}
