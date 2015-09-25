package com.ldsg.battle.handle;

import java.util.HashMap;
import java.util.Map;

import com.ldsg.battle.exception.BattleRuntimeException;

public class BuffHandleFactory {

	// <summary>
	// 增加行动
	// </summary>
	public static String FUNC_ID_ADD_ACTION = "ADD_ACTION";

	// <summary>
	// 增加反击率
	// </summary>
	public static String FUNC_ID_ADD_COUNTER_RATE = "ADD_COUNTER_RATE";

	// <summary>
	// 增加爆击值
	// </summary>
	public static String FUNC_ID_ADD_CRIT = "ADD_CRIT";

	// <summary>
	// 增加闪躲
	// </summary>
	public static String FUNC_ID_ADD_DODGE = "ADD_DODGE";

	// <summary>
	// 增加命中
	// </summary>
	public static String FUNC_ID_ADD_HIT = "ADD_HIT";

	// <summary>
	// 增加生命
	// </summary>
	public static String FUNC_ID_ADD_LIFE = "ADD_LIFE";

	// <summary>
	// 增加最大生命值
	// </summary>
	public static String FUNC_ID_ADD_MAX_LIFE = "ADD_MAX_LIFE";

	// <summary>
	// 增加士气
	// </summary>
	public static String FUNC_ID_ADD_MORALE = "ADD_MORALE";

	// <summary>
	// 增加格挡
	// </summary>
	public static String FUNC_ID_ADD_PARRY = "ADD_PARRY";

	// <summary>
	// 加物攻
	// </summary>
	public static String FUNC_ID_ADD_PHYSICS_ATTACK = "ADD_PHYSICS_ATTACK";

	// <summary>
	// 加物防
	// </summary>
	public static String FUNC_ID_ADD_PHYSICS_DEFENSE = "ADD_PHYSICS_DEFENSE";

	// <summary>
	// 增加速度
	// </summary>
	public static String FUNC_ID_ADD_SPEED = "ADD_SPEED";

	// <summary>
	// 增加策攻
	// </summary>
	public static String FUNC_ID_ADD_STRATEGY_ATTACK = "ADD_STRATEGY_ATTACK";

	// <summary>
	// 增加策防
	// </summary>
	public static String FUNC_ID_ADD_STRATEGY_DEFENSE = "ADD_STRATEGY_DEFENSE";

	// <summary>
	// 恢复生命
	// </summary>
	public static String FUNC_ID_BACK_TO_LIFE = "BACK_TO_LIFE";

	// <summary>
	// 混乱
	// </summary>
	public static String FUNC_ID_CHAOS = "CHAOS";

	// <summary>
	// 清除减益buff
	// </summary>
	public static String FUNC_ID_CLEAR_DEC_BUFF = "CLEAR_DEC_BUFF";

	// <summary>
	// 清除增益buff
	// </summary>
	public static String FUNC_ID_CLEAR_INC_BUFF = "CLEAR_INC_BUFF";

	// <summary>
	// 免疫debuff
	// </summary>
	public static String FUNC_ID_IMMUNE_DEBUFF = "IMMUNE_DEBUFF";

	// <summary>
	// 减少爆击值
	// </summary>
	public static String FUNC_ID_REDUCE_CRIT = "REDUCE_CRIT";

	// <summary>
	// 减少挡格
	// </summary>
	public static String FUNC_ID_REDUCE_DODGE = "REDUCE_DODGE";

	// <summary>
	// 减少命中
	// </summary>
	public static String FUNC_ID_REDUCE_HIT = "REDUCE_HIT";

	// <summary>
	// 减少伤害
	// </summary>
	public static String FUNC_ID_REDUCE_HURT = "REDUCE_HURT";

	// <summary>
	// 生命减少
	// </summary>
	public static String FUNC_ID_REDUCE_LIFE = "REDUCE_LIFE";

	// <summary>
	// 减少最大生命值
	// </summary>
	public static String FUNC_ID_REDUCE_MAX_LIFE = "REDUCE_MAX_LIFE";

	// <summary>
	// 减少士气
	// </summary>
	public static String FUNC_ID_REDUCE_MORALE = "REDUCE_MORALE";

	// <summary>
	// 减少格挡
	// </summary>
	public static String FUNC_ID_REDUCE_PARRY = "REDUCE_PARRY";

	// <summary>
	// 减物攻
	// </summary>
	public static String FUNC_ID_REDUCE_PHYSICS_ATTACK = "REDUCE_PHYSICS_ATTACK";

	// <summary>
	// 减物防
	// </summary>
	public static String FUNC_ID_REDUCE_PHYSICS_DEFENSE = "REDUCE_PHYSICS_DEFENSE";

