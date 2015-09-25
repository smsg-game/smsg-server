package com.lodogame.ldsg.action;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.constant.CodeConstants;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.Config;
import com.lodogame.ldsg.bo.OfflineUserToken;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.manager.OfflineTokenManager;
import com.lodogame.ldsg.manager.TokenManager;
import com.lodogame.ldsg.manager.UserSessionManager;

/**
 * 用户模块对外接口
 * 
 * @author CJ
 * 
 */
public class UserAction extends ProxyBaseAction {
	private final static Logger LOG = Logger.getLogger(UserAction.class);

	public final static int SUCCESS = 1000;
	/**
	 * 用户未注册角色
	 */
	public final static int LOGIN_NO_REG = 2001;
	/**
	 * 用户未登陆
	 */
	public final static int LOGIN_NOT_LOGIN = 2002;

	private String puid;
	private String pid;
	private int ut;
	private String tk;
	private String sid;

	private String uid;
	private int rc;

	private String un;

	private void initTestToken() {

		// TODO will delete
		Map<String, String> m = new HashMap<String, String>();
		m.put("5349499dea96497db4a2e203ef064b15", "tktktktktk");
		m.put("bc268cfae4ca46509b79ff849478c2fa", "tk00000001");
		m.put("43be66888ff04acca0aaf05a74de3a96", "tk00000002");
		m.put("4d5da244bdf94eca848e77db5b269e13", "tk00000003");
		m.put("2412841fe4854062b08c56a1b3b1e30b", "tk00000004");
		m.put("49ff94dbe9ac4b3f953a1870718cb32b", "tk00000005");
		m.put("b5d3b0daae9d43b3bce7dffe7a758233", "tk00000006");
		m.put("711615315fbd495a84bebb3f6f0d01da", "tk00000007");
		m.put("f9d72437a9d4475aa282c614a7c4644e", "tk00000008");
		m.put("559750ae595e4e27a60052ec64cdfc39", "tk00000009");
		m.put("2c50ee50fe04461eaed9f12e28806aa5", "tk00000010");
		m.put("1085d7bf3cd74d0db3c70d2e7a5052e0", "tk00000011");
		m.put("10da636e2fe646c79b0635e93dfdc656", "tk00000012");
		m.put("78449f8a35dc4f779dc2cd895ae158f3", "tk00000013");
		m.put("ed3973846f24479f96a68a73d3e80cb2", "tk00000014");
		m.put("87bfeee8803547a1b2be534845896678", "tk00000015");
		m.put("91c86ecd3e994b91b9dd2de196923cf7", "tk00000016");
		m.put("1c748c057b45496c886c1a8ac561b086", "tk00000017");
		m.put("5b682917bba04a96a2231bd5d19846e1", "tk00000019");
		m.put("5b682917bba04a96a2231bd5d19846e2", "tk00000020");
		m.put("5b682917bba04a96a2231bd5d19846e3", "tk00000021");
		m.put("5b682917bba04a96a2231bd5d19846e4", "tk00000022");
		m.put("5b682917bba04a96a2231bd5d19846e5", "tk00000023");
		m.put("5b682917bba04a96a2231bd5d19846e6", "tk00000024");
		m.put("5b682917bba04a96a2231bd5d19846e7", "tk00000025");
		m.put("5b682917bba04a96a2231bd5d19846e8", "tk00000026");
		m.put("5b682917bba04a96a2231bd5d19846e9", "tk00000027");
		m.put("5b682917bba04a96a2231bd5d19846ea", "tk00000028");
		m.put("5b682917bba04a96a2231bd5d19846eb", "tk00000029");
		m.put("5b682917bba04a96a2231bd5d19846ec", "tk00000030");
		m.put("5b682917bba04a96a2231bd5d19846ed", "tk00000031");
		m.put("5b682917bba04a96a2231bd5d19846a0", "tk00000032");
		m.put("5b682917bba04a96a2231bd5d19846a1", "tk00000033");
		m.put("5b682917bba04a96a2231bd5d19846a2", "tk00000034");
		m.put("5b682917bba04a96a2231bd5d19846a3", "tk00000035");
		m.put("5b682917bba04a96a2231bd5d19846a4", "tk00000036");
		m.put("5b682917bba04a96a2231bd5d19846a5", "tk00000037");
		m.put("5b682917bba04a96a2231bd5d19846a6", "tk00000038");
		m.put("5b682917bba04a96a2231bd5d19846a7", "tk00000039");
		m.put("5b682917bba04a96a2231bd5d19846a8", "tk00000040");
		m.put("5b682917bba04a96a2231bd5d19846a9", "tk00000041");
		m.put("5b682917bba04a96a2231bd5d19846aa", "tk00000042");
		m.put("5b682917bba04a96a2231bd5d19846b1", "tk00000043");
		m.put("5b682917bba04a96a2231bd5d19846b2", "tk00000044");
		m.put("5b682917bba04a96a2231bd5d19846b3", "tk00000045");
		m.put("5b682917bba04a96a2231bd5d19846b4", "tk00000046");
		m.put("5b682917bba04a96a2231bd5d19846b5", "tk00000047");
		m.put("5b682917bba04a96a2231bd5d19846b6", "tk00000048");
		m.put("5b682917bba04a96a2231bd5d19846b7", "tk00000049");
		m.put("6bf4204a7dc84bed81d60ef652756818", "tk00000050");
		m.put("66aea118d8f44899a513a15afcc66666", "tk00000051");
		m.put("5b682917bba04a96a2231bd5d19846b1", "tk00000051");
		m.put("5b682917bba04a96a2231bd5d19846b2", "tk00000052");
		m.put("5b682917bba04a96a2231bd5d19846b3", "tk00000053");
		m.put("5b682917bba04a96a2231bd5d19846b4", "tk00000054");
		m.put("5b682917bba04a96a2231bd5d19846b5", "tk00000055");
		m.put("5b682917bba04a96a2231bd5d19846b6", "tk00000056");
		m.put("5b682917bba04a96a2231bd5d19846b7", "tk00000057");
		m.put("5b682917bba04a96a2231bd5d19846b8", "tk00000058");
		m.put("5b682917bba04a96a2231bd5d19846b9", "tk00000059");
		m.put("5b682917bba04a96a2231bd5d19846c0", "tk00000060");
		

		// m.put(key, value);
		// m.put(key, value);

		for (Entry<String, String> entry : m.entrySet()) {

			String token = entry.getValue();
			String uid = entry.getKey();

			UserToken ut = new UserToken();
			ut.setToken(token);
			ut.setPartnerId(pid);
			ut.setServerId(sid);
			ut.setUserId(uid);
			TokenManager.getInstance().setToken(ut.getToken(), ut);
		}
	}

