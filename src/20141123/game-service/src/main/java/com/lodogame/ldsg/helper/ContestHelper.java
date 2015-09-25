package com.lodogame.ldsg.helper;

import java.util.Calendar;
import java.util.Date;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.constants.ContestConstant;

/**
 * 擂台赛帮助类
 * @author chengevo
 *
 */
public class ContestHelper {
	
	public static String getRoundStartTime(int round) {
		return ContestConstant.START_TIME_OF_EACH_ROUND[round];
	}
	
	public static Date getRoundStartDate(int round) {
		long roundStartTime = DateUtils.getTimeByDayOfWeek(ContestConstant.START_TIME_OF_EACH_ROUND[round]);
		return new Date(roundStartTime);
	}
	
	public static Date getRoundEndDate(int round) {
		// 把每一场比赛的开始时间和结束时间看成是一样的
		return getRoundStartDate(round);
	}

	/**
	 * 计算现在距离这个礼拜某一轮比赛开始还有多长时间
	 * @param round 第几轮比赛
	 */
	public static long getRoundStartCountdown(int round) {
		
		long now = new Date().getTime();
		String roundStartTime = getRoundStartTime(round);
		return DateUtils.getTimeByDayOfWeek(roundStartTime) - now;
		
	}

	public static int getDayOfContestByRound(int round) {
		switch (round) {
		case ContestConstant.ROUND_128:
		case ContestConstant.ROUND_64:
			return 1;
		case ContestConstant.ROUND_32:
		case ContestConstant.ROUND_16:
			return 2;
		default:
			return 3;
		}
	}
	
	public static int getTeamIdByRound(int round) {
		switch (round) {
		case ContestConstant.ROUND_128:
			return 128;
		case ContestConstant.ROUND_64:
			return 64;
		case ContestConstant.ROUND_32:
			return 32;
		case ContestConstant.ROUND_16:
			return 16;
		case ContestConstant.ROUND_8:
			return 8;
		case ContestConstant.ROUND_4:
			return 4;
		case ContestConstant.ROUND_2:
			return 2;
		case ContestConstant.ROUND_1:
			return 1;
		default:
			return 0;
		}
	}
	
	public static String getTeamNameByRound(int round) {
		switch (round) {
		case ContestConstant.ROUND_128:
			return "128强比赛";
		case ContestConstant.ROUND_64:
			return "64强比赛";
		case ContestConstant.ROUND_32:
			return "32强比赛";
		case ContestConstant.ROUND_16:
			return "16强比赛";
		case ContestConstant.ROUND_8:
			return "8强比赛";
		case ContestConstant.ROUND_4:
			return "4强比赛";
		case ContestConstant.ROUND_2:
			return "半强比赛";
		case ContestConstant.ROUND_1:
			return "决赛";
		default:
			return "下注";
		}
	}
	
	public static String getTeamNameByTeamId(int teamId) {
		switch (teamId) {
		case 12801:
			return "青龙组";
		case 12802:
			return "白虎组";
		case 12803:
			return "朱雀组";
		default:
			return "玄武组";
		}
	}
	
	public static int getNextRound(int round) {
		return ContestConstant.ROUNDS[round + 1];
	}
	

	public static int getCurrentSession() {
		Date firstSession = new Date(ContestConstant.FIRST_CONTEST_TIME);
		Date now = new Date();
		
		int dayDiff = DateUtils.getDayDiff(firstSession, now);
		int weekDiff = dayDiff / 7;
		
		return weekDiff + 1;
	}
	