	// <summary>
	// 减少速度
	// </summary>
	public static String FUNC_ID_REDUCE_SPEED = "REDUCE_SPEED";

	// <summary>
	// 减少策攻
	// </summary>
	public static String FUNC_ID_REDUCE_STRATEGY_ATTACK = "REDUCE_STRATEGY_ATTACK";

	// <summary>
	// 减少策防
	// </summary>
	public static String FUNC_ID_REDUCE_STRATEGY_DEFENSE = "REDUCE_STRATEGY_DEFENSE";

	// <summary>
	// 让己方其它武将分担伤害
	// </summary>
	public static String FUNC_ID_SHARE_HURT = "SHARE_HURT";

	// <summary>
	// 眩晕(跳过一轮)
	// </summary>
	public static String FUNC_ID_SKIP_ACTION = "SKIP_ACTION";

	// <summary>
	// 打人不产生士气
	// </summary>
	public static String FUNC_ID_NOT_MORALE = "NOT_MORALE";

	// <summary>
	// 无视对方的防守
	// </summary>
	public static String FUNC_ID_IGNORE_DEFENSE = "IGNORE_DEFENSE";

	// <summary>
	// 不能增加士气
	// </summary>
	public static String FUNC_ID_CAN_NOT_ADD_MORALE = "UNABLE_ADD_MORALE";

	/**
	 * 明悟(降低被附加负面buf的机率)
	 */
	public final static String FUNC_ID_MING_WU = "MING_WU";

	/**
	 * 狂徒(目标生命每减少10%，伤害提高5%)
	 */
	public final static String FUNC_ID_KUANG_TU = "KUANG_TU";

	/**
	 * 致命(生命每减少10%，攻击提升5%)
	 */
	public final static String FUNC_ID_ZHI_MING = "ZHI_MING";

	/**
	 * 致命一击
	 */
	public final static String FUNC_ID_ZHI_MING_YI_JI = "ZHI_MING_YI_JI";

	/**
	 * 压制
	 */
	public final static String FUNC_ID_SUPPRESS = "SUPPRESS";

	/**
	 * 众矢之的
	 */
	public final static String FUNC_ID_ZHONG_SHI_ZHI_DI = "ZHONG_SHI_ZHI_DI";

	/**
	 * 增加隐匿
	 */
	public final static String FUNC_ID_ADD_YIN_NI = "ADD_YIN_NI";

	/**
	 * 增加破击
	 */
	public final static String FUNC_ID_ADD_PO_JI = "ADD_PO_JI";

	/**
	 * 增加韧性
	 */
	public final static String FUNC_ID_ADD_REN_XING = "ADD_REN_XING";

	/**
	 * 治疗加成
	 */
	public final static String FUNC_ID_UP_ADD_LIFE = "UP_ADD_LIFE";

	// <summary>
	// buff处理器dict
	// </summary>
	public static Map<String, IBuffHandle> HANDLER_DICT = null;

