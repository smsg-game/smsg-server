package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.FactionDao;
import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.dao.UserApplyFactionDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserFactionDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.utils.Constant;
import com.lodogame.game.utils.IllegalWordUtills;
import com.lodogame.ldsg.bo.FactionBO;
import com.lodogame.ldsg.bo.FactionMemberBO;
import com.lodogame.ldsg.bo.PkInfoBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.FactionConstants;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.FactionService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.Faction;
import com.lodogame.model.User;
import com.lodogame.model.UserApplyFaction;
import com.lodogame.model.UserFaction;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserTool;

public class FactionServiceImpl implements FactionService {

	private static final Logger logger = Logger.getLogger(FactionServiceImpl.class);

	@Autowired
	private FactionDao factionDao;

	@Autowired
	private UserService userService;

	@Autowired
	private UserFactionDao userFactionDao;

	@Autowired
	private UserApplyFactionDao userApplyFactionDao;

	@Autowired
	private PkService pkService;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private SystemMailDao systemMailDao;
	
	@Autowired
	private CommandDao commandDao;

	@Autowired
	private ToolService toolService;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public void createFaction(String userId, String factionName) {

		// 检验用户是否可以创建帮派
		checkUserIsRight(userId);

		// 检验帮派名字是否合法
		checkFactionNameIsRight(factionName);
		
		if(!toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.TOOL_ID_CRATED_FACTION_ORDER, 1, ToolUseType.REDUCE_CRATED_FACTION)){
			String message = "建帮令不足，创建帮派失败.userId[" + userId + "]";
			logger.info(message);
			throw new ServiceException(CREATE_FACTION_ORDER_NOT_ENOUGHT, message);
		}
		
		// 扣除银币
		if (this.userService.reduceCopper(userId, FactionConstants.CREATE_CONSUME, ToolUseType.REDUCE_CREATE_FACTION)) {
			Faction faction = new Faction();
			faction.setFactionMaster(userId);
			faction.setFactionName(factionName);
			faction.setMemberLimit(FactionConstants.MEMBER_LIMIT);
			faction.setMemberNum(1);
			faction.setCreatedTime(new Date());
			this.factionDao.createFaction(faction);
		} else {
			toolService.addTool(userId, ToolType.MATERIAL, ToolId.TOOL_ID_CRATED_FACTION_ORDER, 1, ToolUseType.ADD_COPPER_NOT_ENOUGHT);
			String message = "银币不足，创建帮派失败.userId[" + userId + "]";
			logger.info(message);
			throw new ServiceException(CREATE_FACTION_COPPER_NOT_ENOUGH, message);
		}

		// 删除申请记录
		this.userApplyFactionDao.delUserApplyFaction(userId);

		Faction faction = this.factionDao.getFactionByName(factionName);

		this.userService.updateUserFactionId(userId, faction.getFactionId());

