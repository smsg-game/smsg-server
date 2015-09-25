package com.lodogame.ldsg.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.ForcesBo;
import com.lodogame.ldsg.bo.SweepInfoBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserSceneBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.UserSweepInfo;

/**
 * 关卡相关action
 * 
 * @author jacky
 * 
 */

public class SceneAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(SceneAction.class);

	@Autowired
	private SceneService sceneService;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private UserService userService;

	@Autowired
	private PushHandler pushHandler;

	private static boolean started = false;

	/**
	 * 读取用户场景
	 */
	public Response loadScenes() {

		LOG.debug("获取用户场景列表.uid[" + getUid() + "]");

		List<UserSceneBO> userSceneList = this.sceneService.getUserSceneList(getUid());

		set("us", userSceneList);

		return this.render();
	}

	/**
	 * 读取用户场景怪物列表
	 */
	public Response loadForces() {

		List<UserForcesBO> rtUserForcesBOList = new ArrayList<UserForcesBO>();
		List<UserForcesBO> userForcesBoList = this.forcesService.getUserForcesList(getUid(), 0);
		for (UserForcesBO ufbo : userForcesBoList) {
			if (ufbo.getTimes() > 0) {
				rtUserForcesBOList.add(ufbo);
			}
		}
		set("fs", rtUserForcesBOList);

		return this.render();
	}

	/**
	 * 突击
	 * @return
	 */
	public Response assault(){
		Integer sid = (Integer) this.request.getParameter("sid");
		sceneService.assault(getUid(), sid, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				set("dr", event.getObject("forcesDropBO"));
				pushHandler.pushUser(getUid());
				return true;
			}
		});
		return this.render();
	}
	/**
	 * 开始扫荡
	 * 
	 * @return
	 */
	public Response sweep() {
		Integer forcesId = (Integer) this.request.getParameter("fid");
		Integer times = (Integer) this.request.getParameter("ts");

		if (times <= 0) {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据异常");
		}

		sceneService.sweep(getUid(), forcesId, times, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				UserSweepInfo sweepInfo = (UserSweepInfo) event.getObject("sweepInfo");
				set("st", sweepInfo.getCreatedTime());
				set("et", sweepInfo.getEndTime());
				set("pw", sweepInfo.getPower());
				set("ts", sweepInfo.getTimes());
				set("fid", sweepInfo.getForcesId());

				pushHandler.pushUser(getUid());
				return true;
			}
		});

		return this.render();
	}

	/**
	 * 停止扫荡
	 * 
	 * @return
	 */
	public Response stopSweep() {
		sceneService.stopSweep(getUid(), new EventHandle() {
			@Override
			public boolean handle(Event event) {
				UserSweepInfo sweepInfo = (UserSweepInfo) event.getObject("sweepInfo");
				Integer ft = (Integer) event.getObject("ft");
				set("ft", ft);
				set("ts", sweepInfo.getTimes());
				set("fid", sweepInfo.getForcesId());
				pushHandler.pushUser(getUid());
				return false;
			}
		});

		return this.render();
	}

	public Response getSweepStatus() {
		SweepInfoBO bo = sceneService.getUserSweepInfo(getUid());
		set("st", bo.getStatus());
		return this.render();
	}

	/**
	 * 立刻完成扫荡
	 * 
	 * @return
	 */
	public Response finishSweep() {
		sceneService.speedUpSweep(getUid());
		pushHandler.pushUser(getUid());
		return this.render();
	}

	/**
	 * 领取扫荡奖励
	 * 
	 * @return
	 */
	public Response receiveSweep() {
		sceneService.receiveSweep(getUid(), new EventHandle() {
			@Override
			public boolean handle(Event event) {
				set("dr", event.getObject("forcesDropBO"));
				set("fid", event.getInt("forcesId"));
				set("ts", event.getInt("times"));
				pushHandler.pushUser(getUid());
				return true;
			}
		});

		return this.render();
	}

	/**
	 * 重置剧情
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response resetForcesTimes() {

		String userId = this.getUid();

		List<Integer> sceneIdList = (List<Integer>) this.getList("sids");

		this.forcesService.resetForcesTimes(userId, sceneIdList);

		this.pushHandler.pushUser(userId);

		return this.render();

	}

	// TODO 需要优化改进
	public void init() {
		synchronized (SceneAction.class) {
			if (started) {
				return;
			}

			if (!Config.ins().isGameServer()) {
				return;
			}

			started = true;

			// TODO 多个逻辑服时处理有问题
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							checkSweepComplete();
							Thread.sleep(1000 * 10);
						} catch (Exception e) {
							LOG.error(e.getMessage(), e);
						}

					}
				}
			}).start();
		}
	}

	/**
	 * 检测扫荡完成的线程， 从数据库中读取状态为0，且当前时间超过endTime的信息，将其状态改为完成（即1），推送至
	 */
	private void checkSweepComplete() {
		String sweepKey = RedisKey.getUserSweepCacheKey();
		Map<String, String> sweepMap = JedisUtils.getMap(sweepKey);
		Collection<String> cl = sweepMap.values();
		for (String sweepInfoStr : cl) {
			UserSweepInfo sweepInfo = Json.toObject(sweepInfoStr, UserSweepInfo.class);
			long now = System.currentTimeMillis();
			long endTime = sweepInfo.getEndTime().getTime();
			// 表示已完成
			if (now >= endTime && sweepInfo.getStatus() == 0) {
				sceneService.updateSweepComplete(sweepInfo.getUserId());
				if (userService.isOnline(sweepInfo.getUserId())) {
					pushHandler.pushSweepStatus(sweepInfo.getUserId(), sweepInfo.getStatus());
				}
			}
		}

	}
	
	/**
	 * 读取FB次数
	 * @return
	 */
	public Response getForcesTimes() {

		Map<String, String> fbm = forcesService.getForcesTimes(3);

		set("fbm", fbm);

		return this.render();

	}
}