	// <summary>
	// 根据funID获取Buff处理类
	// </summary>
	// <param name="funID"></param>
	// <returns></returns>
	public static IBuffHandle getBuffHandler(String funID) {
		IBuffHandle handle;

		synchronized (BuffHandleFactory.class) {
			if (HANDLER_DICT == null) {

				HANDLER_DICT = new HashMap<String, IBuffHandle>();

				// 减少生命(攻击)
				HANDLER_DICT.put(FUNC_ID_REDUCE_LIFE, new HandleProxy(new ReduceLifeHandle()));
				HANDLER_DICT.put(FUNC_ID_BACK_TO_LIFE, new HandleProxy(new BackToLifeHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_MAX_LIFE, new HandleProxy(new AddMaxLifeHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_LIFE, new HandleProxy(new AddLifeHandle()));

				// 眩晕
				HANDLER_DICT.put(FUNC_ID_SKIP_ACTION, new HandleProxy(new SkipActionHandle()));
				// 分担伤害
				HANDLER_DICT.put(FUNC_ID_SHARE_HURT, new HandleProxy(new ShareHurtHandle()));

				// 改属性
				HANDLER_DICT.put(FUNC_ID_ADD_CRIT, new HandleProxy(new AddCritHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_CRIT, new HandleProxy(new ReduceCritHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_HIT, new HandleProxy(new AddHitHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_HIT, new HandleProxy(new ReduceHitHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_PARRY, new HandleProxy(new AddParryHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_PARRY, new HandleProxy(new ReduceParryHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_MORALE, new HandleProxy(new AddMoraleHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_MORALE, new HandleProxy(new ReduceMoraleHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_SPEED, new HandleProxy(new AddSpeedHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_SPEED, new HandleProxy(new ReduceSpeedHandle()));
				HANDLER_DICT.put(FUNC_ID_CLEAR_DEC_BUFF, new HandleProxy(new ClearDecBuffHandle()));
				HANDLER_DICT.put(FUNC_ID_CLEAR_INC_BUFF, new HandleProxy(new ClearIncBuffHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_DODGE, new HandleProxy(new AddDodgeHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_DODGE, new HandleProxy(new ReduceDodgeHandle()));

				// 攻防属性处理器
				HANDLER_DICT.put(FUNC_ID_ADD_PHYSICS_ATTACK, new HandleProxy(new AddPhysicsAttackHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_PHYSICS_ATTACK, new HandleProxy(new ReducePhysicsAttackHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_PHYSICS_DEFENSE, new HandleProxy(new AddPhysicsDefenseHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_PHYSICS_DEFENSE, new HandleProxy(new ReducePhysicsDefenseHandle()));

				HANDLER_DICT.put(FUNC_ID_ADD_STRATEGY_ATTACK, new HandleProxy(new AddStrategyAttackHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_STRATEGY_ATTACK, new HandleProxy(new ReduceStrategyAttackHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_STRATEGY_DEFENSE, new HandleProxy(new AddStrategyDefenseHandle()));
				HANDLER_DICT.put(FUNC_ID_REDUCE_STRATEGY_DEFENSE, new HandleProxy(new ReduceStrategyDefenseHandle()));

				// 免疫deubff处理器
				HANDLER_DICT.put(FUNC_ID_IMMUNE_DEBUFF, new HandleProxy(new ImmuneDebuffHandle()));

				HANDLER_DICT.put(FUNC_ID_REDUCE_HURT, new HandleProxy(new ReduceHurtHandle()));
				HANDLER_DICT.put(FUNC_ID_CHAOS, new HandleProxy(new ChaosHandle()));
				HANDLER_DICT.put(FUNC_ID_ADD_COUNTER_RATE, new HandleProxy(new AddCounterRateHandle()));

				// 增加一次行动
				HANDLER_DICT.put(FUNC_ID_ADD_ACTION, new HandleProxy(new AddActionHandle()));

				HANDLER_DICT.put(FUNC_ID_NOT_MORALE, new HandleProxy(new NotMoraleHandle()));

				// 无视对方防守
				HANDLER_DICT.put(FUNC_ID_IGNORE_DEFENSE, new HandleProxy(new IgnoreDefenseHandle()));

				// 不能增加士气
				HANDLER_DICT.put(FUNC_ID_CAN_NOT_ADD_MORALE, new HandleProxy(new CanNotAddMoraleHandle()));

				// 明悟
				HANDLER_DICT.put(FUNC_ID_MING_WU, new HandleProxy(new AddMingWuHandle()));

				// 狂徒
				HANDLER_DICT.put(FUNC_ID_KUANG_TU, new HandleProxy(new KuangTuHandle()));

				// 致命
				HANDLER_DICT.put(FUNC_ID_ZHI_MING, new HandleProxy(new ZhiMingHandle()));

				// 致使一击
				HANDLER_DICT.put(FUNC_ID_ZHI_MING_YI_JI, new HandleProxy(new ZhiMingYiJiHandle()));

				// 压制
				HANDLER_DICT.put(FUNC_ID_SUPPRESS, new HandleProxy(new SuppressHandle()));

				// 众矢之的
				HANDLER_DICT.put(FUNC_ID_ZHONG_SHI_ZHI_DI, new HandleProxy(new ZhongShiZhiDiHandle()));

				// 增加隐匿
				HANDLER_DICT.put(FUNC_ID_ADD_YIN_NI, new HandleProxy(new AddYinNiHandle()));

				// 增加隐匿
				HANDLER_DICT.put(FUNC_ID_ADD_PO_JI, new HandleProxy(new AddPoJiHandle()));

				// 增加隐匿
				HANDLER_DICT.put(FUNC_ID_ADD_REN_XING, new HandleProxy(new AddRenXingHandle()));

				// 治疗加成
				HANDLER_DICT.put(FUNC_ID_UP_ADD_LIFE, new HandleProxy(new UpAddLifeHandle()));

			}

			if (HANDLER_DICT.containsKey(funID)) {
				handle = HANDLER_DICT.get(funID);
			} else {
				throw new BattleRuntimeException("对应的公式处理器不存在.funID[" + funID + "]");
			}
		}

		return handle;
	}
}