		// 加入到帮派成员列表
		UserFaction userFaction = new UserFaction();
		userFaction.setFactionId(faction.getFactionId());
		userFaction.setUserId(userId);
		userFaction.setCreatedTime(new Date());
		this.userFactionDao.addUserFaction(userFaction);

	}

	private void checkUserIsRight(String userId) {

		User user = this.userService.get(userId);
		if (user.getLevel() < FactionConstants.ENTER_LEVEL) {
			String message = "用户等级不足，创建帮派失败.userId[" + userId + "]";
			logger.info(message);
			throw new ServiceException(CREATE_FACTION_LEVEL_NOT_ENOUGH, message);
		}

		if (user.getFactionId() > 0) {
			String message = "用户等级不足，用户已有帮派.userId[" + userId + "]";
			logger.info(message);
			throw new ServiceException(CREATE_FACTION_IS_IN_FACTION, message);
		}

	}

	private void checkFactionNameIsRight(String factionName) {

		if (this.factionDao.getFactionByName(factionName) != null) {
			throw new ServiceException(CREATE_FACTION_NAME_IS_REPETITION, "帮派的名字已存在.factionName[" + factionName + "]");
		}
		if (factionName.length() == 0) {
			throw new ServiceException(CREATE_FACTION_NAME_IS_NULL, "帮派的名字为空.factionName[" + factionName + "]");
		} else if (Constant.getBytes(factionName) > 16) {
			throw new ServiceException(CREATE_FACTION_NAME_IS_TOO_LONG, "帮派的名字长度非法.factionName[" + factionName + "]");
		} else if (!IllegalWordUtills.checkWords(factionName)) {
			throw new ServiceException(CREATE_FACTION_NAME_IS_ILLEGAL, "帮派的名字包含非法文字.factionName[" + factionName + "]");
		}

	}

	@Override
	public Map<String, Object> enter(String userId) {
		User user = this.userService.get(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		int factionId = user.getFactionId();

		// 帮派列表
		List<Faction> factionList = this.factionDao.getFactionByPage(0, FactionConstants.SEARCH_NUM);
		map.put("fls", createrFactionBOList(factionList));

		if (factionId == 0) {
			return map;
		}

		// 帮派成员对象
		List<UserFaction> factionMemberList = this.userFactionDao.getFactionMemberByFid(factionId);
		map.put("mls", createUserFactionBOList(factionMemberList));

		// 帮派对象
		Faction faction = this.factionDao.getFactionByFid(user.getFactionId());
		map.put("fi", createrFactionBO(faction));

		// 申请帮派的对象
		List<UserApplyFaction> userApplyFactionList = this.userApplyFactionDao.getApplyFactionByFid(factionId);
		map.put("als", createUserApplyFactionBOList(userApplyFactionList));

		return map;
	}

	public List<FactionBO> createrFactionBOList(List<Faction> factionList) {
		List<FactionBO> factionBOList = new ArrayList<FactionBO>();
		for (Faction faction : factionList) {
			factionBOList.add(createrFactionBO(faction));
		}
		return factionBOList;

	}

	public FactionBO createrFactionBO(Faction faction) {
		FactionBO factionBO = new FactionBO();
		User user = this.userService.get(faction.getFactionMaster());

		factionBO.setFactionId(faction.getFactionId());
		factionBO.setFactionMaster(user.getUsername());
		factionBO.setFactionName(faction.getFactionName());
		factionBO.setMemberLimit(faction.getMemberLimit());
		factionBO.setMemberNum(faction.getMemberNum());
		factionBO.setFactionMasterId(user.getLodoId());
		factionBO.setFactionNotice(faction.getFactionNotice());
		factionBO.setFactionIdToStr(factionIdToStr(faction.getFactionId()));

		return factionBO;

	}

	public String factionIdToStr(int factionId) {
		String factionIdStr = String.valueOf(factionId);
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < 8 - factionIdStr.length(); j++) {
			sb.append("0");
		}
		sb.append(factionIdStr);
		return sb.toString();
	}

	public List<FactionMemberBO> createUserApplyFactionBOList(List<UserApplyFaction> factionMemberList) {
		List<FactionMemberBO> userFactionList = new ArrayList<FactionMemberBO>();
		for (UserApplyFaction userApplyFaction : factionMemberList) {
			String userId = userApplyFaction.getUserId();
			User user = this.userService.get(userId);
			PkInfoBO pkInfoBO = this.pkService.getUserPkInfo(userId);
			if (user == null) {
				continue;
			}
			FactionMemberBO fmb = new FactionMemberBO();
			fmb.setLevel(user.getLevel());
			fmb.setPlayerId(user.getLodoId());
			fmb.setRank(pkInfoBO == null ? 0 : pkInfoBO.getRank());
			fmb.setUserName(user.getUsername());
			fmb.setVipLevel(user.getVipLevel());
			userFactionList.add(fmb);
		}

		return userFactionList;
	}

	public List<FactionMemberBO> createUserFactionBOList(List<UserFaction> factionMemberList) {
		List<FactionMemberBO> userFactionList = new ArrayList<FactionMemberBO>();
		for (UserFaction UserFaction : factionMemberList) {
			String userId = UserFaction.getUserId();
			User user = this.userService.get(userId);
			PkInfoBO pkInfoBO = this.pkService.getUserPkInfo(userId);
			if (user == null) {
				continue;
			}
			FactionMemberBO fmb = new FactionMemberBO();
			fmb.setLevel(user.getLevel());
			fmb.setPlayerId(user.getLodoId());
			fmb.setRank(pkInfoBO == null ? 0 : pkInfoBO.getRank());
			fmb.setUserName(user.getUsername());
			fmb.setVipLevel(user.getVipLevel());
			userFactionList.add(fmb);
		}

		return userFactionList;
	}

	@Override
	public void applyForFaction(String userId, int factionId) {
		User user = this.userService.get(userId);

		if (user.getLevel() < FactionConstants.ENTER_LEVEL) {
			String message = "申请加入帮派失败，用户等级不足.userId[" + userId + "],factionId:[" + factionId + "]";
			logger.info(message);
			throw new ServiceException(APPLY_FACTION_LEVEL_NOT_ENOUGH, message);
		}

		if (user.getFactionId() > 0) {
			String message = "申请加入帮派失败，用户已加入帮派.userId[" + userId + "],factionId:[" + factionId + "]";
			logger.info(message);
			throw new ServiceException(APPLY_FACTION_IS_IN_FACTION, message);
		}
		
		List<UserApplyFaction> userApplyFactionList = this.userApplyFactionDao.getApplyFactionByFid(factionId);

		for (UserApplyFaction userApplyFaction : userApplyFactionList) {
			if (userApplyFaction.getUserId().equalsIgnoreCase(userId)) {
				throw new ServiceException(APPLY_FACTION_IS_IN_APPLY_LIST, "申请加入帮派失败,用户已在申请列表");
			}
		}

		UserApplyFaction userApplyFaction = new UserApplyFaction();
		userApplyFaction.setFactionId(factionId);
		userApplyFaction.setUserId(userId);
		userApplyFaction.setCreatedTime(new Date());
		
		this.userApplyFactionDao.addUserApplyFaction(userApplyFaction);
		Faction faction = this.factionDao.getFactionByFid(factionId);
		pushFactionApplyTips(faction.getFactionMaster());
	}

	@Override
	public void approveAddFaction(String userId, String pid, int flag) {
		User user = this.userService.get(userId);

		// 检查用户是否有权限
		checkUserHasRight(user);

		User userApproved = this.userService.getByPlayerId(pid);

		String userApprovedId = userApproved.getUserId();

		// 批准加入
		if (flag == 1) {

			// 检查申请者是否满足条件
			chenckApplyIsRight(pid);

			// 删除用户的所有申请记录
			this.userApplyFactionDao.delUserApplyFaction(userApprovedId);

			Faction faction = this.factionDao.getFactionByFid(user.getFactionId());
			int factionId = faction.getFactionId();
			UserFaction userFaction = new UserFaction();
			userFaction.setFactionId(factionId);
			userFaction.setUserId(userApprovedId);
			userFaction.setCreatedTime(new Date());
			this.userFactionDao.addUserFaction(userFaction);

			// 更新用户的帮派信息
			this.userService.updateUserFactionId(userApprovedId, factionId);

			// 更新帮派成员数目
			this.factionDao.updateFactionMemberNum(factionId, 1);
		} else {
			// 删除申请记录
			this.userApplyFactionDao.delUserApplyFaction(userApprovedId, user.getFactionId());
		}

	}

	private void chenckApplyIsRight(String pid) {

		User user = this.userService.getByPlayerId(pid);

		if (user.getLevel() < FactionConstants.ENTER_LEVEL) {

			// 删除申请记录
			this.userApplyFactionDao.delUserApplyFaction(user.getUserId());

			String message = "批准申请加入失败，申请者等级不足.userId[" + user.getUserId() + "]";
			logger.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (user.getFactionId() > 0) {

			// 删除申请记录
			this.userApplyFactionDao.delUserApplyFaction(user.getUserId());

			String message = "批准申请加入失败，申请者已加入了帮派.userId[" + user.getUserId() + "]";
			logger.info(message);
			throw new ServiceException(APPROVE_FACTION_IS_IN_FACTION, message);
		}

	}

	private void checkUserHasRight(User user) {

		Faction faction = this.factionDao.getFactionByFid(user.getFactionId());
		if (faction == null) {
			String message = "批准申请加入失败，获取帮派失败.userId[" + user.getUserId() + "]";
			logger.info(message);
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}

		if (!faction.getFactionMaster().equalsIgnoreCase(user.getUserId())) {
			String message = "批准申请加入失败，用户不是帮主.userId[" + user.getUserId() + "]";
			logger.info(message);
			throw new ServiceException(APPROVE_FACTION_USER_NOT_MASTER, message);
		}

		if (faction.getMemberNum() >= faction.getMemberLimit()) {
			String message = "批准申请加入失败，帮派已满员.userId[" + user.getUserId() + "]";
			logger.info(message);
			throw new ServiceException(APPROVE_FACTION_MEMBER_IS_LIMIT, message);
		}

	}

	@Override
	public void quitFaction(String userId) {
		User user = this.userService.get(userId);

		int fid = user.getFactionId();

		if (fid > 0) {
			Faction faction = this.factionDao.getFactionByFid(fid);
			if (faction.getFactionMaster().equalsIgnoreCase(userId)) {
				List<UserFaction> list = this.userFactionDao.getFactionMemberByFid(fid);
				factionDao.deleteFaction(faction.getFactionId(), faction.getFactionName());
				
				userFactionDao.delAllUserFactionByFactionId(fid);
				
				userDao.updateAllFactionByFactionId(fid, list);
				
				if(!list.isEmpty()){
					for(UserFaction userFaction : list){
						pushDissolveFaction(userFaction.getUserId());
					}
				}
				return;
			}
		}

		// 删除用户帮派信息
		this.userFactionDao.delUserFaction(userId, fid);

		// 更新用户信息
		this.userService.updateUserFactionId(userId, 0);

		// 更新帮派成员数目
		this.factionDao.updateFactionMemberNum(fid, -1);

	}

	@Override
	public void kickFaction(String userId, String pid) {

		User user = this.userService.get(userId);

		User userKicked = this.userService.getByPlayerId(pid);

		String userIdKicked = userKicked.getUserId();

		int fid = user.getFactionId();
		if (fid > 0) {
			Faction faction = this.factionDao.getFactionByFid(fid);
			if (!faction.getFactionMaster().equalsIgnoreCase(userId)) {
				String message = "开除帮派成员失败，用户不是帮主.userId[" + user.getUserId() + "]";
				logger.info(message);
				throw new ServiceException(KICK_FACTION_USER_IS_NOT_MASTER, message);
			}

			if (faction.getFactionMaster().equalsIgnoreCase(userIdKicked)) {
				String message = "开除帮派成员失败，帮主不能被开除.userId[" + user.getUserId() + "]";
				logger.info(message);
				throw new ServiceException(KICK_FACTION_KICKED_IS_MASTER, message);
			}
		}

		// 删除用户帮派信息
		this.userFactionDao.delUserFaction(userIdKicked, fid);

		// 更新用户信息
		this.userService.updateUserFactionId(userIdKicked, 0);

		// 更新帮派成员数目
		this.factionDao.updateFactionMemberNum(fid, -1);

		// 发送开除邮件
		sendKickMail(userIdKicked);

	}

	private void sendKickMail(String userId) {
		User user = this.userService.get(userId);
		UserMapper userMapper = this.userMapperDao.get(userId);

		mailService.send("开除邮件", "由于某种原因，帮主已把您开除了，请节哀！！！", null, MailTarget.USERS, user.getLodoId() + "", null, new Date(), userMapper.getPartnerId());

	}

	@Override
	public void saveFactionNotice(String userId, String factionNotice) {

		User user = this.userService.get(userId);

		int factionId = user.getFactionId();
		if (factionId > 0) {
			Faction faction = this.factionDao.getFactionByFid(factionId);
			if (!faction.getFactionMaster().equalsIgnoreCase(userId)) {
				String message = "保存帮派公告失败，用户不是帮主.userId[" + user.getUserId() + "]";
				logger.info(message);
				throw new ServiceException(KICK_FACTION_USER_IS_NOT_MASTER, message);
			}

		}

		checkFactionNoticeIsRight(factionNotice);

		// 保存帮派公告
		this.factionDao.saveFactionNotice(factionId, factionNotice);

	}

	private void checkFactionNoticeIsRight(String factionNotice) {

		if (factionNotice.length() == 0) {
			throw new ServiceException(SAVE_FACTION_NOTICE_IS_NULL, "帮派的公告为空.factionName[" + factionNotice + "]");
		} else if (Constant.getBytes(factionNotice) > 160) {
			throw new ServiceException(SAVE_FACTION_NOTICE_IS_TOO_LONG, "帮派公告长度非法.factionName[" + factionNotice + "]");
		} else if (!IllegalWordUtills.checkWords(factionNotice)) {
			throw new ServiceException(SAVE_FACTION_NOTICE_IS_ILLEGA, "帮派公告包含非法文字.factionName[" + factionNotice + "]");
		}

	}

	@Override
	public List<FactionBO> getFactionListByPage(String userId, int pageNum) {
		List<Faction> factionList = this.factionDao.getFactionByPage(pageNum * FactionConstants.SEARCH_NUM, (pageNum + 1) * FactionConstants.SEARCH_NUM);
		return createrFactionBOList(factionList);
	}

	public void pushFactionApplyTips(String userId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_FACTION_APPLY_TIPS);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}
	
	/**
	 * 推送解散帮派
	 * @param userId
	 */
	public void pushDissolveFaction(String userId){
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		Command command = new Command();
		command.setCommand(CommandType.COMMAND_DISSOLVE_FACTION);
		command.setType(CommandType.PUSH_USER);
		command.setPriority(Priority.NORMAL);
		command.setParams(params);

		commandDao.add(command);
	}
}