	/**
	 * 登陆请求
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response login() throws IOException {

		initTestToken();

		UserToken userToken = checkUserLogin(tk, getUserId(), pid, sid);
		if (userToken != null) {
			// 暂时屏蔽
			kickoutOtherSession(userToken);
			UserSessionManager.getInstance().setUserSession(userToken.getUserId(), request.getSession().getSid());

			Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
			if (session == null) {
				LOG.info("无法获取服务器连接session");
			}
			InetSocketAddress address = (InetSocketAddress) request.getSession().getChannel().getRemoteAddress();
			Message msg = getProxyMessage();
			msg.setAct("User.login");
			// ut暂时无用
			msg.setAttribate("ut", ut);
			msg.setAttribate("tk", tk);
			msg.setAttribate("ip", address.getHostName());
			msg.setAttribate("uid", userToken.getUserId());
			request.getSession().setAttribute("userToken", userToken);
			session.send(msg);
		}
		return null;
	}

	public Response reLogin() throws IOException {

		// initTestToken();

		// tk = "tk00000013";

		UserToken userToken = TokenManager.getInstance().getToken(tk);
		if (userToken == null && tk != null) {
			OfflineUserToken offlineUserToken = OfflineTokenManager.getInstance().getToken(tk);
			// 离线不超过一个小时可以再次登录
			if (offlineUserToken != null && System.currentTimeMillis() - offlineUserToken.getLogoutTime() < 12 * 60 * 60 * 1000) {
				userToken = new UserToken();
				userToken.setUserId(offlineUserToken.getUserId());
				userToken.setPartnerUserId(offlineUserToken.getPartnerUserId());
				userToken.setServerId(offlineUserToken.getServerId());
				userToken.setPartnerId(offlineUserToken.getPartnerId());
				userToken.setToken(offlineUserToken.getToken());
				TokenManager.getInstance().setToken(userToken.getToken(), userToken);

				// 从离线那清掉
				OfflineTokenManager.getInstance().removeToken(tk);
			}
		}

		if (userToken == null) {
			throw new ServiceException(LOGIN_NOT_LOGIN, "userToken为空[" + tk + ":" + getUserId() + ":" + pid + "]");
		}

		// UserToken userToken = checkUserLogin(tk, getUserId(), pid, sid);
		if (userToken != null) {
			// 暂时屏蔽
			kickoutOtherSession(userToken);
			UserSessionManager.getInstance().setUserSession(userToken.getUserId(), request.getSession().getSid());

			Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
			if (session == null) {
				LOG.info("无法获取服务器连接session");
			}
			InetSocketAddress address = (InetSocketAddress) request.getSession().getChannel().getRemoteAddress();
			Message msg = getProxyMessage();
			msg.setAct("User.reLogin");
			msg.setAttribate("ut", ut);
			msg.setAttribate("tk", tk);
			msg.setAttribate("ip", address.getHostName());
			msg.setAttribate("uid", userToken.getUserId());
			request.getSession().setAttribute("userToken", userToken);
			session.send(msg);
		}
		return null;
	}

	/**
	 * 踢掉同一个用户的其它登陆链接
	 * 
	 * @param userToken
	 */
	private void kickoutOtherSession(UserToken userToken) {
		String sessionId = UserSessionManager.getInstance().getUserSessionId(userToken.getUserId());
		Session session = DefaultSessionManager.getInstance().getSession(sessionId);
		if (session != null) {
			Message msg = new Message();
			msg.setAct("error");
			msg.setRc(CodeConstants.KICKOUT);
			try {
				ChannelFuture future = session.send(msg);
				future.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						future.getChannel().close();
					}
				});
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 根据token判断用户是否有登陆
	 * 
	 * @param token
	 *            用户登陆令牌
	 * @param userId
	 *            用户ID
	 * @param partnerId
	 *            合作厂商ID
	 * @param serverId
	 *            服务器ID
	 * @return
	 */
	private UserToken checkUserLogin(String token, String userId, String partnerId, String serverId) {
		if (Config.isTest) {
			UserToken userToken = new UserToken();
			userToken.setPartnerId(partnerId);
			userToken.setUserId(userId);
			userToken.setToken(token);
			userToken.setServerId(serverId);
			userToken.setPartnerId(partnerId);
			return userToken;
		}
		if (StringUtils.isBlank(token) || StringUtils.isBlank(userId) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数不正确[" + token + ":" + userId + ":" + partnerId + "]");
		}

		UserToken userToken = TokenManager.getInstance().getToken(token);
		if (userToken == null) {
			throw new ServiceException(LOGIN_NOT_LOGIN, "userToken为空[" + token + ":" + userId + ":" + partnerId + "]");
		}

		if (!userId.equals(userToken.getUserId()) || !partnerId.equals(userToken.getPartnerId()) || !serverId.equals(userToken.getServerId())) {
			throw new ServiceException(LOGIN_NOT_LOGIN, "userToken与缓存不匹配[" + token + ":" + userId + ":" + partnerId + "]");
		}
		return userToken;
	}