	public static Date getFightDate(int round) {
		String roundStartTime = getRoundStartTime(round);
		String[] split = roundStartTime.split(" ");
		int dayOfWeek = Integer.valueOf(split[0]);
		String[] hoursAndMin = split[1].split(":");
		int hour = Integer.valueOf(hoursAndMin[0]);
		int min = Integer.valueOf(hoursAndMin[1]);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek  + 1);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		
		return cal.getTime();
	}

	public static void main(String[] args) {
		System.out.println(getRegStartDate());
	}

	public static int getRankByRound(int round) {
		
		switch (round) {
		case ContestConstant.ROUND_8:
			return 16;
		case ContestConstant.ROUND_4:
			return 8;
		case ContestConstant.ROUND_2:
			return 4;
		case ContestConstant.ROUND_1:
			return 2;
		default:
			return 1;
		}
	}

	/**
	 * 某场比赛结束后，判断这轮比赛是不是结束了
	 * @param round
	 * @return 0表示这轮比赛没有结束；1表示这轮比赛结束
	 */
	public static int isRoundsOfTheDayFinished(int round) {
		switch (round) {
		case ContestConstant.ROUND_64:
		case ContestConstant.ROUND_16:
		case ContestConstant.ROUND_1:
			return 1;
		default:
			return 0;
		}
	}
	
	public static Date getRegStartDate() {
		long regStartTime = DateUtils.getTimeByDayOfWeek(ContestConstant.REG_START_TIME);
		return new Date(regStartTime);
	}

	public static Date getRegEndDate() {
		long regEndTime = DateUtils.getTimeByDayOfWeek(ContestConstant.REG_END_TIME);
		return new Date(regEndTime);
	}
	
	public static boolean isTimeToEnterReg() {
		Date now = new Date();
		Date regStartTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REG_START_TIME));
		Date regEndTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REG_END_TIME));
		if (DateUtils.isBetween(now, regStartTime, regEndTime)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isFirstSessionBegin() {
		Date firstSession = new Date(ContestConstant.FIRST_CONTEST_TIME);
		Date now = new Date();
		return now.after(firstSession);
		
	}
	
	public static boolean isBetTimeEnd() {
		Date betEndDate = ContestHelper.getBetEndDate();
		Date now = new Date();
		return now.after(betEndDate);
	}
	
	public static boolean isTimeToEnterVersus() {
		Date now = new Date();
		Date versus1StartTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.VERSUS_1_START_TIME));
		Date versus2StartTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.VERSUS_2_START_TIME));
		Date versus3StartTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.VERSUS_3_START_TIME));
		Date versus1EndTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.VERSUS_1_END_TIME));
		Date versus2EndTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.VERSUS_2_END_TIME));
		Date versus3EndTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.VERSUS_3_END_TIME));
		
		if (DateUtils.isBetween(now, versus1StartTime, versus1EndTime) ||
				DateUtils.isBetween(now, versus2StartTime, versus2EndTime) ||
				DateUtils.isBetween(now, versus3StartTime, versus3EndTime)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isTimeToEnterRecReward() {
		Date now = new Date();
		Date recReward1StartTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REC_REWARD_1_START_TIME));
		Date recReward2StartTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REC_REWARD_2_START_TIME));
		Date recReward3StartTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REC_REWARD_3_START_TIME));
		Date recReward1EndTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REC_REWARD_1_END_TIME));
		Date recReward2EndTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REC_REWARD_2_END_TIME));
		Date recReward3EndTime = new Date(DateUtils.getTimeByDayOfWeek(ContestConstant.REC_REWARD_3_END_TIME));
		
		if (DateUtils.isBetween(now, recReward1StartTime, recReward1EndTime) || 
				DateUtils.isBetween(now, recReward2StartTime, recReward2EndTime) ||
				DateUtils.isBetween(now, recReward3StartTime, recReward3EndTime)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTimeToBet() {
		Date now = new Date();
		Date roundBetStartDate = ContestHelper.getRoundStartDate(ContestConstant.ROUND_BET);
		Date round8StartDate = ContestHelper.getRoundStartDate(ContestConstant.ROUND_8);
		if (DateUtils.isBetween(now, roundBetStartDate, round8StartDate)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getRoundByTeamId(int teamId) {
		switch (teamId) {
		case 12801:
		case 12802:
		case 12803:
		case 12804:
			return ContestConstant.ROUND_128;
		case 64:
			return ContestConstant.ROUND_64;
		case 32:
			return ContestConstant.ROUND_32;
		case 16:
			return ContestConstant.ROUND_16;
		case 0:
		case 8:
			return ContestConstant.ROUND_8;
		case 4:
			return ContestConstant.ROUND_4;
		case 2:
			return ContestConstant.ROUND_2;
		default:
			return ContestConstant.ROUND_1;
		}
	}
	
	public static int getTeamIdByRewardId(int rewardId) {
		switch (rewardId) {
		case 7:
			return 64;
		case 6:
			return 32;
		case 5:
			return 16;
		case 4:
			return 8;
		case 3:
			return 4;
		case 2:
			return 2;
		default:
			return 1;
		}
	}
	
	public static int getContestNotice() {
		Date now = new Date();
		Date regStartTime = getRegStartDate();
		Date regEndTime = getRegEndDate();
		if (DateUtils.isBetween(now, regStartTime, regEndTime)) {
			return 1;
		}
		
		for (int round : ContestConstant.ROUNDS) {
			long roundStartCountdown = getRoundStartCountdown(round);
			if (roundStartCountdown >= 0 && roundStartCountdown <= 30 * 60 * 1000) {
				return 1;
			}
		}
		
		return 0;
	}

	public static Date getBetEndDate() {
		long betEndTime = DateUtils.getTimeByDayOfWeek(ContestConstant.START_TIME_ROUND_8);
		return new Date(betEndTime);
	}
	
	public static int getTeamCapacityByRound(int round) {
		switch (round) {
		case ContestConstant.ROUND_128:
			return 64;
		case ContestConstant.ROUND_64:
			return 128;
		case ContestConstant.ROUND_32:
			return 64;
		case ContestConstant.ROUND_16:
			return 32;
		case ContestConstant.ROUND_8:
		case ContestConstant.ROUND_BET:
			return 16;
		case ContestConstant.ROUND_4:
			return 8;
		case ContestConstant.ROUND_2:
			return 4;
		case ContestConstant.ROUND_1:
			return 2;
		default:
			return 0;
		}
	}
}
