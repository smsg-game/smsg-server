package com.lodogame.ldsg.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.NoticeDao;
import com.lodogame.game.dao.PackageInfoDao;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.service.WebApiService;
import com.lodogame.model.Notice;
import com.lodogame.model.PackageInfo;
import com.lodogame.model.StaticPackageInfo;

public class WebApiServiceImpl implements WebApiService {

	public static Logger LOG = Logger.getLogger(WebApiServiceImpl.class);

	@Autowired
	protected PackageInfoDao packageInfoDao;
	
	@Autowired
	protected NoticeDao noticeDao;
	
//	private String createUserToken(UserMapper userMapper) {
//		UserToken ut = new UserToken();
//		ut.setToken(EncryptUtil.getMD5(userMapper.getUserId() + System.currentTimeMillis() + UUID.randomUUID()));
//		ut.setPartnerUserId(userMapper.getPartnerUserId());
//		ut.setPartnerId(userMapper.getPartnerId());
//		ut.setServerId(userMapper.getServerId());
//		ut.setUserId(userMapper.getUserId());
//		TokenManager.getInstance().setToken(ut.getToken(), ut);
//		return ut.getToken();
//	}

	// TODO 临时实现，暂时不需太复杂的逻辑
	public boolean managerLogin(String username, String password) {
		if (Config.ins().getAccount().equals(username) && Config.ins().getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addVersion(String version, String versions, String frs, int isTest, String describe, int pkgType, String partnerId, String fullUrl, String upgradeUrl) {
		PackageInfo info = new PackageInfo();
		info.setVersion(version);
		info.setVersions(versions);
		info.setPkgType(pkgType);
		info.setPartnerId(partnerId);
		info.setFrs(frs);
		info.setIsTest(isTest);
		info.setDescription(describe);
		info.setFullUrl(fullUrl);
		info.setUpgradeUrl(upgradeUrl);
		return packageInfoDao.add(info);
	}

	@Override
	public Notice getNotice(String serverId,String partnerId) {
		return noticeDao.getNotice(serverId,partnerId);
	}

	@Override
	public Notice updateNotice(String serverId, int isEnable, String title, String content,String partnerId) {
		Notice notice = new Notice();
		notice.setServerId(serverId);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setIsEnable(isEnable);
		notice.setPartnerId(partnerId);
		noticeDao.updateNotice(notice);
		return notice;
	}

	
}