	/**
	 * 逻辑服返回登陆结果
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response loginRq() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 逻辑服返回重新登陆结果
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response reLoginRq() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 购买银币
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response buyCopper() throws IOException {
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		if (session != null) {
			Message msg = getProxyMessage();
			msg.setAct("User.buyCopper");
			msg.setAttribate("uid", userToken.getUserId());
			session.send(msg);
		}
		return null;
	}

	/**
	 * 恢复体力返回
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response buyCopperRq() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 加载用户信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response resumePower() throws IOException {
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		if (session != null) {
			Message msg = getProxyMessage();
			msg.setAct("User.resumePower");
			msg.setAttribate("uid", userToken.getUserId());
			session.send(msg);
		}
		return null;
	}

	/**
	 * 恢复体力返回
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response resumePowerRq() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response pushUser() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response pushUserData() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response pushUserData2() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 用户退出，清除userToken、Session并断开连接
	 * 
	 * @return
	 */
	public Response logout() {
		Session session = request.getSession();
		UserToken userToken = (UserToken) session.getAttribute("userToken");
		// Session logicSession =
		// ServerConnectorMgr.getInstance().getServerSession("logic");
		// if (logicSession != null) {
		// // 发送退出消息到逻辑服
		// Message msg = response.getMessage();
		// msg.setAct("User.logout");
		// msg.setAttribate("uid", userToken.getUserId());
		// try {
		// logicSession.send(msg);
		// } catch (IOException e) {
		// LOG.error(e.getMessage(), e);
		// }
		// }
		// 清楚userToken和session
		TokenManager.getInstance().removeToken(userToken.getToken());
		DefaultSessionManager.getInstance().closeSession(session);
		return null;
	}

	// 创建角色时所选择的初始武将
	private int sh;
	// 创建角色时所填的玩家名称
	private String rn;

	/**
	 * 创建游戏角色
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response create() throws IOException {
		Session session = request.getSession();
		UserToken userToken = (UserToken) session.getAttribute("userToken");
		Session logicSession = ServerConnectorMgr.getInstance().getServerSession("logic");
		if (logicSession != null) {
			// 发送创建角色消息到逻辑服
			InetSocketAddress address = (InetSocketAddress) request.getSession().getChannel().getRemoteAddress();
			Message msg = getProxyMessage();
			msg.setAct("User.create");
			msg.setAttribate("uid", userToken.getUserId());
			msg.setAttribate("sh", sh);
			msg.setAttribate("rn", rn);
			msg.setAttribate("ip", address.getHostName());
			logicSession.send(msg);
		}
		return null;
	}

	/**
	 * 检测掉线状况，给逻辑服调用，如果socket已经断开，则调用logout接口
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response checkOnline() throws IOException {
		String userSessionId = UserSessionManager.getInstance().getUserSessionId(uid);
		Session userSession = DefaultSessionManager.getInstance().getSession(userSessionId);
		if (userSession == null || userSession.getChannel() == null || !userSession.getChannel().isConnected()) {
			Message msg = response.getMessage();
			msg.setAct("User.logout");
			msg.setAttribate("uid", uid);
			response.setMessage(msg);
			return response;
		} else {
			return null;
		}

	}

	/**
	 * 创建角色逻辑服的返回处理
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response createRq() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 加载用户信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response loadInfo() throws IOException {
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		if (session != null) {
			Message msg = getProxyMessage();
			msg.setAct("User.loadInfo");
			msg.setAttribate("uid", userToken.getUserId());
			session.send(msg);
		}
		return null;
	}

	/**
	 * 逻辑服返回加载用户信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response loadInfoRq() throws IOException {
		this.render();
		return null;
	}

	/**
	 * 检测用户昵称
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response checkName() throws IOException {
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		if (session != null) {
			Message msg = getProxyReqMessage();
			msg.setAct("User.checkName");
			msg.setAttribate("uid", userToken.getUserId());
			session.send(msg);
		}
		return null;
	}

	public Response checkNameRq() throws IOException {
		render();
		return null;
	}

	/**
	 * 购买VIP
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response buyVip() throws IOException {
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		Message msg = getProxyMessage();
		msg.setAct("User.buyVip");
		msg.setAttribate("uid", userToken.getUserId());
		msg.setAttribate("lv", request.getParameter("lv"));
		session.send(msg);
		return null;
	}

	public Response buyVipRq() throws IOException {
		render();
		return null;
	}

	/**
	 * 购买背包
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response buyBag() throws IOException {
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		Message msg = getProxyMessage();
		msg.setAct("User.buyBag");
		msg.setAttribate("uid", userToken.getUserId());
		msg.setAttribate("tp", request.getParameter("tp"));
		msg.setAttribate("num", request.getParameter("num"));
		session.send(msg);
		return null;
	}

	public Response buyBagRq() throws IOException {
		render();
		return null;
	}

	public Response guideStep() throws IOException {
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		Message msg = getProxyMessage();
		msg.setAct("User.guideStep");
		msg.setAttribate("uid", userToken.getUserId());
		msg.setAttribate("gs", request.getParameter("gs"));
		session.send(msg);
		return null;
	}

	public Response guideStepRq() throws IOException {
		render();
		return null;
	}

	public String getPuid() {
		return puid;
	}

	public void setPuid(String puid) {
		this.puid = puid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTk() {
		return tk;
	}

	public void setTk(String tk) {
		this.tk = tk;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public int getUt() {
		return ut;
	}

	public void setUt(int ut) {
		this.ut = ut;
	}

	public int getSh() {
		return sh;
	}

	public void setSh(int sh) {
		this.sh = sh;
	}

	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
	}

	public String getUn() {
		return un;
	}

	public void setUn(String un) {
		this.un = un;
	}

}
